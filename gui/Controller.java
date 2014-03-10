/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author flemmingxu
 */
public class Controller implements MouseListener, MouseMotionListener, ComponentListener {
    
    private MapView view;
    private MapPanel map;
    private DataLoader loader;
    private double initHeight;
    private double initWidth;
    private int xPress, yPress;
    
    public Controller(MapView view) {
        this.view = view;
        this.map = (MapPanel) view.getMap();
        this.loader = map.getData();
        
        initHeight = map.getSize().height - 1;
        initWidth = map.getSize().width - 1;
        
        System.out.println("Initial height: " + initHeight + " Initial width: " + initWidth);
        
        view.addComponentListener(this);
        view.zoomIn.addMouseListener(this);
        view.zoomOut.addMouseListener(this);
        
        map.addMouseListener(this);
        map.addMouseMotionListener(this);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        //This could assign a new list of edges to MapPanel
        if(e.getComponent() == view.zoomIn) {
            map.zoomIn(50);
        }
        else if(e.getComponent() == view.zoomOut) {
            map.zoomOut(50);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        xPress = e.getX();
        yPress = e.getY();
        System.out.println("xPressed: " + xPress + " yPressed: " + yPress);
        //map.currentRect = new Rectangle(x, y, 0, 0);
        //map.updateDrawableRect(getWidth(), getHeight());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int xRelease = e.getX();
        int yRelease = e.getY();
        
        int rectWidth = Math.abs(yRelease - yPress);
        int rectHeight = Math.abs(xRelease - xPress);
        
        System.out.println("xReleased: " + xRelease + " yReleased: " + yRelease);
        System.out.println("Rectangle height: " + rectHeight + " Rectangle width: " + rectWidth);
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
        double resizeWidth = map.getSize().width - 1;
      
        if(resizeHeight < initHeight || resizeHeight > initHeight) { 
            double constant = initHeight/resizeHeight;
            map.updateYConstant(constant); 
            //System.out.println("Resize height: " + resizeHeight + " Resize width: " + resizeWidth);
            //System.out.println(constant);    
        }
        if(resizeWidth < initWidth || resizeWidth > initWidth) {
            double constant = initWidth/resizeWidth;
            map.updateXConstant(constant);
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
    
}
