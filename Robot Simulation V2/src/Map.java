
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Map extends JPanel
{

    boolean[][] map;

    public Map(Parameters params, boolean[][] occupied)
    {
        setSize(params.environmentWidth, params.environmentHeight);
        map = occupied;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[i].length; j++)
            {
                if (map[i][j])
                {
                    g.setColor(Color.BLACK);
                    g.drawLine(i, j, i, j);
                }
                else
                {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawLine(i, j, i, j);
                }
            }
        }
    }
}
