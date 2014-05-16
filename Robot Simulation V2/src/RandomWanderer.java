
import java.awt.event.ActionEvent;
import java.util.Random;

/**
 * Implementation of a random wanderer algorithm. At any given time a robot can
 * perform one of three movements. It may move on unit forward. It may rotate
 * 45 degrees counter clockwise. Or it may rotate 45 degrees counterclockwise.
 * Used as a baseline to compare other navigation algorithms against.
 *
 * @author Jeramey Tyler jeatyler@ius.edu
 */
public class RandomWanderer extends NavigationLibrary
{
    //Seed for random number generation
    Random random = new Random(179424691);

    /**
     *Constructor for RandomWanderer algorithm. Accepts a list of parameters and the environment map as input.
     * @param params    A parameter object that will contain th experiment parameters
     * @param occupied  A boolean matrix representing the map of the environment
     */
    public RandomWanderer(Parameters params, boolean[][] occupied)
    {
        //Parameters are passed to the constructor of the super class
        super(params, occupied);
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
        //Perform only if the time limit has not been reached and there are 
        //still elements that need to be viewed
        if ((params.timeLimit == 0 || movements < params.timeLimit) &&
                viewedElements < totalElements)
        {
            //Perform operations for every robot
            for (Robot r : robots)
            {
                //Determine what the next move for the robot will be
                int move = (random.nextInt(3));
                //If move is equal to 0 move forward if possible
                if ((move == 0 && canIncrementDirection(r)) && !moveConflict(r))
                {
                    incrementDirection(r);
                    updateMapForward();
                }
                //if move is equal to 1 rotate 45 degrees counterclockwise
                else if (move == 1)
                {
                    updateMapRotateCounterClockWise(r);
                    incrementBearing(r);
                }
                //If move is equal to 2 rotate 45 degrees clockwise
                else if (move == 2)
                {
                    updateMapRotateClockWise(r);
                    decrementBearing(r);
                }
            }
            //Increment the count of movements
            movements++;
        }

        //repaint the map including robot positions and sensors
        repaint();
    }

}
