
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

public class RandomWanderer extends JPanel implements ActionListener
{

    private boolean[][] occupied;
    private boolean[][] viewed;
    private int viewedElements = 0;
    private int movements = 0;
    private Parameters params;
    private int totalElements;
    private Timer timer;
    ArrayList<Robot> robots = new ArrayList();
    Random random = new Random(179424691);

    public RandomWanderer(Parameters params, boolean[][] occupied)
    {
        timer = new Timer(100, this);

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setSize(params.environmentWidth, params.environmentHeight);
        this.params = params;
        totalElements = params.environmentHeight * params.environmentWidth;

        switch (params.robotQuantity)
        {
            case 4:
                robots.add(new Robot(new Point(params.environmentWidth - 1,
                        params.environmentHeight - 1), 135, 90, 45,
                        params.sensorDistance));
                robots.get(robots.size() - 1).bearing = 2;
            case 3:
                robots.add(new Robot(new Point(0, params.environmentHeight - 1),
                        45, 0, 315,
                        params.sensorDistance));
                robots.get(robots.size() - 1).bearing = 0;
            case 2:
                robots.add(new Robot(new Point(params.environmentWidth - 1, 0),
                        225, 180, 135,
                        params.sensorDistance));
                robots.get(robots.size() - 1).bearing = 4;
            case 1:
                robots.add(new Robot(new Point(0, 0), 315, 270, 225,
                        params.sensorDistance));
                robots.get(robots.size() - 1).bearing = 6;
                break;
            default:
                robots.add(new Robot(new Point(0, 0), 315, 270, 225,
                        params.sensorDistance));
                robots.get(robots.size() - 1).bearing = 6;
                break;
        }

        this.occupied = occupied.clone();
        viewed = new boolean[params.environmentWidth][params.environmentHeight];
    }

    /**
     *
     * @return
     */
    public int getViewedElements()
    {
        return viewedElements;
    }

    /**
     *
     * @return
     */
    public int getMovements()
    {
        return movements;
    }

    /**
     *
     * @param delay
     */
    public void setTimer(int delay)
    {
        timer.setDelay(delay);
    }

    /**
     *
     */
    public void startTimer()
    {
        timer.start();
    }

    /**
     *
     */
    public void stopTimer()
    {
        timer.stop();
    }

    public void incrementBearing(Robot robot)
    {
        switch (robot.bearing)
        {
            case 0:
                robot.bearing = 1;
                robot.sensor0Angle = 90;
                robot.sensor1Angle = 45;
                robot.sensor2Angle = 0;
                break;
            case 1:
                robot.bearing = 2;
                robot.sensor0Angle = 135;
                robot.sensor1Angle = 90;
                robot.sensor2Angle = 45;
                break;
            case 2:
                robot.bearing = 3;
                robot.sensor0Angle = 180;
                robot.sensor1Angle = 135;
                robot.sensor2Angle = 90;
                break;
            case 3:
                robot.bearing = 4;
                robot.sensor0Angle = 225;
                robot.sensor1Angle = 180;
                robot.sensor2Angle = 135;
                break;
            case 4:
                robot.bearing = 5;
                robot.sensor0Angle = 270;
                robot.sensor1Angle = 225;
                robot.sensor2Angle = 180;
                break;
            case 5:
                robot.bearing = 6;
                robot.sensor0Angle = 315;
                robot.sensor1Angle = 270;
                robot.sensor2Angle = 225;
                break;
            case 6:
                robot.bearing = 7;
                robot.sensor0Angle = 0;
                robot.sensor1Angle = 315;
                robot.sensor2Angle = 270;
                break;
            case 7:
                robot.bearing = 0;
                robot.sensor0Angle = 45;
                robot.sensor1Angle = 0;
                robot.sensor2Angle = 315;
                break;
        }
        robot.setSensors();
    }
    
    public void decrementBearing(Robot robot)
    {
        switch (robot.bearing)
        {
            case 0:
                robot.bearing = 7;
                robot.sensor0Angle = 0;
                robot.sensor1Angle = 315;
                robot.sensor2Angle = 270;
                break;
            case 1:
                robot.bearing = 0;
                robot.sensor0Angle = 45;
                robot.sensor1Angle = 0;
                robot.sensor2Angle = 315;
                break;
            case 2:
                robot.bearing = 1;
                robot.sensor0Angle = 90;
                robot.sensor1Angle = 45;
                robot.sensor2Angle = 0;
                break;
            case 3:
                robot.bearing = 2;
                robot.sensor0Angle = 135;
                robot.sensor1Angle = 90;
                robot.sensor2Angle = 45;
                break;
            case 4:
                robot.bearing = 3;
                robot.sensor0Angle = 180;
                robot.sensor1Angle = 135;
                robot.sensor2Angle = 90;
                break;                
            case 5:
                robot.bearing = 4;
                robot.sensor0Angle = 225;
                robot.sensor1Angle = 180;
                robot.sensor2Angle = 135;
                break;
            case 6:
                robot.bearing = 5;
                robot.sensor0Angle = 270;
                robot.sensor1Angle = 225;
                robot.sensor2Angle = 180;
                break;                
            case 7:
                robot.bearing = 6;
                robot.sensor0Angle = 315;
                robot.sensor1Angle = 270;
                robot.sensor2Angle = 225;
                break;
        }
        robot.setSensors();
    }

