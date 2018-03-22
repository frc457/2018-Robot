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

    // Constants
    private static final int MAX_TICKS = Constants.E_MAX_TICKS;
    private static final int MIN_TICKS = Constants.E_MIN_TICKS;

    // Target variables
    private int targetPosition; // in encoder ticks

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

    public int getMaxTicks () {
        return MAX_TICKS;
    }

    public int getMinTicks () {
        return MIN_TICKS;
    }

    /**
     * Check if the elevator is currently within its' limits.
     * @return 1 if too high, -1 if too low, 0 if good
     */
    public int withinLimits () {
        if (getCurrentPosition() > MAX_TICKS) return 1;
        else if (getCurrentPosition() < MIN_TICKS) return -1;
        else return 0;
    }

    // Misc.

    public void reset () {

        // Return hardware to its' default state
        setSpeed(0.0);

    }

    public void zeroSensors () {

        // Zero the encoders
        encoder.reset();

    }

    @Override
    protected void initDefaultCommand() {
        // TODO: Add the Elevator Controller
    }

}
