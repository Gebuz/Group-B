
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
    public final HelpWindow helpWindow;
    public final JPanel glass;
    public final JPanel mapPanel;
    public final JPanel northPanel, eastPanel, upPanel, downPanel, roadPanel, xyPanel, arrowPanel, zoomPanel, routePanel, fromPanel, toPanel;
    public final JLabel x, y, road, route, fromLabel, toLabel;
    public final JButton up, down, left, right, showFull, zoomIn, zoomOut;
    public final JMenuBar menuBar;
    public final JMenu mapOptionsMenu, helpMenu;
    public final JCheckBoxMenuItem enableRoad, enableRelative, enableNavigation;
    public final JRadioButton walk, car;
    public final ButtonGroup bgroup;
    public final JMenuItem setFrom, setTo, showHelp, clearMap;
    public final JPopupMenu popUp;
    public final JTextField from, to;
    private int condition;
    
    public MapView(String name, final MapPanel mapPanel) {
        super(name);
        
        DataLoader.loadBar.setVisible(false);
        helpWindow = new HelpWindow();
        
        //Setting UIManager settings
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MapView.class.getName()).log(Level.SEVERE, null, ex);
        }
        UIManager.put("MenuBar.background", Color.LIGHT_GRAY);
        UIManager.put("Menu.background", Color.LIGHT_GRAY);
        
        //Making menus
        menuBar = new JMenuBar();
        
        mapOptionsMenu = new JMenu("Map options");
        menuBar.add(mapOptionsMenu);
        
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
        
        mapOptionsMenu.addSeparator();
        
        clearMap = new JMenuItem("Clear map");
        clearMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        mapOptionsMenu.add(clearMap);
        
        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        
        showHelp = new JMenuItem("Help window");
        showHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
        helpMenu.add(showHelp);
        
        popUp = new JPopupMenu();
        setFrom = new JMenuItem("Set from");
        setTo = new JMenuItem("Set to");
        
        popUp.add(setFrom);
        popUp.add(setTo);
        
        //Initializing components  
        this.mapPanel = mapPanel;
        mapPanel.setFocusable(true);
        
        glass = (JPanel) getGlassPane();
        glass.setVisible(true);
        glass.setLayout(new BorderLayout());
        
        northPanel = new JPanel();
        xyPanel = new JPanel();
        
        eastPanel = new JPanel();
        upPanel = new JPanel();
        downPanel = new JPanel();
        arrowPanel = new JPanel();
        zoomPanel = new JPanel();
        roadPanel = new JPanel();
        routePanel = new JPanel();
        
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
        
        bgroup = new ButtonGroup();
        route = new JLabel("Find route for:");
        car = new JRadioButton("Car", true);
        walk = new JRadioButton("Pedestrian");
        
        fromPanel = new JPanel();
        toPanel = new JPanel();
        fromLabel = new JLabel("From");
        toLabel = new JLabel("    To");
        from = new JTextField(14);
        to = new JTextField(14);
        from.setEditable(false);
        to.setEditable(false);
        
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
        
        roadPanel.setLayout(new BorderLayout());
        
        //Adding components
        mapPanel.add(zoomIn);
        mapPanel.add(zoomOut);
        
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
        
        bgroup.add(car);
        bgroup.add(walk);
        
        routePanel.add(car);
        routePanel.add(walk);
        
        fromPanel.add(fromLabel);
        fromPanel.add(from);
        toPanel.add(toLabel);
        toPanel.add(to);
        
        eastPanel.add(fromPanel, gbc);
        eastPanel.add(toPanel, gbc);
        eastPanel.add(route, gbc);
        eastPanel.add(routePanel, gbc);
        eastPanel.add(arrowPanel, gbc);
        
        roadPanel.add(road, BorderLayout.WEST);
        roadPanel.setBackground(new Color(212, 212, 212, 100));
        
        glass.add(roadPanel, BorderLayout.SOUTH);
          
        //Adding final touches to content pane.
        getContentPane().add(mapPanel, BorderLayout.CENTER);
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
    
    public void showWarningForSelectedRoad(int graphState) {
        String message = "";
        String messageTitle = "";
        
        if (graphState == 0) {
            messageTitle = "Invalid road for cars!";
            message = "No cars allowed on the marked road."
                + "\nPlease choose another road.";
        } else if (graphState == 1) {
            messageTitle = "Invalid road for pedestrians!";
            message = "Pedestrians cannot walk on the marked road."
                + "\nPlease choose another road.";
        }

        JOptionPane.showMessageDialog(this, message, messageTitle, 
                JOptionPane.WARNING_MESSAGE);
    }
    
    public void showWarningNoPathFound() {
        String messageTitle = "No path found!";
        String message = "Could not find a path between the selected roads."
                + "\nPlease try again.";
        JOptionPane.showMessageDialog(this, message, messageTitle, 
                JOptionPane.WARNING_MESSAGE);
    }
}
