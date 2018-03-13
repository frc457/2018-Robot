package org.greasemonkeys457.robot2018.util.oxilib;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * Allows us to use the D-pad like normal buttons.
 * @author Benji Z
 */
public class POVButton extends Button {

    public enum Direction {
        UP(0),
        UP_RIGHT(45),
        RIGHT(90),
        DOWN_RIGHT(135),
        DOWN(180),
        DOWN_LEFT(225),
        LEFT(270),
        UP_LEFT(315),
        NEUTRAL(-1);

        public int angle;
        Direction (int angle) {
            this.angle = angle;
        }
    }

    private final GenericHID joystick;
    private final int angle;

    public POVButton (GenericHID joystick, int angle) {
        this.joystick = joystick;
        this.angle = angle;
    }
    public POVButton (GenericHID joystick, Direction direction) {
        this.joystick = joystick;
        this.angle = direction.angle;
    }

    @Override
    public boolean get() {
        return joystick.getPOV() == angle;
    }
}
