
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Implementation of A* pathfinding algorithm. Goal is found by selecting the
 * closest point on the map that has not been previously viewed.
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public class AStar extends NavigationLibrary
{

    /**
     * Constructor for A* algorithm. Accepts a list of parameters and the
     * environment map as input.
     *
     * @param params   A Parameters object that will contain the experiment
     *                 parameters.
     * @param occupied A boolean matrix representing the map of the environment
     */
    public AStar(Parameters params, boolean[][] occupied)
    {
        //Parameters are passed to the constructor of the super class
        super(params, occupied);
    }

    /**
     * Updates the map within the sensor range for a single robot moving forward
     *
     * @param r A Robot object representing the robot that is being incremented
     */
    public void updateMapForward(Robot r)
    {
        //Update the map for sensor 0
        //Increment through the x axis
        for (int i = (((int) r.sensor0.getMinX() < 0) ? 0 : (int) r.sensor0.
                getMinX()); i < ((r.sensor0.getMaxX() >=
                params.environmentWidth) ? params.environmentWidth :
                r.sensor0.getMaxX()); i++)
        {
            //Increment through the y axis
            for (int j = (((int) r.sensor0.getMinY() < 0) ? 0 :
                    (int) r.sensor0.getMinY()); j < ((r.sensor0.getMaxY() >=
                    params.environmentHeight) ? params.environmentHeight :
                    r.sensor0.getMaxY()); j++)
            {
                //If a point is in the sensor's range and it has not been
                //viewed previously, mark it as viewed
                if (r.sensor0.contains(i, j) && !viewed[i][j])
                {
                    viewed[i][j] = true;
                    viewedElements++;
                }
            }
        }
        //Update the map for sensor 1
        //Increment through the x axis
        for (int i = (((int) r.sensor1.getMinX() < 0) ? 0 : (int) r.sensor1.
                getMinX()); i < ((r.sensor1.getMaxX() >=
                params.environmentWidth) ? params.environmentWidth :
                r.sensor1.getMaxX()); i++)
        {
            //Increment through the y axis
            for (int j = (((int) r.sensor1.getMinY() < 0) ? 0 :
                    (int) r.sensor1.getMinY()); j < ((r.sensor1.getMaxY() >=
                    params.environmentHeight) ? params.environmentHeight :
                    r.sensor1.getMaxY()); j++)
            {
                //If a point is in the sensor's range and it has not been
                //viewed previously, mark it as viewed
                if (r.sensor1.contains(i, j) && !viewed[i][j])
                {
                    viewed[i][j] = true;
                    viewedElements++;
                }
            }
        }
        //Update the map for sensor 2
        //Increment through the x axis
        for (int i = (((int) r.sensor2.getMinX() < 0) ? 0 : (int) r.sensor2.
                getMinX()); i < ((r.sensor2.getMaxX() >=
                params.environmentWidth) ? params.environmentWidth :
                r.sensor2.getMaxX()); i++)
        {
            //Increment through the y axis
            for (int j = (((int) r.sensor2.getMinY() < 0) ? 0 :
                    (int) r.sensor2.getMinY()); j < ((r.sensor2.getMaxY() >=
                    params.environmentHeight) ? params.environmentHeight :
                    r.sensor2.getMaxY()); j++)
            {
                //If a point is in the sensor's range and it has not been
                //viewed previously, mark it as viewed
                if (r.sensor2.contains(i, j) && !viewed[i][j])
                {
                    viewed[i][j] = true;
                    viewedElements++;
                }
            }
        }

        //Count the number of elements in the environment that have been viewed
        viewedElements = 0;
        for (boolean[] viewed1 : viewed)
        {
            for (int j = 0; j < viewed1.length; j++)
            {
                if (viewed1[j])
                {
                    viewedElements++;
                }
            }
        }
    }

    /**
     * Updates the map within the sensor range for a single robot rotating
     *
     * @param robot      A Robot object representing the robot that is being
     *                   incremented
     * @param angleStart An int representing the angle the of the rightmost ray
     *                   of sensor 0
     */
    public void updateMapRotate(Robot robot, int angleStart)
    {
        //Create Arc2D to allow iteration through points only inside of sensors.
        //A 45 degree rotation will cause the sensors to sweep 135 degree area,
        //excluding the spread of the sensors
        Arc2D.Double arc = new Arc2D.Double(Arc2D.PIE);
        arc.setFrame(robot.sensor0.getFrame());
        arc.setAngleStart(angleStart);
        arc.setAngleExtent(135);

        //Update the map for arc
        //Increment through the x axis
        for (int i = (((int) arc.getMinX() < 0) ? 0 : (int) arc.getMinX()); i <
                ((arc.getMaxX() >= params.environmentWidth) ?
                params.environmentWidth : arc.getMaxX()); i++)
        {
            //Increment through the y axis
            for (int j = (((int) arc.getMinY() < 0) ? 0 : (int) arc.getMinY());
                    j < ((arc.getMaxY() >= params.environmentHeight) ?
                    params.environmentHeight : arc.getMaxY()); j++)
            {
                //If a point is in the arc's range and it has not been
                //viewed previously, mark it as viewed
                if (arc.contains(i, j) && !viewed[i][j])
                {
                    viewed[i][j] = true;
                    viewedElements++;
                }
            }
        }
        //Reset the angle of the robot's sensors to the new angle
        robot.setSensors();

        //Count the number of elements in the environment that have been viewed
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

    /**
     * Creates the neighbor list that is associated with the A* pathfinding
     * algorithm
     *
     * @param parent The node that will act as the parent node for each node in
     *               the neighbor list
     * @param goal
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
            if ((viewed[parent.getX()][parent.getY() + 1] &&
                    !occupied[parent.getX()][parent.getY() + 1]) ||
                    (goal == matrix[parent.getX()][parent.getY() + 1]))
            {
                Node node = matrix[parent.getX()][parent.getY() + 1];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the Southeast
        if ((parent.getX() + 1 < params.environmentWidth) && (parent.getY() +
                1 < params.environmentHeight))
        {
            if ((viewed[parent.getX() + 1][parent.getY() + 1] &&
                    !occupied[parent.getX() + 1][parent.getY() + 1]) ||
                    (goal == matrix[parent.getX() + 1][parent.getY() + 1]))
            {
                Node node = matrix[parent.getX() + 1][parent.getY() + 1];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the East
        if (parent.getX() + 1 < params.environmentWidth)
        {
            if ((viewed[parent.getX() + 1][parent.getY()] &&
                    !occupied[parent.getX() + 1][parent.getY()]) ||
                    (goal == matrix[parent.getX() + 1][parent.getY()]))
            {
                Node node = matrix[parent.getX() + 1][parent.getY()];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the Northeast        
        if ((parent.getX() + 1 < params.environmentWidth) &&
                (parent.getY() - 1 >= 0))
        {
            if ((viewed[parent.getX() + 1][parent.getY() - 1] &&
                    !occupied[parent.getX() + 1][parent.getY() - 1]) ||
                    (goal == matrix[parent.getX() + 1][parent.getY() - 1]))
            {
                Node node = matrix[parent.getX() + 1][parent.getY() - 1];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the North
        if (parent.getY() - 1 >= 0)
        {
            if ((viewed[parent.getX()][parent.getY() - 1] &&
                    !occupied[parent.getX()][parent.getY() - 1]) ||
                    (goal == matrix[parent.getX()][parent.getY() - 1]))
            {
                Node node = matrix[parent.getX()][parent.getY() - 1];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the Northwest
        if ((parent.getX() - 1 >= 0) && (parent.getY() - 1 >= 0))
        {
            if ((viewed[parent.getX() - 1][parent.getY() - 1] &&
                    !occupied[parent.getX() - 1][parent.getY() - 1]) ||
                    (goal == matrix[parent.getX() - 1][parent.getY() - 1]))
            {
                Node node = matrix[parent.getX() - 1][parent.getY() - 1];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the West
        if (parent.getX() - 1 >= 0)
        {
            if ((viewed[parent.getX() - 1][parent.getY()] &&
                    !occupied[parent.getX() - 1][parent.getY()]) ||
                    (goal == matrix[parent.getX() - 1][parent.getY()]))
            {
                Node node = matrix[parent.getX() - 1][parent.getY()];
                node.calculateG(parent);
                list.add(node);
            }
        }
        //Neighbor to the Southwest
        if ((parent.getX() - 1 >= 0) &&
                (parent.getY() + 1 < params.environmentHeight))
        {
            if ((viewed[parent.getX() - 1][parent.getY() + 1] &&
                    !occupied[parent.getX() - 1][parent.getY() + 1]) ||
                    (goal == matrix[parent.getX() - 1][parent.getY() + 1]))
            {
                Node node = matrix[parent.getX() - 1][parent.getY() + 1];
                node.calculateG(parent);
                list.add(node);
            }
        }

        return list;
    }

    /**
     * After a path has been found, given the end point of the path creates a
     * list of points to move through. End point is pushed onto a stack. The
     * parent of that point is pushed onto the stack. The parent of the parent
     * is pushed onto the stack. This process is repeated until the starting
     * point is left.
     *
     * @param node A Node object representing the end point of the path
     *
     * @return A Stack of Point objects representing the points on the map the
     *         robot must navigate through to reach the goal from its current point
     */
    private Stack<Point> createPath(Node node)
    {
        //Create stack
        Stack<Point> path = new Stack();

        //Iterate through the nodes and add their points to the stack
        while ((node.getParent()) != null)
        {
            path.push(new Point(node.getX(), node.getY()));
            node = node.getParent();
        }
        return path;
    }

    /**
     * Given a robot and its current position the robot will be assigned a goal
     * if a valid goal exists. A boolean value will be returned representing
     * whether or not a valid goal was found.
     *
     * @param currentPoint A Point object representing a robot's current
     *                     position on the map
     * @param r            A Robot object representing the robot that needs a
     *                     goal
     *
     * @return Returns a boolean value representing whether or not a valid goal
     *         has been found
     */
    @Override
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
                if (!(x < 0 || y < 0 ||
                        x >= viewed.length || y >= viewed[x].length))
                {
                    //If viewed at current coordinates has not been viewed, 
                    //return those coordinates as Point.
                    if (!viewed[x][y])
                    {
                        {
                            r.goal = new Point(x, y);
                            boolean valid = aStar(currentPoint, r.goal, r);
                            if (valid)
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

    /**
     * Implementation of the A* pathfinding algorithm. Given a robot, a starting
     * point, and an end point a boolean value is returned representing whether
     * or not a path exists.
     *
     * @param start  A Point object representing the point on the map where the
     *               path will start
     * @param finish A Point object representing the point on the map where the
     *               path will end
     * @param robot  A Robot object representing the robot that needs a path
     *
     * @return A boolean value representing whether or not a path exists
     */
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

    /**
     * Given two nodes returns the distance between them using the Pythagoras
     * theorem
     *
     * @param a A Node object representing the first point in the Pythagoras
     *          theorem
     *
     * @param b A Node object representing the second point in the Pythagoras
     *          theorem
     *
     * @return Returns an int value with is the truncated c value in the
     *         Pythagoras theorem
     */
    public int nodeDistance(Node a, Node b)
    {
        //Pythagoras theorem
        return (int) Math.sqrt((Math.abs(a.getX() - b.getX())) +
                (Math.abs(a.getY() - b.getY())));
    }

    /**
     * Give a robot, the robot's bearing is adjusted to match its next movement
     *
     * @param r A Robot object representing the robot who's bearing is to be
     *          adjusted
     */
    public void adjustBearing(Robot r)
    {
        //Get the point the robot needs to face
        Point next = ((!r.path.isEmpty() || r.path.size() > 0) ?
                r.path.peek() : r.goal);
        //The robot's current point
        Point curr = r.current;

        //Increment bearing
        switch (r.bearing)
        {
            //Robot facing 0 degrees
            case 0:
                //Next point at bearing 0
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 0;
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                //Next point at bearing 1
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                //Next point at bearing 1
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                //Next point at bearing 1
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                //Next point at bearing 1
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                //Next point at bearing 7
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, -90);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                //Next point at bearing 7
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, -90);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                //Next point at bearing 7
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, -90);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                break;
            //Robot facing 45 degrees
            case 1:
                //Next point at bearing 0
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                //Next point at bearing 1
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 1;
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                //Next point at bearing 2
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                //Next point at bearing 2
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                //Next point at bearing 2
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                //Next point at bearing 2
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                //Next point at bearing 0
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                //Next point at bearing 0
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, -45);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                break;
            //Robot facing 90 degrees
            case 2:
                //Next point at bearing 1
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                //Next point at bearing 1
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                //Next point at bearing 2
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 2;
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                //Next point at bearing 3
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                //Next point at bearing 3
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                //Next point at bearing 3
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                //Next point at bearing 3
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                //Next point at bearing 1
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 1;
                    updateMapRotate(r, 0);
                    r.sensor0Angle = 90;
                    r.sensor1Angle = 45;
                    r.sensor2Angle = 0;
                }
                break;
            //Robot facing 135 degrees
            case 3:
                //Next point at bearing 2
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                //Next point at bearing 2
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }
                //Next point at bearing 2
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 2;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 135;
                    r.sensor1Angle = 90;
                    r.sensor2Angle = 45;
                }//Next point at bearing 3
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 3;
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                //Next point at bearing 4
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                //Next point at bearing 4
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                //Next point at bearing 4
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                //Next point at bearing 4
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                break;
            //Robot facing 180 degrees
            case 4:
                //Next point at bearing 5
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 135);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                //Next point at bearing 3
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                //Next point at bearing 3
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                //Next point at bearing 3
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 3;
                    updateMapRotate(r, 45);
                    r.sensor0Angle = 180;
                    r.sensor1Angle = 135;
                    r.sensor2Angle = 90;
                }
                //Next point at bearing 4
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 4;
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                //Next point at bearing 5
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 135);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                //Next point at bearing 5
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 135);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                //Next point at bearing 5
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 135);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                break;
            //Robot facing 225 degrees
            case 5:
                //Next point at bearing 6
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                //Next point at bearing 6
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                //Next point at bearing 4
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                //Next point at bearing 4
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                //Next point at bearing 4
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 4;
                    updateMapRotate(r, 90);
                    r.sensor0Angle = 225;
                    r.sensor1Angle = 180;
                    r.sensor2Angle = 135;
                }
                //Next point at bearing 5
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 5;
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                //Next point at bearing 6
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                //Next point at bearing 6
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                break;
            //Robot facing 270 degrees
            case 6:
                //Next point at bearing 7
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, 225);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                //Next point at bearing 7
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, 225);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                //Next point at bearing 7
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, 225);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                //Next point at bearing 5
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                //Next point at bearing 5
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                //Next point at bearing 5
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 5;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 270;
                    r.sensor1Angle = 225;
                    r.sensor2Angle = 180;
                }
                //Next point at bearing 6
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 6;
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                //Next point at bearing 7
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 7;
                    updateMapRotate(r, 180);
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                break;
            //Robot facing 315 degrees
            case 7:
                //Next point at bearing 0
                if (next.x > curr.x && next.y == curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, 270);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                //Next point at bearing 0
                else if (next.x > curr.x && next.y < curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, 270);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                //Next point at bearing 0
                else if (next.x == curr.x && next.y < curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, 270);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                //Next point at bearing 0
                else if (next.x < curr.x && next.y < curr.y)
                {
                    r.bearing = 0;
                    updateMapRotate(r, 270);
                    r.sensor0Angle = 45;
                    r.sensor1Angle = 0;
                    r.sensor2Angle = 315;
                }
                //Next point at bearing 6
                else if (next.x < curr.x && next.y == curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 225);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                //Next point at bearing 6
                else if (next.x < curr.x && next.y > curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 225);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                //Next point at bearing 6
                else if (next.x == curr.x && next.y > curr.y)
                {
                    r.bearing = 6;
                    updateMapRotate(r, 225);
                    r.sensor0Angle = 315;
                    r.sensor1Angle = 270;
                    r.sensor2Angle = 225;
                }
                //Next point at bearing 7
                else if (next.x > curr.x && next.y > curr.y)
                {
                    r.bearing = 7;
                    r.sensor0Angle = 0;
                    r.sensor1Angle = 315;
                    r.sensor2Angle = 270;
                }
                break;
        }
        //Reset the robot's sensors to batch the current bearing
        r.setSensors();
    }

    /**
     * Given a robot's current location and the location of the next point
     * returns an int value representing the bearing necessary for the robot to
     * move to the next point
     *
     * @param current A Point object representing the current point of a robot
     * @param goal    A Point object representing the intended next movement of
     *                a robot
     *
     * @return Returns an int value representing the bearing a robot needs to
     *         face in order to move to an intended point
     */
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

    /**
     * Implemented from parent class. Whenever the timer event fires this method
     * will be called.
     * Contains the logic for the algorithm
     *
     * @param e An ActionEvent representing a tick of the timer.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //For each robot ensure that its current coordinates and everything 
        //within its sensors have been viewed
        for (Robot r : robots)
        {
            viewed[r.current.x][r.current.y] = true;
            updateMapForward(r);
        }

        //Perform only if the time limit has not been reached and there are 
        //still elements that need to be viewed
        if ((params.timeLimit == 0 || movements < params.timeLimit) &&
                viewedElements < totalElements)
        {
            //Perform operations for every robot
            for (Robot r : robots)
            {
                //Exclude robots who do not have a goal or who have completed 
                //operations
                if (!r.finished && r.hasGoal)
                {
                    //Set temp variable equal to robot's bearing and adjust the 
                    //robot's bearing for its goal.
                    int tempBearing = r.bearing;
                    adjustBearing(r);
                    //If the robot's bearing is equal to the temp bearing the
                    //robot can proceed forward. Otherwise its direction will 
                    //need to be incremented further.
                    if (tempBearing == r.bearing)
                    {
                        //If the path is not empty increment the robot's 
                        //position
                        if (!r.path.isEmpty() && r.path.size() > 0)
                        {
                            r.current = r.path.pop();

                        }
                        //Update the viewed elements and reset the robot's 
                        //sensors. Then determine the next bearing required
                        updateMapForward(r);
                        r.setSensors();
                        int bearingNeeded = bearingNeeded(r.current, r.goal);

                        //Determine if the robot has a goal
                        if (((r.path.isEmpty() || r.path.size() == 0) &&
                                r.bearing == bearingNeeded) ||
                                (r.goal.x == r.current.x &&
                                r.goal.y == r.current.y))
                        {
                            r.hasGoal = false;
                        }
                    }
                }
                //Determine if the robot has finished its operations
                else if (!r.finished)
                {
                    boolean needGoal = true;
                    while (needGoal)
                    {
                        needGoal = !acquireGoal(r.current, r);
                    }
                }
            }
            //Increment the count of movements
            movements++;
            
            //repaint the map including robot positions and sensors
            repaint();
        }
    }

}
