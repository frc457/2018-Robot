package org.greasemonkeys457.robot2018.util.paths;

import edu.wpi.first.wpilibj.DriverStation;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.io.*;
import java.util.ArrayList;

public abstract class Path {

    // Pathfinder variables
    private Waypoint[] points;
    private Trajectory.Config config;
    private Trajectory trajectory;

    // Variables used to handle writing to and reading from files
    private String name;
    private File fConfig;
    private File fTrajectory;

    // Variables loaded from save file if save file exists
    private Waypoint[] loadedPoints;
    private Trajectory.Config loadedConfig;
    private Trajectory loadedTrajectory;

    // Memory variables
    private boolean loaded = false;
    private boolean generated = false;

    public Path () {}

    // Setter functions

    void setConfig (Trajectory.Config config) {
        this.config = config;
    }

    void setPoints(Waypoint[] points) {
        this.points = points;
    }

    void setName (String name) {
        this.name = name;
    }

    // Getter functions

    public Waypoint[] getPoints() {
        return points;
    }

    public Trajectory getTrajectory () {

        if (doesSaveExist()) {

            // Load the save file if it hasn't already been loaded
            if (!loaded) loadSave();

            // If the save is valid, return the loaded trajectory
            if (validateLoadedSave()) return loadedTrajectory;

        }

        // Use the given points and config to generate a path
        generateTrajectory();

        // Return the generated path
        return trajectory;

    }

    public Trajectory getGeneratedTrajectory () {

        if (!generated) {
            DriverStation.reportWarning("Tried to get a trajectory that hasn't been generated yet!", true);
            return null;
        } else {
            return trajectory;
        }

    }

    public Trajectory getLoadedTrajectory() {

        if (!loaded) {
            DriverStation.reportWarning("Tried to get a trajectory that hasn't been loaded yet!", true);
            return null;
        } else {
            return loadedTrajectory;
        }

    }

    public Trajectory loadAndGetTrajectory () {

        loadSave();
        return getLoadedTrajectory();

    }

    // Trajectory generation

    public void generateTrajectory () {

        if (points.length >= 2) {

            // Generate the trajectory
            trajectory = Pathfinder.generate(points, config);

        }

        // If there aren't enough waypoints set...
        else trajectory = null;

        // Remember that the trajectory has been generated
        generated = true;

    }

    public Trajectory generateAndGetTrajectory () {
        generateTrajectory();
        return trajectory;
    }

    // Writing functions

