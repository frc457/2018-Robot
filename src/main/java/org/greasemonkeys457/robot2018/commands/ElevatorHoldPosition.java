package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;
import org.greasemonkeys457.robot2018.subsystems.Elevator;

public class ElevatorHoldPosition extends Command {

    public ElevatorHoldPosition () {

        // This command uses the elevator, so
        requires(Robot.elevator);

    }

    public void execute () {

        // Calculate error
        double targetPosition = Robot.elevator.getTargetPosition();
        double actualPosition = Robot.elevator.getCurrentPosition();
        double error = targetPosition - actualPosition;

        // TODO: Find feedforward (just apply voltage until the elevator doesn't fall)
        // TODO: Implement P + FF controller

        // P controller
        double output = 0.1 * error;
        if (output >= 1) output = 1;
        if (output <= -1) output = -1;

        Robot.elevator.setSpeed(output);

    }

    protected boolean isFinished() {
        return false;
    }

}
