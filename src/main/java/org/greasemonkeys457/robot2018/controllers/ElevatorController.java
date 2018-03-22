package org.greasemonkeys457.robot2018.controllers;

import org.greasemonkeys457.robot2018.Robot;

/**
 * Controls the elevator.
 */
public class ElevatorController {

    // State variables
    private boolean sHPRunning = false;
    private boolean sHPEnabled = false;

    // Constants
    private static final double kP = 1.0 / 64.0; // start slowing down 1 rotation before limit

    public void periodic () {

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

        if (speed == 0) return;

        // Limit position
        if (Robot.elevator.withinLimits() != 0) {

            // Check if speed is in the direction we're exceeding
            if (Math.signum(speed) == Math.signum(Robot.elevator.withinLimits())) {

                // Warn
                System.out.println("WARNING: Don't try to move outside the limits!");

                // Make sure position control is enabled
                sHPRunning = true;

                // Exit out of this function
                return;

            }

        }

        // Disable position control
        sHPRunning = false;

        // Calculate error
        int limit = 0;
        if (speed > 0) limit = Robot.elevator.getMaxTicks();
        if (speed < 0) limit = Robot.elevator.getMinTicks();

        int distFromLimit = limit - Robot.elevator.getCurrentPosition();

        // Calculate max speed at current position
        double max = (double) distFromLimit * kP;
        max = roundPercentOutput(max);

        // Calculate output
        double output = (speed > max) ? max : speed;

        // Set speed
        Robot.elevator.setSpeed(output);

        // Update target position
        Robot.elevator.setTargetPosition(Robot.elevator.getCurrentPosition());

    }

    private void holdPosition () {

        // Check if position control is running (false if the operator is controlling manually)
        if (sHPRunning) {

            // Proportional gain
            double kP = 1.0 / 64.0;

            // Calculate error & output
            int error = Robot.elevator.getTargetPosition() - Robot.elevator.getCurrentPosition();
            double output = (double)error * kP;

            // Set speed
            Robot.elevator.setSpeed(output);

        }

    }

    private double noDeadzones (double input) {

        // TODO: Make a joystick wrapper class and move this function there
        if (Math.abs(input) <= 0.01) return 0;
        else return input;

    }

    public void enable () {
        sHPEnabled = true;
    }

    public void disable () {
        sHPEnabled = false;
    }

    /**
     * Make sure we don't try to set a motor's speed to more than 100% or less than -100%.
     * @param input unrounded input
     * @return input, rounded to between -1 and 1
     */
    private double roundPercentOutput (double input) {
        if (input >= 1) return 1;
        if (input <= -1) return -1;
        return input;
    }

}
