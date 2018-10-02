package org.greasemonkeys457.robot2018.util.oxilib;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

public class AxisButton extends Button {

    private final GenericHID joystick;
    private final int axis;
    private double threshold;

    /**
     * Create a new AxisButton with default threshold 0.5.
     */
    public AxisButton (GenericHID joystick, int axis) {
        this(joystick, axis, 0.5);
    }

    /**
     * Create a new AxisButton.
     */
    public AxisButton (GenericHID joystick, int axis, double threshold) {
        this.joystick = joystick;
        this.axis = axis;
        this.threshold = threshold;
    }

    public boolean get() {
        return joystick.getRawAxis(axis) >= threshold;
    }

}
