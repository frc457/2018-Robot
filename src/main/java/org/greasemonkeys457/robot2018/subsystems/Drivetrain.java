package org.greasemonkeys457.robot2018.subsystems;

import com.ctre.CANTalon;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.greasemonkeys457.robot2018.Constants;
import org.greasemonkeys457.robot2018.RobotMap;
import org.greasemonkeys457.robot2018.commands.DriveFromJoysticks;

public class Drivetrain extends Subsystem {

    // Actuators
    SpeedController rfMotor;
    SpeedController rbMotor;
    SpeedController lfMotor;
    SpeedController lbMotor;

    DoubleSolenoid shifter;

    // Sensors
    Encoder rightEncoder;
    Encoder leftEncoder;

    // State variables
    public boolean areShiftersForward;

    // Constants
    double scale = Constants.scale;

    public Drivetrain () {

        rfMotor = new WPI_TalonSRX(RobotMap.rfMotor);
        rbMotor = new WPI_TalonSRX(RobotMap.rbMotor);
        lfMotor = new WPI_TalonSRX(RobotMap.lfMotor);
        lbMotor = new WPI_TalonSRX(RobotMap.lbMotor);

        shifter = new DoubleSolenoid(RobotMap.shifterForward, RobotMap.shifterReverse);

        // TODO: Encoders?

        // TODO: NavX

        // TODO: Talon config

    }

    public void setRightSpeed (double speed) {
        rfMotor.set(speed);
        rbMotor.set(speed);
    }
    public void setLeftSpeed (double speed) {
        rfMotor.set(speed);
        rbMotor.set(speed);
    }

    // TODO: Rename methods to shiftToHigh and shiftToLow
    public void shiftersForward () {

        // Move the shifters
        shifter.set(DoubleSolenoid.Value.kForward);

        // Set the state variable
        areShiftersForward = true;

    }
    public void shiftersReverse () {

        // Move the shifters
        shifter.set(DoubleSolenoid.Value.kReverse);

        // Set the state variable
        areShiftersForward = false;

    }

    public double driveScaling (double speed) {

        // Run the input speed through the scaling function.
        speed = (speed * Math.abs(speed) * Math.abs(speed) * scale);

        if (Math.abs(speed) < 0.001) {
            speed = 0.0;
        }

        return speed;

    }

    public void reset () {

        // TODO: Zero all sensors, stop all motors, return shifters to initial position

    }

    public void initDefaultCommand () {
        setDefaultCommand(new DriveFromJoysticks());
    }

}
