package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.*;
import krakkit.CoordinateBoundaries;
import krakkit.EdgeData;
import krakkit.NodeData;
import GuiDrawLines.QuadTree;

/**
 *
 * @author flemmingxu
 */
//Our view
//Our model should return lists of edges to be drawn.
public class MapPanel extends JPanel implements Observer {

    private QuadTree qt;
    private DataLoader loader;
    
    private ArrayList<EdgeData> edges; //to be replaced by model through controller depending on how much data is needed.
    private HashMap<Integer, NodeData> nodes;
    
    private int k = 550;
    private double resizeConstant = 1, zoomConstant = 1;
    private double xk = 0, yk = 0;
    final double ratio;
    final float dash[] = {7.0f};
    
    private Rectangle rect;
    private Point2D.Double press, release, vectorLastPress, vectorLastRelease;
    
    private boolean isMap;
    private Graphics2D mapG;
    private BufferedImage map;

    public MapPanel() {
        isMap = false;
        
        loader = new DataLoader();
        edges = loader.edges;
        nodes = loader.nodes;
        vectorLastPress = new Point2D.Double(0.0, 0.0);
        vectorLastRelease = new Point2D.Double(0.0, 0.0);
        press = new Point2D.Double(0, 0);  
        release = new Point2D.Double(850, 660); 
        setPreferredSize(new Dimension(850, 660));
        ratio = release.x / release.y;
        
        qt = new QuadTree(edges, nodes, "0");
        qt.addCoords(CoordinateBoundaries.xMin,
                CoordinateBoundaries.yMin,
                CoordinateBoundaries.xMax - CoordinateBoundaries.xMin,
                CoordinateBoundaries.yMax - CoordinateBoundaries.yMin);
        qt.split();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
              
        Graphics2D g2 = (Graphics2D) g;
        
//        double pressX = (press.x*resizeConstant*zoomConstant*k) + xk + CoordinateBoundaries.xMin;
//        double pressY = (press.y*resizeConstant*zoomConstant*k) + yk + CoordinateBoundaries.yMin;
//        
//        double releaseX2 = CoordinateBoundaries.xMax; //(press.x*resizeConstant*zoomConstant*k) + xk;
//        double releaseY2 = CoordinateBoundaries.yMax;//(press.y*resizeConstant*zoomConstant*k) + yk;
//        
//        double pressX2 = CoordinateBoundaries.xMin;
//        double pressY2 = CoordinateBoundaries.yMin;
//        
//        double releaseX = (release.x*resizeConstant*zoomConstant*k) + xk + CoordinateBoundaries.xMin;
//        double releaseY = (release.y*resizeConstant*zoomConstant*k) + yk + CoordinateBoundaries.yMin;
        
//        System.out.println("Press X true: " + pressX2 + " Press Y true: " + pressY2);
//        System.out.println("Press X: " + pressX + " Press Y: " + pressY);
//        System.out.println("Release X true: " + releaseX2 + " Release Y true: " + releaseY2);
//        System.out.println("Release X: " + releaseX + " Release Y: " + releaseY);
        
//        ArrayList<EdgeData> edges2 = qt.getRoadsImproved(pressX, pressY, releaseX, releaseY);
        
//        ArrayList<EdgeData> edges2 = root.getRoadsImproved(CoordinateBoundaries.xMin+200000,
//                CoordinateBoundaries.yMin+200000,
//                CoordinateBoundaries.xMax-200000,
//                CoordinateBoundaries.yMax-200000);
        
        
        NodeData fn, tn;
        int type;
        double fnX, fnY, tnX, tnY;
        
        if(isMap == false) {
            map = new BufferedImage(850, 660, BufferedImage.TYPE_INT_RGB);
            mapG = (Graphics2D) map.getGraphics();
            mapG.setColor(Color.WHITE);
            mapG.fillRect(0, 0, 850, 660);
            
            if(zoomConstant < 0.03){
                RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                mapG.setRenderingHints(rh);
            }
            
            for (EdgeData ed : edges) {
                fn = loader.nodes.get(ed.FNODE);
                tn = loader.nodes.get(ed.TNODE);
                type = ed.TYP;

                fnX = (((fn.getX() - CoordinateBoundaries.xMin) / k) + xk);
                fnY = (((fn.getY() - CoordinateBoundaries.yMin) / k) + yk); 
                tnX = (((tn.getX() - CoordinateBoundaries.xMin) / k) + xk);
                tnY = (((tn.getY() - CoordinateBoundaries.yMin) / k) + yk);

                //if these coordinates lies within the specified rectangle's bounds) 
                if ((release.x > press.x && release.y > press.y)
                        && (fnX < release.x && tnX < release.x)
                        && (fnX > press.x && tnX > press.x)
                        && (fnY > press.y && tnY > press.y)
                        && (fnY < release.y && tnY < release.y)) {
                    drawSpecified(fnX, fnY, tnX, tnY, type, mapG);
                }
            }
        }
        g2.drawImage(map, 0, 0, null);
        mapG.dispose();
        if (rect != null) {
            drawRect(rect, g2);
        }
    }

