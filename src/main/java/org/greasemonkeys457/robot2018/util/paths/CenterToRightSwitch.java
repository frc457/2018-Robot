package org.greasemonkeys457.robot2018.util.paths;

import jaci.pathfinder.Waypoint;
import org.greasemonkeys457.robot2018.Constants;

public class CenterToRightSwitch extends Path {

    public CenterToRightSwitch () {

        // Set the name of this path
        setName("CenterToRightSwitch");

        // Set the config
        setConfig(Constants.pathfinderConfig);

        double halfBotLen = Constants.kRobotLength / 2.0;
        double halfBotWid = Constants.kRobotWidth / 2.0;

        setPoints(new Waypoint[] {
                new Waypoint((halfBotLen),                             (14.5 - halfBotWid), 0.0),
                new Waypoint((halfBotLen + (2.0/12.0)),                (14.5 - halfBotWid), 0.0),
                new Waypoint(((144.0/12.0) - (2.0/12.0) - halfBotLen), (9.0),               0.0),
                new Waypoint(((144.0/12.0) - halfBotLen),              (9.0),               0.0),
        });

    }

}
