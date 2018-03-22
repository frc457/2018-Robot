package org.greasemonkeys457.robot2018.util.oxilib;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class LogitechController {

    private Joystick joystick;

    public enum ButtonKey {
        A(1),
        B(2),
        X(3),
        Y(4),
        LB(5),
        RB(6),
        Back(7),
        Start(8),
        LS(9),
        RS(10);

        private final int id;

        ButtonKey (int id) {
            this.id = id;
        }
    }

    public enum POVButtonKey {
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

        POVButtonKey (int angle) {
            this.angle = angle;
        }
    }

    public enum AxisKey {
        LX(0),
        LY(1),
        LTrigger(2),
        RTrigger(3),
        RX(4),
        RY(5);

        private final int id;

        AxisKey (int id) {
            this.id = id;
        }
    }

    /**
     * Instantiate a new logitech controller.
     * @param port
     */
    public LogitechController (int port) {
        joystick = new Joystick(port);
    }

    public Button getButton (ButtonKey button) {
        return getButton(button.id);
    }
    public Button getButton (int id) {
        return new JoystickButton(joystick, id);
    }

    public Button getPOVButton (POVButtonKey direction) {
        return getPOVButton(direction.angle);
    }
    public Button getPOVButton (int angle) {
        return new POVButton(joystick, angle);
    }

}
