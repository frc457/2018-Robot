package org.greasemonkeys457.robot2018.util.paths;

import jaci.pathfinder.Waypoint;
import org.greasemonkeys457.robot2018.Constants;

public class RightToLeftSwitch extends Path {

    public RightToLeftSwitch () {

        // Set the name of this path
        setName("RightToLeftSwitch");

        // Set the config
        setConfig(Constants.pathfinderConfig);

        double halfBotLen = Constants.kRobotLength / 2.0;
        double halfBotWid = Constants.kRobotWidth / 2.0;

        setPoints(new Waypoint[] {

                // Starting position
                new Waypoint((halfBotLen),              (2.5 + halfBotWid), 0.0),
                new Waypoint((halfBotLen + (2.0/12.0)), (2.5 + halfBotWid), 0.0),

                // Cross the switch
                new Waypoint((8.0 - halfBotWid), (10.0), Math.toRadians(90)),
                new Waypoint((8.0 - halfBotWid), (15.0), Math.toRadians(90)),

                // Arrive at switch plate
                new Waypoint((12 - (2.0/12.0) - halfBotLen), (20.0 - halfBotWid), 0.0),
                new Waypoint((12 - halfBotLen),              (20.0 - halfBotWid), 0.0),

        });

    }

}
