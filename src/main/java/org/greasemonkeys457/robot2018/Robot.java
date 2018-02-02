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
