package gui;

import GuiDrawLines.QuadTree;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
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
    private double resizeConstant = 1;
    private double zoomConstant = 1;
    private double xk = 0;
    private double yk = 0;
    final double ratio;
    private Rectangle rect;
    private Point2D.Double press, release;
    private Point2D.Double vectorLastPress;
    private Point2D.Double vectorLastRelease;

    //Rectangle rectToDraw = null;
    //Rectangle previousRectDrawn = new Rectangle();
    public MapPanel() {
        loader = new DataLoader();
        edges = loader.edges;
        
        press = new Point2D.Double(0, 0); //new Point2D.Double(100, 100); 
        release = new Point2D.Double(850, 660); //new Point2D.Double(386.36363637, 300);
        
        // initial vectors are (0,0)
        vectorLastPress = new Point2D.Double(0.0, 0.0);
        vectorLastRelease = new Point2D.Double(0.0, 0.0);
        
        //updateZoom((release.y-press.y)/660.0); //IT WORKS!!
        setPreferredSize(new Dimension(850, 660));
        ratio = release.x/release.y;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        NodeData fn;
        NodeData tn;
        int type;
        double fnX, fnY, tnX, tnY;
        
        System.out.println("Draw within points:");
        System.out.println("Press:    "+press.x + ", " + press.y);
        System.out.println("Release:  "+release.x + ", " + release.y);
        System.out.println("Zoom Cnst " + zoomConstant);
        System.out.println("Resi Cnst " + resizeConstant);
        System.out.println("Ratio     " + ratio);
        System.out.println("K         " + k);
        System.out.println("");
            
        for (EdgeData ed : edges) {
            fn = loader.nodes.get(ed.FNODE);
            tn = loader.nodes.get(ed.TNODE);
            type = ed.TYP;

            fnX = (((fn.getX() - CoordinateBoundaries.xMin) / k) + xk);///constant;
            fnY = (((CoordinateBoundaries.yMax - fn.getY()) / k) + yk);///constant;
            tnX = (((tn.getX() - CoordinateBoundaries.xMin) / k) + xk);///constant;
            tnY = (((CoordinateBoundaries.yMax - tn.getY()) / k) + yk);///constant;

            //if these coordinates lies within the specified rectangle's bounds)
            if ((release.x > press.x && release.y > press.y)
                    && (fnX < release.x && tnX < release.x)
                    && (fnX > press.x && tnX > press.x)
                    && (fnY > press.y && tnY > press.y)
                    && (fnY < release.y && tnY < release.y)) {
                drawSpecified(fnX, fnY, tnX, tnY, type, g2D);
            }
        }
    }

    public void drawLine(Graphics2D g, Line2D line, Color color, float width) {
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        g.draw(line);
    }


    public void assignCoords(Point2D.Double press, Point2D.Double release, Rectangle r) {
        this.press = press;
        this.release = release;

        rect = r;

        updateZoom(Math.abs(release.y - press.y) / getHeight());
        repaint();
    }

    private void drawSpecified(double fnX, double fnY, double tnX, double tnY, int type, Graphics2D g2D) {
        //at this point, only the relevant coordinates (those within the rectangle) are accessed.
        fnX = fnX - press.x;
        fnY = fnY - press.y;
        tnX = tnX - press.x;
        tnY = tnY - press.y;

        fnX /= zoomConstant;
        fnY /= zoomConstant;
        tnX /= zoomConstant;
        tnY /= zoomConstant;

        fnX /= resizeConstant;
        fnY /= resizeConstant;
        tnX /= resizeConstant;
        tnY /= resizeConstant;

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

    public void updateResize(double j) {
        resizeConstant = j;
        repaint();
    }
    
    public void updateZoom(double j) {
        zoomConstant = j;
        repaint();
    }
    
    public void defaultMap() {
        press = new Point2D.Double(0, 0);
        release = new Point2D.Double(850, 660);
        zoomConstant = 1;
        resizeConstant = 1;
        yk = 0;
        xk = 0;
        k = 550;
        
        setVectorLastPress(0.0, 0.0);
        setVectorLastRelease(0.0, 0.0);
        
        setPreferredSize(new Dimension(850, 660));

        repaint();
    }



    /**
     * Center zoom in by a certain factor.
     * @param factor How much to zoom in each time. The closer the number is to
     * zero, the slower the zoom will be.
     */
    public void zoomIn(double factor) {
        double deltaX = (release.x-press.x);
        double deltaY = (release.y-press.y);
        press.setLocation(press.x+deltaX*factor, press.y+deltaY*factor);
        release.setLocation(release.x-deltaX*factor, release.y-deltaY*factor);
        
        assignCoords(press, release, rect);
        
        setVectorLastPress(vectorLastPress.x + press.x*zoomConstant*factor, 
                           vectorLastPress.y + press.y*zoomConstant*factor);
        setVectorLastRelease(vectorLastRelease.x - release.x * zoomConstant*factor,
                             vectorLastRelease.y - release.y * zoomConstant*factor);
    }

    /**
     * Center zoom out by a certain factor.
     * @param factor How much to zoom out each time. The closer the number is to
     * zero, the slower the zoom will be.
     */
    public void zoomOut(double factor) {
        double deltaX = (release.x-press.x);
        double deltaY = (release.y-press.y);
        press.setLocation(press.x-deltaX*factor, press.y-deltaY*factor);
        release.setLocation(release.x+deltaX*factor, release.y+deltaY*factor);
        
        assignCoords(press, release, rect);
        
        setVectorLastPress(vectorLastPress.x - press.x*zoomConstant*factor, 
                           vectorLastPress.y - press.y*zoomConstant*factor);
        setVectorLastRelease(vectorLastRelease.x + release.x * zoomConstant*factor,
                             vectorLastRelease.y + release.y * zoomConstant*factor);
    }

    public Point2D.Double getPress() {
        return press;
    }

    public Point2D.Double getRelease() {
        return release;
    }

    public double getZoomConstant() {
        return zoomConstant;
    }

    public Point2D.Double getVectorLastPress() {
        return vectorLastPress;
    }

    public Point2D.Double getVectorLastRelease() {
        return vectorLastRelease;
    }

    public void setVectorLastPress(double x, double y) {
        this.vectorLastPress.setLocation(x, y);
        System.out.println("LPrV = " + vectorLastPress);
    }

    public void setVectorLastRelease(double x, double y) {
        this.vectorLastRelease.setLocation(x, y);
        System.out.println("LReV = " + vectorLastRelease);
    }
    
    public void changeX(double j) {
        xk += j;
        repaint();
    }

    public void changeY(double j) {
        yk += j;
        repaint();
    }
    
    public DataLoader getData() {
        return loader;
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
