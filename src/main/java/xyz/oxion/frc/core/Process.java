package xyz.oxion.frc.core;

/**
 * A controllable process, inspired by FRC 192's GRTFramework.
 */
public class Process implements Runnable {

    protected final String name;
    private int sleepTime;
    private Thread thread = null;

    // State variables
    protected boolean sRunning = false;
    protected boolean sEnabled = false;

    public Process (String name) {
        this(name, -1);
    }

    public Process (String name, int sleepTime) {
        this.name = name;
        this.sleepTime = sleepTime;
    }

    public void run() {
        sRunning = true;
        while (sRunning && sleepTime >= 0) {

            // Poll if enabled
            if (sEnabled) poll();

            // Sleep
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    protected void poll() {}

    public void startPolling () {
        if (sleepTime >= 0 && !isRunning()) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop () {
        sRunning = false;
    }

    public void enable () {
        sEnabled = true;
    }

    public void disable () {
        sEnabled = false;
    }

    public boolean isEnabled () {
        return sEnabled;
    }

    public boolean isRunning () {
        return thread != null && thread.isAlive();
    }

    protected void setSleepTime (int millis) {
        this.sleepTime = millis;
    }

}
