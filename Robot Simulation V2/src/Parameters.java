
import java.util.ArrayList;


/**
 * Object that contains the experiments parameters.
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public class Parameters
{

    public short robotQuantity;
    public short sensorDistance;
    public double obstacleDensity;
    public short environmentWidth;
    public short environmentHeight;
    public int timeLimit;
    public boolean isVisual;
    public int seed;
    ArrayList<Integer> seedList = new ArrayList();
}
