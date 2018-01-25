package org.greasemonkeys457.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import org.greasemonkeys457.robot2018.Constants;
import org.greasemonkeys457.robot2018.RobotMap;
import org.greasemonkeys457.robot2018.commands.DriveFromJoysticks;

public class Drivetrain extends Subsystem {

    // Hardware
    private TalonSRX rightMaster;
    private TalonSRX rightFollower;
    private TalonSRX leftMaster;
    private TalonSRX leftFollower;

    private DoubleSolenoid shifter;

    // Sensors
    public Encoder rightEncoder;
    public Encoder leftEncoder;

    // Pathfinder variables
    private EncoderFollower rightEncoderFollower;
    private EncoderFollower leftEncoderFollower;

    // State variables
    public boolean isLowGear;

    // Constants
    double scale = Constants.scale;
    double wheelDiameter = Constants.wheelDiameter;
    int encoderPulsesPerRev = Constants.pulsesPerRev;
    double maxVelocity = Constants.maxVelocity;
    double maxAccel = Constants.maxAccel;
    double maxJerk = Constants.maxJerk;

    // TESTING
    public double topRightSpeed,  topLeftSpeed  = 0.0;
    public double topRightAccel,  topLeftAccel  = 0.0;
    public double topRightJerk,   topLeftJerk   = 0.0;
    public double lastRightSpeed, lastLeftSpeed = 0.0;
    public double lastRightAccel, lastLeftAccel = 0.0;

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

    public double getRightSpeed () {
        return rightEncoder.getRate();
    }
    public double getLeftSpeed () {
        return leftEncoder.getRate();
    }
    public double getRightAccel () {
        return (rightEncoder.getRate() - lastRightSpeed) / 0.2;
    }
    public double getLeftAccel () {
        return (leftEncoder.getRate() - lastLeftSpeed) / 0.2;
    }
    public double getRightJerk () {
        return (getRightAccel() - lastRightAccel) / 0.2;
    }
    public double getLeftJerk () {
        return (getLeftAccel() - lastLeftAccel) / 0.2;
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

        // TESTING
        topRightSpeed = 0.0;
        topLeftSpeed = 0.0;

    }

    // ----- Pathfinder methods -----

    public void generatePath () {

        /**
         * Some notes:
         *
         * 1. We'll be using feet for units of length instead of meters. Jaci's documentation and example code of
         *    Pathfinder uses meters; however, so long as you stay consistent with the unit, Pathfinder will work with
         *    whatever unit you give it.
         *
         * 2. We'll try to document what our code is doing to the best of our ability. However, if you find yourself
         *    stuck or confused, the Pathfinder examples and/or docs might help. Follow the link below to get there.
         *
         *    https://github.com/JacisNonsense/Pathfinder/tree/master/Pathfinder-Java
         */

        // TODO: Measure max velocity, acceleration, and jerk

        // Trajectory configuration
        Trajectory.Config config = new Trajectory.Config(
                Trajectory.FitMethod.HERMITE_CUBIC, // Fit method used to generate the path
                Trajectory.Config.SAMPLES_HIGH,     // Sample count
                0.05,                               // Time step
                maxVelocity,                        // Max velocity
                maxAccel,                           // Max acceleration
                maxJerk                             // Max jerk
        );

        // Waypoints
        Waypoint[] straightPoints = new Waypoint[] {
                new Waypoint(0.0, 0.0, 0.0),
                new Waypoint(8.0, 0.0, 0.0),
        };

        // Generate a Trajectory
        Trajectory trajectory = Pathfinder.generate(straightPoints, config);

        // Modify the trajectory for tank drive using the wheelbase width
        TankModifier modifier = new TankModifier(trajectory).modify((25.5/12.0));

        // Set the encoder followers
        rightEncoderFollower = new EncoderFollower(modifier.getRightTrajectory());
        leftEncoderFollower = new EncoderFollower(modifier.getLeftTrajectory());

    }

    public void configureFollowers() {

        // TODO: Double check that the ticks per revolution is accurate
        // TODO: Tune PID loops

        // Configure the encoders
        rightEncoderFollower.configureEncoder(rightEncoder.getRaw(), encoderPulsesPerRev, wheelDiameter);
        leftEncoderFollower .configureEncoder(leftEncoder.getRaw(),  encoderPulsesPerRev, wheelDiameter);

        // Configure PIDVA
        rightEncoderFollower.configurePIDVA(1.0, 0.0, 0.0, (1/maxVelocity), 0);
        leftEncoderFollower .configurePIDVA(1.0, 0.0, 0.0, (1/maxVelocity), 0);

    }

    public void followPath () {

        // Calculate desired motor output
        double rightOutput = rightEncoderFollower.calculate(leftEncoder.getRaw());
        double leftOutput  = leftEncoderFollower.calculate(leftEncoder.getRaw());

        // TODO: Add a control loop for angle

        // Set the motor speeds according to the calculated outputs
        setRightSpeed(rightOutput);
        setLeftSpeed(leftOutput);

    }

    public void initDefaultCommand () {
        setDefaultCommand(new DriveFromJoysticks());
    }

}
