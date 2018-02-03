package org.greasemonkeys457.robot2018;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public class Constants {

    // Drive
    public static double kDriveScale = 1.0;
    public static double kDriveWheelDiameter = 4.0 / 12.0; // in feet
    public static int kEncoderPulsesPerRev = 64;

    // Pathfinder variables
    public static double kLowGearMaxVelocity = 6.0;
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
    public enum Paths {

        centerToRightSwitch(new Waypoint[] {
                new Waypoint((16.5/12.0), (159.5/12.0), 0.0),
                new Waypoint((18.5/12.0), (159.5/12.0), 0.0),
                new Waypoint((120/12.0), 9.0, 0.0),
                new Waypoint((123/12.0), 9.0, 0.0),
        }),

        centerToLeftSwitch(new Waypoint[] {
                new Waypoint((16.5/12.0), (159.5/12.0), 0.0),
                new Waypoint((18.5/12.0), (159.5/12.0), 0.0),
                new Waypoint((120/12.0), 18.0, 0.0),
                new Waypoint((123/12.0), 18.0, 0.0),
        });

        public Waypoint[] points;

        Paths (Waypoint[] points) {
            this.points = points;
        }

    }

}
