package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class DriveRunGearboxes extends Command {

    private double speed;

    public DriveRunGearboxes (double speed) {

        // This command uses the drivetrain, so
        requires(Robot.drivetrain);

        // Remember the speed we want to run the motors at
        this.speed = speed;

    }

    public void execute () {

        // Set the speed
        Robot.drivetrain.setRightSpeed(speed);
        Robot.drivetrain.setLeftSpeed(speed);

    }

    public boolean isFinished () {

        // Stop the command if we set it to be 0.0
        if (speed == 0.0)
            return true;
        else
            return false;

    }

}
