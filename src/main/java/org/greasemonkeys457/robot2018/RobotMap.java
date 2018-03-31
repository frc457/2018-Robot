package org.greasemonkeys457.robot2018;

public class RobotMap {

    /*
     * Talons
     */

    // Drivetrain
    public static int lfMotor = 3;
    public static int lbMotor = 2;
    public static int rfMotor = 0;
    public static int rbMotor = 1;

    // Elevator
    public static int elevatorBottomMotor = 7;
    public static int elevatorTopMotor = 6;

    // Mandible
    public static int mandRightMotor = 4;
    public static int mandLeftMotor = 5;

    /*
     * Pneumatics (PCM ports)
     */

    // Drivetrain
    public static int shifterForward = 0;
    public static int shifterReverse = 1;

    // Mandible
    public static int mandGripperForward = 3;
    public static int mandGripperReverse = 2;

    /*
     * Encoders
     */

    // Drivetrain
    public static int rightEncoderA = 4;
    public static int rightEncoderB = 5;
    public static int leftEncoderA  = 3;
    public static int leftEncoderB  = 2;

    // Elevator
    public static int elevatorEncoderA = 0;
    public static int elevatorEncoderB = 1;

}
