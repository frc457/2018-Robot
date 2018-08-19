package org.greasemonkeys457.robot2018.util.oxilib;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * Allows us to use the D-pad like normal buttons.
 * @author Benji Z
 */
public class POVButton extends Button {

    private final GenericHID joystick;
    private final int angle;

    public POVButton (GenericHID joystick, int angle) {
        this.joystick = joystick;
        this.angle = angle;
    }

    @Override
    public boolean get() {
        return joystick.getPOV() == angle;
    }
}
