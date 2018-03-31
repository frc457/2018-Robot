package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class DriveSpeedForTime extends Command {

    private double speed;

    public DriveSpeedForTime (double speed, double time) {
        requires(Robot.drivetrain);

        setTimeout(time);
        this.speed = speed;
    }

    public void execute () {
        Robot.drivetrain.setLeftSpeed(speed);
        Robot.drivetrain.setRightSpeed(speed);
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

}
