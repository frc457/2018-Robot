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
    private double targetPosition; // TODO: Set default value for this (0?)

    // State variables
    EControlMode controlMode;

    public enum EControlMode {
        SpeedControl,
        PositionControl
    }

    public Elevator () {

        // Define hardware
        masterMotor = new TalonSRX(RobotMap.elevatorTopMotor);
        followerMotor = new TalonSRX(RobotMap.elevatorBottomMotor);
        encoder = new Encoder(RobotMap.elevatorEncoderA, RobotMap.elevatorEncoderB);

        // Configuration
        configureTalons();
        configureEncoders();

        // Control mode
        controlMode = Constants.kControlMode;

    }

    public void configureTalons () {

        // Invert one side
        masterMotor.setInverted(false);
        followerMotor.setInverted(true);

        // Set followers
        followerMotor.follow(masterMotor);

    }

    public void configureEncoders () {

        // Reverse direction
        encoder.setReverseDirection(true);

        // Grab constants that we need
        double pitchDiameter = Constants.kElevPitchDiameter;
        double encoderPulsesPerRev = Constants.kEncoderPulsesPerRev;

        // Set distance per pulse
        encoder.setDistancePerPulse(((pitchDiameter * Math.PI) / encoderPulsesPerRev) * 2.0);

    }

    // Setter functions

    /**
     * Sets the speed of the motors on the elevator.
     * @param speed The desired speed for the motors to run at
     */
    public void setSpeed (double speed) {

        masterMotor.set(ControlMode.PercentOutput, speed);

    }

    /**
     * Sets the target height of the final stage of the elevator
     * @param targetPosition The target position, in inches
     */
    public void setTargetPosition (double targetPosition) {

        // TODO: Limit the value of targetPosition if it's too large or too small
        this.targetPosition = targetPosition;

    }

    // Getter functions

    public double getTargetPosition () {
        return this.targetPosition;
    }

    public double getCurrentPosition () {
        return encoder.getDistance();
    }

    public Encoder getEncoder() {
        return encoder;
    }

    // Misc.

    public void reset () {

        // Return hardware to its' default state
        if (controlMode == EControlMode.SpeedControl) {
            setSpeed(0.0);
        }
        if (controlMode == EControlMode.PositionControl) {
            setTargetPosition(0.0);
        }

    }

    public void zeroSensors () {

        // Zero the encoders
        encoder.reset();

    }

    @Override
    protected void initDefaultCommand() {
        if (controlMode == EControlMode.SpeedControl)
            setDefaultCommand(new ElevatorFromJoysticks());
        else if (controlMode == EControlMode.PositionControl)
            setDefaultCommand(new ElevatorHoldPosition());
    }

}
