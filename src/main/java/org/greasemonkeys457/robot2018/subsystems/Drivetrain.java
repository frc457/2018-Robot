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
import org.greasemonkeys457.robot2018.util.paths.Path;

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
    private double kWheelDiameter = Constants.kDriveWheelDiameter;

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

    // Drive functions

    /**
     * Function to control tank drive.
     * @param rightRawInput Right joystick y-axis, assuming positive means forward
     * @param leftRawInput Left joystick y-axis, assuming positive means forward
     */
    public void tankDrive (double rightRawInput, double leftRawInput) {

        // Scale the input accordingly
        double rightScaledInput = driveScaling(rightRawInput);
        double leftScaledInput = driveScaling(leftRawInput);

        // Set the speed
        setRightSpeed(rightScaledInput);
        setLeftSpeed(leftScaledInput);

    }

    /**
     * Method to control arcade drive.
     * @param yRawInput The y-axis of a joystick, assuming positive means forward
     * @param xRawInput The x-axis of a joystick, used to control rotation. Positive means counterclockwise
     */
    public void arcadeDrive (double yRawInput, double xRawInput) {
        // TODO: Implement arcade drive
    }

    // Configuration functions

    private void configureTalons () {

        // Invert one side
        mRightMaster.setInverted(true);
        mRightFollower.setInverted(true);

        // Set followers
        mRightFollower.follow(mRightMaster);
        mLeftFollower.follow(mLeftMaster);

    }

    private void configureEncoders () {

        // Set distance per pulse
        mRightEncoder.setDistancePerPulse((kWheelDiameter * Math.PI) / encoderPulsesPerRev);
        mLeftEncoder.setDistancePerPulse((kWheelDiameter * Math.PI) / encoderPulsesPerRev);

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
        rightEncoderFollower.configureEncoder(mRightEncoder.getRaw(), encoderPulsesPerRev, kWheelDiameter);
        leftEncoderFollower.configureEncoder(mLeftEncoder.getRaw(), encoderPulsesPerRev, kWheelDiameter);

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

    public double getRightVelocity () {
        return mRightEncoder.getRate();
    }
    public double getLeftVelocity () {
        return mLeftEncoder.getRate();
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
    public void setPath (Path path) {

        // TODO: Path manager

        // Generate the path
        Trajectory trajectory = path.getTrajectory();

        // Modify using the wheelbase
        TankModifier modifier = new TankModifier(trajectory).modify(25.5/12.0);

        // Set the encoder followers
        rightEncoderFollower.setTrajectory(modifier.getRightTrajectory());
        leftEncoderFollower.setTrajectory(modifier.getLeftTrajectory());

    }


    public void initDefaultCommand () {
        setDefaultCommand(new DriveFromJoysticks());
    }

}
