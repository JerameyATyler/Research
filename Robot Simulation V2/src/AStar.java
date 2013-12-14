
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AStar extends JPanel implements ActionListener
{

    private boolean[][] occupied;
    private boolean[][] viewed;
    private int viewedElements = 0;
    private int movements = 0;
    private Parameters params;
    private int totalElements;
    private int robotsCompleted = 0;
    private Timer timer;
    ArrayList<Robot> robots = new ArrayList();

    public AStar(Parameters params, boolean[][] occupied)
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
        for (Robot r : robots)
        {
            viewed[r.current.x][r.current.y] = true;
            updateMapForward(r);
        }

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

    public void updateMapForward(Robot r)
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
        viewedElements = 0;
        for (int i = 0; i < viewed.length; i++)
        {
            for (int j = 0; j < viewed[i].length; j++)
            {
                if (viewed[i][j])
                {
                    viewedElements++;
                }
            }
        }
    }

    public void updateMapRotate(Robot robot, int angleStart)
    {
        Arc2D.Double arc = new Arc2D.Double(Arc2D.PIE);
        arc.setFrame(robot.sensor0.getFrame());
        arc.setAngleStart(angleStart);
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
        robot.setSensors();

        viewedElements = 0;
        for (int i = 0; i < viewed.length; i++)
        {
            for (int j = 0; j < viewed[i].length; j++)
            {
                if (viewed[i][j])
                {
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

    /**
     * Creates the neighbor list that is associated with the A* pathfinding
     * algorithm
     *
     * @param parent The node that will act as the parent node for each node in
     *               the neighbor list
     * @param matrix Matrix of Nodes representing the environment
     *
     * @return An array list of Nodes representing the neighbors of the given
     *         parent Node
     */
    public ArrayList<Node> neighborList(Node parent, Node goal, Node[][] matrix)
    {
        //List of all valid neighbors for given Node
        ArrayList<Node> list = new ArrayList();

        //The following section will check if a proposed neighbor is valid, 
        //and if so, create a neighbor Node and calculate its cost

        //Neighbor to the South
        if (parent.getY() + 1 < params.environmentHeight)
        {
            if ((viewed[parent.getX()][parent.getY() + 1]
                    && !occupied[parent.getX()][parent.getY() + 1])
                    || (goal == matrix[parent.getX()][parent.getY() + 1]))
            {
                Node node = matrix[parent.getX()][parent.getY() + 1];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the Southeast
        if ((parent.getX() + 1 < params.environmentWidth) && (parent.getY()
                + 1 < params.environmentHeight))
        {
            if ((viewed[parent.getX() + 1][parent.getY() + 1]
                    && !occupied[parent.getX() + 1][parent.getY() + 1])
                    || (goal == matrix[parent.getX() + 1][parent.getY() + 1]))
            {
                Node node = matrix[parent.getX() + 1][parent.getY() + 1];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the East
        if (parent.getX() + 1 < params.environmentWidth)
        {
            if ((viewed[parent.getX() + 1][parent.getY()]
                    && !occupied[parent.getX() + 1][parent.getY()])
                    || (goal == matrix[parent.getX() + 1][parent.getY()]))
            {
                Node node = matrix[parent.getX() + 1][parent.getY()];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the Northeast        
        if ((parent.getX() + 1 < params.environmentWidth)
                && (parent.getY() - 1 >= 0))
        {
            if ((viewed[parent.getX() + 1][parent.getY() - 1]
                    && !occupied[parent.getX() + 1][parent.getY() - 1])
                    || (goal == matrix[parent.getX() + 1][parent.getY() - 1]))
            {
                Node node = matrix[parent.getX() + 1][parent.getY() - 1];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the North
        if (parent.getY() - 1 >= 0)
        {
            if ((viewed[parent.getX()][parent.getY() - 1]
                    && !occupied[parent.getX()][parent.getY() - 1])
                    || (goal == matrix[parent.getX()][parent.getY() - 1]))
            {
                Node node = matrix[parent.getX()][parent.getY() - 1];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the Northwest
        if ((parent.getX() - 1 >= 0) && (parent.getY() - 1 >= 0))
        {
            if ((viewed[parent.getX() - 1][parent.getY() - 1]
                    && !occupied[parent.getX() - 1][parent.getY() - 1])
                    || (goal == matrix[parent.getX() - 1][parent.getY() - 1]))
            {
                Node node = matrix[parent.getX() - 1][parent.getY() - 1];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the West
        if (parent.getX() - 1 >= 0)
        {
            if ((viewed[parent.getX() - 1][parent.getY()]
                    && !occupied[parent.getX() - 1][parent.getY()])
                    || (goal == matrix[parent.getX() - 1][parent.getY()]))
            {
                Node node = matrix[parent.getX() - 1][parent.getY()];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the Southwest
        if ((parent.getX() - 1 >= 0)
                && (parent.getY() + 1 < params.environmentHeight))
        {
            if ((viewed[parent.getX() - 1][parent.getY() + 1]
                    && !occupied[parent.getX() - 1][parent.getY() + 1])
                    || (goal == matrix[parent.getX() - 1][parent.getY() + 1]))
            {
                Node node = matrix[parent.getX() - 1][parent.getY() + 1];
                node.calculateG(parent);
                list.add(node);
            }
        }

        return list;
    }

    private Stack<Point> createPath(Node node)
    {

        Stack<Point> path = new Stack();
        while ((node.getParent()) != null)
        {
            path.push(new Point(node.getX(), node.getY()));
            node = node.getParent();
        }
        return path;
    }

    public boolean acquireGoal(Point currentPoint, Robot r)
    {
        //Starting coordinates
        int x = currentPoint.x;
        int y = currentPoint.y;
        //Initial direction
        int direction = 0;
        //Width and height of current row or column
        int width = 0;
        int height = 0;

        //For every element in the viewed, perform this loop
        for (int i = 0; i < viewed.length * viewed[0].length;)
        {
            //Increment Width or Height according to direction
            switch (direction)
            {
                case 0: //North
                    height++;
                    break;
                case 1: //East
                    width++;
                    break;
                case 2: //South
                    height++;
                    break;
                case 3: //West
                    width++;
                    break;
                default: //Error, set everything back to original
                    x = currentPoint.x;
                    y = currentPoint.y;
                    direction = 0;
                    i = 0;
                    width = 0;
                    height = 0;
                    break;
            }

            //Iterate through every element of either the current row or 
            //column, depending on the direction            
            for (int j = 0;
                    j < ((direction == 0 || direction == 2) ? height : width);
                    j++)
            {
                //Check if current coordinates are in bounds of viewed
                if (!(x < 0 || y < 0
                        || x >= viewed.length || y >= viewed[x].length))
                {
                    //If viewed at current coordinates has not been viewed, 
                    //return those coordinates as Point.
                    if (!viewed[x][y])
                    {
                        //if (validateGoal(new Point(x, y)))
                        {
                            r.goal = new Point(x,y);
                            boolean valid = aStar(currentPoint, r.goal, r);
                            if(valid)
                            {
                                return true;
                            }
                            else
                            {
                                i++;
                            }
                        }
                    } //If viewed at current coordinates has been viewed,
                    //increment counter
                    else
                    {
                        i++;
                    }
                }
                //Increment coordinates according to direction
                switch (direction)
                {
                    case 0: //North
                        y -= 1;
                        break;
                    case 1: //East
                        x += 1;
                        break;
                    case 2: //South
                        y += 1;
                        break;
                    case 3: //West
                        x -= 1;
                        break;
                }
            }
            //Increment direction
            direction = (direction + 1 > 3) ? 0 : direction + 1;
        }
        /*
         * If no element exists that has not already been viewed, return new
         * Point
         */

        return false;
    }

    public boolean validateGoal(Point point)
    {
        for (int i = ((point.x - 1 < 0) ? 0 : point.x - 1);
                i <= ((point.x + 1 >= params.environmentWidth)
                ? point.x : point.x + 1); i++)
        {
            for (int j = ((point.y - 1 < 0) ? 0 : point.y - 1);
                    j <= ((point.y + 1 >= params.environmentHeight)
                    ? point.y : point.y + 1); j++)
            {
                if (viewed[i][j] && !occupied[i][j])
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean aStar(Point start, Point finish, Robot robot)
    {
        ArrayList<Node> closedSet = new ArrayList();
        ArrayList<Node> openSet = new ArrayList();

        Node[][] matrix =
                new Node[params.environmentWidth][params.environmentHeight];
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix[i].length; j++)
            {
                Node node = new Node();
                node.setX(i);
                node.setY(j);
                matrix[i][j] = node;
            }
        }

        Node current = matrix[start.x][start.y];
        Node goal = matrix[finish.x][finish.y];
        openSet.add(current);

        current.setG(0);
        current.calculateH(goal);
        current.calculateF();

        while (!openSet.isEmpty())
        {
            current = openSet.get(0);
            if (current == goal)
            {
                robot.hasGoal = true;
                robot.path = createPath(goal);
                return true;
            }

            openSet.remove(current);
            closedSet.add(current);

            ArrayList<Node> neighborList = neighborList(current, goal, matrix);

            for (Node n : neighborList)
            {
                int tentativeGScore = current.getG() + nodeDistance(current, n);

                if (closedSet.contains(n))// && tentativeGScore >= n.getG())
                {
                    continue;
                }

                if (!closedSet.contains(n))// || tentativeGScore < n.getG())
                {
                    n.setParent(current);
                    n.setG(tentativeGScore);
                    n.calculateH(goal);
                    n.calculateF();

                    if (!openSet.contains(n))
                    {
                        openSet.add(n);
                        Collections.sort(openSet);
                    }
                }
            }
        }
        return false;
    }

    public int nodeDistance(Node a, Node b)
    {
        return (int) Math.sqrt((Math.abs(a.getX() - b.getX()))
                + (Math.abs(a.getY() - b.getY())));
    }

    public void adjustBearing(Robot r)
    {
        Point next = ((!r.path.isEmpty() || r.path.size() > 0)
                ? r.path.peek() : r.goal);
        Point curr = r.current;

        switch (r.bearing)
        {
            case 0:
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 0;
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, -90);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, -90);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, -90);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                break;
            case 1:
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 1;
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                break;
            case 2:
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 2;
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                break;
            case 3:
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 3;
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                break;
            case 4:
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 135);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 4;
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 135);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 135);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 135);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                break;
            case 5:
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 5;
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                break;
            case 6:
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, 225);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, 225);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, 225);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 6;
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                break;
            case 7:
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, 270);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, 270);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, 270);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, 270);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 225);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 225);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 225);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 7;
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                break;
        }
        r.setSensors();
    }

    public int bearingNeeded(Point current, Point goal)
    {
        if (goal.x < current.x && goal.y < current.y)
        {
            return 3;
        }
        else if (goal.x == current.x && goal.y < current.y)
        {
            return 2;
        }
        else if (goal.x > current.x && goal.y < current.y)
        {
            return 1;
        }
        else if (goal.x > current.x && goal.y == current.y)
        {
            return 0;
        }
        else if (goal.x > current.x && goal.y > current.y)
        {
            return 7;
        }
        else if (goal.x == current.x && goal.y > current.y)
        {
            return 6;
        }
        else if (goal.x < current.x && goal.y > current.y)
        {
            return 5;
        }
        else
        {
            return 4;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if ((params.timeLimit == 0 || movements < params.timeLimit)
                && viewedElements < totalElements
                && robotsCompleted != robots.size())
        {
            for (Robot r : robots)
            {
                if (!r.finished && r.hasGoal)
                {
                    int tempBearing = r.bearing;
                    adjustBearing(r);
                    if (tempBearing == r.bearing)
                    {
                        if (!r.path.isEmpty() && r.path.size() > 0)
                        {
                            r.current = r.path.pop();

                        }
                        updateMapForward(r);
                        r.setSensors();
                        int bearingNeeded = bearingNeeded(r.current, r.goal);

                        if (((r.path.isEmpty() || r.path.size() == 0)
                                && r.bearing == bearingNeeded)
                                || (r.goal.x == r.current.x
                                && r.goal.y == r.current.y))
                        {
                            r.hasGoal = false;
                        }
                    }
                }
                else if (!r.finished)
                {
                    boolean needGoal = true;
                    while (needGoal)
                    {
                        needGoal = !acquireGoal(r.current, r);
                    }
                }
            }

            movements++;

            repaint();
        }
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