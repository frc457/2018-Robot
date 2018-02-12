package org.greasemonkeys457.robot2018.util.paths;

import jaci.pathfinder.Waypoint;
import org.greasemonkeys457.robot2018.Constants;

public class LeftToLeftScale extends Path {

    public LeftToLeftScale () {

        // Set the name of this path
        setName("LeftToLeftScale");

        // Set the config
        setConfig(Constants.pathfinderConfig);

        double halfBotLen = Constants.kRobotLength / 2.0;
        double halfBotWid = Constants.kRobotWidth / 2.0;

        setPoints(new Waypoint[] {
                new Waypoint((halfBotLen),              (27.0 - 2.5 - halfBotWid), 0.0),
                new Waypoint((halfBotLen + (2.0/12.0)), (27.0 - 2.5 - halfBotWid), 0.0),
                new Waypoint((11.0),                    (25.0),                    0.0),
                new Waypoint((17.0),                    (25.0),                    0.0),
                new Waypoint((23.0),                    (22.5),                    Math.toRadians(-45.0)),
        });

    }

}
