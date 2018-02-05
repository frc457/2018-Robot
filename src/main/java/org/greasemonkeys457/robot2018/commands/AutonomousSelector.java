package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import openrio.powerup.MatchData;
import org.greasemonkeys457.robot2018.Constants;
import org.greasemonkeys457.robot2018.Robot;

public class AutonomousSelector extends CommandGroup {

    /**
     * Enumeration used to represent the different positions our robot might start in
     */
    public enum StartingPosition {
        Left,
        Center,
        Right
    }

    /**
     * Enumeration used to represent the different goals we might want to attempt in auto
     */
    public enum Goal {
        Nothing,
        Baseline,
        Switch,
        Scale
    }

    // Input values
    private StartingPosition startingPosition;
    private Goal goal;

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

        // Left-side autonomous routines
        if (this.startingPosition == StartingPosition.Left) {
            leftRoutines();
        }

        // Center autonomous routines
        else if (this.startingPosition == StartingPosition.Center) {
            centerRoutines();
        }

        // Right-side autonomous routines
        else if (this.startingPosition == StartingPosition.Right) {
            rightRoutines();
        }

    }

    /**
     * Uses the inputted goal to generate a routine from the left starting position.
     */
    private void leftRoutines () {

        // Left-side baseline auto
        if (this.goal == Goal.Baseline) {
            // TODO: Generate a path to cross the baseline
        }

        // Left-side switch auto
        if (this.goal == Goal.Switch) {
            // TODO: Add logic to recognize if the left side of the switch belongs to us
            // TODO: Generate a path to place a cube in the switch
        }

        if (this.goal == Goal.Scale) {
            // TODO: Add logic to recognize if the left side of the scale belongs to us
            // TODO: Generate a path to place a cube in the scale
        }

    }

    /**
     * Uses the inputted goal to generate a routine from the center starting position.
     */
    private void centerRoutines () {

        if (this.goal == Goal.Baseline) {

            // Smart baseline
            if (MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR) == MatchData.OwnedSide.LEFT) {

                // Drive to the right side of the switch
                Robot.drivetrain.setPath(Constants.Paths.centerToRightSwitch.points);

            } else {

                // Drive to the left side of the switch
                Robot.drivetrain.setPath(Constants.Paths.centerToLeftSwitch.points);

            }

        }

        if (this.goal == Goal.Switch) {

            // Place in switch
            if (MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR) == MatchData.OwnedSide.LEFT) {

                // Drive to the left side of the switch
                Robot.drivetrain.setPath(Constants.Paths.centerToLeftSwitch.points);

                // TODO: Place cube

            } else if (MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR) == MatchData.OwnedSide.RIGHT){

                // Drive to the right side of the switch
                Robot.drivetrain.setPath(Constants.Paths.centerToRightSwitch.points);

                // TODO: Place cube

            }

        }

        // No scale autonomous from the center position (for now)

    }

    /**
     * Uses the inputted goal to generate a routine from the right starting position.
     */
    private void rightRoutines () {

        // Right-side baseline auto
        if (this.goal == Goal.Baseline) {
            // TODO: Generate a path to cross the baseline
        }

        // Left-side switch auto
        if (this.goal == Goal.Switch) {
            // TODO: Add logic to recognize if we own the right side of the switch
            // TODO: Generate a path from the right stpos to the right switch
        }

        if (this.goal == Goal.Scale) {
            // TODO: Right stpos to right scale auto
        }

    }

}
