package org.greasemonkeys457.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.greasemonkeys457.robot2018.Constants;
import org.greasemonkeys457.robot2018.RobotMap;
import org.greasemonkeys457.robot2018.commands.DriveFromJoysticks;

public class Drivetrain extends Subsystem {

    // Hardware
    private final TalonSRX mRightMaster, mRightFollower, mLeftMaster, mLeftFollower;
    private final DoubleSolenoid mShifter;
    private final Encoder mRightEncoder, mLeftEncoder;

    // State variables
    private boolean mIsLowGear;

    // Constants
    private double scale = Constants.scale;
    private double wheelDiameter = Constants.wheelDiameter;
    private double encoderPulsesPerRev = Constants.pulsesPerRev;

    public Drivetrain () {

        // TODO: NavX

        // Define stuff
        mRightMaster = new TalonSRX(RobotMap.rfMotor);
        mRightFollower = new TalonSRX(RobotMap.rbMotor);
        mLeftMaster = new TalonSRX(RobotMap.lfMotor);
        mLeftFollower = new TalonSRX(RobotMap.lbMotor);

        mRightEncoder = new Encoder(RobotMap.rightEncoderA, RobotMap.rightEncoderB);
        mLeftEncoder = new Encoder(RobotMap.leftEncoderA,  RobotMap.leftEncoderB);

        mShifter = new DoubleSolenoid(RobotMap.shifterForward, RobotMap.shifterReverse);

        // Configure stuff
        configureTalons();
        configureEncoders();

    }

    /**
     * Configuration functions for various things that might need configuration.
     */
    private void configureTalons () {

        // Invert one side
        mRightMaster.setInverted(true);
        mRightFollower.setInverted(true);

        // Set followers
        mRightFollower.follow(mRightMaster);
        mLeftFollower.follow(mLeftMaster);

        // TODO: Limit voltage outputs

    }
    private void configureEncoders () {

        // Set distance per pulse
        mRightEncoder.setDistancePerPulse((wheelDiameter * Math.PI) / encoderPulsesPerRev);
        mLeftEncoder.setDistancePerPulse((wheelDiameter * Math.PI) / encoderPulsesPerRev);

        // Invert one side
        mLeftEncoder.setReverseDirection(true);

    }

    /**
     * Functions to set drivetrain's speed, in percentage (-1 to 1).
     */
    public void setRightSpeed (double speed) {
        mRightMaster.set(ControlMode.PercentOutput, speed);
    }
    public void setLeftSpeed (double speed) {
        mLeftMaster.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Shifts the gearboxes.
     */
    public void setLowGear(boolean wantsLowGear) {

        // Shift to the desired state
        if (wantsLowGear)
            mShifter.set(DoubleSolenoid.Value.kForward);
        else
            mShifter.set(DoubleSolenoid.Value.kReverse);

        // Update the state variable
        mIsLowGear = wantsLowGear;

    }

    /**
     * @return Whether or not the drivetrain is in low gear
     */
    public boolean isLowGear () {
        return mIsLowGear;
    }

    /**
     * Scales the given speed.
     * @param speed The input speed, from -1 to 1
     * @return The scaled speed
     */
    public double driveScaling (double speed) {

        // Run the input speed through the scaling function.
        speed = (speed * Math.abs(speed) * Math.abs(speed) * scale);

        if (Math.abs(speed) < 0.001) {
            speed = 0.0;
        }

        return speed;

    }

    /**
     * Sets all hardware to its' default position.
     */
    public void reset () {

        // Stop the motors
        setRightSpeed(0);
        setLeftSpeed(0);

        // Make sure shifters are where they should be
        setLowGear(true);

        // Zero the sensors
        mRightEncoder.reset();
        mLeftEncoder.reset();

    }

    public void initDefaultCommand () {
        setDefaultCommand(new DriveFromJoysticks());
    }

}
