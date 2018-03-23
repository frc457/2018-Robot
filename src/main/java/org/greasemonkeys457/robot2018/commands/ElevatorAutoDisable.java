package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;
import org.greasemonkeys457.robot2018.subsystems.Elevator;

/**
 * This command is basically an emergency command. If the elevator's
 * closed-loop controller goes out of control, use this command to stop it.
 */
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
