package org.greasemonkeys457.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
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
    private final TalonSRX mRightMaster, mRightFollower, mLeftMaster, mLeftFollower;
    private final DoubleSolenoid mShifter;
    private final Encoder mRightEncoder, mLeftEncoder;
    public AHRS navx;

    // State variables
    private boolean mIsLowGear;

    // Pathfinder variables
    public EncoderFollower rightEncoderFollower, leftEncoderFollower;

    // Constants
    int encoderPulsesPerRev = Constants.kEncoderPulsesPerRev;
    double maxVelocity = Constants.kLowGearMaxVelocity;
    double maxAccel = Constants.kLowGearMaxAccel;
    double maxJerk = Constants.kLowGearMaxJerk;

    // TESTING
    public double topRightSpeed, topRightAccel, topRightJerk,
                  topLeftSpeed,  topLeftAccel,  topLeftJerk = 0.0;

    public double lastRightSpeed, lastRightAccel,
                  lastLeftSpeed,  lastLeftAccel = 0.0

    public Drivetrain () {

        // TODO: NavX

        // Define stuff
        mRightMaster = new TalonSRX(RobotMap.rfMotor);
        mRightFollower = new TalonSRX(RobotMap.rbMotor);
        mLeftMaster = new TalonSRX(RobotMap.lfMotor);
        mLeftFollower = new TalonSRX(RobotMap.lbMotor);

        mRightEncoder = new Encoder(RobotMap.rightEncoderA, RobotMap.rightEncoderB);
        mLeftEncoder = new Encoder(RobotMap.leftEncoderA, RobotMap.leftEncoderB);

        mShifter = new DoubleSolenoid(RobotMap.shifterForward, RobotMap.shifterReverse);

        // Configure stuff
        configureTalons();
        configureEncoders();

    }

    /**
     * Configuration functions for various things that might need configuration.
     */
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

        configureNavX();

        // Pathfinder
        rightEncoderFollower = new EncoderFollower();
        leftEncoderFollower = new EncoderFollower();

    }

    private void configureNavX () {

        navx = new AHRS(SPI.Port.kMXP, (byte) 200);

    }

    /**
     * Functions to set drivetrain's speed, in percentage (-1 to 1).
     */
    public void setRightSpeed (double speed) {
        mRightMaster.set(ControlMode.PercentOutput, speed);
    }
    public void setLeftSpeed (double speed) {
        mLeftMaster.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Shifts the gearboxes.
     */
    public void setLowGear(boolean wantsLowGear) {

        // Shift to the desired state
        if (wantsLowGear)
            mShifter.set(DoubleSolenoid.Value.kForward);
        else
            mShifter.set(DoubleSolenoid.Value.kReverse);

        // Update the state variable
        mIsLowGear = wantsLowGear;

    }

    /**
     * @return Whether or not the drivetrain is in low gear
     */
    public boolean isLowGear () {
        return mIsLowGear;
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

    /**
     * Zeroes all sensors
     */
    public void zeroSensors () {

        mRightEncoder.reset();
        mLeftEncoder.reset();

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

        // Zero the sensors
        zeroSensors();
    
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

        System.out.println("Generating path...");

        // Trajectory configuration
        Trajectory.Config config = new Trajectory.Config(
                Trajectory.FitMethod.HERMITE_CUBIC, // Fit method used to generate the path
                Trajectory.Config.SAMPLES_HIGH,     // Sample count
                0.02,                               // Time step
                maxVelocity,                        // Max velocity
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
                new Waypoint((28.5/12.0), (159.5/12.0), 0.0),
                new Waypoint((103.5/12.0), 9.0, 0.0),
        };

        // Generate a Trajectory
        Trajectory trajectory = Pathfinder.generate(centerToRightSwitchPoints, config);

        // Modify the trajectory for tank drive using the wheelbase width
        // TODO: Fix bug where this modifier might tell one side to run faster than it actually can
        TankModifier modifier = new TankModifier(trajectory).modify((25.5/12.0));

        // Set the encoder followers
        rightEncoderFollower.setTrajectory(modifier.getRightTrajectory());
        leftEncoderFollower.setTrajectory(modifier.getLeftTrajectory());

        System.out.println("Done generating path!");

    }

    public void configureFollowers () {

        // Configure the encoders
        rightEncoderFollower.configureEncoder(rightEncoder.getRaw(), encoderPulsesPerRev, Constants.kDriveWheelDiameter);
        leftEncoderFollower .configureEncoder(leftEncoder.getRaw(),  encoderPulsesPerRev, Constants.kDriveWheelDiameter);

        // Configure PIDVA
        rightEncoderFollower.configurePIDVA(0.001, 0.0, 0.0, (1/maxVelocity), 0);
        leftEncoderFollower .configurePIDVA(0.001, 0.0, 0.0, (1/maxVelocity), 0);

    }

    public void resetFollowers () {

        // Reset the followers
        rightEncoderFollower.reset();
        leftEncoderFollower.reset();

        // Reset the sensors
        zeroSensors();

        // Reconfigure followers
        configureFollowers();

    }

    public void followPath () {

        // Calculate desired motor output
        double rightOutput = rightEncoderFollower.calculate(rightEncoder.getRaw());
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
