package org.greasemonkeys457.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.greasemonkeys457.robot2018.RobotMap;

public class Elevator extends Subsystem {

    // Hardware
    private final TalonSRX motor;
    private final Encoder encoder;

    public Elevator () {

        // Define hardware
        motor = new TalonSRX(RobotMap.elevatorMotor);
        encoder = new Encoder(RobotMap.elevatorEncoderA, RobotMap.elevatorEncoderB);

        // TODO: Configure talon (voltage limits)
        // TODO: Configure encoder

    }

    // TODO: Set position method

    public void reset () {

        // Return hardware to its' default state
        // TODO: Set default position

    }

    @Override
    protected void initDefaultCommand() {
        // No default command.
        // Perhaps a "hold position" command?
    }

}
