package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class DriveFromJoysticks extends Command {

    public DriveFromJoysticks () {
        requires(Robot.drivetrain);
    }

    protected void execute () {

        // Get input from the driver's controller
        double rightSpeed = -Robot.oi.driverController.getRawAxis(5);
        double leftSpeed = -Robot.oi.driverController.getRawAxis(1);

        // Triggers are used to drive straight backward / forward
        double rightTrigger = Robot.oi.driverController.getRawAxis(3);
        double leftTrigger = Robot.oi.driverController.getRawAxis(2);

        // Straight forward using right trigger
        if (Math.abs(rightTrigger) >= 0.01 ) {
            rightSpeed = rightTrigger;
            leftSpeed = rightTrigger;
        }

        // Straight backward using left trigger
        if (Math.abs(leftTrigger) >= 0.01 ) {
            rightSpeed = -leftTrigger;
            leftSpeed = -leftTrigger;
        }

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
