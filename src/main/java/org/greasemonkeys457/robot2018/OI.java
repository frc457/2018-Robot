package org.greasemonkeys457.robot2018;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.greasemonkeys457.robot2018.commands.*;

public class OI {

    // Controllers
    public Joystick driverController = new Joystick(0);
    public Joystick operatorController = new Joystick(1);

    // Buttons
    public Button driverA  = new JoystickButton(driverController, 1);
    public Button driverLB = new JoystickButton(driverController, 5);
    public Button driverRB = new JoystickButton(driverController, 6);

    public OI () {

        // Drive shifting
        driverA.whenPressed(new DriveToggleGears());
        driverLB.whenPressed(new DriveShiftToLow());
        driverRB.whenPressed(new DriveShiftToHigh());

        // TODO: Add control for the mandible

    }

}
