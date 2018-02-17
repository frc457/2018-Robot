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

    public Elevator () {

        // Define hardware
        masterMotor = new TalonSRX(RobotMap.elevatorTopMotor);
        followerMotor = new TalonSRX(RobotMap.elevatorBottomMotor);
        encoder = new Encoder(RobotMap.elevatorEncoderA, RobotMap.elevatorEncoderB);

        // Configuration
        configureTalons();
        configureEncoders();

    }

    public void configureTalons () {

        // Invert one side
        masterMotor.setInverted(false);
        followerMotor.setInverted(true);

        // Set followers
        followerMotor.follow(masterMotor);

    }

    public void configureEncoders () {

        // Grab constants that we need
        double pitchDiameter = Constants.kElevPitchDiameter;
        double encoderPulsesPerRev = Constants.kEncoderPulsesPerRev;

        // Set distance per pulse
        encoder.setDistancePerPulse(((pitchDiameter * Math.PI) / encoderPulsesPerRev) * 2.0);

    }

    // Setter functions

    /**
     * Sets the speed of the motors on the elevator.
     * @deprecated Try to use setTargetPosition instead.
     * @param speed The desired speed for the motors to run at
     */
    @Deprecated
    public void setSpeed (double speed) {

        /*
         * This is marked as deprecated because it's really a terrible way to control the elevator.
         * Using position control rather than speed control is preferred. Use that instead if it works.
         */
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

    // Misc.

    public void reset () {

        // Return hardware to its' default state
        // TODO: Set default position

    }

    public void zeroSensors () {

        // Zero the encoders
        encoder.reset();

    }

    @Override
    protected void initDefaultCommand() {
        // TODO: Change this to ElevatorHoldPosition
        setDefaultCommand(new ElevatorFromJoysticks());
    }

}
