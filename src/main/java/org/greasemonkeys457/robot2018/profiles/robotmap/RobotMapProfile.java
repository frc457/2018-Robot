package org.greasemonkeys457.robot2018.profiles.robotmap;

/**
 * Interface that holds all the values needed for the electronics.
 */
public interface RobotMapProfile {

    /**
     * Prefix key:
     *
     * d- Drivetrain
     * e- Elevator
     * m- Mandible / Hugger
     */

    // Drivetrain talons
    public int dLeftMotorA();
    public int dLeftMotorB();
    public int dRightMotorA();
    public int dRightMotorB();

    // Drivetrain solenoid
    public int dShifterForward();
    public int dShifterReverse();

    // Drivetrain encoders
    public int dLeftEncoderA();
    public int dLeftEncoderB();
    public int dRightEncoderA();
    public int dRightEncoderB();

    // Elevator motors
    public int eTopMotor();
    public int eBottomMotor();

    // Elevator encoder
    public int eEncoderA();
    public int eEncoderB();

    // Mandible motors
    public int mRightMotor();
    public int mLeftMotor();

    // Mandible solenoid
    public int mOpen();
    public int mClose();

}
