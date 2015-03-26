
package Algorithms;

import Engine.Parameters;
import Engine.NavigationLibrary;
import Engine.Robot;

/**
 * Implementation of Lennard-JonesModel algorithm. Goal is found by selecting
 * the closest point on the map that has not been previously viewed.
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public class LennardJonesModel extends NavigationLibrary
{

    float c;

    float d;

    float epsilon;

    float deltaT = 1;

    float vMax = 10;

    float FR = 1;

    float R = params.sensorDistance;

    float timeStep = 1;

    float mass = 1;
    private float FMAX;

    /**
     *
     * @param params
     * @param occupied
     */
    public LennardJonesModel(Parameters params, boolean[][] occupied)
    {
        super(params, occupied);
    }

    private void computeNewLocation(Robot r)
    {
        r.v *= FR;

        float theta = (r.deltaX != 0) ? (float) Math.atan(r.deltaY / r.deltaX)
                : 0;

        for (Robot rCurr : super.robots)
        {
            if (rCurr != r)
            {
                float rDistance = (float) Math.sqrt(
                        (r.current.x - rCurr.current.x)
                        * (r.current.x - rCurr.current.x)
                        + (r.current.y - rCurr.current.y)
                        * (r.current.y - rCurr.current.y));

                if (rDistance <= 1.5 * R)
                {
                    r.netForce
                            = interactionForce(R, rDistance, objectType.ROBOT);
                }

                r.Fx += r.netForce * Math.cos(theta);
                r.Fy += r.netForce * Math.sin(theta);
            }
        }

        /*
         * Begin implementation of forces from obstacles
         */
        for (int i = 0; i < params.environmentWidth; i++)
        {
            for (int j = 0; j < params.environmentWidth; j++)
            {
                //If a particular cell in the environment has been viewed and 
                //contains an obstacle then calculate the force exerted by that 
                //cell
                if (viewed[i][j] && occupied[i][j])
                {
                    float rDistance = (float) Math.sqrt((r.current.x + i)
                            + (r.current.y + j));

                    if (rDistance <= 1.5 * R)
                    {
                        r.netForce = interactionForce(R, rDistance,
                                objectType.OBSTACLE);
                    }

                    r.Fx += r.netForce * Math.cos(theta);
                    r.Fy += r.netForce * Math.sin(theta);
                }
            }
        }
        /*
         * End implementation of forces from obstacles
         */

        /*
         * Begin implementation of forces from goal
         */
        float rDistance = (float) Math.sqrt(Math.abs(Math.pow(r.current.x
                - r.goal.x, 2))
                + Math.abs(Math.pow(r.current.y - r.goal.y, 2)));

        if (rDistance <= 1.5 * R)
        {
            r.netForce = interactionForce(R, rDistance,
                    objectType.GOAL);
        }

        r.Fx += r.netForce * Math.cos(theta);
        r.Fy += r.netForce * Math.sin(theta);
        /*
         * End implementation of forces from goal
         */
        r.delta_vX = timeStep * r.Fx / mass;
        r.delta_vY = timeStep * r.Fy / mass;
        r.vX += r.delta_vX;
        r.vY += r.delta_vY;

        r.v = (float) Math.sqrt(r.vX * r.vX + r.vY * r.vY);

        if (r.v > vMax)
        {
            r.vX = (r.vX * timeStep);
            r.vY = (r.vY * timeStep);
        }

        r.deltaX = r.vX * timeStep;
        r.deltaY = r.vY * timeStep;
        if (r.current.x < r.goal.x)
        {
            r.nextPoint.x = (int) (r.current.x + r.deltaX);
        }
        else if (r.current.x > r.goal.x)
        {
            r.nextPoint.x = (int) (r.current.x - r.deltaX);
        }
        else
        {
            r.nextPoint.x = (int) r.current.x;

        }
        if (r.current.y < r.goal.y)
        {
            r.nextPoint.y = (int) (r.current.y + r.deltaY);
        }
        else if (r.current.y > r.goal.y)
        {
            r.nextPoint.y = (int) (r.current.y - r.deltaY);
        }
        else
        {
            r.nextPoint.y = (int) r.current.y;

        }
    }

    private float interactionForce(float desiredDistance, float distance,
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
            case OBSTACLE:
                repulsive = (float) (c * Math.pow(desiredDistance, 6) / Math.
                        pow(
                                distance, 7));
                attractive = 0;
                break;
            case ROBOT:
                repulsive = (float) (c * Math.pow(desiredDistance, 6) / Math.
                        pow(
                                distance, 7));
                attractive = (float) (2 * d * Math.pow(desiredDistance,
                        12) / Math.pow(distance, 13));
                break;
            case GOAL:
                attractive = (float) (2 * d * Math.pow(desiredDistance,
                        12) / Math.pow(distance, 13));
                repulsive = 0;
                break;
            default:
                attractive = repulsive = 0;
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

    @Override
    public void navigation(Robot r)
    {
        this.acquireGoal(r.current, r);
        computeNewLocation(r);
    }
}

enum objectType
{

    OBSTACLE(1), ROBOT(2), GOAL(3);

    final int value;

    objectType(int value)
    {
        this.value = value;
    }
}
