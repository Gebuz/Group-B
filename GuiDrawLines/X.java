
package mapofdenmark.GuiDrawLines;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class X
{

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.add(new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                Line2D line = new Line2D.Double(10, 10, 40, 40);
                g2.setColor(Color.blue);
                g2.setStroke(new BasicStroke(10));
                g2.draw(line);
            }
        });
        frame.setSize(new Dimension(200,200));
        frame.setVisible(true);
    }
}