/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

/**
 *
 * @author flemmingxu
 */
public class Controller implements MouseListener, MouseMotionListener, ComponentListener, ActionListener {

    private final MapView view;
    private final MapPanel map;
    private final double initHeight;
    private final double initWidth;
    private double xPress, yPress;

    public Controller(MapView view) {
        this.view = view;
        this.map = (MapPanel) view.getMap();
         
        initHeight = map.getSize().height - 1;
        initWidth = map.getSize().width - 1;

        view.addComponentListener(this);
        view.zoomIn.addMouseListener(this);
        view.zoomOut.addMouseListener(this);
        view.showFull.addMouseListener(this);
        view.up.addMouseListener(this);
        view.down.addMouseListener(this);
        view.left.addMouseListener(this);
        view.right.addMouseListener(this);

        //map.addComponentListener(this);
        map.addMouseListener(this);
        map.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //This could assign a new list of edges to MapPanel
        if (e.getComponent() == view.zoomIn) {
            map.zoomIn(0.10);
        }
        if (e.getComponent() == view.zoomOut) {
            map.zoomOut(0.10);
        }
        if (e.getComponent() == view.showFull) {
            map.defaultMap();
            view.pack();
        }
        if (e.getComponent() == view.up) {
            if (map.getZoom() != 1 && map.getZoom() >= 0.4) {
                map.changeY(20);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.4
                    && map.getZoom() >= 0.15) {
                map.changeY(10);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.15
                    && map.getZoom() >= 0.09) {
                map.changeY(5);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.09
                    && map.getZoom() >= 0.03) {
                map.changeY(1);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.03) {
                map.changeY(0.2);
            }
        }
        if (e.getComponent() == view.down) {
            if (map.getZoom() != 1 && map.getZoom() >= 0.4) {
                map.changeY(-20);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.4
                    && map.getZoom() >= 0.15) {
                map.changeY(-10);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.15
                    && map.getZoom() >= 0.09) {
                map.changeY(-5);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.09
                    && map.getZoom() >= 0.03) {
                map.changeY(-1);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.03) {
                map.changeY(-0.2);
            }
        }
        if (e.getComponent() == view.left) {
            if (map.getZoom() != 1 && map.getZoom() >= 0.4) {
                map.changeX(20);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.4
                    && map.getZoom() >= 0.15) {
                map.changeX(10);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.15
                    && map.getZoom() >= 0.09) {
                map.changeX(5);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.09
                    && map.getZoom() >= 0.03) {
                map.changeX(1);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.03) {
                map.changeX(0.2);
            }
        }
        if (e.getComponent() == view.right) {
            if (map.getZoom() != 1 && map.getZoom() >= 0.4) {
                map.changeX(-20);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.4
                    && map.getZoom() >= 0.15) {
                map.changeX(-10);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.15
                    && map.getZoom() >= 0.09) {
                map.changeX(-5);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.09
                    && map.getZoom() >= 0.03) {
                map.changeX(-1);
            }
            if (map.getZoom() != 1 && map.getZoom() < 0.03) {
                map.changeX(-0.2);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getComponent() == map) {
            xPress = e.getX();
            yPress = e.getY();
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        double xDrag = e.getX();
        double yDrag = e.getY();
        double rectHeight = Math.abs(yDrag - yPress);
        double rectWidth = rectHeight * map.ratio;
        
        Rectangle rectangle = new Rectangle((int) xPress,(int) yPress, 0, 0);
        
        if (xDrag > xPress && yDrag > yPress) {
                rectangle = new Rectangle((int) xPress,(int) yPress,(int) rectWidth, (int) rectHeight);
            }
        if (xDrag > xPress && yDrag < yPress) {
                rectangle = new Rectangle((int) xPress,(int) yDrag,(int) rectWidth, (int) rectHeight);
            }
        if (xDrag < xPress && yDrag > yPress) {
                double xReleaseLeft = xPress - rectWidth;
                rectangle = new Rectangle((int) xReleaseLeft,(int) yPress,(int) rectWidth, (int) rectHeight);
            }
        if (xDrag < xPress && yDrag < yPress) {
                double xReleaseLeft = xPress - rectWidth;
                rectangle = new Rectangle((int) xReleaseLeft,(int) yDrag,(int) rectWidth, (int) rectHeight);
        }
        map.assignRect(rectangle);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getComponent() == map) {
            map.removeRect();
            double yRelease = e.getY();
            double xRelease = e.getX();

            double rectHeight = Math.abs(yRelease - yPress);
            double rectWidth = rectHeight * map.ratio;

            Point2D.Double press = null;
            Point2D.Double release = null;

            if (xRelease > xPress && yRelease > yPress) {
                double xReleaseRight = rectWidth + xPress;
                press = new Point2D.Double(xPress, yPress);
                release = new Point2D.Double(xReleaseRight, yRelease);
            }
            if (xRelease > xPress && yRelease < yPress) {
                double xReleaseRight = rectWidth + xPress;
                press = new Point2D.Double(xPress, yRelease);
                release = new Point2D.Double(xReleaseRight, yPress);
            }
            if (xRelease < xPress && yRelease > yPress) {
                double xReleaseLeft = xPress - rectWidth;
                press = new Point2D.Double(xReleaseLeft, yPress);
                release = new Point2D.Double(xPress, yRelease);
            }
            if (xRelease < xPress && yRelease < yPress) {
                double xReleaseLeft = xPress - rectWidth;
                press = new Point2D.Double(xReleaseLeft, yRelease);
                release = new Point2D.Double(xPress, yPress);
            }
            
            if (press != null && release != null) { // Avoid nullPointerException
 
                //System.out.println("\nRatio between press and release BEFORE:");
                double w = -(press.x - release.x);
                double h = -(press.y - release.y);
                double mouseRatio = w / h;
                //System.out.println("ratio = " + mouseRatio + "\n");
 
                // *** Sjurdur ***
                double zoomConstant = map.getZoomConstant();
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
               
               
               
                //System.out.println("\nRatio between press and release AFTER:");
                double w1 = -(press.x - release.x);
                double h1 = -(press.y - release.y);
                //System.out.println("ratio = " + (w1 / h1));
 
                // ***************
                map.assignCoords(press, release);
            }
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        view.x.setText("" + e.getX());
        view.y.setText("" + e.getY());
        
//        double x = ((e.getX()*map.resizeConstant*map.k)/map.zoomConstant) + (map.xk*map.k) + CoordinateBoundaries.xMin;
//        double y = ((e.getY()*map.resizeConstant*map.k)/map.zoomConstant) + (map.yk*map.k) + CoordinateBoundaries.yMin;
//               
//        EdgeData edge = map.qt.getClosestRoad(x, y);
//        
//        view.road.setText(edge.VEJNAVN);

        //use Point2D.double for storing x, y coordinate of edges, put the point objects in Hash map.
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
        double resizeHeight = map.getSize().height;
 
        Point2D.Double release = map.getRelease();
        map.setVectorLastRelease(release.x - map.getWidth(),
                release.y - map.getHeight());
        
        if (resizeHeight < initHeight || resizeHeight > initHeight) {
            double constant = initHeight / resizeHeight;
            map.updateResize(constant);
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
    }
}
