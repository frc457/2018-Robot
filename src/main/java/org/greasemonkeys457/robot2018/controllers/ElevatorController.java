package org.greasemonkeys457.robot2018.controllers;

import org.greasemonkeys457.robot2018.Robot;

/**
 * Controls the elevator.
 */
public class ElevatorController {

    // State variables
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

        if (sHPEnabled) {

            // Manual control if the triggers are being pressed
            if      (opRTrig > 0) manualControl(opRTrig);
            else if (opLTrig > 0) manualControl(-opLTrig);

            // Automatic control if they aren't
            else holdPosition();

        } else {

            // Super manual control (straight set speed)
            if      (opRTrig > 0) superManualControl(opRTrig);
            else if (opLTrig > 0) superManualControl(-opLTrig);
            else superManualControl(0);

        }

    }

    private void superManualControl (double speed) {
        Robot.elevator.setSpeed(speed);
    }

    private void manualControl (double speed) {

        // Get limit based on direction
        int limit = Robot.elevator.getCurrentPosition();
        if (speed > 0) limit = Robot.elevator.getMaxTicks();
        if (speed < 0) limit = Robot.elevator.getMinTicks();

        // Calculate distance from limit (sign = direction, num = magnitude)
        int distFromLimit = limit - Robot.elevator.getCurrentPosition();

        // Calculate max speed at this point
        double maxSpeed = (double)distFromLimit * kP;
        maxSpeed = roundPercentOutput(maxSpeed);

        // Determine which speed we want to use
        double output = 0.0;
        if (Math.abs(speed) > Math.abs(maxSpeed)) output = maxSpeed;
        if (Math.abs(speed) < Math.abs(maxSpeed)) output = speed;

        // Set speed
        Robot.elevator.setSpeed(output);

        // Reset target position
        Robot.elevator.setTargetPosition(Robot.elevator.getCurrentPosition());

    }

    private void holdPosition () {

        // Calculate error & output
        int error = Robot.elevator.getTargetPosition() - Robot.elevator.getCurrentPosition();
        double output = (double)error * kP;

        // Set speed
        if (sHPEnabled) Robot.elevator.setSpeed(output);

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
