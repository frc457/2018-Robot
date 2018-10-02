package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class MandibleSpeedForTime extends Command {

    private double speed;

    public MandibleSpeedForTime (double speed, double time) {
        requires(Robot.mandible);
        this.speed = speed;
        setTimeout(time);
    }

    public void execute () {
        Robot.mandible.setSpeed(speed);
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

}
