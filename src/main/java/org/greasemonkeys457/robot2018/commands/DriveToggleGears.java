package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class DriveToggleGears extends Command {

    public DriveToggleGears () {
        requires(Robot.drivetrain);
    }

    public void execute () {

        // Toggle the position of the shifters
        if (Robot.drivetrain.areShiftersForward)
            Robot.drivetrain.shiftersReverse();
        else
            Robot.drivetrain.shiftersForward();

    }

    public boolean isFinished () {

        // We only want this command to run once, so the command ends immediately.
        return true;

    }

}
