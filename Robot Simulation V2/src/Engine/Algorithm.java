
package Engine;

/**
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public enum Algorithm
{

    RW("Random Wanderer"), 
    PM("Primary Movement"), 
    AS("A*"), 
    LJ("Lennard-Jones"),
    KT("Kinetic Theory"), 
    LJKT("LJ/KT Hybrid");

    private final String algorithmString;

    Algorithm(String str)
    {
        this.algorithmString = str;
    }

    public String getAlgorithmString()
    {
        return algorithmString;
    }
}
