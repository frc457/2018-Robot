package org.greasemonkeys457.robot2018;

public class RobotMap {

    /*
     * Talons
     * TODO: Configure and update talons (comp bot)
     */

    // Drivetrain
    public static int lfMotor = 1;
    public static int lbMotor = 2;
    public static int rfMotor = 3;
    public static int rbMotor = 4;

    // Elevator
    public static int elevatorBottomMotor = 5;
    public static int elevatorTopMotor = 6;

    // Mandible
    public static int mandRightMotor = 7;
    public static int mandLeftMotor = 8;

    /*
     * Pneumatics (PCM ports)
     * TODO: Test shifters (practice bot)
     * TODO: Test shifters (comp bot)
     */

    // Drivetrain
    public static int shifterForward = 0;
    public static int shifterReverse = 1;

    // Mandible
    public static int mandGripperForward = 3;
    public static int mandGripperReverse = 2;

    /*
     * Encoders
     * TODO: Test encoder direction (practice bot)
     * TODO: Test encoder direction (comp bot)
     */

    // Drivetrain
    public static int rightEncoderA = 0;
    public static int rightEncoderB = 1;
    public static int leftEncoderA  = 2;
    public static int leftEncoderB  = 3;

    // Elevator
    public static int elevatorEncoderA = 4;
    public static int elevatorEncoderB = 5;

}
