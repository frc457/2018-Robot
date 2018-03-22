package org.greasemonkeys457.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.greasemonkeys457.robot2018.Constants;
import org.greasemonkeys457.robot2018.RobotMap;
import org.greasemonkeys457.robot2018.commands.ElevatorFromJoysticks;
import org.greasemonkeys457.robot2018.commands.ElevatorHoldPosition;

public class Elevator extends Subsystem {

    // Hardware
    private final TalonSRX masterMotor, followerMotor;
    private final Encoder encoder;

    // Target variables
    private int targetPosition; // in encoder ticks

    // State variables
    private EControlMode sControlMode;

    /**
     * Control modes.
     * This allows us to easily switch between position control (automatic) or speed control (manual).
     */
    public enum EControlMode {
        SpeedControl,
        PositionControl
    }

    /**
     * Constructor. Defines and configures everything.
     */
    public Elevator () {

        // Define hardware
        masterMotor = new TalonSRX(RobotMap.elevatorTopMotor);
        followerMotor = new TalonSRX(RobotMap.elevatorBottomMotor);
        encoder = new Encoder(RobotMap.elevatorEncoderA, RobotMap.elevatorEncoderB);

        // Configuration
        configureTalons();
        configureEncoders();

        // Control mode
        sControlMode = Constants.kControlMode;

    }

    // Configuration functions

    private void configureTalons () {

        // Invert one side
        masterMotor.setInverted(false);
        followerMotor.setInverted(true);

        // Set followers
        followerMotor.follow(masterMotor);

    }

    private void configureEncoders () {

        // Reverse direction
        encoder.setReverseDirection(true);

        // Grab constants that we need
        double pitchDiameter = Constants.kElevPitchDiameter;
        double encoderPulsesPerRev = Constants.kEncoderPulsesPerRev;

        // Set distance per pulse
        encoder.setDistancePerPulse(((pitchDiameter * Math.PI) / encoderPulsesPerRev) * 2.0);

    }

    // Speed

    /**
     * Sets the speed of the motors on the elevator.
     * @param speed The desired speed for the motors to run at
     */
    public void setSpeed (double speed) {

        // Scale
        speed = elevatorScaling(speed);

        // Set
        masterMotor.set(ControlMode.PercentOutput, speed);

    }

    private double elevatorScaling (double speed) {

        // Scale the inputted speed
        speed = (speed * Constants.kElevatorScale);

        // Eliminate deadzones
        if (Math.abs(speed) < 0.001) {
            speed = 0.0;
        }

        // Return
        return speed;

    }

    // Position

    /**
     * Sets the target height of the final stage of the elevator
     * @param targetPosition The target position, in inches
     */
    public void setTargetPosition (int targetPosition) {

        // TODO: Limit the value of targetPosition if it's too large or too small
        this.targetPosition = targetPosition;

    }

    public int getTargetPosition () {
        return this.targetPosition;
    }

    public int getCurrentPosition () {
        return encoder.get();
    }

    // Misc.

    public void reset () {

        // Return hardware to its' default state
        if (sControlMode == EControlMode.SpeedControl) {
            setSpeed(0.0);
        }
        if (sControlMode == EControlMode.PositionControl) {
            setTargetPosition(0);
        }

    }

    public void zeroSensors () {

        // Zero the encoders
        encoder.reset();

    }

    @Override
    protected void initDefaultCommand() {
        if (sControlMode == EControlMode.SpeedControl)
            setDefaultCommand(new ElevatorFromJoysticks());
        else if (sControlMode == EControlMode.PositionControl)
            setDefaultCommand(new ElevatorHoldPosition());
    }

}
