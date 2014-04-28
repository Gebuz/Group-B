
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author flemmingxu
 */

public class MapView extends JFrame {
    public final JPanel mapPanel;
    public final JPanel northPanel, eastPanel, upPanel, downPanel, roadPanel, xyPanel, arrowPanel, zoomPanel;
    public final JLabel x, y, road;
    public final JButton up, down, left, right, showFull, zoomIn, zoomOut;
    public final JMenuBar menuBar;
    public final JMenu mapOptionsMenu, helpMenu;
    public final JCheckBoxMenuItem enableRoad, enableRelative, enableNavigation;
    private int condition;
    
    public MapView(String name, final MapPanel mapPanel) {
        super(name);
        
        //Setting UIManager settings
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MapView.class.getName()).log(Level.SEVERE, null, ex);
        }
        UIManager.put("MenuBar.background", Color.LIGHT_GRAY);
        UIManager.put("Menu.background", Color.LIGHT_GRAY);
        
        //Making menu
        menuBar = new JMenuBar();
        
        mapOptionsMenu = new JMenu("Map options");
        menuBar.add(mapOptionsMenu);
        
        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        
        enableRoad = new JCheckBoxMenuItem("Enable road names");
        enableRoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        enableRoad.setEnabled(true);
        enableRoad.setSelected(true);
        mapOptionsMenu.add(enableRoad);
        
        enableRelative = new JCheckBoxMenuItem("Enable relative mouse zoom");
        enableRelative.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK));
        enableRelative.setEnabled(true);
        enableRelative.setSelected(true);
        mapOptionsMenu.add(enableRelative);
        
        mapOptionsMenu.addSeparator();
        
        enableNavigation = new JCheckBoxMenuItem("Show navigation panel");
        enableNavigation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        enableNavigation.setEnabled(true);
        enableNavigation.setSelected(true);
        mapOptionsMenu.add(enableNavigation);
        
        //Initializing components  
        this.mapPanel = mapPanel;
        
        northPanel = new JPanel();
        xyPanel = new JPanel();
        
        eastPanel = new JPanel();
        upPanel = new JPanel();
        downPanel = new JPanel();
        arrowPanel = new JPanel();
        zoomPanel = new JPanel();
        roadPanel = new JPanel();
        
        x = new JLabel("0");
        y = new JLabel("0");
        road = new JLabel("");
        
        showFull = new JButton ("▣");
        zoomIn = new JButton("+");
        zoomOut = new JButton("-");
        
        up = new JButton("↑");
        down = new JButton("↓");
        left = new JButton("←");
        right = new JButton("→");
        
//        relativeZoomCheckBox = new JCheckBox("Enable Relative Mouse Zoom");
//        relativeZoomCheckBox.setEnabled(true);
//        relativeZoomCheckBox.setMnemonic(KeyEvent.VK_R);
//        relativeZoomCheckBox.setSelected(true);
        
        //Mapping key inputs to button
        KeyStroke keyUp = KeyStroke.getKeyStroke("UP"); 
        KeyStroke keyDown = KeyStroke.getKeyStroke("DOWN");
        KeyStroke keyLeft = KeyStroke.getKeyStroke("LEFT");
        KeyStroke keyRight = KeyStroke.getKeyStroke("RIGHT");
        condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        
        mapPanel.getInputMap(condition).put(keyUp, "up");
        mapPanel.getActionMap().put("up", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mapPanel.getZoom() >= 0.4)
                    mapPanel.changeY(20);
                else if (mapPanel.getZoom() >= 0.15) 
                    mapPanel.changeY(10);
                else if (mapPanel.getZoom() >= 0.09) 
                    mapPanel.changeY(5);
                else if (mapPanel.getZoom() >= 0.03) 
                    mapPanel.changeY(1);
                else if (mapPanel.getZoom() < 0.03) 
                    mapPanel.changeY(0.08);
            }
        });
        mapPanel.getInputMap(condition).put(keyDown, "down");
        mapPanel.getActionMap().put("down", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mapPanel.getZoom() >= 0.4) 
                    mapPanel.changeY(-20);
                else if(mapPanel.getZoom() >= 0.15) 
                    mapPanel.changeY(-10);
                else if(mapPanel.getZoom() >= 0.09) 
                    mapPanel.changeY(-5);
                else if(mapPanel.getZoom() >= 0.03) 
                    mapPanel.changeY(-1);
                else if(mapPanel.getZoom() < 0.03)
                    mapPanel.changeY(-0.08);
            }
        });
        mapPanel.getInputMap(condition).put(keyLeft, "left");
        mapPanel.getActionMap().put("left", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mapPanel.getZoom() >= 0.4) 
                    mapPanel.changeX(20);
                else if(mapPanel.getZoom() >= 0.15) 
                    mapPanel.changeX(10);
                else if(mapPanel.getZoom() >= 0.09) 
                    mapPanel.changeX(5);
                else if(mapPanel.getZoom() >= 0.03) 
                    mapPanel.changeX(1);
                else if(mapPanel.getZoom() < 0.03)
                    mapPanel.changeX(0.08);
            }
        });
        mapPanel.getInputMap(condition).put(keyRight, "right");
        mapPanel.getActionMap().put("right", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mapPanel.getZoom() >= 0.4) 
                    mapPanel.changeX(-20);
                else if(mapPanel.getZoom() >= 0.15) 
                    mapPanel.changeX(-10);
                else if(mapPanel.getZoom() >= 0.09) 
                    mapPanel.changeX(-5);
                else if(mapPanel.getZoom() >= 0.03) 
                    mapPanel.changeX(-1);
                else if(mapPanel.getZoom() < 0.03)
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
        
        //Adding components
        //zoomIn.setBorderPainted(false);
        //zoomIn.setAlignmentY(0.0f);
        mapPanel.add(zoomIn);
        mapPanel.add(zoomOut);
        
        //mapPanel.add(zoomOut);
        
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
        
        eastPanel.add(arrowPanel, gbc);
        
        roadPanel.add(road);
        
        //Spacing
        northPanel.add(Box.createHorizontalGlue());
        
        //Adding components to panels
//        northPanel.add(zoomIn);
//        northPanel.add(zoomOut);
        
        xyPanel.add(new JLabel("x: "));
        xyPanel.add(x);
        xyPanel.add(new JLabel(" y: "));
        xyPanel.add(y);
        
        northPanel.add(Box.createHorizontalGlue());
        
        northPanel.add(roadPanel);
        
        
        
        //Spacing, uncomment this if removing xyPanel from northPanel
        //northPanel.add(Box.createHorizontalGlue());
        
        //Adding components to panels
        northPanel.add(xyPanel);
        
        //Adding final touches to content pane.
        getContentPane().add(mapPanel, BorderLayout.CENTER);
        //getContentPane().add(northPanel, BorderLayout.NORTH);
        getContentPane().add(eastPanel, BorderLayout.EAST);
        
        //Show window
        int minHeight = 300;
        int minLength = minHeight*2;
        setMinimumSize(new Dimension(minLength, minHeight));
        setJMenuBar(menuBar);
        pack();
        setLocationRelativeTo(null);
        setVisible(false);
    }
}
