package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Constants.ElevatorPosition;
import org.greasemonkeys457.robot2018.Robot;

public class ElevatorSetPosition extends Command {

    public ElevatorSetPosition (int height) {

        // This command uses the elevator, so
        requires(Robot.elevator);

        // Set the position
        Robot.elevator.setTargetPosition(height);

    }

    public ElevatorSetPosition (ElevatorPosition position) {
        this(position.ticks);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
