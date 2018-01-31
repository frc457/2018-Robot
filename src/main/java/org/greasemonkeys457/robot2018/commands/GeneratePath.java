package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class GeneratePath extends Command {

    public GeneratePath () {
        requires(Robot.drivetrain);
    }

    public void execute () {
        Robot.drivetrain.generatePath();
    }

    public boolean isFinished () {
        return true;
    }

}
