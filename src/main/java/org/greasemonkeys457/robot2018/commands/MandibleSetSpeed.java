package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class MandibleSetSpeed extends Command {

    double speed;

    public MandibleSetSpeed (double speed) {
        requires(Robot.mandible);
        this.speed = speed;
    }

    public void execute () {
        Robot.mandible.setSpeed(speed);
    }

    @Override
    protected boolean isFinished() {
        if (speed == 0.0) return true;
        else return false;
    }

}
