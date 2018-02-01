package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class FollowPath extends Command {

    public FollowPath() {

        // This command uses the drivetrain, so...
        requires(Robot.drivetrain);

    }

    public void initialize () {

        // Reset the followers
        Robot.drivetrain.resetFollowers();

        // Make sure the robot is in low gear
        Robot.drivetrain.setLowGear(true);

    }

    public void execute () {

        // Tell the drivetrain to follow the path previously generated
        Robot.drivetrain.followPath();

    }

    public boolean isFinished () {

        return Robot.drivetrain.leftEncoderFollower.isFinished() && Robot.drivetrain.rightEncoderFollower.isFinished();

    }

}
