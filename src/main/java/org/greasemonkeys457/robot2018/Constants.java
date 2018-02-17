package org.greasemonkeys457.robot2018;

import jaci.pathfinder.Trajectory;
import org.greasemonkeys457.robot2018.util.paths.*;

public class Constants {

    // Drive
    public static double kDriveScale = 1.0;
    public static double kDriveWheelDiameter = 6.0 / 12.0; // in feet
    public static int kEncoderPulsesPerRev = 64;
    public static double kWheelbaseWidth = 23.5 / 12.0;
    public static double kRobotWidth = 27.5 / 12.0;
    public static double kRobotLength = 32.5 / 12.0;

    // Elevator
    public static double kElevPitchDiameter = 1.45; // in inches

    // Pathfinder variables
    public static double kLowGearMaxVelocity = 8.4;
    public static double kLowGearMaxAccel = 8.0;
    public static double kLowGearMaxJerk = 10.0;
    public static Trajectory.Config pathfinderConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_CUBIC, // Fit method used to generate the path
            Trajectory.Config.SAMPLES_HIGH,     // Sample count
            0.02,                               // Time step
            4.25,                               // Max velocity
            kLowGearMaxAccel,                   // Max acceleration
            kLowGearMaxJerk                     // Max jerk
    );

    // Paths
    public static Path centerToRightSwitch = new CenterToRightSwitch();
    public static Path centerToLeftSwitch  = new CenterToLeftSwitch();
    public static Path leftToLeftSwitch    = new LeftToLeftSwitch();
    public static Path leftCrossBaseline   = new LeftCrossBaseline();
    public static Path leftToLeftScale     = new LeftToLeftScale();
    public static Path rightToRightSwitch  = new RightToRightSwitch();
    public static Path rightCrossBaseline  = new RightCrossBaseline();
    public static Path rightToRightScale   = new RightToRightScale();

}
