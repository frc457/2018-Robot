package org.greasemonkeys457.robot2018.commands;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import edu.wpi.first.wpilibj.command.CommandGroup;
import openrio.powerup.MatchData;
import org.greasemonkeys457.robot2018.Constants;
import org.greasemonkeys457.robot2018.Constants.ElevatorPosition;
import org.greasemonkeys457.robot2018.Robot;

import static openrio.powerup.MatchData.OwnedSide.*;

public class AutonomousSelector extends CommandGroup {

    /**
     * Enumeration used to represent the different positions our robot might start in
     */
    public enum StartingPosition {
        Left("Left"),
        Center("Center"),
        Right("Right");

        public String name;

        StartingPosition (String name) {
            this.name = name;
        }
    }

    /**
     * Enumeration used to represent the different goals we might want to attempt in auto
     */
    public enum Goal {
        Nothing("Do nothing"),
        Baseline("Cross the baseline"),
        Switch("Place a cube in the switch"),
        Scale("Place a cube in the scale");

        public String name;

        Goal (String name) {
            this.name = name;
        }
    }

    // Input values
    private StartingPosition startingPosition;
    private Goal goal;
    private boolean moveElevator = Constants.kMoveElevatorInAuto;

    public AutonomousSelector (StartingPosition startingPosition, Goal goal) {

        // Require the subsystems this command group uses
        requires(Robot.drivetrain);

        // Remember the input values
        this.startingPosition = startingPosition;
        this.goal = goal;

        // Generate the routine using the input values
        generateRoutine();

    }

    /**
     * Uses the inputted starting position to pick a function to generate a routine.
     */
    private void generateRoutine () {

        switch(this.startingPosition) {
            case Left:
                switch(this.goal) {
                    case Baseline:

                        // Cross the baseline
                        leftCrossBaseline();

                        break;

                    case Switch:

                        // Check if we own the close switch
                        if (ownedSwitchSide() == LEFT) {

                            // Drive to the side of the switch
                            leftToLeftSwitch();

                            // Place a cube
                            placeCube();

                        }

                        // Cross the baseline if we don't
                        else {

                            // Cross the baseline
                            leftCrossBaseline();

                        }

                        break;

                    case Scale:

                        // Check if we own the close scale
                        if (ownedScaleSide() == LEFT) {

                            // Drive to the left scale
                            leftToLeftScale();

                            // Place a cube
                            placeCube();

                        }

                        // Cross the baseline if we don't
                        else {

                            // Cross the baseline
                            leftCrossBaseline();

                        }

                        break;
                }
                break;
            case Center:

                switch (this.goal) {
                    case Baseline:

                        // Drive to the side of the switch we don't own
                        if (ownedSwitchSide() == LEFT) {

                            // Drive to the right side of the switch
                            centerToRightSwitch();

                        } else if (ownedSwitchSide() == MatchData.OwnedSide.RIGHT) {

                            // Drive to the left side of the switch
                            centerToLeftSwitch();

                        }

                        break;

                    case Switch:

                        // Drive to the side of the switch we own
                        if (ownedSwitchSide() == LEFT) centerToLeftSwitch();
                        if (ownedSwitchSide() == RIGHT) centerToRightSwitch();

                        // Place a cube
                        placeCube();

                        break;

                    // No scale from center autonomous

                }

                break;
            case Right:

                switch (this.goal) {
                    case Baseline:

                        // Cross the baseline
                        rightCrossBaseline();

                        break;

                    case Switch:

                        // Check if we own the close switch
                        if (ownedSwitchSide() == RIGHT) {

                            // Drive to the side of the switch
                            rightToRightSwitch();

                            // Place a cube
                            placeCube();

                        }

                        // Cross the baseline if we don't
                        else {

                            // Cross the baseline
                            rightCrossBaseline();

                        }

                        break;

                    case Scale:

                        // Check if we own the close scale
                        if (ownedScaleSide() == RIGHT) {

                            // Drive to the side of the switch
                            rightToRightScale();

                            // Place a cube
                            placeCube();

                        }

                        // Cross the baseline if we don't
                        else {

                            // Cross the baseline
                            rightCrossBaseline();

                        }

                        break;

                }

                break;
        }

    }

