package org.greasemonkeys457.robot2018.util.paths;

import jaci.pathfinder.Waypoint;
import org.greasemonkeys457.robot2018.Constants;

public class LeftCrossBaseline extends Path {

    public LeftCrossBaseline() {

        // Set the name of this path
        setName("LeftCrossBaseline");

        // Set the config
        setConfig(Constants.pathfinderConfig);

        double halfBotLen = Constants.kRobotLength / 2.0;
        double halfBotWid = Constants.kRobotWidth / 2.0;

        setPoints(new Waypoint[] {
                new Waypoint((halfBotLen),              (27.0 - 2.5 - halfBotWid), 0.0),
                new Waypoint((halfBotLen + (2.0/12.0)), (27.0 - 2.5 - halfBotWid), 0.0),
                new Waypoint((9.0),                     (25.0),                    0.0),
                new Waypoint((14.0),                    (25.0),                    0.0),
        });

    }

}
