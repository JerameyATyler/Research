
package Engine;


import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Creates the environment map that will be used for the experiment
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public class Map extends JPanel
{

    //boolean matrix representing the environment and its obstacle placement

    boolean[][] map;

    /**
     * Constructor for Map object. Accepts a list of parameters and the
     * environment map as input.
     *
     * @param params   A Parameters object that will contain the experiment
     *                 parameters.
     * @param occupied A boolean matrix representing the map of the environment
     */
    public Map(Parameters params, boolean[][] occupied)
    {
        //Sets the size of the JPanel to the width and height chosen by the 
        //user
        setSize(params.environmentWidth, params.environmentHeight);

        map = occupied;
        repaint();
    }
    /**
     * Implemented from the parent class. Draws the map on the screen. Black
     * pixels represent obstacles in the environment. Light gray pixels
     * represent free space in the environment.
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //Iterate through every x element
        for (int i = 0; i < map.length; i++)
        {
            //Iterate through every y element
            for (int j = 0; j < map[i].length; j++)
            {
                //If the element at (i,j) is true then that element is occupied
                //and should be painted black
                if (map[i][j])
                {
                    g.setColor(Color.BLACK);
                    g.drawLine(i, j, i, j);
                }
                //If the element at (i,j) is not true then it is free space and
                //should be painted light gray
                else 
                {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawLine(i, j, i, j);
                }
            }
        }
    }
}
