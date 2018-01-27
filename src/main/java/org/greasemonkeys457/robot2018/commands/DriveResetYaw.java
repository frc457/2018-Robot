package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class DriveResetYaw extends Command {

    public DriveResetYaw () {
        requires(Robot.drivetrain);
    }

    public void execute () {
        Robot.drivetrain.navx.zeroYaw();
    }

    public boolean isFinished () {
        // We only want this command to run once, so
        return true;
    }

}
