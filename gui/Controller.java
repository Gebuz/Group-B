/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.MouseListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import javax.swing.SwingUtilities;

/**
 *
 * @author flemmingxu
 */
public class Controller implements MouseListener, MouseMotionListener, ComponentListener, ActionListener, MouseWheelListener, ItemListener, KeyListener {

    private final MapView view;
    private final MapPanel map;
    private final double initHeight;
    private final double initWidth;
    private double xPress, yPress, xPressLocal, yPressLocal;
    private double mousePosX, mousePosY;
    private boolean centerZoom;
    
    private boolean cancelDrag = false;
    
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
        view.addComponentListener(this);
        view.enableRelative.addItemListener(this);
        view.enableRoad.addItemListener(this);
        view.enableNavigation.addItemListener(this);
//        view.roadOn.addActionListener(this);
//        view.roadOff.addActionListener(this);
//        view.relativeZoomCheckBox.addItemListener(this);
        
        map.addMouseListener(this);
        map.addMouseMotionListener(this);
        map.addMouseWheelListener(this);
        map.addKeyListener(this);
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
                heightRatio = (heightRatio * map.INIT_HEIGHT - map.INIT_HEIGHT) * map.getZoom() / 2;    

                map.changeX(widthDiff - heightRatio);
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
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
        double resizeHeight = map.getHeight();
        double resizeWidth = map.getWidth();
        double widthDiff = resizeWidth - map.lastWidth;
        double heightRatio = resizeHeight/map.lastHeight;
        
        if (resizeHeight < initHeight || resizeHeight > initHeight) {
            double constant = (initHeight+1) / resizeHeight;
            map.updateResize(constant);
        }
           
        widthDiff = widthDiff * map.getResizeConstant() * map.getZoom() / 2;
        heightRatio = (heightRatio * map.lastWidth - map.lastWidth) * map.getZoom() * map.getResizeConstant() / 2;
        
        Point2D.Double press = map.getPress();
        Point2D.Double release = map.getRelease();
        
        map.changeX(widthDiff - heightRatio);
       
        // Update maps last width.
        map.lastWidth = resizeWidth;
        map.lastHeight = resizeHeight;

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
        if      (e.getSource() == view.zoomIn) {
            map.zoomIn(0.10);
        }
        else if (e.getSource() == view.zoomOut) {
            map.zoomOut(0.10);
        }
        else if (e.getSource() == view.showFull) {
            map.defaultMap();
            view.pack();
        }
//        else if (e.getSource() == view.roadOn) {
//            view.roadOn.setEnabled(false);
//            view.roadOff.setEnabled(true);
//            view.roadOnOff.setText("Show Road Names: ON");
//            map.roadSwitch();
//        }
//        else if (e.getSource() == view.roadOff) {
//            view.roadOff.setEnabled(false);
//            view.roadOn.setEnabled(true);
//            view.roadOnOff.setText("Show Road Names: OFF");
//            map.roadSwitch();
//        }
        
        else if (e.getSource() == view.up) {
            if      (map.getZoom() >= 0.4)  map.changeY(20);
            else if (map.getZoom() >= 0.15) map.changeY(10);
            else if (map.getZoom() >= 0.09) map.changeY(5);
            else if (map.getZoom() >= 0.03) map.changeY(1);
            else if (map.getZoom() <  0.03) map.changeY(0.08);
        }
        else if (e.getSource() == view.down) {
            if     (map.getZoom() >= 0.4)   map.changeY(-20);
            else if(map.getZoom() >= 0.15)  map.changeY(-10);
            else if(map.getZoom() >= 0.09)  map.changeY(-5);
            else if(map.getZoom() >= 0.03)  map.changeY(-1);
            else if(map.getZoom() <  0.03)  map.changeY(-0.08);
        }
        else if (e.getSource() == view.left) {
            if     (map.getZoom() >= 0.4)   map.changeX(20);
            else if(map.getZoom() >= 0.15)  map.changeX(10);
            else if(map.getZoom() >= 0.09)  map.changeX(5);
            else if(map.getZoom() >= 0.03)  map.changeX(1);
            else if(map.getZoom() <  0.03)  map.changeX(0.08);
        }
        else if (e.getSource() == view.right) {
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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            map.removeRect();
            map.repaint();
            cancelDrag = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