    private void leftCrossBaseline () {

        // Drive past the baseline
        addSequential(new FollowPath(Constants.leftCrossBaseline));

    }

    private void leftToLeftSwitch () {

        if (moveElevator) {

            // Step 1: Drive to the left side of the switch and lift the elevator
            addParallel(new ElevatorSetPosition(ElevatorPosition.SWITCH));
            addSequential(new FollowPath(Constants.leftToLeftSwitch));

            // Step 2: Wait for the elevator to get to the target position
            addSequential(new WaitForElevatorPosition());

        }

        else {

            // Drive to the left side of the switch
            addSequential(new FollowPath(Constants.leftToLeftSwitch));

        }

    }

    private void leftToLeftScale () {

        if (moveElevator) {

            // Step 1: Drive to the left side of the scale and lift the elevator
            addParallel(new ElevatorSetPosition(ElevatorPosition.SCALE));
            addSequential(new FollowPath(Constants.leftToLeftScale));

            // Step 2: Wait for the elevator to fet to the target position
            addSequential(new WaitForElevatorPosition());

        }

        else {

            // Drive to the left side of the scale
            addSequential(new FollowPath(Constants.leftToLeftScale));

        }

    }

    private void centerToRightSwitch () {

        if (moveElevator) {

            // Step 1: Drive to the right side of the switch and lift the elevator
            addParallel(new ElevatorSetPosition(ElevatorPosition.SWITCH));
            addSequential(new FollowPath(Constants.centerToRightSwitch));

            // Step 2: Wait for the elevator to get to the target position
            addSequential(new WaitForElevatorPosition());

        }

        else {

            // Drive to the right side of the switch
            addSequential(new FollowPath(Constants.centerToRightSwitch));

        }

    }

    private void centerToLeftSwitch () {

        if (moveElevator) {

            // Step 1: Drive to the left side of the switch and lift the elevator
            addParallel(new ElevatorSetPosition(ElevatorPosition.SWITCH));
            addSequential(new FollowPath(Constants.centerToLeftSwitch));

            // Step 2: Wait for the elevator to get to the target position
            addSequential(new WaitForElevatorPosition());

        }

        else {

            // Drive to the left side of the switch
            addSequential(new FollowPath(Constants.centerToLeftSwitch));

        }

    }

    private void rightCrossBaseline () {

        // Drive past the baseline
        addSequential(new FollowPath(Constants.rightCrossBaseline));

    }

    private void rightToRightSwitch () {

        if (moveElevator) {

            // Step 1: Drive to the right side of the switch and lift the elevator
            addParallel(new ElevatorSetPosition(ElevatorPosition.SWITCH));
            addSequential(new FollowPath(Constants.rightToRightSwitch));

            // Step 2: Wait for the elevator to reach its' target position
            addSequential(new WaitForElevatorPosition());

        }

        else {

            // Drive to the right side of the switch
            addSequential(new FollowPath(Constants.rightToRightSwitch));

        }

    }

    private void rightToRightScale () {

        if (moveElevator) {
            // Step 1: Drive to the right side of the scale and lift the elevator
            addParallel(new ElevatorSetPosition(ElevatorPosition.SCALE));
            addSequential(new FollowPath(Constants.rightToRightScale));

            // Step 2: Wait for the elevator to reach its' target position
            addSequential(new WaitForElevatorPosition());
        }

        else {

            // Drive to the right side of the scale
            addSequential(new FollowPath(Constants.rightToRightScale));

        }

    }

    private void placeCube () {
        // TODO
    }

    // Helper functions
    private MatchData.OwnedSide ownedSwitchSide () {
        return MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
    }
    private MatchData.OwnedSide ownedScaleSide () {
        return MatchData.getOwnedSide(MatchData.GameFeature.SCALE);
    }

}
