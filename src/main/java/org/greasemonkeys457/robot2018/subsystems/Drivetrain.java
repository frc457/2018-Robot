package org.greasemonkeys457.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.greasemonkeys457.robot2018.Constants;
import org.greasemonkeys457.robot2018.RobotMap;
import org.greasemonkeys457.robot2018.commands.DriveFromJoysticks;

public class Drivetrain extends Subsystem {

    // Actuators
    TalonSRX rightMaster;
    TalonSRX rightFollower;
    TalonSRX leftMaster;
    TalonSRX leftFollower;

    DoubleSolenoid shifter;

    // Sensors
    public Encoder rightEncoder;
    public Encoder leftEncoder;

    // State variables
    public boolean isLowGear;

    // Constants
    double scale = Constants.scale;
    double wheelDiameter = Constants.wheelDiameter;
    double encoderPulsesPerRev = Constants.pulsesPerRev;

    public Drivetrain () {

        rightMaster   = new TalonSRX(RobotMap.rfMotor);
        rightFollower = new TalonSRX(RobotMap.rbMotor);
        leftMaster    = new TalonSRX(RobotMap.lfMotor);
        leftFollower  = new TalonSRX(RobotMap.lbMotor);

        shifter = new DoubleSolenoid(RobotMap.shifterForward, RobotMap.shifterReverse);

        // Encoders
        // Note: This code only works when the encoders are plugged into the DIO on the roboRIO.
        //       If we connect the encoders through the talons, this configuration will be different.
        rightEncoder = new Encoder(RobotMap.rightEncoderA, RobotMap.rightEncoderB);
        leftEncoder  = new Encoder(RobotMap.leftEncoderA,  RobotMap.leftEncoderB);

        // Encoder configuration
        rightEncoder.setDistancePerPulse((wheelDiameter * Math.PI) / encoderPulsesPerRev);
        leftEncoder .setDistancePerPulse((wheelDiameter * Math.PI) / encoderPulsesPerRev);

        leftEncoder.setReverseDirection(true);

        // TODO: NavX

        // ----- Talon configuration begins -----

        // Set follower talons
        rightFollower.follow(rightMaster);
        leftFollower .follow(leftMaster);

        // Invert the right side
        rightMaster  .setInverted(true);
        rightFollower.setInverted(true);

        // ------ Talon configuration ends ------

    }

    public void setRightSpeed (double speed) {
        rightMaster.set(ControlMode.PercentOutput, speed);
    }
    public void setLeftSpeed (double speed) {
        leftMaster.set(ControlMode.PercentOutput, speed);
    }

    public void shiftToLow () {

        // Move the shifters
        shifter.set(DoubleSolenoid.Value.kForward);

        // Set the state variable
        isLowGear = true;

    }
    public void shiftToHigh () {

        // Move the shifters
        shifter.set(DoubleSolenoid.Value.kReverse);

        // Set the state variable
        isLowGear = false;

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

        rightEncoder.reset();
        leftEncoder .reset();

    }

    public void initDefaultCommand () {
        setDefaultCommand(new DriveFromJoysticks());
    }

}
