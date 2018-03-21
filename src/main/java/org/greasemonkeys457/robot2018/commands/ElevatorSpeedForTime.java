package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class ElevatorSpeedForTime extends Command {

    private double speed;

    /**
     * Set the elevator speed for x seconds.
     * @param speed Speed, in percentage
     * @param time Time to be run, in seconds
     */
    public ElevatorSpeedForTime (double speed, double time) {
        requires(Robot.elevator);
        this.speed = speed;
        setTimeout(time);
    }

    public void execute () {
        Robot.elevator.setSpeed(speed);
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }
}
