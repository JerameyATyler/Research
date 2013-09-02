
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * Creates a dialog window that allows the user to select the parameters that
 * will be used in the experiment.
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public class ParameterSelectionDialog extends JDialog
        implements ActionListener, PropertyChangeListener
{

    //Slider containing the max and min number of robots
    static final short MIN_ROBOTS = 1;
    static final short MAX_ROBOTS = 4;
    short robotQuantity = MIN_ROBOTS;
    JSlider quantitySlider = new JSlider(JSlider.HORIZONTAL, MIN_ROBOTS,
            MAX_ROBOTS, MIN_ROBOTS);
    //Slider containing the max and min distance sensors can reach
    static final short MIN_SENSOR_DISTANCE = 1;
    static final short MAX_SENSOR_DISTANCE = 10;
    short sensorDistance = MIN_SENSOR_DISTANCE;
    JSlider sensorSlider = new JSlider(JSlider.HORIZONTAL,
            MIN_SENSOR_DISTANCE, MAX_SENSOR_DISTANCE,
            MIN_SENSOR_DISTANCE);
    //Slider containing the max and min obstacle density
    static final short MIN_OBSTACLE_DENSITY = 0;
    static final short MAX_OBSTACLE_DENSITY = 30;
    short obstacleDensity = MIN_OBSTACLE_DENSITY;
    JSlider obstacleSlider = new JSlider(JSlider.HORIZONTAL,
            MIN_OBSTACLE_DENSITY, MAX_OBSTACLE_DENSITY,
            MIN_OBSTACLE_DENSITY);
    //Slider containing the max and min environment width
    static final short MIN_ENVIRONMENT_WIDTH = 100;
    static final short MAX_ENVIRONMENT_WIDTH = 1000;
    short environmentWidth = MIN_ENVIRONMENT_WIDTH;
    JSlider environmentWidthSlider = new JSlider(JSlider.HORIZONTAL,
            MIN_ENVIRONMENT_WIDTH, MAX_ENVIRONMENT_WIDTH,
            MIN_ENVIRONMENT_WIDTH);
    //Slider containing the max and min environment height
    static final short MIN_ENVIRONMENT_HEIGHT = 100;
    static final short MAX_ENVIRONMENT_HEIGHT = 1000;
    short environmentHeight = MIN_ENVIRONMENT_HEIGHT;
    JSlider environmentHeightSlider = new JSlider(JSlider.HORIZONTAL,
            MIN_ENVIRONMENT_HEIGHT, MAX_ENVIRONMENT_HEIGHT,
            MIN_ENVIRONMENT_HEIGHT);
    //Field containing the time limit for the experiment
    JFormattedTextField timeLimitField;
    private NumberFormat timeLimitFormat;
    int timeLimit;
    //Show visual button
    JCheckBox isVisual = new JCheckBox("Show Visuals?");
    //Ok button
    JButton okButton;
    private boolean okPressed;
    //Cancel button
    JButton cancelButton;

    /**
     * No parameter constructor. Sets the properties of each slider, button,
     * combo box, frame, and panel. Each component is added to its respective
     * panel, then all panels are added to the dialog.
     */
    public ParameterSelectionDialog()
    {
        quantitySlider.setMajorTickSpacing(MIN_ROBOTS);
        quantitySlider.setPaintTicks(true);
        quantitySlider.setPaintLabels(true);
        quantitySlider.setSnapToTicks(true);
        quantitySlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.
                createLineBorder(Color.BLACK), "Robot Quantity"));

        sensorSlider.setMajorTickSpacing(MIN_SENSOR_DISTANCE);
        sensorSlider.setPaintTicks(true);
        sensorSlider.setPaintLabels(true);
        sensorSlider.setSnapToTicks(true);
        sensorSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.
                createLineBorder(Color.BLACK), "Sensor Distance"));

        obstacleSlider.setMajorTickSpacing(MAX_OBSTACLE_DENSITY / 6);
        obstacleSlider.setMinorTickSpacing(1);
        obstacleSlider.setPaintTicks(true);
        obstacleSlider.setPaintLabels(true);
        obstacleSlider.setSnapToTicks(true);
        obstacleSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.
                createLineBorder(Color.BLACK), "Obstacle Density"));

        environmentWidthSlider.setMajorTickSpacing(MIN_ENVIRONMENT_WIDTH);
        environmentWidthSlider.setMinorTickSpacing(MIN_ENVIRONMENT_WIDTH / 4);
        environmentWidthSlider.setPaintTicks(true);
        environmentWidthSlider.setPaintLabels(true);
        environmentWidthSlider.setSnapToTicks(true);
        environmentWidthSlider.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Environment Width"));

        environmentHeightSlider.setMajorTickSpacing(MIN_ENVIRONMENT_HEIGHT);
        environmentHeightSlider.setMinorTickSpacing(MIN_ENVIRONMENT_HEIGHT / 4);
        environmentHeightSlider.setPaintTicks(true);
        environmentHeightSlider.setPaintLabels(true);
        environmentHeightSlider.setSnapToTicks(true);
        environmentHeightSlider.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "Environment Height"));

        timeLimitField = new JFormattedTextField(timeLimitFormat);
        timeLimitField.setValue(new Integer(timeLimit));
        timeLimitField.setColumns(10);
        timeLimitField.addPropertyChangeListener("value", this);

        JPanel sliderPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        this.getContentPane().setLayout(new BoxLayout(getContentPane(),
                BoxLayout.PAGE_AXIS));
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));

        sliderPanel.add(quantitySlider);
        sliderPanel.add(sensorSlider);
        sliderPanel.add(obstacleSlider);
        sliderPanel.add(environmentWidthSlider);
        sliderPanel.add(environmentHeightSlider);
        sliderPanel.add(new JLabel("Time Limit: "));
        sliderPanel.add(timeLimitField);

        okButton = new JButton("OK");
        okButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        this.setSize(400, 450);
        this.setTitle("Simulation Parameters");
        this.add(sliderPanel);
        this.add(buttonPanel);
    }

    /**
     * Displays the dialog to the user. If the Ok button has been pressed, all
     * values are transfered from the sliders and combo box to the corresponding
     * properties in the Parameters object.
     *
     * @param params A Parameters object that will contain the experiment
     *               parameters.
     *
     * @return A Parameters object that contains the experiment parameters.
     */
    public boolean showDialog(Parameters params)
    {
        okPressed = false;
        show();
        if (okPressed)
        {
            okPressed = true;
            params.robotQuantity = (short) quantitySlider.getValue();
            params.sensorDistance = (short) sensorSlider.getValue();
            params.obstacleDensity = obstacleSlider.getValue() / 100.0;
            params.environmentWidth = (short) environmentWidthSlider.getValue();
            params.environmentHeight = (short) environmentHeightSlider.
                    getValue();
            params.timeLimit = timeLimit;
            params.isVisual = true;
        }
        return okPressed;
    }

    /**
     * Implementation of the ActionListener interface. If the action that has
     * been performed was a click of the Ok button, okPressed is set to true the
     * dialog's visibility is set to false. If the action that has been
     * performed was a click of the Cancel button, the dialog's visibility is
     * set to false.
     *
     * @param e ActionEvent object that corresponds to the action performed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == okButton)
        {
            okPressed = true;
            setVisible(false);
        }
        else if (source == cancelButton)
        {
            setVisible(false);
        }
    }

    /**
     * Implementation of the PropertyChange interface. If the property that has
     * been changed was the timeLimitField, timeLimit is set to the integer
     * value contained in the timeLimitField.
     *
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        Object source = evt.getSource();

        if (source == timeLimitField)
        {
            timeLimit = ((Number) timeLimitField.getValue()).intValue();
        }
    }
}