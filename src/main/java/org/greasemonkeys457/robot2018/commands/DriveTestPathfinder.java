package org.greasemonkeys457.robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.greasemonkeys457.robot2018.Robot;

public class DriveTestPathfinder extends Command {

    public DriveTestPathfinder () {

        // This command uses the drivetrain, so...
        requires(Robot.drivetrain);

        // Set up the path
        Robot.drivetrain.generatePath();

        // Configure followers
        Robot.drivetrain.configureFollowers();

    }

    public void execute () {

        // Tell the drivetrain to follow the path generated in this command's constructor
        Robot.drivetrain.followPath();

    }

    public boolean isFinished () {

        // TODO: Add logic to tell when this command is done
        return true;

    }

}
