package org.greasemonkeys457.robot2018.util.paths;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.greasemonkeys457.robot2018.Constants;

public abstract class Path {

    private Waypoint[] points;

    public Path () {
        // Do... something?
    }

    public void setPoints(Waypoint[] points) {
        this.points = points;
    }

    public Waypoint[] getPoints() {
        return points;
    }

    public Trajectory getTrajectory () {

        // TODO: Check to see if the trajectory is saved in the filesystem

        return generateTrajectory();

    }

    private Trajectory generateTrajectory () {

        if (points != null) {

            // Grab the trajectory configuration from Constants
            Trajectory.Config config = Constants.pathfinderConfig;

            // Generate the trajectory
            Trajectory trajectory = Pathfinder.generate(points, config);

            // TODO: Save the trajectory, config, and points to a file

            // Return the trajectory
            return trajectory;

        }

        else return null;

    }

}
