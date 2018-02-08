package org.greasemonkeys457.robot2018;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public class Constants {

    // Drive
    public static double kDriveScale = 1.0;
    public static double kDriveWheelDiameter = 6.0 / 12.0; // in feet
    public static int kEncoderPulsesPerRev = 64;
    public static double kWheelbaseWidth = 23.5 / 12.0;
    public static double kRobotWidth = 27.5 / 12.0;
    public static double kRobotLength = 32.5 / 12.0;

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

}
