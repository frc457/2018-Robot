package org.greasemonkeys457.robot2018;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import org.greasemonkeys457.robot2018.subsystems.Drivetrain;

public class Robot extends IterativeRobot {

    // Subsystems
    public static Drivetrain drivetrain;

    // OI
    public static OI oi;

    public void robotInit() {

        // Define subsystems
        drivetrain = new Drivetrain();

        // Define OI
        oi = new OI();

        // ----- Path generation begins -----

        /**
         * Some notes:
         *
         * 1. We'll be using feet for units of length instead of meters. Jaci's documentation and example code of
         *    Pathfinder uses meters; however, so long as you stay consistent with the unit, Pathfinder will work with
         *    whatever unit you give it.
         *
         * 2. We'll try to document what our code is doing to the best of our ability. However, if you find yourself
         *    stuck or confused, the Pathfinder examples and/or docs might help. Follow the link below to get there.
         *
         *    https://github.com/JacisNonsense/Pathfinder/tree/master/Pathfinder-Java
         */

        // TODO: Move this to somewhere more appropriate. Perhaps look at 254's 2017 file structure to figure out a good place to move it.
        // TODO: Measure max velocity, acceleration, and jerk, and then attach it to the Drivetrain subsystem.

        // Trajectory configuration
        Trajectory.Config config = new Trajectory.Config(
                Trajectory.FitMethod.HERMITE_CUBIC, // Fit method used to generate the path
                Trajectory.Config.SAMPLES_HIGH,     // Sample count
                0.05,                               // Time step
                1.7,                                // Max velocity
                2.0,                                // Max acceleration
                60.0                                // Max jerk
        );

        // Waypoints
        Waypoint[] straightPoints = new Waypoint[] {
                new Waypoint(0.0, 0.0, 0.0),
                new Waypoint(8.0, 0.0, 0.0),
        };

        // Generate a Trajectory
        Trajectory trajectory = Pathfinder.generate(straightPoints, config);

        // Modify the trajectory for tank drive using the wheelbase width
        TankModifier modifier = new TankModifier(trajectory).modify((25.5/12.0));

        // Get the trajectories for each respective side
        Trajectory rightTrajectory = modifier.getRightTrajectory();
        Trajectory leftTrajectory  = modifier.getLeftTrajectory();

        // TODO: Do something with the trajectories

        // ------ Path generation ends ------

    }

    public void disabledInit() {
        reset();
    }

    public void autonomousInit() {
        // TODO: Autonomous selector
    }

    public void teleopInit() {}

    public void testInit() {}


    public void disabledPeriodic() {}

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    public void testPeriodic() {}


    // Reset all subsystems
    public void reset () {

        drivetrain.reset();

    }

}
