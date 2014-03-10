
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
    JPanel southPanel;
    private JPanel xyPanel;
    JButton zoomIn;
    JButton zoomOut;
    JLabel x;
    JLabel y;
    
    public MapView(String name, JPanel mapPanel) {
        super(name);
        
        //Initializing components
        this.mapPanel = mapPanel;
        southPanel = new JPanel();
        xyPanel = new JPanel();
        
        x = new JLabel("0");
        y = new JLabel("0");
        
        //Setting layouts
        setLayout(new BorderLayout());
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.LINE_AXIS));
        southPanel.setBorder(BorderFactory.createEtchedBorder(Color.PINK, Color.MAGENTA));
        
        //Spacing
        southPanel.add(Box.createHorizontalGlue());
        
        //Initializing components
        zoomIn = new JButton("Zoom In (+)");
        zoomOut = new JButton("Zoom Out (-)");
        
        //Adding components to panels
        southPanel.add(zoomIn);
        southPanel.add(zoomOut);
        xyPanel.add(new JLabel("x: "));
        xyPanel.add(x);
        xyPanel.add(new JLabel(" y: "));
        xyPanel.add(y);
        
        
        //Spacing, uncomment this if removing xyPanel from southPanel
        //southPanel.add(Box.createHorizontalGlue());
        
        //Adding components to panels
        southPanel.add(xyPanel);
        
        //Adding final touches to content pane.
        getContentPane().add(mapPanel, BorderLayout.CENTER);
        getContentPane().add(southPanel, BorderLayout.NORTH);
        
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
