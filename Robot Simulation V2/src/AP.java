
import java.awt.Point;
import java.awt.event.ActionEvent;

/**
 * Implementation of AP algorithm. Goal is found by selecting the
 * closest point on the map that has not been previously viewed.
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public class AP extends NavigationLibrary
{

    float c;

    float d;

    float epsilon;

    float deltaT = 1;

    float vMax = 10;

    float FR = 1;

    float R = 50;

    float timeStep = 1;

    float mass = 1;
    private float FMAX;

    /**
     *
     * @param params
     * @param occupied
     */
    public AP(Parameters params, boolean[][] occupied)
    {
        super(params, occupied);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

    }

    private void computeNewLocation(Robot r)
    {

    }

    private float interactionForceGoal(float desiredDistance, float distance,
            objectType object)
    {
        //Attractive force acting on robot
        float attractive;
        //Repulsive force acting on the robot
        float repulsive;
        //Net force acting on robot
        float force;

        switch (object)
        {
            case OBSTACLE: repulsive =
                        (float) (c * Math.pow(desiredDistance, 6) / Math.pow(
                                distance, 7));
                attractive = 0;
                break;
            case ROBOT: repulsive =
                        (float) (c * Math.pow(desiredDistance, 6) / Math.pow(
                                distance, 7));
                attractive = 0;
                break;
            case GOAL: attractive = (float) (2 * d * Math.pow(desiredDistance,
                        12) / Math.pow(distance, 13));
                repulsive = 0;
                break;
            default: attractive = repulsive = 0;
        }

        force = 24 * epsilon * (attractive - repulsive);
        if (force > FMAX)
        {
            force = FMAX;
        }
        if (force < -FMAX)
        {
            force = -FMAX;
        }

        return force;
    }
}

enum objectType
{

    OBSTACLE(1), ROBOT(2), GOAL(3);

    private final int value;

    objectType(int value)
    {
        this.value = value;
    }
}
