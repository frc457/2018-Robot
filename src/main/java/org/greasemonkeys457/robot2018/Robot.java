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

        // TESTING

        // Check for top values
        if (drivetrain.getLeftSpeed() > drivetrain.topLeftSpeed) {
            drivetrain.topLeftSpeed = drivetrain.getLeftSpeed();
        }
        if (drivetrain.getRightSpeed() > drivetrain.topRightSpeed) {
            drivetrain.topRightSpeed = drivetrain.getRightSpeed();
        }
        if (drivetrain.getRightAccel() > drivetrain.topRightAccel) {
            drivetrain.topRightAccel = drivetrain.getRightAccel();
        }
        if (drivetrain.getLeftAccel() > drivetrain.topLeftAccel) {
            drivetrain.topRightAccel = drivetrain.getLeftAccel();
        }
        if (drivetrain.getRightJerk() > drivetrain.topRightJerk) {
            drivetrain.topRightJerk = drivetrain.getRightJerk();
        }
        if (drivetrain.getLeftJerk() > drivetrain.topLeftJerk) {
            drivetrain.topLeftJerk = drivetrain.getLeftJerk();
        }

        // Set last values
        drivetrain.lastRightSpeed = drivetrain.getRightSpeed();
        drivetrain.lastLeftSpeed = drivetrain.getLeftSpeed();
        drivetrain.lastRightAccel = drivetrain.getRightAccel();
        drivetrain.lastLeftAccel = drivetrain.getLeftAccel();

        // Record top values. For science.
        SmartDashboard.putNumber("Top right speed", drivetrain.topRightSpeed);
        SmartDashboard.putNumber("Top right accel", drivetrain.topRightAccel);
        SmartDashboard.putNumber("Top right jerk", drivetrain.topRightJerk);
        SmartDashboard.putNumber("Top left speed", drivetrain.topLeftSpeed);
        SmartDashboard.putNumber("Top left accel", drivetrain.topLeftAccel);
        SmartDashboard.putNumber("Top left jerk", drivetrain.topLeftJerk);

        SmartDashboard.putNumber("Right speed", drivetrain.getRightSpeed());
        SmartDashboard.putNumber("Right accel", drivetrain.getRightAccel());
        SmartDashboard.putNumber("Right jerk",  drivetrain.getRightJerk());
        SmartDashboard.putNumber("Left speed",  drivetrain.getLeftSpeed());
        SmartDashboard.putNumber("Left accel",  drivetrain.getLeftAccel());
        SmartDashboard.putNumber("Left jerk",   drivetrain.getLeftJerk());

    }

    public void testPeriodic() {}


    // Reset all subsystems
    public void reset () {

        drivetrain.reset();

    }

}
