
package Engine;


import java.awt.Point;

/**
 * Node class for implementation of A* search algorithm
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public class Node implements Comparable
{

    //Position coordinate
    private int x;
    private int y;

    //Values required for A* algorithm
    private int f;
    private int g;
    private int h;

    //Parent Node of the current Node 
    private Node parent;

    /**
     * No parameter constructor for Node object. Initializes x = y = 0. All
     * other parameters are created with default parameters.
     */
    public Node()
    {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Parameter constructor for Node object. Accepts a Point object
     * representing the Position coordinates of the node
     *
     * @param point A Point object representing the position coordinates of the
     *              Node
     */
    public Node(Point point)
    {
        this.x = (int) point.getX();
        this.y = (int) point.getY();
    }

    /**
     * Parameterized constructor for Node object. Creates a Node objects with x
     * and y values initialized to the parameters that are provided.
     *
     * @param x Corresponds to the x coordinate of the Node object.
     * @param y Corresponds to the y coordinate of the Node object.
     */
    public Node(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x value of the Node object.
     *
     * @return Returns the integer value stored in the x property.
     */
    public int getX()
    {
        return x;
    }

    /**
     * Returns the y value of the Node object.
     *
     * @return Returns the integer value stored in the y property.
     */
    public int getY()
    {
        return y;
    }

    /**
     * Returns the f value of the Node object.
     *
     * @return Returns the integer value stored in the f property.
     */
    public int getF()
    {
        return f;
    }

    /**
     * Returns the g value of the Node object.
     *
     * @return Returns the integer value stored in the g property.
     */
    public int getG()
    {
        return g;
    }

    /**
     * Returns the h value of the Node object.
     *
     * @return Returns the integer value stored in the h property.
     */
    public int getH()
    {
        return h;
    }

    /**
     * Returns the Parent node of the Node object.
     *
     * @return Returns the Parent Node of the Node object
     */
    public Node getParent()
    {
        return parent;
    }

    /**
     * Sets the x value to the parameter specified.
     *
     * @param x Corresponds to the value to be stored in x.
     */
    public void setX(int x)
    {
        this.x = x;
    }

    /**
     * Sets the y value to the parameter specified.
     *
     * @param y Corresponds to the value to be stored in y.
     */
    public void setY(int y)
    {
        this.y = y;
    }

    /**
     * Sets the g value to the parameter specified.
     *
     * @param g Corresponds to the value to be stored in g.
     */
    public void setG(int g)
    {

        this.g = g + ((parent == null) ? 0 : parent.g);
    }

    /**
     * Set the parent of the Node
     *
     * @param parent A Node object representing the parent of the current Node
     */
    public void setParent(Node parent)
    {
        this.parent = parent;
    }

    /**
     * Calculates the f value of the Node by adding the g and h values.
     */
    public void calculateF()
    {
        f = g + h;
    }

    /**
     * Calculates the g value of the Node. Vertical and horizontal movements are
     * given a lower value than diagonal movements.
     *
     * @param goal Node object that corresponds to the goal node.
     */
    public void calculateG(Node goal)
    {
        if (goal.x == this.x || goal.y == this.y)
        {
            this.g = 10 + ((parent == null) ? 0 : parent.g);
        }
        else
        {
            this.g = 10 + ((parent == null) ? 0 : parent.g);
        }
    }

    /**
     * Calculates the h or heuristic value of the Node. Attempts to even out
     * movement along axes.
     *
     * @param goal Node object that corresponds to the goal node.
     */
    public void calculateH(Node goal)
    {
        int xDistance = Math.abs(x - goal.x);
        int yDistance = Math.abs(y - goal.y);

        if (xDistance > yDistance)
        {
            h = (14 * yDistance) + (10 * (xDistance - yDistance));
        }
        else
        {
            h = (14 * xDistance) + (10 * (yDistance - xDistance));
        }
    }

    /**
     * Implementation of Comparable interface. Allows for comparison of two Node
     * objects.
     *
     * @param o Corresponds to the Node object to be compared against.
     *
     * @return Returns 1 if the current Node is greater than the compared Node,
     *         0 if the current Node is equal to the compared Node, and -1 if
     *         the current Node is less than the compared Node.
     */
    @Override
    public int compareTo(Object o)
    {
        if (this.f > ((Node) o).getF())
        {
            return 1;
        }
        else
        {
            if (this.f == ((Node) o).getF())
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
    }
}
