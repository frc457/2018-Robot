package org.greasemonkeys457.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.greasemonkeys457.robot2018.RobotMap;

public class Mandible extends Subsystem {

    // Hardware
    private final TalonSRX mRightMotor, mLeftMotor;
    private final DoubleSolenoid mGripper;

    // State variables
    private boolean mIsGripping;

    public Mandible () {

        // Define hardware
        mRightMotor = new TalonSRX(RobotMap.mandRightMotor);
        mLeftMotor = new TalonSRX(RobotMap.mandLeftMotor);
        mGripper = new DoubleSolenoid(RobotMap.mandGripperForward, RobotMap.mandGripperReverse);

        // Configure everything
        configureTalons();

        // Ensure subsystem is in its' default state
        reset();

    }

    private void configureTalons () {

        // TODO: Invert one motor
        // TODO: Set a master and a follower
        // TODO: Configure voltage outputs (?)

    }

    public void setGripping (boolean wantsToGrip) {

        // Actuate
        if (wantsToGrip) {
            mGripper.set(DoubleSolenoid.Value.kForward);
        } else {
            mGripper.set(DoubleSolenoid.Value.kReverse);
        }

        // Update the state variable
        mIsGripping = wantsToGrip;

    }

    public void setSpeed (double leftSpeed, double rightSpeed) {

        mLeftMotor.set(ControlMode.PercentOutput, leftSpeed);
        mRightMotor.set(ControlMode.PercentOutput, rightSpeed);

    }

    public void setSpeed (double speed) {

        setSpeed(speed, speed);

    }

    public boolean isGripping () {
        return mIsGripping;
    }

    public void reset () {

        // Return hardware to their default states
        setGripping(true);
        setSpeed(0);

    }

    @Override
    protected void initDefaultCommand () {
        // No default command.
    }

}
