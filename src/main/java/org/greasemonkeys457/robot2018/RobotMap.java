package org.greasemonkeys457.robot2018;

public class RobotMap {

    /*
     * Talons
     * TODO: Configure and update talons (comp bot)
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
     * TODO: Test shifters (practice bot)
     * TODO: Test shifters (comp bot)
     */

    // Drivetrain
    public static int shifterForward = 2;
    public static int shifterReverse = 3;

    // Mandible
    public static int mandGripperForward = 1;
    public static int mandGripperReverse = 0;

    /*
     * Encoders
     * TODO: Test encoder direction (practice bot)
     * TODO: Test encoder direction (comp bot)
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
