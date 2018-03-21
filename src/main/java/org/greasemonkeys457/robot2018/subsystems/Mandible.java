package org.greasemonkeys457.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.greasemonkeys457.robot2018.Constants;
import org.greasemonkeys457.robot2018.RobotMap;
import org.greasemonkeys457.robot2018.commands.MandibleFromJoysticks;

public class Mandible extends Subsystem {

    // Hardware
    private final TalonSRX mRightMotor, mLeftMotor;
    private final DoubleSolenoid mGripper;

    // State variables
    private boolean sIsGripping;

    /**
     * Constructor. Defines and configures everything.
     */
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

    // Configuration functions

    private void configureTalons () {

        // Invert one motor
        mRightMotor.setInverted(true);
        mLeftMotor.setInverted(false);
        // TODO: Set a master and a follower
        // TODO: Configure voltage outputs (?)

    }

    // Setter functions

    public void setGripping (boolean wantsToGrip) {

        // Actuate
        if (wantsToGrip) {
            mGripper.set(DoubleSolenoid.Value.kForward);
        } else {
            mGripper.set(DoubleSolenoid.Value.kReverse);
        }

        // Update the state variable
        sIsGripping = wantsToGrip;

    }

    public void setSpeed (double leftSpeed, double rightSpeed) {

        // Scale
        leftSpeed = speedScaling(leftSpeed);
        rightSpeed = speedScaling(rightSpeed);

        // Set
        mLeftMotor.set(ControlMode.PercentOutput, leftSpeed);
        mRightMotor.set(ControlMode.PercentOutput, rightSpeed);

    }

    public void setSpeed (double speed) {

        setSpeed(speed, speed);

    }

    // Getter functions

    public boolean isGripping () {
        return sIsGripping;
    }

    // Misc.

    public double speedScaling (double speed) {

        // Scale the input
        speed = speed * Constants.kMandibleScale;

        // Return
        return speed;

    }

    public void reset () {

        // Return hardware to their default states
        setGripping(true);
        setSpeed(0);

    }

    @Override
    protected void initDefaultCommand () {
        setDefaultCommand(new MandibleFromJoysticks());
    }

}
