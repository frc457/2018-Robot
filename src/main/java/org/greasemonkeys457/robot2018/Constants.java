package org.greasemonkeys457.robot2018;

import jaci.pathfinder.Trajectory;
import org.greasemonkeys457.robot2018.subsystems.Elevator;
import org.greasemonkeys457.robot2018.util.paths.*;

public class Constants {

    // Drive
    public static double kDriveScale = 0.7;
    public static double kDriveWheelDiameter = 6.0 / 12.0; // in feet
    public static int kEncoderPulsesPerRev = 64;

    // Elevator
    public static double kElevatorScale = 0.7;
    public static double kElevPitchDiameter = 1.45; // in inches

    /**
     * This class connects different elevator positions for different goals with their associated encoder ticks value.
     * This allows us to use the encoder ticks value as a target for where we want the elevator to be.
     */
    public enum ElevatorPosition {

        // TODO: Measure these and set accordingly
        START(0), // resting on the bumpers
        MIN(100), // just above the bumpers
        SWITCH(200),
        SCALE(300),
        MAX(400); // highest point

        public final int ticks;

        ElevatorPosition(int ticks) {
            this.ticks = ticks;
        }

    }

    // Mandible
    public static double kMandibleScale = 0.4;

    // Path following variables
    public static double kLowGearMaxVelocity = 8.4;
    public static double kLowGearMaxAccel = 8.0;
    public static double kLowGearMaxJerk = 10.0;

    // Path generating variables
    public static double kWheelbaseWidth = 23.5 / 12.0;
    public static double kRobotWidth = 34.0 / 12.0;
    public static double kRobotLength = 39.0 / 12.0;

    public static Trajectory.Config pathfinderConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_CUBIC, // Fit method used to generate the path
            Trajectory.Config.SAMPLES_HIGH,     // Sample count
            0.02,                               // Time step
            4.25,                               // Max velocity (underestimate)
            6.0,                                // Max acceleration (underestimate)
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