    public void drawLine(Graphics2D g, Line2D line, Color color, float width) {
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        g.draw(line);
    }

    public void assignRect(Rectangle r) {
        rect = new Rectangle(r.x, r.y, r.width, r.height);
        repaint();
    }
    
    public void drawRect(Rectangle r, Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, dash, 0.0f));
        g2.drawRect(rect.x, rect.y, rect.width, rect.height);
    }
    
    public void assignCoords(Point2D.Double press, Point2D.Double release) {
        this.press = press;
        this.release = release;
        
        System.out.println("pressX: " + press.x + " pressY: " + press.y);
        System.out.println("releaseX: " + release.x + " releaseY: " + release.y);
        
        isMap = false;
        
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
        isMap = true;
    }

    public void updateResize(double j) {
        resizeConstant = j;
        isMap = false;
        repaint();
    }

    public void updateZoom(double j) {
//        oldZoom = zoomConstant;
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
        setPreferredSize(new Dimension(850, 660));
        setVectorLastPress(0.0, 0.0);
        setVectorLastRelease(0.0, 0.0);
        isMap = false;
        repaint();
    }

    /**
     * Center zoom in by a certain factor.
     *
     * @param factor How much to zoom in each time. The closer the number is to
     * zero, the slower the zoom will be.
     */
    public void zoomIn(double factor) {
        isMap = false;
        double deltaX = (release.x - press.x);
        double deltaY = (release.y - press.y);
        press.setLocation(press.x + deltaX * factor, press.y + deltaY * factor);
        release.setLocation(release.x - deltaX * factor, release.y - deltaY * factor);

        assignCoords(press, release);

        setVectorLastPress(vectorLastPress.x + press.x * zoomConstant * factor,
                vectorLastPress.y + press.y * zoomConstant * factor);
        setVectorLastRelease(vectorLastRelease.x - release.x * zoomConstant * factor,
                vectorLastRelease.y - release.y * zoomConstant * factor);
    }

    /**
     * Center zoom out by a certain factor.
     *
     * @param factor How much to zoom out each time. The closer the number is to
     * zero, the slower the zoom will be.
     */
    public void zoomOut(double factor) {
        isMap = false;
        double deltaX = (release.x - press.x);
        double deltaY = (release.y - press.y);
        press.setLocation(press.x - deltaX * factor, press.y - deltaY * factor);
        release.setLocation(release.x + deltaX * factor, release.y + deltaY * factor);

        assignCoords(press, release);

        setVectorLastPress(vectorLastPress.x - press.x * zoomConstant * factor,
                vectorLastPress.y - press.y * zoomConstant * factor);
        setVectorLastRelease(vectorLastRelease.x + release.x * zoomConstant * factor,
                vectorLastRelease.y + release.y * zoomConstant * factor);
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
        isMap = false;
        xk += j;
        repaint();
    }

    public void changeY(double j) {
        isMap = false;
        yk += j;
        repaint();
    }

    public DataLoader getData() {
        return loader;
    }

    public double getZoom() {
        return zoomConstant;
    }
    
    public void removeRect() {
        rect = null;
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
