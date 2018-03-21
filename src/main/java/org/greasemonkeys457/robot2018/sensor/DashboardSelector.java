package org.greasemonkeys457.robot2018.sensor;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import xyz.oxion.frc.core.Sensor;

public class DashboardSelector<V> extends Sensor {

    private SendableChooser<V> chooser;

    // Data
    public static final int SELECTED = 0;

    public static final int NUM_DATA = 1;

    public DashboardSelector(String name, int numData) {
        super(name, numData);
        chooser = new SendableChooser<>();
    }

    public void addObject () {

    }

    protected void poll() {
        super.poll();
    }
}
