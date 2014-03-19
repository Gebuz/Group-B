
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.*;

/**
 *
 * @author flemmingxu
 */

public class MapView extends JFrame {
    private final JPanel mapPanel;
    JPanel northPanel, eastPanel, arrowPanel, upPanel, downPanel;
    private JPanel xyPanel;
    JButton zoomIn, zoomOut, showFull, up, down, left, right;
    JLabel x, y, road; 
    
    public MapView(String name, final MapPanel mapPanel) {
        super(name);
        
        //Initializing components
        this.mapPanel = mapPanel;
        
        northPanel = new JPanel();
        xyPanel = new JPanel();
        
        eastPanel = new JPanel();
        arrowPanel = new JPanel();
        upPanel = new JPanel();
        downPanel = new JPanel();
        
        x = new JLabel("0");
        y = new JLabel("0");
        road = new JLabel("");
       
        
        showFull = new JButton ("▣");
        zoomIn = new JButton("Zoom In (+)");
        zoomOut = new JButton("Zoom Out (-)");
        
        up = new JButton("↑");
        down = new JButton("↓");
        left = new JButton("←");
        right = new JButton("→");
        
        //Mapping key inputs to button
        KeyStroke keyUp = KeyStroke.getKeyStroke("UP"); 
        KeyStroke keyDown = KeyStroke.getKeyStroke("DOWN");
        KeyStroke keyLeft = KeyStroke.getKeyStroke("LEFT");
        KeyStroke keyRight = KeyStroke.getKeyStroke("RIGHT");
        
        up.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW).put(keyUp, "up");
        up.getActionMap().put("up", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.4) 
                    mapPanel.changeY(20);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.4 
                        && mapPanel.getZoom() >= 0.15) 
                    mapPanel.changeY(10);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.15
                        && mapPanel.getZoom() >= 0.09) 
                    mapPanel.changeY(5);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.09
                        && mapPanel.getZoom() >= 0.03) 
                    mapPanel.changeY(1);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.03)
                    mapPanel.changeY(0.2);
            }
        });
        down.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW).put(keyDown, "down");
        down.getActionMap().put("down", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.4) 
                    mapPanel.changeY(-20);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.4 
                        && mapPanel.getZoom() >= 0.15) 
                    mapPanel.changeY(-10);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.15
                        && mapPanel.getZoom() >= 0.09) 
                    mapPanel.changeY(-5);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.09
                        && mapPanel.getZoom() >= 0.03) 
                    mapPanel.changeY(-1);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.03)
                    mapPanel.changeY(-0.2);
            }
        });
        left.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW).put(keyLeft, "left");
        left.getActionMap().put("left", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.4) 
                    mapPanel.changeX(20);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.4 
                        && mapPanel.getZoom() >= 0.15) 
                    mapPanel.changeX(10);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.15
                        && mapPanel.getZoom() >= 0.09) 
                    mapPanel.changeX(5);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.09
                        && mapPanel.getZoom() >= 0.03) 
                    mapPanel.changeX(1);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.03)
                    mapPanel.changeX(0.2);
            }
        });
        right.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW).put(keyRight, "right");
        right.getActionMap().put("right", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.4) 
                    mapPanel.changeX(-20);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.4 
                        && mapPanel.getZoom() >= 0.15) 
                    mapPanel.changeX(-10);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.15
                        && mapPanel.getZoom() >= 0.09) 
                    mapPanel.changeX(-5);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.09
                        && mapPanel.getZoom() >= 0.03) 
                    mapPanel.changeX(-1);
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.03)
                    mapPanel.changeX(-0.2);
            }
        });
        
        //Setting layouts
        setLayout(new BorderLayout());
        
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.LINE_AXIS));
        northPanel.setBorder(BorderFactory.createEtchedBorder(Color.PINK, Color.MAGENTA));
        
        eastPanel.setLayout(new FlowLayout());
        eastPanel.setBorder(BorderFactory.createEtchedBorder(Color.PINK, Color.MAGENTA));
        
        arrowPanel.setLayout(new BorderLayout());
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.LINE_AXIS));
        downPanel.setLayout(new BoxLayout(downPanel, BoxLayout.LINE_AXIS));

        
        //Adding components to panels
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
        
        eastPanel.add(arrowPanel);
        
        //Spacing
        northPanel.add(Box.createHorizontalGlue());
        
        //Adding components to panels
        northPanel.add(zoomIn);
        northPanel.add(zoomOut);
        
        xyPanel.add(new JLabel("x: "));
        xyPanel.add(x);
        xyPanel.add(new JLabel(" y: "));
        xyPanel.add(y);
        
        northPanel.add(Box.createHorizontalGlue());
        
        northPanel.add(road);
        
        
        
        //Spacing, uncomment this if removing xyPanel from northPanel
        //northPanel.add(Box.createHorizontalGlue());
        
        //Adding components to panels
        northPanel.add(xyPanel);
        
        //Adding final touches to content pane.
        getContentPane().add(mapPanel, BorderLayout.CENTER);
        getContentPane().add(northPanel, BorderLayout.NORTH);
        getContentPane().add(eastPanel, BorderLayout.EAST);
        
        //Show window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
    public JPanel getMap() {
        return mapPanel;
    }
}
