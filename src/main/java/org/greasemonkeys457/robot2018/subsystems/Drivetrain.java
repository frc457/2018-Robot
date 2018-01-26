package org.greasemonkeys457.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
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
    private TalonSRX rightMaster, rightFollower, leftMaster, leftFollower;
    private DoubleSolenoid shifter;

    // Sensors
    public Encoder rightEncoder, leftEncoder;

    // Pathfinder variables
    private EncoderFollower rightEncoderFollower, leftEncoderFollower;

    // State variables
    public boolean mIsLowGear;

    // Constants
    int encoderPulsesPerRev = Constants.kEncoderPulsesPerRev;
    double maxVelocity = Constants.kLowGearMaxVelocity;
    double maxAccel = Constants.kLowGearMaxAccel;
    double maxJerk = Constants.kLowGearMaxJerk;

    // TESTING
    public double topRightSpeed, topRightAccel, topRightJerk,
                  topLeftSpeed,  topLeftAccel,  topLeftJerk = 0.0;

    public double lastRightSpeed, lastRightAccel,
                  lastLeftSpeed,  lastLeftAccel = 0.0;

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
        rightEncoder.setDistancePerPulse((Constants.kDriveWheelDiameter * Math.PI) / encoderPulsesPerRev);
        leftEncoder .setDistancePerPulse((Constants.kDriveWheelDiameter * Math.PI) / encoderPulsesPerRev);

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
        mIsLowGear = true;

    }
    public void shiftToHigh () {

        // Move the shifters
        shifter.set(DoubleSolenoid.Value.kReverse);

        // Set the state variable
        mIsLowGear = false;

    }

    public double getRightSpeed () {
        return Math.abs(rightEncoder.getRate());
    }
    public double getLeftSpeed () {
        return Math.abs(leftEncoder.getRate());
    }
    public double getRightAccel () {
        return Math.abs((getRightSpeed() - lastRightSpeed) / 0.2);
    }
    public double getLeftAccel () {
        return Math.abs((getLeftSpeed() - lastLeftSpeed) / 0.2);
    }
    public double getRightJerk () {
        return Math.abs((getRightAccel() - lastRightAccel) / 0.2);
    }
    public double getLeftJerk () {
        return Math.abs((getLeftAccel() - lastLeftAccel) / 0.2);
    }

    public double driveScaling (double speed) {

        // Run the input speed through the scaling function.
        speed = (speed * Math.abs(speed) * Math.abs(speed) * Constants.kDriveScale);

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
        topLeftSpeed = 0.0;
        topLeftAccel = 0.0;
        topLeftJerk = 0.0;
        topRightSpeed = 0.0;
        topRightAccel = 0.0;
        topRightJerk = 0.0;

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
                new Waypoint(28.0, 0.0, 0.0),
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
        rightEncoderFollower.configurePIDVA(0.5, 0.0, 0.0, (1/maxVelocity), 0);
        leftEncoderFollower .configurePIDVA(0.5, 0.0, 0.0, (1/maxVelocity), 0);

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
