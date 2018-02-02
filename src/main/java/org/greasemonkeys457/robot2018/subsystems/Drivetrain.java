package org.greasemonkeys457.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    private final TalonSRX mRightMaster, mRightFollower, mLeftMaster, mLeftFollower;
    private final DoubleSolenoid mShifter;
    private final Encoder mRightEncoder, mLeftEncoder;
    private final AHRS mNavX;

    // State variables
    private boolean mIsLowGear;

    // Constants
    private int encoderPulsesPerRev = Constants.kEncoderPulsesPerRev;
    private double maxVelocity = Constants.kLowGearMaxVelocity;
    private double maxAccel = Constants.kLowGearMaxAccel;
    private double maxJerk = Constants.kLowGearMaxJerk;
    private double wheelDiameter = Constants.kDriveWheelDiameter;

    // Pathfinder variables
    public EncoderFollower rightEncoderFollower;
    public EncoderFollower leftEncoderFollower;

    /**
     * Constructor. Defines and configures everything.
     */
    public Drivetrain () {

        // Talons
        mRightMaster = new TalonSRX(RobotMap.rfMotor);
        mRightFollower = new TalonSRX(RobotMap.rbMotor);
        mLeftMaster = new TalonSRX(RobotMap.lfMotor);
        mLeftFollower = new TalonSRX(RobotMap.lbMotor);

        // Solenoids
        mShifter = new DoubleSolenoid(RobotMap.shifterForward, RobotMap.shifterReverse);

        // Encoders
        mRightEncoder = new Encoder(RobotMap.rightEncoderA, RobotMap.rightEncoderB);
        mLeftEncoder = new Encoder(RobotMap.leftEncoderA, RobotMap.leftEncoderB);

        // NavX
        mNavX = new AHRS(SPI.Port.kMXP, (byte) 200);

        // Pathfinder path followers
        rightEncoderFollower = new EncoderFollower();
        leftEncoderFollower = new EncoderFollower();

        // Configure stuff
        configureTalons();
        configureEncoders();

    }

    // Configuration functions

    private void configureTalons () {

        // Invert one side
        mRightMaster.setInverted(true);
        mRightFollower.setInverted(true);

        // Set followers
        mRightFollower.follow(mRightMaster);
        mLeftFollower.follow(mLeftMaster);

        // TODO: Limit voltage outputs

    }

    private void configureEncoders () {

        // Set distance per pulse
        mRightEncoder.setDistancePerPulse((wheelDiameter * Math.PI) / encoderPulsesPerRev);
        mLeftEncoder.setDistancePerPulse((wheelDiameter * Math.PI) / encoderPulsesPerRev);

        // Invert one side
        mLeftEncoder.setReverseDirection(true);

    }

    private void configureFollowers () {

        // PIDVA variables
        double kP = 0.001;
        double kI = 0;
        double kD = 0;
        double kV = (1.0/maxVelocity);
        double kA = 0;

        // Configure the encoders
        rightEncoderFollower.configureEncoder(mRightEncoder.getRaw(), encoderPulsesPerRev, Constants.kDriveWheelDiameter);
        leftEncoderFollower.configureEncoder(mLeftEncoder.getRaw(),  encoderPulsesPerRev, Constants.kDriveWheelDiameter);

        // Configure PIDVA
        rightEncoderFollower.configurePIDVA(kP, kI, kD, kV, kA);
        leftEncoderFollower.configurePIDVA(kP, kI, kD, kV, kA);

    }

    // Setter functions

    public void setRightSpeed (double speed) {
        mRightMaster.set(ControlMode.PercentOutput, speed);
    }

    public void setLeftSpeed (double speed) {
        mLeftMaster.set(ControlMode.PercentOutput, speed);
    }

    public void setLowGear(boolean wantsLowGear) {

        // Shift to the desired state
        if (wantsLowGear)
            mShifter.set(DoubleSolenoid.Value.kForward);
        else
            mShifter.set(DoubleSolenoid.Value.kReverse);

        // Update the state variable
        mIsLowGear = wantsLowGear;

    }

    // Getter functions

    /**
     * @return The drivetrain's angle, measured by the NavX
     */
    public double getYaw () {
        return -mNavX.getYaw();
    }

    /**
     * @return Whether or not the robot is currently in low gear
     */
    public boolean isLowGear () {
        return mIsLowGear;
    }

    /**
     * Scales the given speed.
     * @param speed The input speed, from -1 to 1
     * @return The scaled speed
     */
    public double driveScaling (double speed) {

        // Run the input speed through the scaling function.
        speed = (speed * Math.abs(speed) * Math.abs(speed) * Constants.kDriveScale);

        if (Math.abs(speed) < 0.001) {
            speed = 0.0;
        }

        return speed;

    }

    // Reset functions

    /**
     * Zeroes all sensors.
     */
    public void zeroSensors () {

        mRightEncoder.reset();
        mLeftEncoder.reset();
        mNavX.zeroYaw();

    }

    /**
     * Sets all hardware to its' default position.
     */
    public void reset () {

        // Stop the motors
        setRightSpeed(0);
        setLeftSpeed(0);

        // Make sure shifters are where they should be
        setLowGear(true);

        // Zero sensors
        zeroSensors();

    }

    /**
     * Resets the encoder followers to prepare to follow a path
     */
    public void resetFollowers () {

        // Reset the followers
        rightEncoderFollower.reset();
        leftEncoderFollower.reset();

        // Reset the sensors
        zeroSensors();

        // Reconfigure followers
        configureFollowers();

    }

    // Helper functions

    /**
     * Keeps a variable between -1 and 1 for motor speed input.
     * @param input The input
     * @return The input, kept between -1 and 1
     */
    private double boundPercentage (double input) {
        if (input < -1) input = -1;
        if (input > 1) input = 1;
        return input;
    }

    // Pathfinder functions
    public void generatePath () {

        /*
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

        System.out.println("Generating path...");

        // Trajectory configuration
        Trajectory.Config config = new Trajectory.Config(
                Trajectory.FitMethod.HERMITE_CUBIC, // Fit method used to generate the path
                Trajectory.Config.SAMPLES_HIGH,     // Sample count
                0.02,                               // Time step
                4.25,                               // Max velocity
                maxAccel,                           // Max acceleration
                maxJerk                             // Max jerk
        );

        // Waypoints
        Waypoint[] straightPoints = new Waypoint[] {
                new Waypoint(0.0, 0.0, 0.0),
                new Waypoint(7.0, 0.0, 0.0),
        };

        // Center to right switch
        Waypoint[] centerToRightSwitchPoints = new Waypoint[] {
                new Waypoint((16.5/12.0), (159.5/12.0), 0.0),
                new Waypoint((18.5/12.0), (159.5/12.0), 0.0),
                new Waypoint((120/12.0), 9.0, 0.0),
                new Waypoint((123/12.0), 9.0, 0.0),
        };

        // Center to left switch
        Waypoint[] centerToLeftSwitchPoints = new Waypoint[] {
                new Waypoint((16.5/12.0), (159.5/12.0), 0.0),
                new Waypoint((18.5/12.0), (159.5/12.0), 0.0),
                new Waypoint((120/12.0), 18.0, 0.0),
                new Waypoint((123/12.0), 18.0, 0.0),
        };

        // Center to right scale (will we even ever use this?)
        Waypoint[] centerToRightScalePoints = new Waypoint[] {
                new Waypoint((16.5/12.0), (159.5/12.0), 0.0),
                new Waypoint(10.0, 6.0, Math.toRadians(-45)),
                new Waypoint(27.0, 3.0, 0.0)
        };

        // Generate a Trajectory
        Trajectory trajectory = Pathfinder.generate(centerToRightScalePoints, config);

        // Modify the trajectory for tank drive using the wheelbase width
        TankModifier modifier = new TankModifier(trajectory).modify((25.5/12.0));

        // Set the encoder followers
        rightEncoderFollower.setTrajectory(modifier.getRightTrajectory());
        leftEncoderFollower.setTrajectory(modifier.getLeftTrajectory());

        System.out.println("Done generating path!");

    }
    public void followPath () {

        // Calculate desired motor output
        double rightOutput = boundPercentage(rightEncoderFollower.calculate(mRightEncoder.getRaw()));
        double leftOutput = boundPercentage(leftEncoderFollower.calculate(mLeftEncoder.getRaw()));

        // Angle control
        double actualAngle = getYaw();
        double targetAngle = Pathfinder.boundHalfDegrees(Math.toDegrees(rightEncoderFollower.getHeading()));
        double angleError = Pathfinder.boundHalfDegrees(targetAngle - actualAngle);

        double turn = 0.8 * (-1.0/80.0) * angleError;

        // Set the motor speeds according to the calculated outputs
        setRightSpeed(rightOutput - turn);
        setLeftSpeed(leftOutput + turn);

    }

    public void initDefaultCommand () {
        setDefaultCommand(new DriveFromJoysticks());
    }

}
