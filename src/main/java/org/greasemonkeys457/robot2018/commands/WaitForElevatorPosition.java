package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.TimedCommand;
import org.greasemonkeys457.robot2018.Robot;
import org.greasemonkeys457.robot2018.subsystems.Elevator;

/**
 * This command doesn't finish until either:
 *   1) The elevator reaches its' target position, or
 *   2) It times out.
 *
 * Super hacky, but it works for what we need it to do.
 */
public class WaitForElevatorPosition extends TimedCommand {

    public WaitForElevatorPosition () {
        this(5);
    }
    public WaitForElevatorPosition (double timeout) {

        // Set the timeout
        super(timeout);

        // This command uses the elevator, so
        requires(Robot.elevator);

    }

    public void execute () {

        // Debug
        System.out.println("Waiting for elevator to reach its' target position...");

        // Periodic call (since the periodic command isn't running while this command is)
        Elevator.controller.periodic();

    }

    public boolean isFinished () {
        return Robot.elevator.atTargetPosition() || isTimedOut();
    }

}
