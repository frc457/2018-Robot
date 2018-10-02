package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class MandibleClose extends Command {

    public MandibleClose () {
        requires(Robot.mandible);
    }

    public void execute () {
        Robot.mandible.setGripping(true);
    }

    protected boolean isFinished() {
        return true;
    }

}
