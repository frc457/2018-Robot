package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class MandibleOpen extends Command {

    public MandibleOpen () {
        requires(Robot.mandible);
    }

    public void execute () {
        Robot.mandible.setGripping(false);
    }

    protected boolean isFinished() {
        return true;
    }

}
