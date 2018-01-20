package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class DriveShiftersForward extends Command {

    // TODO: Rename class to 'DriveShiftToHigh' or 'DriveShiftToLow'

    public DriveShiftersForward () {
        requires(Robot.drivetrain);
    }

    public void execute () {
        Robot.drivetrain.shiftToLow();
    }

    public boolean isFinished () {

        // We only want this command to run once, so the command ends immediately.
        return true;

    }

}
