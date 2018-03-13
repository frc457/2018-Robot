package org.greasemonkeys457.robot2018;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.greasemonkeys457.robot2018.commands.*;
import org.greasemonkeys457.robot2018.util.oxilib.POVButton;

public class OI {

    // Controllers
    public Joystick driverController = new Joystick(0);
    public Joystick operatorController = new Joystick(1);

    // Buttons
    public Button driverA  = new JoystickButton(driverController, 1);
    public Button driverLB = new JoystickButton(driverController, 5);
    public Button driverRB = new JoystickButton(driverController, 6);

    public Button operatorA  = new JoystickButton(operatorController, 1);
    public Button operatorLB = new JoystickButton(operatorController, 6);

    public Button operatorUp = new POVButton(operatorController, POVButton.Direction.UP);
    public Button operatorDown = new POVButton(operatorController, POVButton.Direction.DOWN);

    public OI () {

        // Drive shifting
        driverA.whenPressed(new DriveToggleGears());
        driverLB.whenPressed(new DriveShiftToLow());
        driverRB.whenPressed(new DriveShiftToHigh());

        // Mandible control
        operatorLB.whenPressed(new MandibleToggleGrip());

        /*
        operatorUp.whenPressed(new MandibleSetSpeed(.75));
        operatorUp.whenReleased(new MandibleSetSpeed(0.0));

        operatorDown.whenPressed(new MandibleSetSpeed(-.75));
        operatorDown.whenReleased(new MandibleSetSpeed(0.0));
        */

    }

}
