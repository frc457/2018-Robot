package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;
import org.greasemonkeys457.robot2018.subsystems.Elevator;

public class ElevatorHoldPosition extends Command {

    public ElevatorHoldPosition () {

        // This command uses the elevator, so
        requires(Robot.elevator);

    }

    public void execute () {

        // PID gains
        // TODO: Add these values to the smart dashboard so they can be tuned whenever needed
        double kP = 0.0;
        double kI = 0.0;
        double kD = 0.0;

        // Calculate error
        double targetPosition = Robot.elevator.getTargetPosition();
        double actualPosition = Robot.elevator.getCurrentPosition();
        double error = targetPosition - actualPosition;

        // TODO: Add a PID loop to control elevator position

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
