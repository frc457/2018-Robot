package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class DriveFromJoysticks extends Command {

    // Initial angle value
    private double angle = 0.0;

    public DriveFromJoysticks () {
        requires(Robot.drivetrain);
    }

    protected void execute () {

        // Input variables
        double rightJoystick = -Robot.oi.driverController.getRawAxis(5);
        double leftJoystick = -Robot.oi.driverController.getRawAxis(1);
        double rightTrigger = Robot.oi.driverController.getRawAxis(3);
        double leftTrigger = Robot.oi.driverController.getRawAxis(2);

        // Output variables
        double rightOutput;
        double leftOutput;

        // Decide the speed to be used
        if (Math.abs(rightTrigger) >= 0.01) {

            // Drive straight forward if the right trigger is being pressed
            rightOutput = rightTrigger;
            leftOutput = rightTrigger;

        } else if (Math.abs(leftTrigger) >= 0.01) {

            // Drive straight backwards if the left trigger is being pressed
            rightOutput = -leftTrigger;
            leftOutput = -leftTrigger;

        } else {

            // Drive using joysticks if neither of the triggers are being pressed
            rightOutput = rightJoystick;
            leftOutput = leftJoystick;

            // Reset the angle if neither of them are being pressed
            angle = 0.0;

        }

        // Set the target angle if either of the triggers are being pressed
        if (((Math.abs(rightTrigger) >= 0.01) || (Math.abs(leftTrigger) >= 0.01)) && (angle == 0.0)) {
            angle = Robot.drivetrain.getYaw();
        }

        // Scale it accordingly
        rightOutput = Robot.drivetrain.driveScaling(rightOutput);
        leftOutput = Robot.drivetrain.driveScaling(leftOutput);

        // Hold angle logic
        double turn;

        if (angle != 0.0) {

            // Angle control PI loop
            double error = -(angle - Robot.drivetrain.getYaw());

            // Bound the value of the angle to -180, 180
            while (error >= 180) error -= 360;
            while (error < -180) error += 360;

            // Turn calculation
            turn = (2.0 * (1.0/80.0) * error);

        } else {
            turn = 0.0;
        }

        // Set the speed
        Robot.drivetrain.setRightSpeed(rightOutput - turn);
        Robot.drivetrain.setLeftSpeed(leftOutput + turn);

    }

    protected boolean isFinished () {
        return false;
    }

}
