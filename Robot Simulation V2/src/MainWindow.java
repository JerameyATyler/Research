
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

/**
 * Creates the main window that a user will interface with. Contains a tabbed
 * pane with individual tabs for each navigation algorithm as well as a tab for
 * the map. A pane exists at the bottom displaying the metrics that have been
 * gathered
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public class MainWindow extends JFrame implements ActionListener
{

    //Variables independent of navigation algorithms
    //JTabbedPane container to hold navigation algorithms and map
    private JTabbedPane tabbedPane;
    //Buttons for user control
    private JButton runButton;
    private JButton ffButton;
    private JButton stopButton;
    //Count of all elements in the environment
    private int totalElements;
    //The parameters to be used in the simulation
    private Parameters parameters;
    //Panel containing the map
    private Map mapPanel;
    //Timer that controls the updates of the metric display
    private Timer timer;

    //Variables dependent on navigation algorithms
    //Navigation algorithms
    private RandomWanderer randomPanel;
    private PrimaryMovement primaryPanel;
    private AStar aStarPanel;
    //Labels containing the metrics to be dislayed
    private JLabel randomViewed;
    private JLabel randomMovements;
    private JLabel primaryViewed;
    private JLabel primaryMovements;
    private JLabel aStarViewed;
    private JLabel aStarMovements;

    /**
     * Constructor for MainWindow. Accepts a Parameter object and a boolean
     * matrix as parameters. The parameter object contains the simulation
     * parameters while the boolean matrix represents the map of the
     * environment.
     *
     * @param params   A Parameter object representing the simulation parameters
     * @param occupied A boolean matrix representing the map of the environment
     */
    public MainWindow(Parameters params, boolean[][] occupied)
    {

        this.parameters = params;

        //Initialize the parameters of the main window
        setTitle("Robot Simulation Version 2");
        int width = params.environmentWidth + 50;
        int height = params.environmentHeight + 150;
        totalElements = params.environmentHeight * params.environmentWidth;
        setMinimumSize(new Dimension(500, 300));
        setSize(width, height);
        setResizable(false);
        setBackground(Color.gray);
        getContentPane().setLayout(new BoxLayout(getContentPane(),
                BoxLayout.PAGE_AXIS));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        timer = new Timer(100, this);

        //Initialize the tabs and add them to the tabbed pane
        //Panes independent of navigation algorithms
        tabbedPane = new JTabbedPane();
        mapPanel = new Map(params, occupied);
        tabbedPane.addTab("Map", mapPanel);
        //Navigation algorithm dependent tabs
        randomPanel = new RandomWanderer(params, occupied);
        primaryPanel = new PrimaryMovement(params, occupied);
        aStarPanel = new AStar(params, occupied);
        tabbedPane.addTab("Random Wanderer", randomPanel);
        tabbedPane.addTab("Primary Movement", primaryPanel);
        tabbedPane.addTab("A* Pathfinding", aStarPanel);
        //Add tabbed pane to main window
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        //Create results panel and add it to the main window
        JPanel resultsPanel = createResultsPanel();
        getContentPane().add(resultsPanel);

        //Initialize buttons
        runButton = new JButton("Start");
        runButton.addActionListener(new RunButtonPressedListener());
        runButton.setEnabled(true);

        ffButton = new JButton("Fast Forward");
        ffButton.addActionListener(new FFButtonPressedListener());
        ffButton.setEnabled(false);

        stopButton = new JButton("Stop");
        stopButton.addActionListener(new StopButtonPressedListener());
        stopButton.setEnabled(false);

        //Create button panel and add buttons to it
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 4, 2));
        buttonPanel.add(runButton);
        buttonPanel.add(ffButton);
        buttonPanel.add(stopButton);

        getContentPane().add(buttonPanel);
    }

    /**
     * Creates the JPanel that will be used to display all of the information
     * about the metrics that have been gathered
     *
     * @return Returns a JPanel that will be used to contain the gathered
     *         metrics
     */
    public JPanel createResultsPanel()
    {
        //Labels dependent on navigation algorithms
        randomViewed = new JLabel();
        randomMovements = new JLabel();
        primaryViewed = new JLabel();
        primaryMovements = new JLabel();
        aStarViewed = new JLabel();
        aStarMovements = new JLabel();

        randomViewed.setText("" + randomPanel.getViewedElements());
        randomMovements.setText("" + randomPanel.getMovements());

        primaryViewed.setText("" + primaryPanel.getViewedElements());
        primaryMovements.setText("" + primaryPanel.getMovements());

        aStarViewed.setText("" + aStarPanel.getViewedElements());
        aStarMovements.setText("" + aStarPanel.getMovements());

        //Creation of JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 4, 4, 2));

        //Add navigation algorithm identification labels to JPanel. Empty panel
        //is used for spacing.
        panel.add(new JLabel(""));
        panel.add(new JLabel("Random Wanderer"));
        panel.add(new JLabel("Primary Movement"));
        panel.add(new JLabel("A* Pathfinding"));

        //Add labels for percentage viewed metric
        panel.add(new JLabel("Percentage Viewed: "));
        panel.add(randomViewed);
        panel.add(primaryViewed);
        panel.add(aStarViewed);

        //Add labels for movements taken metric
        panel.add(new JLabel("Movements Taken: "));
        panel.add(randomMovements);
        panel.add(primaryMovements);
        panel.add(aStarMovements);

        return panel;
    }

    /**
     * Implemented from parent class. Whenever the timer event fires this method
     * will be called. Updates metrics displayed on screen.
     *
     * @param e An ActionEvent representing a tick of the timer.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //Create format for percent viewed labels
        DecimalFormat df = new DecimalFormat("#.000");

        //Display metrics gathered from navigation algorithms. Dependent on 
        //navigation algorithms.
        randomViewed.setText("" + df.format((randomPanel.getViewedElements() /
                (totalElements * 1.0)) * 100));
        randomMovements.setText("" + randomPanel.getMovements());

        primaryViewed.setText("" + df.format((primaryPanel.getViewedElements() /
                (totalElements * 1.0)) * 100));
        primaryMovements.setText("" + primaryPanel.getMovements());

        aStarViewed.setText("" + df.format((aStarPanel.getViewedElements() /
                (totalElements * 1.0)) * 100));
        aStarMovements.setText("" + aStarPanel.getMovements());
    }

    /**
     * Inner class RunButtonPressedListener handles the logic for the run
     * button. The run button starts the timers on each of the navigation
     * algorithms as well as the timer for the main window. Also disables the
     * run button and enables the fast forward and stop buttons.
     */
    class RunButtonPressedListener implements ActionListener
    {

        /**
         * Implemented from parent class. Whenever the run button pressed event
         * fires this method will be called. Starts Timers for each algorithm
         * and sets the enabled property of the various buttons to their
         * appropriate state.
         *
         * @param e An ActionEvent representing a press of the run button.
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //Start timers
            timer.start();
            //Timers dependent on navigation algorithms
            randomPanel.startTimer();
            primaryPanel.startTimer();
            aStarPanel.startTimer();

            //Set the enabled property of the buttons
            runButton.setEnabled(false);
            ffButton.setEnabled(true);
            stopButton.setEnabled(true);
        }
    }

    /**
     * Inner class FFButtonPressedListener handles the logic for the fast
     * forward button. The fast forward button will decrease the delay between
     * timer fires to 10 millisecond and changes the text to read 'Resume'. If
     * the delay is already 1 millisecond pressing the resume button will
     * increase the delay of the timer back to its original value of 100
     * milliseconds and resets the text to 'Fast Forward'.
     */
    class FFButtonPressedListener implements ActionListener
    {

        /**
         * Implemented from parent class. Whenever the fast forward button
         * pressed event fires this method will be called. Decreases the timer
         * delay for each algorithm if it is at 100 milliseconds and increases
         * it if it is and changes the text of the Fast Forward button.
         *
         * @param e An ActionEvent representing a press of the fast forward
         *          button.
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //If the timer delay is set to the slower speed decrease the timer
            //delay to 10 milliseconds and change the button text to 'Resume'
            if (timer.getDelay() > 10)
            {
                timer.setDelay(10);
                //Navigation algorithm dependent timers
                randomPanel.setTimer(10);
                primaryPanel.setTimer(10);
                aStarPanel.setTimer(10);

                ffButton.setText("Resume");
            }
            //If the timer is already at its minimum delay set the timer to its
            //maximum delay and change the button text to 'Fast Forward'.
            else
            {
                timer.setDelay(100);
                //Navigation algorithm dependent timers
                randomPanel.setTimer(100);
                primaryPanel.setTimer(100);
                aStarPanel.setTimer(100);

                ffButton.setText("Fast Forward");
            }
        }
    }

    /**
     * Inner class StopButtonPressedListener handles the logic for the stop
     * button. The stop button will stop the timers for each navigation
     * algorithm as well as the timer for the main window. Also, the stop
     * button will write a report to the directory that contains the
     * application's source files. The report contains all parameters used for
     * the simulation, all metrics gathered, and the identification of the
     * navigation algorithms used.
     */
    class StopButtonPressedListener implements ActionListener
    {

        /**
         * Implemented from parent class. Whenever the stop button
         * pressed event fires this method will be called. Stops the timer
         * for each algorithm and generates a report containing all gathered
         * metrics, algorithm identification, and parameters chosen..
         *
         * @param e An ActionEvent representing a press of the Stop button.
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            timer.stop();
            //Navigation algorithm dependent timers
            randomPanel.stopTimer();
            primaryPanel.stopTimer();
            aStarPanel.stopTimer();

            runButton.setEnabled(true);
            ffButton.setEnabled(false);
            stopButton.setEnabled(false);

            //The generated report will be named using the current date and time
            //Creates date format for the name of the report
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
            Date date = new Date();

            //Create file to write report to
            File output = new File(dateFormat.format(date) + ".txt");
            try
            {
                PrintWriter out = new PrintWriter(output);

                //Write our simulation parameters
                out.println("-------Experiment Parameters-------");
                out.println("Robot swarm size: " + parameters.robotQuantity);
                out.println("Robot sensor distance: " +
                        parameters.sensorDistance);
                out.println("Obstacle density: " + parameters.obstacleDensity +
                        "%");
                out.println("Environment Width: " +
                        parameters.environmentWidth);
                out.println("Environment Height: " +
                        parameters.environmentHeight);
                out.println("Random Seed: " +
                        parameters.seed);
                out.println();

                //Total number of elements is used to determine what percent
                //of the environment was viewed
                int totalElements = parameters.environmentHeight *
                        parameters.environmentWidth;
                //Creates the decimal format used for writing the percent viewed
                DecimalFormat df = new DecimalFormat("#.000");

                //Write out metrics
                //Navigation algorithm dependent
                out.println(df.format((randomPanel.getViewedElements() /
                        (totalElements * 1.0)) * 100) +
                        "\t" +
                        df.format((primaryPanel.getViewedElements() /
                                (totalElements * 1.0)) * 100) +
                        "\t" +
                        df.format((aStarPanel.getViewedElements() /
                                (totalElements * 1.0)) * 100));
                out.println(randomPanel.getMovements() +
                        "\t" + primaryPanel.getMovements() +
                        "\t" + aStarPanel.getMovements());

                out.close();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

}
