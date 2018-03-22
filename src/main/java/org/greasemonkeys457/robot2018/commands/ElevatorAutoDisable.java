package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;
import org.greasemonkeys457.robot2018.subsystems.Elevator;

public class ElevatorAutoDisable extends Command {

    public ElevatorAutoDisable () {
        requires(Robot.elevator);
    }

    public void execute () {
        Elevator.controller.disable();
    }

    protected boolean isFinished() {
        return true;
    }

}
