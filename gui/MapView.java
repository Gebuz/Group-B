
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 *
 * @author flemmingxu
 */

public class MapView extends JFrame {
    private JPanel mapPanel;
    JPanel northPanel;
    private JPanel xyPanel;
    JButton zoomIn, zoomOut, showFull, up, down, left, right;
    JLabel x;
    JLabel y;
    
    public MapView(String name, JPanel mapPanel) {
        super(name);
        
        //Initializing components
        this.mapPanel = mapPanel;
        northPanel = new JPanel();
        xyPanel = new JPanel();
        
        x = new JLabel("0");
        y = new JLabel("0");
        
        showFull = new JButton ("Show default view :^)");
        zoomIn = new JButton("Zoom In (+)");
        zoomOut = new JButton("Zoom Out (-)");
        
        up = new JButton();
        down = new JButton();
        left = new JButton();
        right = new JButton();
        
        up.setVisible(false);
        down.setVisible(false);
        left.setVisible(false);
        right.setVisible(false);
        
        //Setting layouts
        setLayout(new BorderLayout());
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.LINE_AXIS));
        northPanel.setBorder(BorderFactory.createEtchedBorder(Color.PINK, Color.MAGENTA));
        
        //Adding components to panels
        northPanel.add(up);
        northPanel.add(down);
        northPanel.add(showFull);
        northPanel.add(left);
        northPanel.add(right);
        
        //Spacing
        northPanel.add(Box.createHorizontalGlue());
        
        //Adding components to panels
        northPanel.add(zoomIn);
        northPanel.add(zoomOut);
        xyPanel.add(new JLabel("x: "));
        xyPanel.add(x);
        xyPanel.add(new JLabel(" y: "));
        xyPanel.add(y);
        
        
        //Spacing, uncomment this if removing xyPanel from northPanel
        //northPanel.add(Box.createHorizontalGlue());
        
        //Adding components to panels
        northPanel.add(xyPanel);
        
        //Adding final touches to content pane.
        getContentPane().add(mapPanel, BorderLayout.CENTER);
        getContentPane().add(northPanel, BorderLayout.NORTH);
        
        //Show window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        //setSize(new Dimension(850, 670));
        setVisible(true);
    }
    
    public JPanel getMap() {
        return mapPanel;
    }
}
