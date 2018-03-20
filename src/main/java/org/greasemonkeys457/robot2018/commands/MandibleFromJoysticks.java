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

        // Run the mandible using the D-pad if the D-pad is being used
        if (Robot.oi.operatorUp.get()) Robot.mandible.setSpeed(1.0);
        else if (Robot.oi.operatorDown.get()) Robot.mandible.setSpeed(-1.0);

        // Use the joysticks otherwise
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
