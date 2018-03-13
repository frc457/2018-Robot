package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;
import org.greasemonkeys457.robot2018.subsystems.Elevator;

public class ElevatorFromJoysticks extends Command {

    public ElevatorFromJoysticks () {

        // This command uses the elevator, so
        requires(Robot.elevator);

    }

    public void execute () {

        // Grab the controller's input
        double rightTrigger = Robot.oi.operatorController.getRawAxis(3);
        double leftTrigger = Robot.oi.operatorController.getRawAxis(2);

        // Calculate the speed we want to use using the triggers
        double output;
        if (rightTrigger >= 0.01)
            output = rightTrigger;
        else if (leftTrigger >= 0.01)
            output = -leftTrigger;
        else
            output = 0.0;

        // TODO: Remove test code
        System.out.println("Elevator output: " + output);

        // Set the speed of the elevator motors
        Robot.elevator.setSpeed(output);

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
