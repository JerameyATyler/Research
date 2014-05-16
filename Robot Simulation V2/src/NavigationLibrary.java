
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The library of common methods to be used by the navigation algorithms
 * implemented in this application
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public abstract class NavigationLibrary extends JPanel implements ActionListener
{

    /**
     * boolean matrix representing the placement of obstacles in the environment
     */
    public boolean[][] occupied;

    /**
     * boolean matrix representing the elements of the environment that have
     * been viewed
     */
    public boolean[][] viewed;

    /**
     * The quantity of elements in the environment that have been viewed
     */
    public int viewedElements = 0;

    /**
     * The number of movements that have been taken
     */
    public int movements = 0;

    /**
     * A Parameters object that contains the experiment parameters chosen by the
     * user
     */
    public Parameters params;

    /**
     * The total number of elements in the environment
     */
    public int totalElements;

    /**
     * The timer that is used to control when the robotic movements occur
     */
    public Timer timer;

    /**
     * An ArrayList of Robot objects that represents all of the robots in the
     * experiment
     */
    public ArrayList<Robot> robots = new ArrayList();

    /**
     * Constructor for NavigationLibrary. Accepts a list of parameters and the
     * environment map as input.
     *
     * @param params   A Parameters object that will contain the experiment
     *                 parameters.
     * @param occupied A boolean matrix representing the map of the environment.
     */
    public NavigationLibrary(Parameters params, boolean[][] occupied)
    {

        timer = new Timer(100, this);

        //Set the size of the JPanel and creates a border around it
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setSize(params.environmentWidth, params.environmentHeight);
        this.params = params;

        totalElements = params.environmentHeight * params.environmentWidth;

        //Places the robots in the environment in the appropriate places
        switch (params.robotQuantity)
        {
            case 4:
                robots.add(new Robot(new Point(params.environmentWidth - 1,
                        params.environmentHeight - 1), 135, 90, 45,
                        params.sensorDistance));
                robots.get(robots.size() - 1).bearing = 2;
            case 3:
                robots.add(new Robot(new Point(0, params.environmentHeight - 1),
                        45, 0, 315,
                        params.sensorDistance));
                robots.get(robots.size() - 1).bearing = 0;
            case 2:
                robots.add(new Robot(new Point(params.environmentWidth - 1, 0),
                        225, 180, 135,
                        params.sensorDistance));
                robots.get(robots.size() - 1).bearing = 4;
            case 1:
                robots.add(new Robot(new Point(0, 0), 315, 270, 225,
                        params.sensorDistance));
                robots.get(robots.size() - 1).bearing = 6;
                break;
            //In case of failure create one robot and place it in upper 
            //left corner
            default:
                robots.add(new Robot(new Point(0, 0), 315, 270, 225,
                        params.sensorDistance));
                robots.get(robots.size() - 1).bearing = 6;
                break;
        }

        this.occupied = occupied.clone();
        viewed = new boolean[params.environmentWidth][params.environmentHeight];
    }

    /**
     * Get how many elements in the environment have been viewed
     *
     * @return An int value representing the number of elements in the
     *         environment that have been viewed
     */
    public int getViewedElements()
    {
        return viewedElements;
    }

    /**
     * Get how many movements have been taken
     *
     * @return An int value representing the number of movements taken by the
     *         robots
     */
    public int getMovements()
    {
        return movements;
    }

    /**
     * Set the delay of the timer
     *
     * @param delay An int representing the new delay for the timer
     */
    public void setTimer(int delay)
    {
        timer.setDelay(delay);
    }

    /**
     * Start the timer
     */
    public void startTimer()
    {
        timer.start();
    }

    /**
     * Stop the timer
     */
    public void stopTimer()
    {
        timer.stop();
    }

    /**
     * Increments a robot's bearing by one and sets the sensor angles
     * accordingly
     *
     * @param robot A Robot object representing the robot that needs its bearing
     *              incremented
     */
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
    }

    /**
     * Decrements a robot's bearing by one and sets the sensor angles
     * accordingly
     *
     * @param robot A Robot object representing the robot that needs its bearing
     *              decremented
     */
    public void decrementBearing(Robot robot)
    {
        switch (robot.bearing)
        {
            case 0:
                robot.bearing = 7;
                robot.sensor0Angle = 0;
                robot.sensor1Angle = 315;
                robot.sensor2Angle = 270;
                break;
            case 1:
                robot.bearing = 0;
                robot.sensor0Angle = 45;
                robot.sensor1Angle = 0;
                robot.sensor2Angle = 315;
                break;
            case 2:
                robot.bearing = 1;
                robot.sensor0Angle = 90;
                robot.sensor1Angle = 45;
                robot.sensor2Angle = 0;
                break;
            case 3:
                robot.bearing = 2;
                robot.sensor0Angle = 135;
                robot.sensor1Angle = 90;
                robot.sensor2Angle = 45;
                break;
            case 4:
                robot.bearing = 3;
                robot.sensor0Angle = 180;
                robot.sensor1Angle = 135;
                robot.sensor2Angle = 90;
                break;
            case 5:
                robot.bearing = 4;
                robot.sensor0Angle = 225;
                robot.sensor1Angle = 180;
                robot.sensor2Angle = 135;
                break;
            case 6:
                robot.bearing = 5;
                robot.sensor0Angle = 270;
                robot.sensor1Angle = 225;
                robot.sensor2Angle = 180;
                break;
            case 7:
                robot.bearing = 6;
                robot.sensor0Angle = 315;
                robot.sensor1Angle = 270;
                robot.sensor2Angle = 225;
                break;
        }
        robot.setSensors();
    }

    /**
     * Increments a robots current position forward one unit and resets its
     * sensors accordingly.
     *
     * @param robot A Robot object representing the robot that needs its
     *              position incremented
     */
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
    }

    /**
     * Checks to see if a robot can move forward without a conflict occurring.
     *
     * @param robot A Robot object representing the robot that needs to have its
     *              position incremented
     *
     * @return Returns a boolean value of true if the robot can proceed forward
     *         and false if it cannot
     */
    public boolean canIncrementDirection(Robot robot)
    {
        switch (robot.bearing)
        {
            case 0:
                return robot.current.x + 1 < params.environmentWidth;
            case 1:
                return robot.current.x + 1 < params.environmentWidth &&
                        robot.current.y - 1 >= 0;
            case 2:
                return robot.current.y - 1 >= 0;
            case 3:
                return robot.current.x - 1 >= 0 &&
                        robot.current.y - 1 >= 0;
            case 4:
                return robot.current.x - 1 >= 0;
            case 5:
                return robot.current.x - 1 >= 0 &&
                        robot.current.y + 1 < params.environmentWidth;
            case 6:
                return robot.current.y + 1 < params.environmentWidth;
            case 7:
                return robot.current.x + 1 < params.environmentWidth &&
                        robot.current.y + 1 < params.environmentWidth;
            default:
                return false;
        }
    }

    /**
     * For every robot in the ArrayList robots update the viewed matrix where
     * elements lie in the view of a robot's sensor.
     */
    public void updateMapForward()
    {
        for (Robot r : robots)
        {
            //For sensor0 increment through the x axis
            for (int i = (((int) r.sensor0.getMinX() < 0) ? 0 : (int) r.sensor0.
                    getMinX()); i < ((r.sensor0.getMaxX() >=
                    params.environmentWidth) ? params.environmentWidth :
                    r.sensor0.getMaxX()); i++)
            {
                //Increment through the y axis
                for (int j = (((int) r.sensor0.getMinY() < 0) ? 0 :
                        (int) r.sensor0.getMinY()); j <
                        ((r.sensor0.getMaxY() >=
                        params.environmentHeight) ? params.environmentHeight :
                        r.sensor0.getMaxY()); j++)
                {
                    //If the element at (i,j) is inside of sensor0 and has not 
                    //been viewed, mark it viewed and increment the 
                    //viewedElements
                    if (r.sensor0.contains(i, j) && !viewed[i][j])
                    {
                        viewed[i][j] = true;
                        viewedElements++;
                    }
                }
            }
            //For sensor1 increment through the x axis
            for (int i = (((int) r.sensor1.getMinX() < 0) ? 0 : (int) r.sensor1.
                    getMinX()); i < ((r.sensor1.getMaxX() >=
                    params.environmentWidth) ? params.environmentWidth :
                    r.sensor1.getMaxX()); i++)
            {
                //Increment through the y axis
                for (int j = (((int) r.sensor1.getMinY() < 0) ? 0 :
                        (int) r.sensor1.getMinY()); j <
                        ((r.sensor1.getMaxY() >=
                        params.environmentHeight) ? params.environmentHeight :
                        r.sensor1.getMaxY()); j++)
                {
                    //If the element at (i,j) is inside of sensor0 and has not 
                    //been viewed, mark it viewed and increment the 
                    //viewedElements
                    if (r.sensor1.contains(i, j) && !viewed[i][j])
                    {
                        viewed[i][j] = true;
                        viewedElements++;
                    }
                }
            }
            //For sensor2 increment through the x axis
            for (int i = (((int) r.sensor2.getMinX() < 0) ? 0 : (int) r.sensor2.
                    getMinX()); i < ((r.sensor2.getMaxX() >=
                    params.environmentWidth) ? params.environmentWidth :
                    r.sensor2.getMaxX()); i++)
            {
                //Increment through the y axis
                for (int j = (((int) r.sensor2.getMinY() < 0) ? 0 :
                        (int) r.sensor2.getMinY()); j <
                        ((r.sensor2.getMaxY() >=
                        params.environmentHeight) ? params.environmentHeight :
                        r.sensor2.getMaxY()); j++)
                {
                    //If the element at (i,j) is inside of sensor0 and has not 
                    //been viewed, mark it viewed and increment the 
                    //viewedElements
                    if (r.sensor2.contains(i, j) && !viewed[i][j])
                    {
                        viewed[i][j] = true;
                        viewedElements++;
                    }
                }
            }
        }
    }

    /**
     * Updates the map within the sensor range for a single robot rotating
     * clockwise
     *
     * @param robot A Robot object representing the robot that is being
     *              incremented
     */
    public void updateMapRotateClockWise(Robot robot)
    {
        //Create Arc2D to allow iteration through points only inside of sensors.
        //A 45 degree rotation will cause the sensors to sweep 135 degree area,
        //excluding the spread of the sensors
        Arc2D.Double arc = new Arc2D.Double(Arc2D.PIE);
        arc.setFrame(robot.sensor0.getFrame());
        arc.setAngleStart(robot.sensor2.getAngleStart() + 45);
        arc.setAngleExtent(135);

        //Update the map for arc
        //Increment through the x axis
        for (int i = (((int) arc.getMinX() < 0) ? 0 : (int) arc.getMinX()); i <
                ((arc.getMaxX() >= params.environmentWidth) ?
                params.environmentWidth : arc.getMaxX()); i++)
        {
            //Increment through the y axis
            for (int j = (((int) arc.getMinY() < 0) ? 0 : (int) arc.getMinY());
                    j < ((arc.getMaxY() >= params.environmentHeight) ?
                    params.environmentHeight : arc.getMaxY()); j++)
            {
                //If a point is in the arc's range and it has not been
                //viewed previously, mark it as viewed
                if (arc.contains(i, j) && !viewed[i][j])
                {
                    viewed[i][j] = true;
                    viewedElements++;
                }
            }
        }
    }

    /**
     * Updates the map within the sensor range for a single robot rotating
     * counterclockwise
     *
     * @param robot A Robot object representing the robot that is being
     *              incremented
     */
    public void updateMapRotateCounterClockWise(Robot robot)
    {
        //Create Arc2D to allow iteration through points only inside of sensors.
        //A 45 degree rotation will cause the sensors to sweep 135 degree area,
        //excluding the spread of the sensors
        Arc2D.Double arc = new Arc2D.Double(Arc2D.PIE);
        arc.setFrame(robot.sensor0.getFrame());
        arc.setAngleStart(robot.sensor2.getAngleStart());
        arc.setAngleExtent(135);

        //Update the map for arc
        //Increment through the x axis
        for (int i = (((int) arc.getMinX() < 0) ? 0 : (int) arc.getMinX()); i <
                ((arc.getMaxX() >= params.environmentWidth) ?
                params.environmentWidth : arc.getMaxX()); i++)
        {
            //Increment through the y axis
            for (int j = (((int) arc.getMinY() < 0) ? 0 : (int) arc.getMinY());
                    j < ((arc.getMaxY() >= params.environmentHeight) ?
                    params.environmentHeight : arc.getMaxY()); j++)
            {
                //If a point is in the arc's range and it has not been
                //viewed previously, mark it as viewed
                if (arc.contains(i, j) && !viewed[i][j])
                {
                    viewed[i][j] = true;
                    viewedElements++;
                }
            }
        }
    }

    /**
     * Given a Robot object returns a boolean value representing whether or not
     * incrementing the robot in the direction of its bearing will result in a
     * move conflict
     *
     * @param robot A Robot object representing the robot that needs its
     *              direction incremented
     *
     * @return A boolean value representing whether or not incrementing the
     *         robot's position in the direction of its bearing will result in a
     *         move conflict
     */
    public boolean moveConflict(Robot robot)
    {
        Point point = new Point();
        switch (robot.bearing)
        {
            case 0:
                point.x = (robot.current.x + 1 >= params.environmentWidth) ?
                        (params.environmentWidth - 1) : robot.current.x + 1;
                point.y = robot.current.y;
                break;
            case 1:
                point.x = (robot.current.x + 1 >= params.environmentWidth) ?
                        (params.environmentWidth - 1) : robot.current.x + 1;
                point.y = (robot.current.y - 1 <= 0) ?
                        0 : robot.current.y - 1;
                break;
            case 2:
                point.x = robot.current.x;
                point.y = (robot.current.y - 1 <= 0) ?
                        0 : robot.current.y - 1;
                break;
            case 3:
                point.x = (robot.current.x - 1 <= 0) ?
                        0 : robot.current.x - 1;
                point.y = (robot.current.y - 1 <= 0) ?
                        0 : robot.current.y - 1;
                break;
            case 4:
                point.x = (robot.current.x - 1 <= 0) ?
                        0 : robot.current.x - 1;
                point.y = robot.current.y;
                break;
            case 5:
                point.x = (robot.current.x - 1 <= 0) ?
                        0 : robot.current.x - 1;
                point.y = (robot.current.y + 1 >= params.environmentHeight) ?
                        (params.environmentHeight - 1) : robot.current.y + 1;
                break;
            case 6:
                point.x = robot.current.x;
                point.y = (robot.current.y + 1 >= params.environmentHeight) ?
                        (params.environmentHeight - 1) : robot.current.y + 1;
                break;
            case 7:
                point.x = (robot.current.x + 1 >= params.environmentWidth) ?
                        (params.environmentWidth - 1) : robot.current.x + 1;
                point.y = (robot.current.y + 1 >= params.environmentHeight) ?
                        (params.environmentHeight - 1) : robot.current.y + 1;
                break;
        }
        try
        {
        if (occupied[point.x][point.y])
        {
            return true;
        }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        for (Robot r : robots)
        {
            if ((r.current.x == point.x) && (r.current.y == point.y))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Given a Robot and a starting Point, iterates in a spiral outward from the
     * robot's current position until an element is found that have not been
     * viewed. This element will be set as the robot's goal. A boolean value
     * is returned representing whether or not a goal is found.
     *
     * @param currentPoint A Point object representing the starting point for
     *                     iteration
     * @param r            A Robot object representing the robot in need of a
     *                     goal
     *
     * @return A boolean value representing whether or not a goal was found
     */
    public boolean acquireGoal(Point currentPoint, Robot r)
    {
        //Starting coordinates
        int x = currentPoint.x;
        int y = currentPoint.y;
        //Initial direction
        int direction = 0;
        //Width and height of current row or column
        int width = 0;
        int height = 0;

        //For every element in the viewed, perform this loop
        for (int i = 0; i < viewed.length * viewed[0].length;)
        {
            //Increment Width or Height according to direction
            switch (direction)
            {
                case 0: //North
                    height++;
                    break;
                case 1: //East
                    width++;
                    break;
                case 2: //South
                    height++;
                    break;
                case 3: //West
                    width++;
                    break;
                default: //Error, set everything back to original
                    x = currentPoint.x;
                    y = currentPoint.y;
                    direction = 0;
                    i = 0;
                    width = 0;
                    height = 0;
                    break;
            }

            //Iterate through every element of either the current row or 
            //column, depending on the direction            
            for (int j = 0;
                    j < ((direction == 0 || direction == 2) ? height : width);
                    j++)
            {
                //Check if current coordinates are in bounds of viewed
                if (!(x < 0 || y < 0 ||
                        x >= viewed.length || y >= viewed[x].length))
                {
                    //If viewed at current coordinates has not been viewed, 
                    //return those coordinates as Point.
                    if (!viewed[x][y])
                    {
                        r.goal = new Point(x, y);
                        return true;
                    } //If viewed at current coordinates has been viewed,
                    //increment counter
                    else
                    {
                        i++;
                    }
                }
                //Increment coordinates according to direction
                switch (direction)
                {
                    case 0: //North
                        y -= 1;
                        break;
                    case 1: //East
                        x += 1;
                        break;
                    case 2: //South
                        y += 1;
                        break;
                    case 3: //West
                        x -= 1;
                        break;
                }
            }
            //Increment direction
            direction = (direction + 1 > 3) ? 0 : direction + 1;
        }
        /*
         * If no element exists that has not already been viewed, return new
         * Point
         */

        return false;
    }

    /**
     * Abstract method to be implemented in the child class. Whenever the timer
     * event fires this method will be called. Contains the logic for the
     * algorithm
     *
     * @param e An ActionEvent representing a tick of the timer.
     */
    @Override
    public abstract void actionPerformed(ActionEvent e);

    /**
     * Implemented from the parent class. Draws the map on the screen. Black
     * pixels represent obstacles in the environment that have been viewed.
     * White pixels represent free space in the environment that has been
     * viewed. Dark gray pixels represent elements in the environment that have
     * not yet been viewed.
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //Iterate through every x element
        for (int i = 0; i < viewed.length; i++)
        {
            //Iterate through every y element
            for (int j = 0; j < viewed[i].length; j++)
            {
                //If the element at (i,j) has been viewed and contains 
                //an obstacle it should be painted black
                if (viewed[i][j] && occupied[i][j])
                {
                    g.setColor(Color.BLACK);
                    g.drawLine(i, j, i, j);
                }
                //If the element at (i,j) has been viewed and does not contain 
                //and obstacle it should be painted white
                else if (viewed[i][j])
                {
                    g.setColor(Color.WHITE);
                    g.drawLine(i, j, i, j);
                }
                //If the element at (i,j) has not been viewed it should be
                //painted dark gray
                else
                {
                    g.setColor(Color.DARK_GRAY);
                    g.drawLine(i, j, i, j);
                }
            }
        }

        Graphics2D g2 = (Graphics2D) g;
        //For every robot in the ArrayList robots place a red pixel and draw
        //a green arc for each of its sensors
        for (Robot r : robots)
        {

            g2.setColor(Color.GREEN);
            g2.fill(r.sensor0);
            g2.fill(r.sensor1);
            g2.fill(r.sensor2);

            g2.setColor(Color.RED);
            g2.drawLine(r.current.x, r.current.y, r.current.x, r.current.y);
        }
    }
}
