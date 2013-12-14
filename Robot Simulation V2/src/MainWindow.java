
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

public class MainWindow extends JFrame implements ActionListener
{

    private JTabbedPane tabbedPane;
    private RandomWanderer randomPanel;
    private PrimaryMovement primaryPanel;
    AStar aStarPanel;
    private Map mapPanel;
    private Timer timer;
    private JLabel randomViewed;
    private JLabel randomMovements;
    private JLabel primaryViewed;
    private JLabel primaryMovements;
    private JLabel aStarViewed;
    private JLabel aStarMovements;
    private JButton runButton;
    private JButton ffButton;
    private JButton stopButton;
    private int totalElements;
    private Parameters parameters;

    public MainWindow(Parameters params, boolean[][] occupied)
    {
        this.parameters = params;
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

        tabbedPane = new JTabbedPane();
        randomPanel = new RandomWanderer(params, occupied);
        primaryPanel = new PrimaryMovement(params, occupied);
        aStarPanel = new AStar(params, occupied);
        mapPanel = new Map(params, occupied);

        tabbedPane.addTab("Random Wanderer", randomPanel);
        tabbedPane.addTab("Primary Movement", primaryPanel);
        tabbedPane.addTab("A* Pathfinding", aStarPanel);
        tabbedPane.addTab("Map", mapPanel);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel resultsPanel = createResultsPanel();
        getContentPane().add(resultsPanel);

        runButton = new JButton("Start");
        runButton.addActionListener(new RunButtonPressedListener());
        runButton.setEnabled(true);

        ffButton = new JButton("Fast Forward");
        ffButton.addActionListener(new FFButtonPressedListener());
        ffButton.setEnabled(false);

        stopButton = new JButton("Stop");
        stopButton.addActionListener(new StopButtonPressedListener());
        stopButton.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 4, 2));
        buttonPanel.add(runButton);
        buttonPanel.add(ffButton);
        buttonPanel.add(stopButton);

        getContentPane().add(buttonPanel);
    }

    public JPanel createResultsPanel()
    {
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

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 4, 4, 2));

        panel.add(new JLabel(""));
        panel.add(new JLabel("Random Wanderer"));
        panel.add(new JLabel("Primary Movement"));
        panel.add(new JLabel("A* Pathfinding"));

        panel.add(new JLabel("Percentage Viewed: "));
        panel.add(randomViewed);
        panel.add(primaryViewed);
        panel.add(aStarViewed);

        panel.add(new JLabel("Movements Taken: "));
        panel.add(randomMovements);
        panel.add(primaryMovements);
        panel.add(aStarMovements);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        DecimalFormat df = new DecimalFormat("#.000");

        randomViewed.setText("" + df.format((randomPanel.getViewedElements()
                / (totalElements * 1.0)) * 100));
        randomMovements.setText("" + randomPanel.getMovements());

        primaryViewed.setText("" + df.format((primaryPanel.getViewedElements()
                / (totalElements * 1.0)) * 100));
        primaryMovements.setText("" + primaryPanel.getMovements());

        aStarViewed.setText("" + df.format((aStarPanel.getViewedElements()
                / (totalElements * 1.0)) * 100));
        aStarMovements.setText("" + aStarPanel.getMovements());
        if ((aStarPanel.getViewedElements()
                / (totalElements * 1.0) * 100) >= 100)
        {
            StopButtonPressedListener sb = new StopButtonPressedListener();
            sb.actionPerformed(e);
            RobotSim sim = new RobotSim();
            if (parameters.obstacleDensity == 0)
            {
                if (parameters.robotQuantity == 1)
                {
                    if (parameters.seedList.size() > 0)
                    {
                        parameters.robotQuantity = 4;
                        parameters.obstacleDensity = .3;
                        parameters.seed = parameters.seedList.get(0);
                        parameters.seedList.remove(0);
                    }
                }
                else
                {
                    parameters.robotQuantity -= 1;
                    parameters.obstacleDensity = .3;
                }
            }
            else if (parameters.obstacleDensity == .1)
            {
                parameters.obstacleDensity = 0;
            }
            else if (parameters.obstacleDensity == .2)
            {
                parameters.obstacleDensity = .1;
            }
            else if (parameters.obstacleDensity == .3)
            {
                parameters.obstacleDensity = .2;
            }
            sim.start(parameters);
            this.dispose();
        }


    }

    public void start()
    {
        RunButtonPressedListener rb = new RunButtonPressedListener();
        rb.actionPerformed(null);

        FFButtonPressedListener ff = new FFButtonPressedListener();
        ff.actionPerformed(null);
    }

    class RunButtonPressedListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            timer.start();
            randomPanel.startTimer();
            primaryPanel.startTimer();
            aStarPanel.startTimer();

            runButton.setEnabled(false);
            ffButton.setEnabled(true);
            stopButton.setEnabled(true);
        }
    }

    class FFButtonPressedListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (timer.getDelay() > 10)
            {
                timer.setDelay(10);
                randomPanel.setTimer(10);
                primaryPanel.setTimer(10);
                aStarPanel.setTimer(10);

                ffButton.setText("Resume");
            }
            else
            {
                timer.setDelay(100);
                randomPanel.setTimer(100);
                primaryPanel.setTimer(100);
                aStarPanel.setTimer(100);

                ffButton.setText("Fast Forward");
            }
        }
    }

    class StopButtonPressedListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            timer.stop();
            randomPanel.stopTimer();
            primaryPanel.stopTimer();
            aStarPanel.stopTimer();

            runButton.setEnabled(true);
            ffButton.setEnabled(false);
            stopButton.setEnabled(false);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
            Date date = new Date();

            File output = new File(dateFormat.format(date) + ".txt");
            try
            {
                PrintWriter out = new PrintWriter(output);

                out.println("-------Experiment Parameters-------");
                out.println("Robot swarm size: " + parameters.robotQuantity);
                out.println("Robot sensor distance: "
                        + parameters.sensorDistance);
                out.println("Obstacle density: " + parameters.obstacleDensity
                        + "%");
                out.println("Environment Width: "
                        + parameters.environmentWidth);
                out.println("Environment Height: "
                        + parameters.environmentHeight);
                out.println("Random Seed: "
                        + parameters.seed);
                out.println();

                int totalElements = parameters.environmentHeight
                        * parameters.environmentWidth;
                DecimalFormat df = new DecimalFormat("#.000");

                out.println(df.format((randomPanel.getViewedElements()
                        / (totalElements * 1.0)) * 100)
                        + "\t"
                        + df.format((primaryPanel.getViewedElements()
                        / (totalElements * 1.0)) * 100)
                        + "\t"
                        + df.format((aStarPanel.getViewedElements()
                        / (totalElements * 1.0)) * 100));
                out.println(randomPanel.getMovements()
                        + "\t" + primaryPanel.getMovements()
                        + "\t" + aStarPanel.getMovements());

                out.close();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

}
