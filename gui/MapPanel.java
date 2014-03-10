package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import krakkit.CoordinateBoundaries;
import krakkit.EdgeData;
import krakkit.NodeData;

/**
 *
 * @author flemmingxu
 */

//Our view
//Our model should return lists of edges to be drawn.
public class MapPanel extends JPanel implements Observer {
    private DataLoader loader;
    private ArrayList<EdgeData> edges; //to be replaced by model through controller depending on how much data is needed.
    private int k = 550; 
    private double yk = 1;
    private double xk = 1;
    
    
    //Rectangle currentRect = null;
    //Rectangle rectToDraw = null;
    //Rectangle previousRectDrawn = new Rectangle();
    
    public MapPanel() {
        loader = new DataLoader();
        edges = loader.edges;
        setPreferredSize(new Dimension(850, 660)); //y = 645 before, 660
        System.out.println(CoordinateBoundaries.yMax-CoordinateBoundaries.yMin);
        System.out.println((CoordinateBoundaries.yMax-CoordinateBoundaries.yMin)/k);
        System.out.println(((CoordinateBoundaries.yMax-CoordinateBoundaries.yMin)/k)/1.32);
        System.out.println(((CoordinateBoundaries.yMax-CoordinateBoundaries.yMin)/k)/0.857);
        System.out.println((CoordinateBoundaries.yMax-CoordinateBoundaries.yMin)/(k+150));
    }
    
    @Override
    public void paintComponent(Graphics g) {
      //int k = 550;
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        
        //if(currentRect != null) {
        //g.setXORMode(Color.white); //Color of line varies
                                           //depending on image colors
        //g.drawRect(rectToDraw.x, rectToDraw.y, rectToDraw.width - 1, rectToDraw.height - 1);
        //}
        
        //for (int i = 1; i < 812301; i++) {
        for(int i = 0; i < edges.size(); i++) {
            EdgeData ed = edges.get(i);
            NodeData fn = loader.nodes.get(ed.FNODE);
            NodeData tn = loader.nodes.get(ed.TNODE);
            int type = ed.TYP;

            double fnX = ((fn.X_COORD - CoordinateBoundaries.xMin) / k)/xk;
            double fnY = ((CoordinateBoundaries.yMax - fn.Y_COORD) / k)/yk;
            double tnX = ((tn.X_COORD - CoordinateBoundaries.xMin) / k)/xk;
            double tnY = ((CoordinateBoundaries.yMax - tn.Y_COORD) / k)/yk;

            Line2D line = new Line2D.Double(fnX, fnY, tnX, tnY);
            switch (type) {
                case 5:
                case 6:
                    drawLine(g2D, line, Color.PINK, 1);
                    break;
                case 1:
                case 31:
                case 41:
                    drawLine(g2D, line, Color.RED, 1);
                    break;
                case 2:
                case 32:
                    drawLine(g2D, line, Color.GRAY, 1);
                    break;
                case 8:
                case 48:
                    drawLine(g2D, line, Color.GREEN, 1);
                    break;
                default:
                    drawLine(g2D, line, Color.BLUE, 1);
                    break;
            }
        }
    }

    public void drawLine(Graphics2D g, Line2D line, Color color, float width) {
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        g.draw(line);
    }
    
    public void updateYConstant(double j) {
        yk = j;
        repaint();
    }
    
    public void updateXConstant(double j) {
        xk = j;
        repaint();
    }

    public void zoomIn(int j) {
        k -= j;
        repaint();
    }
    
    public void zoomOut(int j) {
        k += j;
        repaint();
    }
    
    public DataLoader getData() {
        return loader;
    }
    
//    private void updateDrawableRect(int compWidth, int compHeight) {
//            int x = currentRect.x;
//            int y = currentRect.y;
//            int width = currentRect.width;
//            int height = currentRect.height;
//    
//            //Make the width and height positive, if necessary.
//            if (width < 0) {
//                width = 0 - width;
//                x = x - width + 1; 
//                if (x < 0) {
//                    width += x; 
//                    x = 0;
//                }
//            }
//            if (height < 0) {
//                height = 0 - height;
//                y = y - height + 1; 
//                if (y < 0) {
//                    height += y; 
//                    y = 0;
//                }
//            }
//    
//            //The rectangle shouldn't extend past the drawing area.
//            if ((x + width) > compWidth) {
//                width = compWidth - x;
//            }
//            if ((y + height) > compHeight) {
//                height = compHeight - y;
//            }
//          
//            //Update rectToDraw after saving old value.
//            if (rectToDraw != null) {
//                previousRectDrawn.setBounds(
//                            rectToDraw.x, rectToDraw.y, 
//                            rectToDraw.width, rectToDraw.height);
//                rectToDraw.setBounds(x, y, width, height);
//            } else {
//                rectToDraw = new Rectangle(x, y, width, height);
//            }
//        }
    
    @Override
    public void update(Observable o, Object arg) {   
    }
}
