package org.greasemonkeys457.robot2018.util.paths;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.greasemonkeys457.robot2018.Constants;

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

    public Path () {
        // Do... something?
    }

    void setPoints(Waypoint[] points) {
        this.points = points;
    }

    void setName (String name) {
        this.name = name;
    }

    public Waypoint[] getPoints() {
        return points;
    }

    public Trajectory getTrajectory () {

        if (doesCacheExist()) {
            // return cached trajectory
        }

        // Use the given points and config to generate a path
        generateTrajectory();

        // Return the generated path
        return trajectory;

    }

    private void generateTrajectory () {

        if (points != null) {

            // Grab the trajectory configuration from Constants
            config = Constants.pathfinderConfig;

            // Generate the trajectory
            trajectory = Pathfinder.generate(points, config);

            // TODO: Save the generated trajectory to a file
            savePath();

        }

        // If there are no waypoints set...
        else trajectory = null;

    }

    private void setFiles () {
        fConfig = new File("/home/lvuser/paths/" + name + "/config.txt");
        fTrajectory = new File("/home/lvuser/paths/" + name + "/trajectory.txt");
    }

    private void savePath () {

        // Set the paths of the files
        setFiles();

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
                confPrintWriter.printf("%-4.2f, %-4.2f, %-4.2f%n", point.x, point.y, Math.toDegrees(point.angle));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save the main trajectory
        Pathfinder.writeToCSV(fTrajectory, trajectory);

    }

    private boolean doesCacheExist () throws IOException {

        // TODO: Split this function into smaller functions for testing and readability

        // Set the paths of the files
        setFiles();

        // Check to see if the files exist
        if (!fConfig.exists() || !fTrajectory.exists()) return false;

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
            if (rFitMethod.equals("Cubic")) {
                aFitMethod = Trajectory.FitMethod.HERMITE_CUBIC;
            } else if (rFitMethod.equals("Quintic")) {
                aFitMethod = Trajectory.FitMethod.HERMITE_QUINTIC;
            } else {
                aFitMethod = null;
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
            while (!(point = reader.readLine()).equals("")) {

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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (readConfig == config) {
            System.out.println("yo the configs match!");
        } else {
            System.out.println("yo the configs dont match!");
        }

        if (readPoints == points) {
            System.out.println("yo the waypoints match!");
        } else {
            System.out.println("yo the points dont match!");
        }

        // TODO: Finish this

        return false;

    }

}
