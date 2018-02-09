package org.greasemonkeys457.robot2018;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greasemonkeys457.robot2018.commands.AutonomousSelector;
import org.greasemonkeys457.robot2018.commands.AutonomousSelector.StartingPosition;
import org.greasemonkeys457.robot2018.commands.AutonomousSelector.Goal;
import org.greasemonkeys457.robot2018.subsystems.Drivetrain;
import org.greasemonkeys457.robot2018.util.paths.CenterToRightSwitch;
import org.greasemonkeys457.robot2018.util.paths.Path;

public class Robot extends IterativeRobot {

    // Subsystems
    public static Drivetrain drivetrain;

    // OI
    public static OI oi;

    // TODO Remove test code
    Path path = new CenterToRightSwitch();

    // Autonomous chooser
    SendableChooser<Command> chooser;
    SendableChooser<StartingPosition> stposChooser;
    SendableChooser<Goal> goalChooser;

    public void robotInit() {

        // Define subsystems
        drivetrain = new Drivetrain();

        // Define OI
        oi = new OI();

        // Starting position chooser
        stposChooser = new SendableChooser<>();
        stposChooser.setName("Starting Position:");
        stposChooser.addDefault("Left", StartingPosition.Left);
        stposChooser.addObject("Center", StartingPosition.Center);
        stposChooser.addObject("Right", StartingPosition.Right);

        // Goal chooser
        goalChooser = new SendableChooser<>();
        goalChooser.setName("Goal:");
        goalChooser.addDefault("Nothing", Goal.Nothing);
        goalChooser.addObject("Baseline", Goal.Baseline);
        goalChooser.addObject("Switch", Goal.Switch);
        goalChooser.addObject("Scale", Goal.Scale);

        // Put the choosers on the smart dashboard
        SmartDashboard.putData(stposChooser);
        SmartDashboard.putData(goalChooser);

    }

    public void disabledInit() {
        reset();
    }

    public void autonomousInit() {

        // Grab the selected starting position and goal
        StartingPosition startingPosition = stposChooser.getSelected();
        Goal goal = goalChooser.getSelected();

        // Use the selected st. pos. and goal to select an autonomous routine
        Command autoCommand = new AutonomousSelector(startingPosition, goal);

        // Start the autonomous routine
        autoCommand.start();

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

    public void testPeriodic() {

        // TODO: Remove test code
        System.out.println("--------------------------------------------------");
        System.out.println("Robot is in test mode!");
        System.out.println("Press one of the buttons to perform one of the actions:");
        System.out.println("A. Generate a path");
        System.out.println("B. Save a path");
        System.out.println("X. Load a path");
        System.out.println("Y. Validate a path");
        System.out.println("--------------------------------------------------");

        boolean ap = false;
        boolean bp = false;
        boolean xp = false;
        boolean yp = false;

        // A button
        if (oi.driverA.get() && !ap) {
            ap = true;
            System.out.println("Generating path...");
            path.generateTrajectory();
            System.out.println("Done generating path!");
            System.out.println("--------------------------------------------------");
        } else ap = false;

        // B button
        if (oi.driverB.get() && !bp) {
            bp = true;
            System.out.println("Saving path...");
            path.savePath();
            System.out.println("Done saving path!");
            System.out.println("--------------------------------------------------");
        } else bp = false;

        // X button
        if (oi.driverX.get() && !xp) {
            xp = true;
            System.out.println("Loading save...");
            path.loadSave();
            System.out.println("Save loaded!");
            System.out.println("--------------------------------------------------");
        } else xp = false;

        // Y button
        if (oi.driverY.get() && !yp) {
            yp = true;
            System.out.println("Validating path...");
            path.validateLoadedSave();
            System.out.println("Path validation done!");
            System.out.println("--------------------------------------------------");
        }

    }


    // Reset all subsystems
    public void reset () {

        drivetrain.reset();

    }

}