    public void incrementDirection(Robot robot)
    {
        switch (robot.bearing)
        {
            case 0:
                robot.current =
                        new Point(robot.current.x + 1, robot.current.y);
                break;
            case 1:
                robot.current =
                        new Point(robot.current.x + 1, robot.current.y - 1);
                break;
            case 2:
                robot.current =
                        new Point(robot.current.x, robot.current.y - 1);
                break;
            case 3:
                robot.current =
                        new Point(robot.current.x - 1, robot.current.y - 1);
                break;
            case 4:
                robot.current =
                        new Point(robot.current.x - 1, robot.current.y);
                break;
            case 5:
                robot.current =
                        new Point(robot.current.x - 1, robot.current.y + 1);
                break;
            case 6:
                robot.current =
                        new Point(robot.current.x, robot.current.y + 1);
                break;
            case 7:
                robot.current =
                        new Point(robot.current.x + 1, robot.current.y + 1);
                break;
        }
        robot.setSensors();
    }

    public boolean canIncrementDirection(Robot robot)
    {
        switch (robot.bearing)
        {
            case 0:
                return robot.current.x + 1 < params.environmentWidth;
            case 1:
                return robot.current.x + 1 < params.environmentWidth
                        && robot.current.y - 1 >= 0;
            case 2:
                return robot.current.y - 1 >= 0;
            case 3:
                return robot.current.x - 1 >= 0
                        && robot.current.y - 1 >= 0;
            case 4:
                return robot.current.x - 1 >= 0;
            case 5:
                return robot.current.x - 1 >= 0
                        && robot.current.y + 1 < params.environmentWidth;
            case 6:
                return robot.current.y + 1 < params.environmentWidth;
            case 7:
                return robot.current.x + 1 < params.environmentWidth
                        && robot.current.y + 1 < params.environmentWidth;
            default:
                return false;
        }
    }

    public void updateMapForward()
    {
        for (Robot r : robots)
        {
            for (int i = (((int) r.sensor0.getMinX() < 0) ? 0 : (int) r.sensor0.
                    getMinX()); i < ((r.sensor0.getMaxX()
                    >= params.environmentWidth) ? params.environmentWidth
                    : r.sensor0.getMaxX()); i++)
            {
                for (int j = (((int) r.sensor0.getMinY() < 0) ? 0
                        : (int) r.sensor0.getMinY()); j < ((r.sensor0.getMaxY()
                        >= params.environmentHeight) ? params.environmentHeight
                        : r.sensor0.getMaxY()); j++)
                {
                    if (r.sensor0.contains(i, j) && !viewed[i][j])
                    {
                        viewed[i][j] = true;
                        viewedElements++;
                    }
                }
            }
            for (int i = (((int) r.sensor1.getMinX() < 0) ? 0 : (int) r.sensor1.
                    getMinX()); i < ((r.sensor1.getMaxX()
                    >= params.environmentWidth) ? params.environmentWidth
                    : r.sensor1.getMaxX()); i++)
            {
                for (int j = (((int) r.sensor1.getMinY() < 0) ? 0
                        : (int) r.sensor1.getMinY()); j < ((r.sensor1.getMaxY()
                        >= params.environmentHeight) ? params.environmentHeight
                        : r.sensor1.getMaxY()); j++)
                {
                    if (r.sensor1.contains(i, j) && !viewed[i][j])
                    {
                        viewed[i][j] = true;
                        viewedElements++;
                    }
                }
            }
            for (int i = (((int) r.sensor2.getMinX() < 0) ? 0 : (int) r.sensor2.
                    getMinX()); i < ((r.sensor2.getMaxX()
                    >= params.environmentWidth) ? params.environmentWidth
                    : r.sensor2.getMaxX()); i++)
            {
                for (int j = (((int) r.sensor2.getMinY() < 0) ? 0
                        : (int) r.sensor2.getMinY()); j < ((r.sensor2.getMaxY()
                        >= params.environmentHeight) ? params.environmentHeight
                        : r.sensor2.getMaxY()); j++)
                {
                    if (r.sensor2.contains(i, j) && !viewed[i][j])
                    {
                        viewed[i][j] = true;
                        viewedElements++;
                    }
                }
            }
        }
    }

