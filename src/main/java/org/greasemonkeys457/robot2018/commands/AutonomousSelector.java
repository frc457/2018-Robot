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

        // Cross the baseline
        if (this.goal == Goal.Baseline) {

            /*
             * Note that these autonomous routines drive to the side of the switch that we don't own, but never place
             * the cube. At first glance, it might seem like we're scoring for the opposing side. However, unless the
             * cube falls out of the robot, that should never happen.
             *
             * If it does happen, we'll generate new paths that don't risk that. Until then, this will work fine.
             */

            if (ownedSwitchSide() == MatchData.OwnedSide.LEFT) {

                // Drive to the right side of the switch
                addSequential(new FollowPath(Constants.Paths.centerToRightSwitch));

            }

            if (ownedSwitchSide() == MatchData.OwnedSide.RIGHT) {

                // Drive to the left side of the switch
                addSequential(new FollowPath(Constants.Paths.centerToLeftSwitch));

            }

        }

        // Place a cube in the switch
        if (this.goal == Goal.Switch) {

            if (ownedSwitchSide() == MatchData.OwnedSide.LEFT) {

                // Drive to the left side of the switch
                addSequential(new FollowPath(Constants.Paths.centerToLeftSwitch));

                // TODO: Place cube

            } else if (ownedSwitchSide() == MatchData.OwnedSide.RIGHT){

                // Drive to the right side of the switch
                addSequential(new FollowPath(Constants.Paths.centerToRightSwitch));

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

    // Helper functions
    private MatchData.OwnedSide ownedSwitchSide () {
        return MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
    }
}
