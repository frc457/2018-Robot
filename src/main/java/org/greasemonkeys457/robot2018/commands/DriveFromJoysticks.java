package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class DriveFromJoysticks extends Command {

    public DriveFromJoysticks () {
        requires(Robot.drivetrain);
    }

    protected void execute () {

        // TODO: Possibly flip rightSpeed and leftSpeed

        // Get input from the driver's controller
        double rightSpeed = Robot.oi.driverController.getRawAxis(5);
        double leftSpeed = Robot.oi.driverController.getRawAxis(1);

        // Scale it accordingly
        rightSpeed = Robot.drivetrain.driveScaling(rightSpeed);
        leftSpeed = Robot.drivetrain.driveScaling(leftSpeed);

        // Set the speed
        Robot.drivetrain.setRightSpeed(rightSpeed);
        Robot.drivetrain.setLeftSpeed(leftSpeed);

    }

    protected boolean isFinished () {
        return false;
    }

}
