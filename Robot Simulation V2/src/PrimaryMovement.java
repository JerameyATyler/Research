
import java.awt.Point;
import java.awt.event.ActionEvent;

/**
 * Implementation of a Navigation algorithm designed by the author. Robots will
 * move forward as long as their path is not obstructed. If their path is
 * obstructed the robot will rotate 45 degrees and reevaluate if their path is
 * obstructed. If their path is unobstructed the robot will proceed forward and
 * the process will repeat. If a robot performs a full 360 degree rotation it
 * will determine that it has no available movements and will cease operations.
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public class PrimaryMovement extends NavigationLibrary
{

    //The number of robots that have ceased operations
    private int robotsCompleted = 0;

    /**
     * Constructor for Primary Movement algorithm. Accepts a list of parameters
     * and the environment map as input.
     *
     * @param params   A Parameter object that will contain the experiment
     *                 parameters
     * @param occupied A boolean matrix representing the map of the environment
     */
    public PrimaryMovement(Parameters params, boolean[][] occupied)
    {
        //Parameters are passed to the constructor of the super class
        super(params, occupied);
    }

    /**
     * Increments a robot's bearing by one and sets the sensor angles
     * accordingly. Overrides method from parent class. Has added functionality
     * of incrementing the count of a robot's rotations.
     *
     * @param robot A Robot object representing the robot that needs its bearing
     *              incremented
     */
    @Override
    public void incrementBearing(Robot robot)
    {
        switch (robot.bearing)
        {
            case 0:
                robot.bearing = 1;
                robot.sensor0Angle = 90;
                robot.sensor1Angle = 45;
                robot.sensor2Angle = 0;
                break;
            case 1:
                robot.bearing = 2;
                robot.sensor0Angle = 135;
                robot.sensor1Angle = 90;
                robot.sensor2Angle = 45;
                break;
            case 2:
                robot.bearing = 3;
                robot.sensor0Angle = 180;
                robot.sensor1Angle = 135;
                robot.sensor2Angle = 90;
                break;
            case 3:
                robot.bearing = 4;
                robot.sensor0Angle = 225;
                robot.sensor1Angle = 180;
                robot.sensor2Angle = 135;
                break;
            case 4:
                robot.bearing = 5;
                robot.sensor0Angle = 270;
                robot.sensor1Angle = 225;
                robot.sensor2Angle = 180;
                break;
            case 5:
                robot.bearing = 6;
                robot.sensor0Angle = 315;
                robot.sensor1Angle = 270;
                robot.sensor2Angle = 225;
                break;
            case 6:
                robot.bearing = 7;
                robot.sensor0Angle = 0;
                robot.sensor1Angle = 315;
                robot.sensor2Angle = 270;
                break;
            case 7:
                robot.bearing = 0;
                robot.sensor0Angle = 45;
                robot.sensor1Angle = 0;
                robot.sensor2Angle = 315;
                break;
        }
        robot.setSensors();
        robot.rotations++;
        if (robot.rotations == 8)
        {
            robot.finished = true;
            robotsCompleted++;
        }
    }

    /**
     * Increments a robot's bearing by one and sets the sensor angles
     * accordingly. Overrides method from parent class. Has added
     * functionality of resetting a robot's rotation count to 0 if a robot is
     * able to increment its direction
     *
     * @param robot A Robot object representing the robot that needs its bearing
     *              incremented
     */
    @Override
    public void incrementDirection(Robot robot)
    {
        switch (robot.bearing)
        {
            case 0:
                robot.current =
                        new Point(robot.current.x + 1, robot.current.y);
                break;
            case 1:
                robot.current =
                        new Point(robot.current.x + 1, robot.current.y - 1);
                break;
            case 2:
                robot.current =
                        new Point(robot.current.x, robot.current.y - 1);
                break;
            case 3:
                robot.current =
                        new Point(robot.current.x - 1, robot.current.y - 1);
                break;
            case 4:
                robot.current =
                        new Point(robot.current.x - 1, robot.current.y);
                break;
            case 5:
                robot.current =
                        new Point(robot.current.x - 1, robot.current.y + 1);
                break;
            case 6:
                robot.current =
                        new Point(robot.current.x, robot.current.y + 1);
                break;
            case 7:
                robot.current =
                        new Point(robot.current.x + 1, robot.current.y + 1);
                break;
        }
        robot.setSensors();
        robot.rotations = 0;
    }

    /**
     * Given a Robot, returns a boolean value representing whether or not a
     * forward movement will reveal more of the environment
     *
     * @param robot A Robot object representing the robot in need of a forward
     *              movement
     *
     * @return A boolean value representing whether or not a forward movement
     *         will unveil more of the map
     */
    public boolean moveReveal(Robot robot)
    {
        //Create a robot and perform the move on it
        Robot temp = robot.clone();
        temp.bearing = robot.bearing;
        incrementDirection(temp);

        //For sensor0 of the temp robot check if any unviewed element is within
        //its sensor range.
        //Iterate through x axis
        for (int i = (((int) temp.sensor0.getMinX() < 0) ? 0 :
                (int) temp.sensor0.
                getMinX()); i < ((temp.sensor0.getMaxX() >=
                params.environmentWidth) ? params.environmentWidth :
                temp.sensor0.getMaxX()); i++)
        {
            //Iterate through y axis
            for (int j = (((int) temp.sensor0.getMinY() < 0) ? 0 :
                    (int) temp.sensor0.getMinY()); j < ((temp.sensor0.
                    getMaxY() >=
                    params.environmentHeight) ? params.environmentHeight :
                    temp.sensor0.getMaxY()); j++)
            {
                //If the element at (i,j) has not been viewed and it is in the
                //sensor range return true
                if (temp.sensor0.contains(i, j) && !viewed[i][j])
                {
                    return true;
                }
            }
        }
        //For sensor1 of the temp robot check if any unviewed element is within
        //its sensor range.
        //Iterate through x axis
        for (int i = (((int) temp.sensor1.getMinX() < 0) ? 0 :
                (int) temp.sensor1.
                getMinX()); i < ((temp.sensor1.getMaxX() >=
                params.environmentWidth) ? params.environmentWidth :
                temp.sensor1.getMaxX()); i++)
        {
            //Iterate through y axis
            for (int j = (((int) temp.sensor1.getMinY() < 0) ? 0 :
                    (int) temp.sensor1.getMinY()); j < ((temp.sensor1.
                    getMaxY() >=
                    params.environmentHeight) ? params.environmentHeight :
                    temp.sensor1.getMaxY()); j++)
            {
                //If the element at (i,j) has not been viewed and it is in the
                //sensor range return true
                if (temp.sensor1.contains(i, j) && !viewed[i][j])
                {
                    return true;
                }
            }
        }
        //For sensor1 of the temp robot check if any unviewed element is within
        //its sensor range.
        //Iterate through x axis
        for (int i = (((int) temp.sensor2.getMinX() < 0) ? 0 :
                (int) temp.sensor2.
                getMinX()); i < ((temp.sensor2.getMaxX() >=
                params.environmentWidth) ? params.environmentWidth :
                temp.sensor2.getMaxX()); i++)
        {
            //Iterate through y axis
            for (int j = (((int) temp.sensor2.getMinY() < 0) ? 0 :
                    (int) temp.sensor2.getMinY()); j < ((temp.sensor2.
                    getMaxY() >=
                    params.environmentHeight) ? params.environmentHeight :
                    temp.sensor2.getMaxY()); j++)
            {
                //If the element at (i,j) has not been viewed and it is in the
                //sensor range return true
                if (temp.sensor2.contains(i, j) && !viewed[i][j])
                {
                    return true;
                }
            }
        }
        //If no unviewed element exists return false
        return false;
    }

    /**
     * Implemented from parent class. Whenever the timer event fires this method
     * will be called.
     * Contains the logic for the algorithm
     *
     * @param e An ActionEvent representing a tick of the timer.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //Perform only if the time limit has not been reached, there still 
        //exists a robot that has not ceased operations, and there are still 
        //elements that need to be viewed
        if ((params.timeLimit == 0 || movements < params.timeLimit) &&
                viewedElements < totalElements &&
                robotsCompleted != robots.size())
        {
            //Perform operations for every robot
            for (Robot r : robots)
            {
                //Exclude robots who have completed 
                //operations
                if (!r.finished)
                {
                    //If a robot can move forward and reveal more of the
                    //environemnt increment the direction and update the map
                    if (canIncrementDirection(r) && moveReveal(r) &&
                            !moveConflict(r))
                    {
                        incrementDirection(r);
                        updateMapForward();
                    }
                    //If a robot cannot move forward or reveal more of the 
                    //environment rotate counterclockwise and incremente the
                    //robot's bearing
                    else
                    {
                        updateMapRotateCounterClockWise(r);
                        incrementBearing(r);
                    }
                }
            }
            //Increment the count of movements
            movements++;
        }
        //repaint the map including robot positions and sensors
        repaint();
    }
}
