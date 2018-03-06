package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class MandibleFromJoysticks extends Command {

    public MandibleFromJoysticks () {

        // This command uses the mandible, so
        requires(Robot.mandible);

    }

    public void execute () {

        // Grab input from the controller
        double leftJoystick = -Robot.oi.operatorController.getRawAxis(1);
        double rightJoystick = -Robot.oi.operatorController.getRawAxis(5);
        //double rightTrigger = Robot.oi.operatorController.getRawAxis(3);
        //double leftTrigger = Robot.oi.operatorController.getRawAxis(2);
        // TODO: Triggers are disabled here because they're being used for the elevator.
        // Reimplement triggers once we have position control for the elevator
        double leftTrigger = 0;
        double rightTrigger = 0;

        // Decide what control method to use
        if (Math.abs(leftTrigger) > 0.01) {

            // Run the motors inward at the same speed
            Robot.mandible.setSpeed(-leftTrigger);

        }

        else if (Math.abs(rightTrigger) > 0.01) {

            // Run the motors outward at the same speed
            Robot.mandible.setSpeed(rightTrigger);

        }

        else {

            // Run the motors using the joysticks
            Robot.mandible.setSpeed(leftJoystick, rightJoystick);

        }

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
