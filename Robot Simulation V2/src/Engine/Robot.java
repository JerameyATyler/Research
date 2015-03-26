
package Engine;

import java.awt.Point;
import java.awt.geom.Arc2D;
import java.util.Stack;

/**
 * Object that contains properties and methods specific to a Robot
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public final class Robot
{

    /**
     * A Point object representing the robot's current (x,y) coordinate
     */
    public Point current;

    /**
     * A Point object representing the (x,y) coordinate of the robot's goal
     */
    public Point goal;

    /**
     * An int value representing direction the robot is facing. Bearing is
     * determined by 45 degree increments starting at 0 degrees facing right.
     * Bearing of 0 would be 0 degrees. Bearing of 1 would be 45 degrees.
     * Bearing of 7 would be 315 degrees.
     */
    public short bearing;

    /**
     * An int value representing the number of 45 degree rotations that a robot
     * has performed
     */
    public short rotations;

    /**
     * A boolean value representing whether or not a robot has a goal
     */
    public boolean hasGoal;

    /**
     * A boolean value representing whether or not a robot has completed its
     * operations
     */
    public boolean finished = false;

    /**
     * A Stack of Point objects representing a sequence of Points a robot needs
     * to move to in order to progress from a starting Point to an ending Point
     */
    public Stack<Point> path;

    /**
     * An int value representing the distance that the robot's sensors can
     * reach. Sensor distance is a ratio to the robot's body size. Sensor
     * distance of 10 would result in a sensor distance 10 times the robot's
     * size.
     */
    public int sensorDistance;

    /**
     * An Arc2D.Double representing the robot's sensor at -45 degrees from the
     * robot's forward direction. Angle of extent may be modified to create a
     * sensor with a wider spread.
     */
    public Arc2D.Double sensor0 = new Arc2D.Double(Arc2D.PIE);
    /**
     * An int value representing the angle of sensor 0 from the robot's forward
     * direction.
     */
    public int sensor0Angle;

    /**
     * An Arc2D.Double representing the robot's sensor at 0 degrees from the
     * robot's forward direction. Angle of extent may be modified to create a
     * sensor with a wider spread.
     */
    public Arc2D.Double sensor1 = new Arc2D.Double(Arc2D.PIE);
    /**
     * An int value representing the angle of sensor 0 from the robot's forward
     * direction.
     */
    public int sensor1Angle;

    /**
     * An Arc2D.Double representing the robot's sensor at 45 degrees from the
     * robot's forward direction. Angle of extent may be modified to create a
     * sensor with a wider spread.
     */
    public Arc2D.Double sensor2 = new Arc2D.Double(Arc2D.PIE);
    /**
     * An int value representing the angle of sensor 0 from the robot's forward
     * direction.
     */
    public int sensor2Angle;

    //Properties for AP algorithm
    /**
     * Distance between the robot and the element of the environment that is
     * currently being evaluated
     */
    public float distance;
    /**
     *
     */
    public float turn;
    /**
     * Velocity along the x axis
     */
    public float vX = 0;
    /**
     * Velocity along the y axis
     */
    public float vY = 0;
    /**
     * Change in vX
     */
    public float delta_vX = 0;
    /**
     * Change in vY
     */
    public float delta_vY = 0;

    /**
     * The net force acting on a robot
     */
    public float netForce;
    /*
     * The change in position along the x axis
     */
    public float deltaX = 0;
    /**
     * The change in position along the y axis
     */
    public float deltaY = 0;
    /**
     * The coordinates of the next position for the robot
     */
    public Point nextPoint = new Point();
    /**
     * Force along the x axis
     */
    public float Fx = 1;
    /**
     * Force along the y axis
     */
    public float Fy = 1;
    
    public float v = 1;
    //End properties for AP algorithm
    
    /**
     * Constructor for Robot object. Accepts A Point representing the starting
     * (x, y) coordinates of the robot, int values representing the angles of
     * sensors 0 1 and 2, and an int value representing the sensor distance
     *
     * @param start          A Point object representing the starting (x, y)
     *                       coordinates of the robot
     * @param sensor0Angle   An int value representing the angle of sensor 0
     *                       from the robot's forward direction.
     * @param sensor1Angle   An int value representing the angle of sensor 1
     *                       from the robot's forward direction.
     * @param sensor2Angle   An int value representing the angle of sensor 2
     *                       from the robot's forward direction.
     * @param sensorDistance An Arc2D.Double representing the robot's sensor at
     *                       -45 degrees from the robot's forward direction.
     *                       Angle of extent may be modified to create a sensor
     *                       with a wider spread.
     */
    public Robot(Point start, int sensor0Angle,
            int sensor1Angle, int sensor2Angle, int sensorDistance)
    {
        current = start;

        this.sensor0Angle = sensor0Angle;
        this.sensor1Angle = sensor1Angle;
        this.sensor2Angle = sensor2Angle;

        this.sensorDistance = sensorDistance;

        setSensors();

        sensor0.setAngleExtent(10);
        sensor1.setAngleExtent(10);
        sensor2.setAngleExtent(10);
    }

    /**
     * Sets the frame and the start angle of each of the robot's sensors. The
     * frame is the starting (x,y) coordinates of the frame and the width and
     * height of the frame. Basically creates a square around the robot with a
     * width and height equal to the sensor distance. An arc will be drawn using
     * a starting angle and an angle of extent. This arc will represent a
     * sensor.
     */
    public void setSensors()
    {
        sensor0.setFrame(current.x - sensorDistance, current.y - sensorDistance,
                (sensorDistance * 2), (sensorDistance * 2));
        sensor1.setFrame(current.x - sensorDistance, current.y - sensorDistance,
                (sensorDistance * 2), (sensorDistance * 2));
        sensor2.setFrame(current.x - sensorDistance, current.y - sensorDistance,
                (sensorDistance * 2), (sensorDistance * 2));

        sensor0.setAngleStart(this.sensor0Angle);
        sensor1.setAngleStart(this.sensor1Angle);
        sensor2.setAngleStart(this.sensor2Angle);
    }

    /**
     * Overrides method from parent class. Creates an exact copy of a robot.
     *
     * @return
     */
    @Override
    public Robot clone()
    {
        return new Robot(new Point(current.x, current.y), sensor0Angle,
                sensor1Angle, sensor2Angle, sensorDistance);
    }
}
