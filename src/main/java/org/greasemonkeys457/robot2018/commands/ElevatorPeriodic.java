package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;
import org.greasemonkeys457.robot2018.subsystems.Elevator;

public class ElevatorPeriodic extends Command {

    public ElevatorPeriodic () {
        requires(Robot.elevator);
    }

    public void execute () {
        Elevator.controller.periodic();
    }

    protected boolean isFinished() {
        return false;
    }

}
