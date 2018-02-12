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

        // Decide the speed to be used
        if (Math.abs(rightTrigger) >= 0.01) {

            // Drive straight forward if the right trigger is being pressed
            handleStraightDrive(rightTrigger);

        } else if (Math.abs(leftTrigger) >= 0.01) {

            // Drive straight backwards if the left trigger is being pressed
            handleStraightDrive(-leftTrigger);

        } else {

            // Drive using joysticks if neither of the triggers are being pressed
            handleTankDrive(rightJoystick, leftJoystick);

        }

    }

    private void handleTankDrive (double rightInput, double leftInput) {

        // Use the drivetrain's tankDrive method to control tank drive
        Robot.drivetrain.tankDrive(rightInput, leftInput);

        // Reset the angle for straight drive
        angle = 0.0;

    }

    private void handleStraightDrive (double speed) {

        // Set the angle to hold if it hasn't been set already
        if (angle == 0.0) {
            angle = Robot.drivetrain.getYaw();
        }

        // Angle control P loop
        double error = -(angle - Robot.drivetrain.getYaw());

        // Bound the value of the angle to -180, 180
        while (error >= 180) error -= 360;
        while (error < -180) error += 360;

        // Turn calculation
        double turn = (2.0 * (1.0/80.0) * error);

        // Set the speed for each side
        Robot.drivetrain.setRightSpeed(speed - turn);
        Robot.drivetrain.setLeftSpeed(speed + turn);

    }

    protected boolean isFinished () {
        return false;
    }

}
