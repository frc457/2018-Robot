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

public class Robot extends IterativeRobot {

    // Subsystems
    public static Drivetrain drivetrain;

    // OI
    public static OI oi;

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
        stposChooser.addDefault("Left", StartingPosition.Left);
        stposChooser.addObject("Center", StartingPosition.Center);
        stposChooser.addObject("Right", StartingPosition.Right);

        // Goal chooser
        goalChooser = new SendableChooser<>();
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

    public void testPeriodic() {}


    // Reset all subsystems
    public void reset () {

        drivetrain.reset();

    }

}
