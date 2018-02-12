package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class MandibleToggleGrip extends Command {

    public MandibleToggleGrip () {
        requires(Robot.mandible);
    }

    public void execute () {

        // Toggle the state of the mandible's grip.
        Robot.mandible.setGripping(!Robot.mandible.isGripping());

    }

    @Override
    protected boolean isFinished() {

        // We only want this command to be run once, so
        return true;

    }

}
