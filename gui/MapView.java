
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 *
 * @author flemmingxu
 */

public class MapView extends JFrame {
    public final JPanel mapPanel;
    public final JPanel northPanel, eastPanel, arrowPanel, upPanel, downPanel, roadPanel;
    public final JPanel xyPanel;
    public final JButton zoomIn, zoomOut, showFull, up, down, left, right, roadOn, roadOff;
    public final JLabel x, y, road, roadOnOff; 
    public final JCheckBox relativeZoomCheckBox;
    
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
        roadPanel = new JPanel();
        
        x = new JLabel("0");
        y = new JLabel("0");
        road = new JLabel("");
        roadOnOff = new JLabel("Show road names:");
       
        
        showFull = new JButton ("▣");
        zoomIn = new JButton("Zoom In (+)");
        zoomOut = new JButton("Zoom Out (-)");
        
        up = new JButton("↑");
        down = new JButton("↓");
        left = new JButton("←");
        right = new JButton("→");
        
        roadOn = new JButton("On");
        roadOff = new JButton("Off");
        roadOn.setEnabled(false);
        
        relativeZoomCheckBox = new JCheckBox("Relative Mouse Zoom");
        relativeZoomCheckBox.setMnemonic(KeyEvent.VK_R);
        relativeZoomCheckBox.setEnabled(true);
        relativeZoomCheckBox.setSelected(true);
        
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
                if (mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.4)
                    mapPanel.changeY(20);
                else if (mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.15) 
                    mapPanel.changeY(10);
                else if (mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.09) 
                    mapPanel.changeY(5);
                else if (mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.03) 
                    mapPanel.changeY(1);
                else if (mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.03) 
                    mapPanel.changeY(0.08);
            }
        });
        down.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW).put(keyDown, "down");
        down.getActionMap().put("down", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.4) 
                    mapPanel.changeY(-20);
                else if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.15) 
                    mapPanel.changeY(-10);
                else if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.09) 
                    mapPanel.changeY(-5);
                else if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.03) 
                    mapPanel.changeY(-1);
                else if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.03)
                    mapPanel.changeY(-0.08);
            }
        });
        left.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW).put(keyLeft, "left");
        left.getActionMap().put("left", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.4) 
                    mapPanel.changeX(20);
                else if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.15) 
                    mapPanel.changeX(10);
                else if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.09) 
                    mapPanel.changeX(5);
                else if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.03) 
                    mapPanel.changeX(1);
                else if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.03)
                    mapPanel.changeX(0.08);
            }
        });
        right.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW).put(keyRight, "right");
        right.getActionMap().put("right", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.4) 
                    mapPanel.changeX(-20);
                else if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.15) 
                    mapPanel.changeX(-10);
                else if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.09) 
                    mapPanel.changeX(-5);
                else if(mapPanel.getZoom() != 1 && mapPanel.getZoom() >= 0.03) 
                    mapPanel.changeX(-1);
                else if(mapPanel.getZoom() != 1 && mapPanel.getZoom() < 0.03)
                    mapPanel.changeX(-0.08);
            }
        });
        
        //Setting layouts
        setLayout(new BorderLayout());
        
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.LINE_AXIS));
        northPanel.setBorder(BorderFactory.createEtchedBorder(Color.PINK, Color.MAGENTA));
        
        eastPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        eastPanel.setBorder(BorderFactory.createEtchedBorder(Color.PINK, Color.MAGENTA));
        
        arrowPanel.setLayout(new BorderLayout());
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.LINE_AXIS));
        downPanel.setLayout(new BoxLayout(downPanel, BoxLayout.LINE_AXIS));
        
        roadPanel.setLayout(new FlowLayout());

        
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
        
        roadPanel.add(roadOn);
        roadPanel.add(roadOff);
        
        
        eastPanel.add(arrowPanel, gbc);
        eastPanel.add(roadOnOff, gbc);
        eastPanel.add(roadPanel, gbc);
        eastPanel.add(relativeZoomCheckBox, gbc);
        
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
}
