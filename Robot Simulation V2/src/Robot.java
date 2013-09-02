
import java.awt.Point;
import java.awt.geom.Arc2D;
import java.util.Stack;

public final class Robot
{

    public Point current;
    public Point goal;
    public short bearing;
    public short rotations;
    public boolean hasGoal;
    public boolean finished = false;
    public Stack<Point> path;
    public int sensorDistance;
    public Arc2D.Double sensor0 = new Arc2D.Double(Arc2D.PIE);
    int sensor0Angle;
    public Arc2D.Double sensor1 = new Arc2D.Double(Arc2D.PIE);
    int sensor1Angle;
    public Arc2D.Double sensor2 = new Arc2D.Double(Arc2D.PIE);
    int sensor2Angle;

    public Robot(Point start, int sensor0Angle,
            int sensor1Angle, int sensor2Angle, int sensorDistance)
    {
        current = start;

        this.sensor0Angle = sensor0Angle;
        this.sensor1Angle = sensor1Angle;
        this.sensor2Angle = sensor2Angle;

        this.sensorDistance = sensorDistance;

        setSensors();

        sensor0.setAngleExtent(10);
        sensor1.setAngleExtent(10);
        sensor2.setAngleExtent(10);
    }

    public void setSensors()
    {
        sensor0.setFrame(current.x - sensorDistance, current.y - sensorDistance,
                (sensorDistance * 2), (sensorDistance * 2));
        sensor1.setFrame(current.x - sensorDistance, current.y - sensorDistance,
                (sensorDistance * 2), (sensorDistance * 2));
        sensor2.setFrame(current.x - sensorDistance, current.y - sensorDistance,
                (sensorDistance * 2), (sensorDistance * 2));

        sensor0.setAngleStart(this.sensor0Angle);
        sensor1.setAngleStart(this.sensor1Angle);
        sensor2.setAngleStart(this.sensor2Angle);
    }

    @Override
    public Robot clone()
    {
        return new Robot(new Point(current.x, current.y), sensor0Angle,
                sensor1Angle, sensor2Angle, sensorDistance);
    }
}