    public void updateMapRotateClockWise(Robot robot)
    {
        Arc2D.Double arc = new Arc2D.Double(Arc2D.PIE);
        arc.setFrame(robot.sensor0.getFrame());
        arc.setAngleStart(robot.sensor2.getAngleStart() + 45);
        arc.setAngleExtent(135);

        for (int i = (((int) arc.getMinX() < 0) ? 0 : (int) arc.getMinX()); i
                < ((arc.getMaxX() >= params.environmentWidth)
                ? params.environmentWidth : arc.getMaxX()); i++)
        {
            for (int j = (((int) arc.getMinY() < 0) ? 0 : (int) arc.getMinY());
                    j < ((arc.getMaxY() >= params.environmentHeight)
                    ? params.environmentHeight : arc.getMaxY()); j++)
            {
                if (arc.contains(i, j) && !viewed[i][j])
                {
                    viewed[i][j] = true;
                    viewedElements++;
                }
            }
        }
    }
    
    public void updateMapRotateCounterClockWise(Robot robot)
    {
        Arc2D.Double arc = new Arc2D.Double(Arc2D.PIE);
        arc.setFrame(robot.sensor0.getFrame());
        arc.setAngleStart(robot.sensor2.getAngleStart());
        arc.setAngleExtent(135);

        for (int i = (((int) arc.getMinX() < 0) ? 0 : (int) arc.getMinX()); i
                < ((arc.getMaxX() >= params.environmentWidth)
                ? params.environmentWidth : arc.getMaxX()); i++)
        {
            for (int j = (((int) arc.getMinY() < 0) ? 0 : (int) arc.getMinY());
                    j < ((arc.getMaxY() >= params.environmentHeight)
                    ? params.environmentHeight : arc.getMaxY()); j++)
            {
                if (arc.contains(i, j) && !viewed[i][j])
                {
                    viewed[i][j] = true;
                    viewedElements++;
                }
            }
        }
    }

    public boolean moveConflict(Robot robot)
    {
        Point point = new Point();
        switch (robot.bearing)
        {
            case 0:
                point.x = robot.current.x + 1;
                point.y = robot.current.y;
                break;
            case 1:
                point.x = robot.current.x + 1;
                point.y = robot.current.y - 1;
                break;
            case 2:
                point.x = robot.current.x;
                point.y = robot.current.y - 1;
                break;
            case 3:
                point.x = robot.current.x - 1;
                point.y = robot.current.y - 1;
                break;
            case 4:
                point.x = robot.current.x - 1;
                point.y = robot.current.y;
                break;
            case 5:
                point.x = robot.current.x - 1;
                point.y = robot.current.y + 1;
                break;
            case 6:
                point.x = robot.current.x;
                point.y = robot.current.y + 1;
                break;
            case 7:
                point.x = robot.current.x + 1;
                point.y = robot.current.y + 1;
                break;
        }
        if (occupied[point.x][point.y])
        {
            return true;
        }
        for (Robot r : robots)
        {
            if ((r.current.x == point.x) && (r.current.y == point.y))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if ((params.timeLimit == 0 || movements < params.timeLimit)
                && viewedElements < totalElements)
        {
            for (Robot r : robots)
            {
                int move = (random.nextInt(3));
                if ((move == 0 && canIncrementDirection(r)) && !moveConflict(r))
                {
                    incrementDirection(r);
                    updateMapForward();
                }
                else if(move == 1)
                {
                    updateMapRotateCounterClockWise(r);
                    incrementBearing(r);
                }
                else if(move == 2)
                {
                    updateMapRotateClockWise(r);
                    decrementBearing(r);
                }
            }
            movements++;
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (int i = 0; i < viewed.length; i++)
        {
            for (int j = 0; j < viewed[i].length; j++)
            {
                if (viewed[i][j] && occupied[i][j])
                {
                    g.setColor(Color.BLACK);
                    g.drawLine(i, j, i, j);
                }
                else if (viewed[i][j])
                {
                    g.setColor(Color.WHITE);
                    g.drawLine(i, j, i, j);
                }
                else
                {
                    g.setColor(Color.DARK_GRAY);
                    g.drawLine(i, j, i, j);
                }
            }
        }

        Graphics2D g2 = (Graphics2D) g;
        for (Robot r : robots)
        {

            g2.setColor(Color.GREEN);
            g2.fill(r.sensor0);
            g2.fill(r.sensor1);
            g2.fill(r.sensor2);

            g2.setColor(Color.RED);
            g2.drawLine(r.current.x, r.current.y, r.current.x, r.current.y);
        }
    }
}