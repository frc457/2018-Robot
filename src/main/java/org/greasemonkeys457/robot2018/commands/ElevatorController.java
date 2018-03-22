package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

/**
 * Controls the elevator.
 */
public class ElevatorController extends Command {

    // State variables
    private boolean sHPRunning = false;
    private boolean sHPEnabled = false;

    public ElevatorController() {
        requires(Robot.elevator);
    }

    public void execute () {

        // Grab operator input
        double opRTrig = Robot.oi.operatorController.getRawAxis(3);
        double opLTrig = Robot.oi.operatorController.getRawAxis(2);

        // Eliminate deadzones
        opRTrig = noDeadzones(opRTrig);
        opLTrig = noDeadzones(opLTrig);

        // Move the elevator if needed
        if (opRTrig != 0)
            manualControl(opRTrig);
        else if (opLTrig != 0)
            manualControl(-opLTrig);

        // Enable position control if there's no input
        else sHPRunning = true;

        // Hold position if enabled
        if (sHPEnabled) holdPosition();

    }

    private void manualControl (double speed) {

        // Disable position control
        sHPRunning = false;

        // TODO: Limit position so the operator can't tear up the elevator

        // Move the elevator
        Robot.elevator.setSpeed(speed);

        // TODO: Update target position

    }

    // TODO: This function
    private void holdPosition () {

        if (sHPRunning) {

            // Proportional gain
            double kP = 1.0 / 64.0;

            // Calculate error
            int error = Robot.elevator.getTargetPosition() - Robot.elevator.getCurrentPosition();

            double output = (double)error * kP;

            Robot.elevator.setSpeed(output);

        }

    }

    private double noDeadzones (double input) {

        // TODO: Make a joystick wrapper class and move this function there
        if (Math.abs(input) <= 0.01) return 0;
        else return input;

    }

    protected boolean isFinished() {
        return false;
    }

}
