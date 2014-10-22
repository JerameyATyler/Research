
package Engine;

/**
 * Object that contains the experiments parameters.
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public class Parameters
{

    /**
     * The quantity of robots to be used in the experiment
     */
    public short robotQuantity;

    /**
     * The distance that the robot's sensors can reach as a ratio to the robot's
     * size
     */
    public short sensorDistance;

    /**
     * The percentage of the environment that is to be occupied by obstacles
     */
    public double obstacleDensity;

    /**
     * The width of the environment
     */
    public short environmentWidth;

    /**
     * The height of the environment
     */
    public short environmentHeight;

    /**
     * An optional time limit for the experiment. Does not represent clock time
     * but instead is the a limit on the number of movements to be performed
     */
    public int timeLimit;

    /**
     * Represents whether or not the experiment should be visual or merely
     * output text at the end of the simulation
     */
    public boolean isVisual;

    /**
     * The random seed that will be used to generate obstacles in the map. Is
     * optional. A seed already exists, but this can be modified to change the
     * placement of obstacles in the environment
     */
    public int seed;
}