    public void savePath () {

        // Set the paths of the files
        setFiles();

        // Create the files if they don't exist
        createFiles();

        // Reset the files so that they're empty
        resetFiles();

        // Save the config & points
        FileWriter confFileWriter;
        try {

            // Prepare to write to a file
            confFileWriter = new FileWriter(fConfig);
            PrintWriter confPrintWriter = new PrintWriter(confFileWriter);

            // Since fit methods aren't stored as strings
            String sFit;
            if (config.fit == Trajectory.FitMethod.HERMITE_CUBIC) {
                sFit = "Cubic";
            } else if (config.fit == Trajectory.FitMethod.HERMITE_QUINTIC) {
                sFit = "Quintic";
            } else {
                sFit = "Unknown";
            }

            // Print all the configuration variables
            confPrintWriter.println(sFit);
            confPrintWriter.println(config.sample_count);
            confPrintWriter.println(config.dt);
            confPrintWriter.println(config.max_velocity);
            confPrintWriter.println(config.max_acceleration);
            confPrintWriter.println(config.max_jerk);

            // Print all of the points
            for (Waypoint point : points) {
                confPrintWriter.printf("%-4.6f, %-4.6f, %-4.6f%n", point.x, point.y, Math.toDegrees(point.angle));
            }

            confPrintWriter.close();
            confFileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save the main trajectory
        Pathfinder.writeToCSV(fTrajectory, trajectory);

    }

    // Reading functions

    public void loadSave () {

        // Make sure the file paths are properly set
        setFiles();

        // Read the config file
        Trajectory.Config readConfig = new Trajectory.Config(null,0,0,0,0,0);
        Waypoint[] readPoints = new Waypoint[]{};
        try {

            // Prepare to read the file
            BufferedReader reader = new BufferedReader(new FileReader(fConfig));

            // Read the file
            String rFitMethod = reader.readLine();
            String rSampleCount = reader.readLine();
            String rTimestep = reader.readLine();
            String rMaxVelocity = reader.readLine();
            String rMaxAcceleration = reader.readLine();
            String rMaxJerk = reader.readLine();

            // Parse the read values
            Trajectory.FitMethod aFitMethod;
            switch (rFitMethod) {
                case "Cubic":
                    aFitMethod = Trajectory.FitMethod.HERMITE_CUBIC;
                    break;
                case "Quintic":
                    aFitMethod = Trajectory.FitMethod.HERMITE_QUINTIC;
                    break;
                default:
                    aFitMethod = null;
                    break;
            }
            int aSampleCount = Integer.parseInt(rSampleCount);
            double aTimestep = Double.parseDouble(rTimestep);
            double aMaxVelocity = Double.parseDouble(rMaxVelocity);
            double aMaxAcceleration = Double.parseDouble(rMaxAcceleration);
            double aMaxJerk = Double.parseDouble(rMaxJerk);

            // Use the read values to generate a config object
            readConfig = new Trajectory.Config(aFitMethod, aSampleCount, aTimestep, aMaxVelocity, aMaxAcceleration, aMaxJerk);

            // Read the waypoints from the file
            ArrayList<Waypoint> rPoints = new ArrayList<>();
            String point;
            while (((point = reader.readLine()) != null)) {

                // Split the string
                String[] split = point.trim().split(", ");

                // Parse the split string
                double dX = Double.parseDouble(split[0]);
                double dY = Double.parseDouble(split[1]);
                double dAngle = Math.toRadians(Double.parseDouble(split[2]));

                // Add the point
                rPoints.add(new Waypoint(dX, dY, dAngle));

            }

            // Convert from ArrayList to Array
            readPoints = rPoints.toArray(new Waypoint[rPoints.size()]);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the loaded configuration and points
        loadedConfig = readConfig;
        loadedPoints = readPoints;

        // Set the loaded trajectory
        loadedTrajectory = Pathfinder.readFromCSV(fTrajectory);

        // TODO: Maybe set this variable to be false if there's an error
        loaded = true;

    }

    // Validation functions

    public boolean validateLoadedSave () {
        return validateLoadedSave(false);
    }

    public boolean validateLoadedSave (boolean printErrors) {

        if (loaded) {

            boolean failed = false;

            // Compare the loaded config object to the inputted config object
            if (config.dt != loadedConfig.dt || config.sample_count != loadedConfig.sample_count ||
                    config.max_velocity != loadedConfig.max_velocity || config.max_acceleration != loadedConfig.max_acceleration ||
                    config.max_jerk != loadedConfig.max_jerk || config.fit != loadedConfig.fit)
            {
                if (printErrors) System.out.println("The trajectory configurations don't match!");
                failed = true;
            }

            // Compare the loaded waypoints to the inputted waypoints
            for (int i = 0; i < points.length; i++) {

                double maxDiff = 1E-5;

                if (!fuzzyEquals(points[i].x, loadedPoints[i].x, maxDiff)) {
                    if (printErrors) System.out.print("X value on point " + i + " doesn't match! ");
                    if (printErrors) System.out.println("Difference: " + (points[i].x - loadedPoints[i].x));
                    failed = true;
                }

                if (!fuzzyEquals(points[i].y, loadedPoints[i].y, maxDiff)) {
                    if (printErrors) System.out.print("Y value on point " + i + " doesn't match! ");
                    if (printErrors) System.out.println("Difference: " + (points[i].y - loadedPoints[i].y));
                    failed = true;
                }

                if (!fuzzyEquals(points[i].angle, loadedPoints[i].angle, maxDiff)) {
                    if (printErrors) System.out.print("Angle on point " + i + " doesn't match! ");
                    if (printErrors) System.out.println("Difference: " + (points[i].x - loadedPoints[i].x));
                    failed = true;
                }

            }

            return !failed;

        }

        // If nothing has been loaded, then the loaded files are nonexistent, and are not valid.
        else return false;

    }

    // File helper functions

    private void setFiles () {
        fConfig = new File("/home/lvuser/paths/" + name + "/config.txt");
        fTrajectory = new File("/home/lvuser/paths/" + name + "/trajectory.txt");
    }

    private void createFiles () {

        try {

            // Paths folder
            if (!fConfig.getParentFile().getParentFile().exists())
                fConfig.getParentFile().getParentFile().createNewFile();

            // Folder for this specific path
            if (!fConfig.getParentFile().exists())
                fConfig.getParentFile().createNewFile();

            // Config and points file
            if (!fConfig.exists())
                fConfig.createNewFile();

            // Trajectory file
            if (!fTrajectory.exists())
                fTrajectory.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void deleteFiles () {

        // Delete the files
        fConfig.delete();
        fTrajectory.delete();

        // Delete the containing folder
        fConfig.getParentFile().delete();

    }

    private void resetFiles () {
        deleteFiles();
        createFiles();
    }

    private boolean doesSaveExist() {

        // Make sure the file paths are properly set
        setFiles();

        // Returns true if and only if all save files for this path exists
        return fConfig.exists() && fTrajectory.exists();

    }

    // Misc. helper functions

    public boolean fuzzyEquals (double a, double b, double maxDiff) {
        return Math.abs(a - b) <= maxDiff;
    }

}
