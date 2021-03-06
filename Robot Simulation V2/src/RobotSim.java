
import java.util.Random;
import javax.swing.JFrame;

/**
 * The entry point of the application. Contains the main method.
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public class RobotSim extends JFrame
{

    /**
     * Main method of the application. Opens Parameter selection window,
     * generates map, then opens the main window.
     *
     * @param args Required for main method. Unnecessary in this implementation.
     */
    public static void main(String[] args)
    {
        RobotSim sim = new RobotSim();

        Parameters params = sim.gatherParameters();

        boolean[][] occupied = sim.generateMap(params);

        MainWindow window = new MainWindow(params, occupied);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    /**
     * Opens a ParameterSelectionDialog window that allows the user to select
     * the parameters that will be used in the current experiment.
     *
     * @return Returns a Parameters object that contains the user selected
     *         parameters for the experiment
     */
    public Parameters gatherParameters()
    {
        Parameters params = new Parameters();
        ParameterSelectionDialog selectionDialog =
                new ParameterSelectionDialog();
        selectionDialog.setModal(true);
        if (!selectionDialog.showDialog(params))
        {
            System.exit(0);
        }
        selectionDialog.dispose();
        return params;
    }

    /**
     * Given a Parameters object, generates a matrix that corresponds to the
     * occupied area of the environment.
     *
     * @param params Contains the size of the matrix and obstacle density of the
     *               matrix that is to created.
     *
     * @return Returns a boolean matrix that corresponds to the occupied area of
     *         the environment.
     */
    public boolean[][] generateMap(Parameters params)
    {
        boolean[][] occupied =
                new boolean[params.environmentWidth][params.environmentHeight];

        int totalElements = params.environmentHeight * params.environmentWidth;

        int occupiedElements = 0;

        int neededElements =
                (int) Math.ceil(totalElements * params.obstacleDensity);

        Random random =
                new Random((params.seed != 0) ? params.seed : 2147483647);

        while (neededElements > occupiedElements)
        {
            int y = (int) (random.nextDouble() * params.environmentHeight);
            int x = (int) (random.nextDouble() * params.environmentWidth);
            int width = (int) (1 + random.nextDouble() * 4);
            int height = (int) (1 + random.nextDouble() * 4);

            if ((x != 0 && y != params.environmentHeight - 1) && (x !=
                    params.environmentWidth - 1 && y != 0) && (x !=
                    params.environmentWidth - 1 &&
                    y != params.environmentHeight - 1) &&
                    (x + width < params.environmentWidth) &&
                    (y + height < params.environmentHeight))
            {
                for (int i = x; i < ((x + width < params.environmentWidth) ?
                        (x +
                        width) : params.environmentWidth); i++)
                {
                    for (int j = y; j <
                            ((y + height < params.environmentHeight) ?
                            (y + height) : params.environmentHeight); j++)
                    {
                        occupied[i][j] = true;
                    }
                }
            }

            occupiedElements = 0;

            for (int i = 0; i < params.environmentWidth; i++)
            {
                for (int j = 0; j < params.environmentHeight; j++)
                {
                    if (occupied[i][j])
                    {
                        occupiedElements++;
                    }
                }
            }
        }
        return occupied;
    }
}
