package org.greasemonkeys457.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.greasemonkeys457.robot2018.Constants;
import org.greasemonkeys457.robot2018.RobotMap;
import org.greasemonkeys457.robot2018.commands.ElevatorFromJoysticks;
import org.greasemonkeys457.robot2018.commands.ElevatorPeriodic;
import org.greasemonkeys457.robot2018.controllers.ElevatorController;

public class Elevator extends Subsystem {

    // Controller
    public static ElevatorController controller = new ElevatorController();

    // Hardware
    private final TalonSRX topMotor, bottomMotor;
    private final Encoder encoder;

    // Constants
    private static final int MAX_TICKS = Constants.ElevatorPosition.MAX.ticks;
    private static final int MIN_TICKS = Constants.ElevatorPosition.MIN.ticks;
    private static final int ALLOWANCE = Constants.kElevatorAllowance;

    // Target variables
    private int targetPosition = MIN_TICKS; // in encoder ticks

    /**
     * Constructor. Defines and configures everything.
     */
    public Elevator () {

        // Define hardware
        topMotor = new TalonSRX(RobotMap.elevatorTopMotor);
        bottomMotor = new TalonSRX(RobotMap.elevatorBottomMotor);
        encoder = new Encoder(RobotMap.elevatorEncoderA, RobotMap.elevatorEncoderB);

        // Configuration
        configureTalons();
        configureEncoders();

    }

    // Configuration functions

    private void configureTalons () {

        // Invert one side
        topMotor.setInverted(false);
        bottomMotor.setInverted(true);

        // Set followers
        bottomMotor.follow(topMotor);

    }

    private void configureEncoders () {

        // Reverse direction
        encoder.setReverseDirection(true);

        // Grab constants that we need
        double pitchDiameter = Constants.kElevPitchDiameter;
        double encoderPulsesPerRev = Constants.kEncoderPulsesPerRev;

        // Set distance per pulse
        encoder.setDistancePerPulse(((pitchDiameter * Math.PI) / encoderPulsesPerRev) * 2.0);

    }

    // Speed

    /**
     * Sets the speed of the motors on the elevator.
     * @param speed The desired speed for the motors to run at
     */
    public void setSpeed (double speed) {

        // Scale
        speed = elevatorScaling(speed);

        // Set
        topMotor.set(ControlMode.PercentOutput, speed);

    }

    private double elevatorScaling (double speed) {

        // Scale the inputted speed
        speed = (speed * Constants.kElevatorScale);

        // Eliminate deadzones
        if (Math.abs(speed) < 0.001) {
            speed = 0.0;
        }

        // Return
        return speed;

    }

    // Position

    /**
     * Sets the target height of the final stage of the elevator
     * @param targetPosition The target position, in inches
     */
    public void setTargetPosition (int targetPosition) {

        // Limit the target to within the minimum and maximum values
        if (targetPosition > MAX_TICKS) targetPosition = MAX_TICKS;
        if (targetPosition < MIN_TICKS) targetPosition = MIN_TICKS;

        // Set
        this.targetPosition = targetPosition;

    }

    public int getTargetPosition () {
        return this.targetPosition;
    }

    public int getCurrentPosition () {
        return encoder.get();
    }

    public boolean atTargetPosition () {
        return Math.abs(getTargetPosition() - getCurrentPosition()) <= ALLOWANCE;
    }

    public int getMaxTicks () {
        return MAX_TICKS;
    }

    public int getMinTicks () {
        return MIN_TICKS;
    }

    /**
     * Check if the elevator is currently within its' limits.
     * @return 1 if too high, -1 if too low, 0 if good
     */
    public int withinLimits () {
        if (getCurrentPosition() > MAX_TICKS) return 1;
        else if (getCurrentPosition() < MIN_TICKS) return -1;
        else return 0;
    }

    // Misc.

    public void reset () {

        // Return hardware to its' default state
        setSpeed(0.0);

    }

    public void zeroSensors () {

        // Zero the encoders
        encoder.reset();

    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ElevatorPeriodic());
    }

}
