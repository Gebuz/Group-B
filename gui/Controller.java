/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;

/**
 *
 * @author flemmingxu
 */
public class Controller implements MouseListener, MouseMotionListener, ComponentListener, ActionListener {

    private MapView view;
    private MapPanel map;
    private DataLoader loader;
    private double initHeight;
    private double initWidth;
    private double xPress, yPress;

    public Controller(MapView view) {
        this.view = view;
        this.map = (MapPanel) view.getMap();
        this.loader = map.getData();
                
        initHeight = map.getSize().height - 1;
        initWidth = map.getSize().width - 1;

        String upKey = "UP";
        String downKey = "DOWN";
        String leftKey = "LEFT";
        String rightKey = "RIGHT";

        KeyStroke up = KeyStroke.getKeyStroke("UP");
        KeyStroke down = KeyStroke.getKeyStroke("DOWN");
        KeyStroke left = KeyStroke.getKeyStroke("LEFT");
        KeyStroke right = KeyStroke.getKeyStroke("RIGHT");

        InputMap upInput = view.up.getInputMap();
        ActionMap upAction = view.up.getActionMap();

        InputMap downInput = view.down.getInputMap();
        ActionMap downAction = view.down.getActionMap();

        InputMap leftInput = view.left.getInputMap();
        ActionMap leftAction = view.left.getActionMap();

        InputMap rightInput = view.right.getInputMap();
        ActionMap rightAction = view.right.getActionMap();

        upInput.put(up, upKey);


        view.addComponentListener(this);
        view.zoomIn.addMouseListener(this);
        view.zoomOut.addMouseListener(this);
        view.showFull.addMouseListener(this);

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
        if(e.getComponent() == view.showFull) {
            map.defaultMap();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getComponent() == map) {
            xPress = e.getX();
            yPress = e.getY();
        }

        //map.currentRect = new Rectangle(x, y, 0, 0);
        //map.updateDrawableRect(getWidth(), getHeight());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getComponent() == map) {
            
            double yRelease = e.getY();
            double xRelease = e.getX();
            
            double rectHeight = Math.abs(yRelease - yPress);
            double rectWidth = rectHeight*map.ratio; //Math.abs(xRelease - xPress);
            
            Point2D.Double press = null;
            Point2D.Double release = null;
            
            if(xRelease > xPress && yRelease > yPress) {
                double xReleaseRight = rectWidth + xPress; 
                press = new Point2D.Double(xPress, yPress);
                release = new Point2D.Double(xReleaseRight, yRelease);
            }
            if(xRelease > xPress && yRelease < yPress) {
                double xReleaseRight = rectWidth + xPress; 
                press = new Point2D.Double(xPress, yRelease);
                release = new Point2D.Double(xReleaseRight, yPress);
            }
            if(xRelease < xPress && yRelease > yPress) {
                double xReleaseLeft = xPress - rectWidth;
                press = new Point2D.Double(xReleaseLeft, yPress);
                release = new Point2D.Double(xPress, yRelease);
            }
            if(xRelease < xPress && yRelease < yPress) {
                double xReleaseLeft = xPress - rectWidth;
                press = new Point2D.Double(xReleaseLeft, yRelease);
                release = new Point2D.Double(xPress, yPress);
            }
            
            
            if (press != null && release != null) { // Avoid nullPointerException

                Rectangle rect = new Rectangle((int) xPress, (int) yPress, (int) rectWidth, (int) rectHeight);

                // *** Sjurdur ***
                double zoomConstant = map.getZoomConstant();
                Point2D.Double vectorLastPress = map.getVectorLastPress();
                Point2D.Double vectorLastRelease = map.getVectorLastRelease();

                // Top left corner
                double vectorNewPressX = (press.x) * zoomConstant;
                double vectorNewPressY = (press.y) * zoomConstant;

                press.setLocation(vectorLastPress.x + vectorNewPressX, 
                                  vectorLastPress.y + vectorNewPressY);

                map.setVectorLastPress( vectorLastPress.x + vectorNewPressX, 
                                        vectorLastPress.y + vectorNewPressY);

                // Bottom right corner.
                double vectorNewReleaseX = (release.x - initWidth)  * zoomConstant;
                double vectorNewReleaseY = (release.y - initHeight) * zoomConstant;

                release.setLocation(initWidth  + vectorLastRelease.x + vectorNewReleaseX , 
                                    initHeight + vectorLastRelease.y + vectorNewReleaseY);

                map.setVectorLastRelease(   vectorLastRelease.x + vectorNewReleaseX, 
                                            vectorLastRelease.y + vectorNewReleaseY);

                // ***************

                map.assignCoords(press, release, rect);
                //view.pack(); Bliver ved med at resize hele programvinduet :(
            }
            
        }
        //view.pack();

//        if(yRelease > yPress && xRelease > xPress) {
//            release2 = new Point2D.Double(xPress+rectWidth, yRelease-rectHeight);
//            press2 = new Point2D.Double(xPress, yPress+rectHeight);
//            
//            System.out.println("Release: " + xRelease + " " + yRelease);
//            System.out.println("Press: " + xPress + " " + yPress);
//            System.out.println("Release2: " + release2.x + " " + release2.y);
//            System.out.println("Press2: " + press2.x + " " + press2.y);
//            
//            Rectangle rect = new Rectangle((int) xPress,(int) yPress,(int) rectWidth,(int) rectHeight);
//            map.drawRect(rect, release2, press2);
//        }
//        if(yRelease < yPress && xRelease < xPress) {
//            release2 = new Point2D.Double(xPress-rectWidth, yRelease+rectHeight);
//            press2 = new Point2D.Double(xPress, yPress-rectHeight);
//            
//            System.out.println("Release: " + xRelease + " " + yRelease);
//            System.out.println("Press: " + xPress + " " + yPress);
//            System.out.println("Release: " + release2.x + " " + release2.y);
//            System.out.println("Press: " + press2.x + " " + press2.y);
//            
//            Rectangle rect = new Rectangle((int) xPress,(int) yPress,(int) rectWidth,(int) rectHeight);
//            map.drawRect(rect, release2, press2);
//        }
//        if(yRelease > yPress && xRelease < xPress) {
//            release2 = new Point2D.Double(xPress-rectWidth, yRelease-rectHeight);
//            press2 = new Point2D.Double(xPress, yPress+rectHeight);
//            
//            System.out.println("Release: " + xRelease + " " + yRelease);
//            System.out.println("Press: " + xPress + " " + yPress);
//            System.out.println("Release: " + release2.x + " " + release2.y);
//            System.out.println("Press: " + press2.x + " " + press2.y);
//            
//            Rectangle rect = new Rectangle((int) xPress,(int) yPress,(int) rectWidth,(int) rectHeight);
//            map.drawRect(rect, release2, press2);
//        }
//        if(yRelease < yPress && xRelease > xPress) {
//            release2 = new Point2D.Double(xPress+rectWidth, yRelease+rectHeight);
//            press2 = new Point2D.Double(xPress, yPress-rectHeight);
//            
//            System.out.println("Release: " + xRelease + " " + yRelease);
//            System.out.println("Press: " + xPress + " " + yPress);
//            System.out.println("Release: " + release2.x + " " + release2.y);
//            System.out.println("Press: " + press2.x + " " + press2.y);
//            
//            Rectangle rect = new Rectangle((int) xPress,(int) yPress,(int) rectWidth,(int) rectHeight);
//            map.drawRect(rect, release2, press2);
//        }

        //System.out.println("xReleased: " + xRelease + " yReleased: " + yRelease);
        //System.out.println("Rectangle width: " + rectWidth + " Rectangle height: " + rectHeight);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        view.x.setText("" + e.getX());
        view.y.setText("" + e.getY());

        //use Point2D.double for storing x, y coordinate of edges, put the point objects in Hash map.
    }

    @Override
    public void componentResized(ComponentEvent e) {
        double resizeHeight = map.getSize().height - 1;

        if (resizeHeight < initHeight || resizeHeight > initHeight) {
            double constant = initHeight / resizeHeight;
            map.updateResize(constant);
        }
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
