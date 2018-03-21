package xyz.oxion.frc.core;

import java.util.Vector;

public class Sensor extends Process {

    protected final int numData;

    // Instance variables
    private final Vector stateChangeListeners;
    private double data[];

    public Sensor (String name, int numData) {
        this(name, -1, numData);
    }

    public Sensor (String name, int sleepTime, int numData) {
        super(name, sleepTime);
        this.numData = numData;
        stateChangeListeners = new Vector();
        data = new double[numData];
    }

    public void setState (int id, double datum) {

        // Remember the previous value
        double previous = data[id];

        // Notify listeners if the value has changed
        if (previous != datum) {
            data[id] = datum;
        }

        data[id] = datum;

    }

    protected void notifyStateChange (int id, double datum) {
        // TODO
    }

}
