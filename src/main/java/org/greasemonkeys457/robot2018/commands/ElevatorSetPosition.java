package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Constants.ElevatorPosition;
import org.greasemonkeys457.robot2018.Robot;

public class ElevatorSetPosition extends Command {

    private int height;

    public ElevatorSetPosition (int height) {

        // This command uses the elevator, so
        requires(Robot.elevator);

        // Remember the position
        this.height = height;

    }

    public ElevatorSetPosition (ElevatorPosition position) {
        this(position.ticks);
    }

    public void execute () {

        // Set the position
        Robot.elevator.setTargetPosition(height);

        // TODO: Debug code
        System.out.println("Set target height to " + height);

    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
