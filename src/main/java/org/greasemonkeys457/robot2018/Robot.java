package org.greasemonkeys457.robot2018;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greasemonkeys457.robot2018.commands.AutonomousSelector;
import org.greasemonkeys457.robot2018.commands.AutonomousSelector.StartingPosition;
import org.greasemonkeys457.robot2018.commands.AutonomousSelector.Goal;
import org.greasemonkeys457.robot2018.subsystems.Drivetrain;
import org.greasemonkeys457.robot2018.subsystems.Elevator;
import org.greasemonkeys457.robot2018.subsystems.Mandible;

public class Robot extends IterativeRobot {

    // Subsystems
    public static Drivetrain drivetrain;
    public static Elevator elevator;
    public static Mandible mandible;

    // OI
    public static OI oi;

    // Autonomous chooser
    private SendableChooser<StartingPosition> stposChooser;
    private SendableChooser<Goal> goalChooser;
    private SendableChooser<Boolean> elevatorSafetyChooser;

    // Autonomous command
    Command autoCommand;

    public void robotInit() {

        // Define subsystems
        drivetrain = new Drivetrain();
        elevator = new Elevator();
        mandible = new Mandible();

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

        // Elevator safety chooser
        elevatorSafetyChooser = new SendableChooser<>();
        elevatorSafetyChooser.setName("Elevator");
        elevatorSafetyChooser.addDefault("Disabled", Boolean.FALSE);
        elevatorSafetyChooser.addObject("Enabled", Boolean.TRUE);

        // Put the choosers on the smart dashboard
        SmartDashboard.putData(stposChooser);
        SmartDashboard.putData(goalChooser);
        SmartDashboard.putData("Elevator", elevatorSafetyChooser);

        // Camera
        Thread visionThread = new Thread(() -> {

            UsbCamera usbCamera1 = CameraServer.getInstance().startAutomaticCapture();
            usbCamera1.setResolution(640, 480);

            UsbCamera usbCamera2 = CameraServer.getInstance().startAutomaticCapture();
            usbCamera2.setResolution(640,480);

    });
        visionThread.setDaemon(true);
        visionThread.start();

}

    public void disabledInit() {
        reset();
        if (autoCommand != null) autoCommand.cancel();
    }

    public void autonomousInit() {

        // Grab the selected starting position and goal
        StartingPosition startingPosition = stposChooser.getSelected();
        Goal goal = goalChooser.getSelected();

        // Make sure the controller is activated if we want it to be
        if (Constants.kMoveElevatorInAuto) Elevator.controller.enable();

        // Use the selected st. pos. and goal to select an autonomous routine
        autoCommand = new AutonomousSelector(startingPosition, goal);

        // Start the autonomous routine
        autoCommand.start();

    }

    public void teleopInit() {

        // Enable/disable elevator if neeeded
        if (elevatorSafetyChooser.getSelected() == Boolean.TRUE)
            Elevator.controller.enable();
        else
            Elevator.controller.disable();

        Robot.elevator.setTargetPosition(Constants.ElevatorPosition.MIN.ticks);

    }

    public void testInit() {

        // Generate paths if needed
        Constants.centerToRightSwitch.generatePathIfNeeded(true);
        Constants.centerToLeftSwitch.generatePathIfNeeded(true);
        Constants.leftToLeftSwitch.generatePathIfNeeded(true);
        Constants.leftCrossBaseline.generatePathIfNeeded(true);
        Constants.leftToLeftScale.generatePathIfNeeded(true);
        Constants.rightToRightSwitch.generatePathIfNeeded(true);
        Constants.rightCrossBaseline.generatePathIfNeeded(true);
        Constants.rightToRightScale.generatePathIfNeeded(true);

    }


    public void disabledPeriodic() {}

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("Elevator enc", elevator.getCurrentPosition());
    }

    public void testPeriodic() {}


    // Reset all subsystems
    public void reset () {

        drivetrain.reset();
        elevator.reset();
        mandible.reset();

    }

}
