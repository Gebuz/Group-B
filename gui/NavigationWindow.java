package gui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author flemmingxu
 */
public class NavigationWindow extends JFrame {
    public final JPanel navigationPanel, arrowPanel, upPanel, downPanel, zoomPanel;
    public final JButton up, down, left, right, showFull, zoomIn, zoomOut;
    
    public NavigationWindow() {        
        super("Navigation window");
                
        navigationPanel = new JPanel();
        navigationPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        showFull = new JButton ("▣");
        zoomIn = new JButton("Zoom In (+)");
        zoomOut = new JButton("Zoom Out (-)");
        
        up = new JButton("↑");
        down = new JButton("↓");
        left = new JButton("←");
        right = new JButton("→");
        
        arrowPanel = new JPanel();
        upPanel = new JPanel();
        downPanel = new JPanel();
        zoomPanel = new JPanel();
        
        
        arrowPanel.setLayout(new BorderLayout());
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.LINE_AXIS));
        downPanel.setLayout(new BoxLayout(downPanel, BoxLayout.LINE_AXIS));
        
        upPanel.add(Box.createHorizontalGlue());
        upPanel.add(up);
        upPanel.add(Box.createHorizontalGlue());
        
        downPanel.add(Box.createHorizontalGlue());
        downPanel.add(down);
        downPanel.add(Box.createHorizontalGlue());
        
        arrowPanel.add(upPanel, BorderLayout.NORTH);
        arrowPanel.add(downPanel, BorderLayout.SOUTH);
        arrowPanel.add(left, BorderLayout.WEST);
        arrowPanel.add(right, BorderLayout.EAST);
        arrowPanel.add(showFull, BorderLayout.CENTER);
        
        zoomPanel.add(zoomIn);
        zoomPanel.add(zoomOut);
        
        navigationPanel.add(arrowPanel);
        navigationPanel.add(zoomPanel);
        
        getContentPane().add(navigationPanel, BorderLayout.CENTER);
        //setLocation(, mapView.getLocationOnScreen().y);
        pack();
        setVisible(true);
    }
    
    
}
