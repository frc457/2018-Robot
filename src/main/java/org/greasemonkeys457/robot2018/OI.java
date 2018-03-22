package org.greasemonkeys457.robot2018;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.greasemonkeys457.robot2018.commands.*;
import org.greasemonkeys457.robot2018.util.oxilib.LogitechController;
import org.greasemonkeys457.robot2018.util.oxilib.POVButton;

import static org.greasemonkeys457.robot2018.util.oxilib.LogitechController.ButtonKey.*;
import static org.greasemonkeys457.robot2018.util.oxilib.LogitechController.POVButtonKey.*;

public class OI {

    // Controllers
    public LogitechController driverController = new LogitechController(0);
    public LogitechController operatorController = new LogitechController(1);

    // Buttons
    public Button driverA  = driverController.getButton(A);
    public Button driverB  = driverController.getButton(B);
    public Button driverX  = driverController.getButton(X);
    public Button driverLB = driverController.getButton(LB);
    public Button driverRB = driverController.getButton(RB);

    public Button operatorA  = operatorController.getButton(A);
    public Button operatorLB = operatorController.getButton(LB);

    public Button operatorUp   = operatorController.getPOVButton(UP);
    public Button operatorDown = operatorController.getPOVButton(DOWN);

    public OI () {

        // Drive shifting
        driverA.whenPressed(new DriveToggleGears());
        driverLB.whenPressed(new DriveShiftToLow());
        driverRB.whenPressed(new DriveShiftToHigh());

        // Mandible control
        operatorLB.whenPressed(new MandibleToggleGrip());

        // Elevator control testing
        driverB.whenPressed(new ElevatorAutoDisable());
        driverX.whenPressed(new ElevatorAutoEnable());

    }

}
