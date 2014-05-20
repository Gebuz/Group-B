package gui;

import Model.PathFinder;
import interfaces.MapEdge;
import java.awt.event.MouseListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author flemmingxu
 */
public class Controller implements MouseListener, MouseMotionListener, ComponentListener, ActionListener, MouseWheelListener, ItemListener {

    private final MapView view;
    private final MapPanel map;
    private final double initHeight;
    private final double initWidth;
    private double xPress, yPress, xPressLocal, yPressLocal;
    private double mousePosX, mousePosY;
    private double popUpPosX, popUpPosY;
    private boolean fromSet, toSet;
    private MapEdge fromEdge, toEdge;
    private boolean centerZoom;
    private boolean cancelDrag;
    
    /**
     * Sets the view, map and all necessary listeners.
     * @param view The main window.
     */
    public Controller(MapView view) {
        
        this.view = view;
        
        this.map = (MapPanel) view.mapPanel;
         
        initHeight = map.getSize().height - 1;
        initWidth = map.getSize().width - 1;
        
        centerZoom = false;

        view.zoomIn.addActionListener(this);
        view.zoomOut.addActionListener(this);
        view.showFull.addActionListener(this);
        view.up.addActionListener(this);
        view.down.addActionListener(this);
        view.left.addActionListener(this);
        view.right.addActionListener(this);
        view.showHelp.addActionListener(this);
        view.clearMap.addActionListener(this);
        view.setFrom.addActionListener(this);
        view.setTo.addActionListener(this);
        view.car.addActionListener(this);
        view.walk.addActionListener(this);
        view.addComponentListener(this);
        view.enableRelative.addItemListener(this);
        view.enableRoad.addItemListener(this);
        view.enableNavigation.addItemListener(this);
        
        map.addMouseListener(this);
        map.addMouseMotionListener(this);
        map.addMouseWheelListener(this);
        
        map.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),"cancelDrag");
        map.getActionMap().put("cancelDrag", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map.removeRect();
                map.repaint();
                cancelDrag = true;
            }
        });
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        cancelDrag = false;
        if (e.getComponent() == map) {
            xPress = e.getX()* map.getResizeConstant();
            yPress = e.getY()* map.getResizeConstant();
            xPressLocal = map.getPress().x + (map.getRelease().x - map.getPress().x)*xPress/850;
            yPressLocal = map.getPress().y + (map.getRelease().y - map.getPress().y)*yPress/660;
        }
        
        if(e.isPopupTrigger() && e.isShiftDown()) {
            popUpPosX = e.getX();
            popUpPosY = e.getY();
            view.popUp.show(e.getComponent(),(int) popUpPosX,(int) popUpPosY);
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        double xDrag = e.getX();
        double yDrag = e.getY();

        mousePosX = e.getX()*map.getResizeConstant();
        mousePosY = e.getY()*map.getResizeConstant();
        
        double rectHeight = Math.abs(yDrag - (yPress / map.getResizeConstant()));
        double rectWidth = rectHeight * map.ratio;
        
        if(SwingUtilities.isRightMouseButton(e)) {
            double xDragLocal = map.getPress().x + (map.getRelease().x - map.getPress().x)*e.getX()* map.getResizeConstant()/850; 
            double yDragLocal = map.getPress().y + (map.getRelease().y - map.getPress().y)*e.getY()* map.getResizeConstant()/660;
            double xDiff = xDragLocal - xPressLocal;
            double yDiff = yDragLocal - yPressLocal;
            
            xPressLocal += xDiff;
            yPressLocal += yDiff;
            map.changeX(xDiff);
            map.changeY(yDiff);            
        }

        else if(SwingUtilities.isLeftMouseButton(e) && !cancelDrag) {
            double xPressTemp = xPress / map.getResizeConstant();
            double yPressTemp = yPress / map.getResizeConstant();
            
            Rectangle rectangle = new Rectangle((int) xPressTemp,(int) yPressTemp, 0, 0);

            if (xDrag > xPressTemp && yDrag > yPressTemp) {
                    rectangle = new Rectangle((int) xPressTemp,(int) yPressTemp,(int) rectWidth, (int) rectHeight);
                }
            else if (xDrag > xPressTemp && yDrag < yPressTemp) {
                    rectangle = new Rectangle((int) xPressTemp,(int) yDrag,(int) rectWidth, (int) rectHeight);
                }
            else if (xDrag < xPressTemp && yDrag > yPressTemp) {
                    double xReleaseLeft = xPressTemp - rectWidth;
                    rectangle = new Rectangle((int) xReleaseLeft,(int) yPressTemp,(int) rectWidth, (int) rectHeight);
                }
            else if (xDrag < xPressTemp && yDrag < yPressTemp) {
                    double xReleaseLeft = xPressTemp - rectWidth;
                    rectangle = new Rectangle((int) xReleaseLeft,(int) yDrag,(int) rectWidth, (int) rectHeight);
            }
            map.assignRect(rectangle);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!cancelDrag && e.getComponent() == map && SwingUtilities.isLeftMouseButton(e)) {
            map.removeRect();
            
            double yRelease = e.getY()* map.getResizeConstant();
            double xRelease = e.getX()* map.getResizeConstant();

            double rectHeight = Math.abs(yRelease - yPress);
            double rectWidth = rectHeight * map.ratio;

            Point2D.Double press = null;
            Point2D.Double release = null;
            
            if      (xRelease > xPress && yRelease > yPress) {
                double xReleaseRight = rectWidth + xPress;
                press = new Point2D.Double(xPress, yPress);
                release = new Point2D.Double(xReleaseRight, yRelease);
            }
            else if (xRelease > xPress && yRelease < yPress) {
                double xReleaseRight = rectWidth + xPress;
                press = new Point2D.Double(xPress, yRelease);
                release = new Point2D.Double(xReleaseRight, yPress);
            }
            else if (xRelease < xPress && yRelease > yPress) {
                double xReleaseLeft = xPress - rectWidth;
                press = new Point2D.Double(xReleaseLeft, yPress);
                release = new Point2D.Double(xPress, yRelease);
            }
            else if (xRelease < xPress && yRelease < yPress) {
                double xReleaseLeft = xPress - rectWidth;
                press = new Point2D.Double(xReleaseLeft, yRelease);
                release = new Point2D.Double(xPress, yPress);
            }
            
            if (press != null && release != null) { // Avoid nullPointerException
 
                double zoomConstant = map.getZoom();
                Point2D.Double vectorLastPress = map.getVectorLastPress();
                Point2D.Double vectorLastRelease = map.getVectorLastRelease();
 
                // Top left corner
                double vectorNewPressX = (press.x) * zoomConstant;
                double vectorNewPressY = (press.y) * zoomConstant;
     
                press.setLocation(vectorLastPress.x + vectorNewPressX, vectorLastPress.y + vectorNewPressY);
                map.setVectorLastPress(vectorLastPress.x + vectorNewPressX, vectorLastPress.y + vectorNewPressY);
 
                // Bottom right corner.                
                double vectorNewReleaseY = (release.y - initHeight) * zoomConstant;
                double vectorNewReleaseX = (release.x - initWidth)  * zoomConstant;
               
                // New Part
                double newReleaseY = map.getHeight() + vectorLastRelease.y + vectorNewReleaseY;
                rectHeight = Math.abs(newReleaseY - press.y);
                rectWidth = rectHeight * map.ratio;
                double newReleaseX = rectWidth + press.x;
                release.setLocation(newReleaseX, newReleaseY);
 
                map.setVectorLastRelease(map.getVectorLastRelease().x + vectorNewReleaseX,
                map.getVectorLastRelease().y + vectorNewReleaseY);
               
                map.assignCoords(press, release);

                double heightRatio = map.getHeight()/(map.INIT_HEIGHT * 1.0);
                double widthDiff = (map.getWidth() - map.INIT_WIDTH )* map.getResizeConstant() * map.getZoom() / 2;
                heightRatio = (heightRatio * map.INIT_HEIGHT - map.INIT_HEIGHT)* map.getResizeConstant() * map.getZoom() / 2;  
    

                map.changeX(widthDiff - heightRatio);

            }
        }
        else if (e.getComponent() == map && SwingUtilities.isRightMouseButton(e)) {
            if(e.isPopupTrigger() && e.isShiftDown()) {
                popUpPosX = e.getX();
                popUpPosY = e.getY();
                view.popUp.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        view.x.setText("" + e.getX());
        view.y.setText("" + e.getY());
        mousePosX = e.getX()*map.getResizeConstant();
        mousePosY = e.getY()*map.getResizeConstant();
        view.road.setText(map.getRoadName(mousePosX, mousePosY));
        map.repaint();
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
        double resizeHeight = map.getHeight();
        double resizeWidth = map.getWidth();
        double widthDiff = resizeWidth - map.getLastWidth();
        double heightRatio = resizeHeight/map.getLastHeight();

        
        if (resizeHeight < initHeight || resizeHeight > initHeight) {
            double constant = (initHeight+1) / resizeHeight;
            map.updateResize(constant);
        }
           
        widthDiff = widthDiff * map.getResizeConstant() * map.getZoom() / 2;
        heightRatio = (heightRatio * map.getLastWidth() - map.getLastWidth()) * map.getZoom() * map.getResizeConstant() / 2;
        
        map.changeX(widthDiff - heightRatio);
        
        Point2D.Double press = map.getPress();
        Point2D.Double release = map.getRelease();
        
        // Update maps last width.
        map.setLastWidth(resizeWidth);
        map.setLastHeight(resizeHeight);


        map.setVectorLastRelease(release.x - map.getWidth(),
                release.y - map.getHeight());
        map.assignCoords(press, release);
        
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
       int notches = e.getWheelRotation();
       if (!centerZoom) {
            if (notches > 0) map.zoomOutRelativeToMouse(0.05, mousePosX, mousePosY);
            else             map.zoomInRelativeToMouse (0.05, mousePosX, mousePosY);
       } else /* center zoom */ {
            if (notches > 0) map.zoomOut(0.05);
            else             map.zoomIn (0.05);
       }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if      (source == view.zoomIn) {
            map.zoomIn(0.10);
        }
        else if (source == view.zoomOut) {
            map.zoomOut(0.10);
        }
        else if (source == view.showFull) {
            map.defaultMap();
            view.pack();
        }
        else if (source == view.showHelp) {
            view.helpWindow.setVisible(true);
        }
        else if (source == view.clearMap) {
            clear();
        }
        else if (source == view.setFrom) {
            double x = popUpPosX*map.getResizeConstant();
            double y = popUpPosY*map.getResizeConstant();
            if(toSet) {
                if (!isRoadValid(map.getClosestRoad(x, y, false))) {
                    return;
                }
                fromEdge = map.getClosestRoad(x, y, false);
        
                try {
                    getAndDrawShortestPath(fromEdge, toEdge);
                } catch (Exception ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                fromSet = true;
            }
            else {
                if (!isRoadValid(map.getClosestRoad(x, y, false))) {
                    fromSet = true; //avoid resetting the whole pathfinding process.
                    toSet = true;
                    return;
                }  
                fromEdge = map.getClosestRoad(x, y, false);
                fromSet = true;
            }
            view.from.setText(fromEdge.getName());
            map.assignPinPos(x, y, 0);
        }
        else if (source == view.setTo) {
            double x = popUpPosX*map.getResizeConstant();
            double y = popUpPosY*map.getResizeConstant();
            if(fromSet) {      
                if (!isRoadValid(map.getClosestRoad(x, y, false))) {
                   return;
                } 
                toEdge = map.getClosestRoad(x, y, false);
               
                try {
                    getAndDrawShortestPath(fromEdge, toEdge);
                } catch (Exception ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                toSet = true;
            }
            else {
                if (!isRoadValid(map.getClosestRoad(x, y, false))) {
                    toSet = true; //avoid resetting the whole pathfinding process.
                    fromSet = true;
                    return;
                }
                toEdge = map.getClosestRoad(x, y, false);
                toSet = true;
            }
            view.to.setText(toEdge.getName());
            map.assignPinPos(x, y, 1);
        }
        else if (source == view.car) {
            PathFinder.deleteGraph();
            PathFinder.createGraph(0, DataLoader.edgesGreen, DataLoader.getRoadCounter());
            clear();
        }
        else if (source == view.walk) {
            PathFinder.deleteGraph();
            PathFinder.createGraph(1, DataLoader.edgesGreen, DataLoader.getRoadCounter());
            clear();
        }
        else if (source == view.up) {
            if      (map.getZoom() >= 0.4)  map.changeY(20);
            else if (map.getZoom() >= 0.15) map.changeY(10);
            else if (map.getZoom() >= 0.09) map.changeY(5);
            else if (map.getZoom() >= 0.03) map.changeY(1);
            else if (map.getZoom() <  0.03) map.changeY(0.08);
        }
        else if (source == view.down) {
            if     (map.getZoom() >= 0.4)   map.changeY(-20);
            else if(map.getZoom() >= 0.15)  map.changeY(-10);
            else if(map.getZoom() >= 0.09)  map.changeY(-5);
            else if(map.getZoom() >= 0.03)  map.changeY(-1);
            else if(map.getZoom() <  0.03)  map.changeY(-0.08);
        }
        else if (source == view.left) {
            if     (map.getZoom() >= 0.4)   map.changeX(20);
            else if(map.getZoom() >= 0.15)  map.changeX(10);
            else if(map.getZoom() >= 0.09)  map.changeX(5);
            else if(map.getZoom() >= 0.03)  map.changeX(1);
            else if(map.getZoom() <  0.03)  map.changeX(0.08);
        }
        else if (source == view.right) {
            if     (map.getZoom() >= 0.4)   map.changeX(-20);
            else if(map.getZoom() >= 0.15)  map.changeX(-10);
            else if(map.getZoom() >= 0.09)  map.changeX(-5);
            else if(map.getZoom() >= 0.03)  map.changeX(-1);
            else if(map.getZoom() <  0.03)  map.changeX(-0.08);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        if (source == view.enableRelative) {
            if (centerZoom) {
                centerZoom = false;
            } else {
                centerZoom = true;
            }
        }
        else if (source == view.enableRoad) {
            map.roadSwitch();
        }
        else if (source == view.enableNavigation) {
            if (view.eastPanel.isVisible()) {
                view.eastPanel.setVisible(false);
                view.repaint();
                map.updateMap();
            }
            else {
                view.eastPanel.setVisible(true);
                view.repaint();
                map.updateMap();
            }
        }
    }
    
    /**
     * Helper method to find the shortest path. This method adds a timeout to
     * the PathFinder.getShortestPath() method. Set the timeout in milliseconds.
     * @param fromEdge The beginning point.
     * @param toEdge The end point.
     */
    public void getAndDrawShortestPath(MapEdge fromEdge, MapEdge toEdge) {
        try {
            ArrayList<MapEdge> path = PathFinder.getShortestPath(fromEdge, toEdge);
            if (path.isEmpty()) {
                view.showWarningNoPathFound();
                clear();
            }
            else map.drawShortestPath(path);
        } catch (NullPointerException ex) {
            view.showWarningNoPathFound();
            clear();
        }
    }
    
    /**
     * Checks if the specified edge is valid for the graph in use.
     * @param edge The specified edge.
     * @return Returns false if not valid, true if valid.
     */
    public boolean isRoadValid(MapEdge edge) {
        int graphState = PathFinder.getState();
        int type = edge.getType();
        String oneway = edge.getOneWay();

        if (graphState == 0) { /* car graph */
            if (oneway.equalsIgnoreCase("n") || edge.getMaxSpeed() == 0 || type == 8 || type == 48) {
                view.showWarningForSelectedRoad(graphState);
                return false;
            } else {
                return true;
            }
        } else { /* walk graph */
            if (type == 1 || type == 31 || type == 41) { /* Highway */
                view.showWarningForSelectedRoad(graphState);
                return false;
            } else {
                return true;
            }
        }
    }
    
    private void clear() {
        map.clearPath();
        view.from.setText("");
        view.to.setText("");
        fromSet = false;
        toSet = false;
    }
}
