/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

    private MapView view;
    private MapPanel map;
    private DataLoader loader;
    private final double initHeigth, initWidth; // Initial heigth and width.
    private double height, width; // current height and width.
    private double xPress, yPress;

    public Controller(MapView view) {
        this.view = view;
        this.map = (MapPanel) view.getMap();
        this.loader = map.getData();

        this.height = map.getSize().height;
        this.width = map.getSize().width;
        
        this.initHeigth = height;
        this.initWidth = width;

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
            double zoomConstant = map.getZoomConstant();
            if (zoomConstant != 1 && zoomConstant >= 0.4) {
                map.changeY(20);
            }
            if (zoomConstant != 1 && zoomConstant < 0.4
                    && zoomConstant >= 0.15) {
                map.changeY(10);
            }
            if (zoomConstant != 1 && zoomConstant < 0.15
                    && zoomConstant >= 0.09) {
                map.changeY(5);
            }
            if (zoomConstant != 1 && zoomConstant < 0.09
                    && zoomConstant >= 0.03) {
                map.changeY(1);
            }
            if (zoomConstant != 1 && zoomConstant < 0.03) {
                map.changeY(0.2);
            }
        }
        if (e.getComponent() == view.down) {
            double zoomConstant = map.getZoomConstant();
            if (zoomConstant != 1 && zoomConstant >= 0.4) {
                map.changeY(-20);
            }
            if (zoomConstant != 1 && zoomConstant < 0.4
                    && zoomConstant >= 0.15) {
                map.changeY(-10);
            }
            if (zoomConstant != 1 && zoomConstant < 0.15
                    && zoomConstant >= 0.09) {
                map.changeY(-5);
            }
            if (zoomConstant != 1 && zoomConstant < 0.09
                    && zoomConstant >= 0.03) {
                map.changeY(-1);
            }
            if (zoomConstant != 1 && zoomConstant < 0.03) {
                map.changeY(-0.2);
            }
        }
        if (e.getComponent() == view.left) {
            double zoomConstant = map.getZoomConstant();
            if (zoomConstant != 1 && zoomConstant >= 0.4) {
                map.changeX(20);
            }
            if (zoomConstant != 1 && zoomConstant < 0.4
                    && zoomConstant >= 0.15) {
                map.changeX(10);
            }
            if (zoomConstant != 1 && zoomConstant < 0.15
                    && zoomConstant >= 0.09) {
                map.changeX(5);
            }
            if (zoomConstant != 1 && zoomConstant < 0.09
                    && zoomConstant >= 0.03) {
                map.changeX(1);
            }
            if (zoomConstant != 1 && zoomConstant < 0.03) {
                map.changeX(0.2);
            }
        }
        if (e.getComponent() == view.right) {
            double zoomConstant = map.getZoomConstant();
            if (zoomConstant != 1 && zoomConstant >= 0.4) {
                map.changeX(-20);
            }
            if (zoomConstant != 1 && zoomConstant < 0.4
                    && zoomConstant >= 0.15) {
                map.changeX(-10);
            }
            if (zoomConstant != 1 && zoomConstant < 0.15
                    && zoomConstant >= 0.09) {
                map.changeX(-5);
            }
            if (zoomConstant != 1 && zoomConstant < 0.09
                    && zoomConstant >= 0.03) {
                map.changeX(-1);
            }
            if (zoomConstant != 1 && zoomConstant < 0.03) {
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
                double vectorNewReleaseX = (release.x - map.getWidth())  * zoomConstant;
                double vectorNewReleaseY = (release.y - map.getHeight()) * zoomConstant;

                release.setLocation(map.getWidth()  + vectorLastRelease.x + vectorNewReleaseX , 
                                    map.getHeight() + vectorLastRelease.y + vectorNewReleaseY);

                map.setVectorLastRelease(   vectorLastRelease.x + vectorNewReleaseX, 
                                            vectorLastRelease.y + vectorNewReleaseY);

                // ***************

                map.assignCoords(press, release, rect);
            }
        }
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
        double resizeHeight = map.getSize().height;
        double resizeWidth  = map.getSize().width;
        
        System.out.println("Height  = " + resizeHeight);
        System.out.println("Width   = " + resizeWidth);

        if (resizeHeight < height || resizeHeight > height) {
            if (resizeHeight == initHeigth && resizeWidth == initWidth) {
                map.updateResize(1.0);
            } else {
                double constant = height / resizeHeight; 
                map.updateResize(constant);
            }

            // Difference in height and difference in width
            double heightScale = resizeHeight / height;
            double widthScale = resizeWidth / width;
            
            Point2D.Double vectorLastPress = map.getVectorLastPress();
            Point2D.Double vectorLastRelease = map.getVectorLastRelease();
            
            map.setVectorLastPress( vectorLastPress.x*widthScale, 
                                    vectorLastPress.y*heightScale);
            map.setVectorLastRelease(   vectorLastRelease.x*widthScale,
                                        vectorLastRelease.y*heightScale);
            
            this.height = resizeHeight;
            this.width = resizeWidth;
            
            map.updateZoom(Math.abs(map.getRelease().y-map.getPress().y)/height);
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
