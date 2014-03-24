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
public class MapPanel extends JPanel implements Observer {

    public final QuadTree qtBlue;
    public final QuadTree qtPink;
    public final QuadTree qtGreen;
    private Colour colour;
    public final DataLoader loader;
    
    private ArrayList<String> roadList = new ArrayList<String>();
    private boolean found = false;
    private String roadName;
    
    public final int k = 550;
    private double resizeConstant = 1, zoomConstant = 1; //oldResize = 1, oldZoom = 1;
    private double xk = 0, yk = 0; //oldXK = 0, oldYK = 0
    public final double ratio;
    public final float dash[] = {7.0f};
    
    private Rectangle rect;
    private Point2D.Double press, release, vectorLastPress, vectorLastRelease;
    
    private boolean isMap;
    private Graphics2D mapG;
    private BufferedImage map;
    
    private final Area area;
    
    private final int INIT_WIDTH = 850;
    private final int INIT_HEIGHT = 660;
    
    double pressX, pressY, releaseX, releaseY;

    public MapPanel() {
        isMap = false;
        
        area = new Area();
        
        loader = new DataLoader();
        vectorLastPress = new Point2D.Double(0.0, 0.0);
        vectorLastRelease = new Point2D.Double(0.0, 0.0);
        press = new Point2D.Double(0, 0);  
        release = new Point2D.Double(INIT_WIDTH, INIT_HEIGHT);
        setPreferredSize(new Dimension(INIT_WIDTH, INIT_HEIGHT));
        ratio = release.x / release.y;
        
        qtBlue = new QuadTree(loader.edgesBlue, loader.nodes, "0");
        qtBlue.addCoords(CoordinateBoundaries.xMin,
                CoordinateBoundaries.yMin,
                CoordinateBoundaries.xMax - CoordinateBoundaries.xMin,
                CoordinateBoundaries.yMax - CoordinateBoundaries.yMin);
        qtBlue.split(250);
        
        qtPink = new QuadTree(loader.edgesPink, loader.nodes, "0");
        qtPink.addCoords(CoordinateBoundaries.xMin,
                CoordinateBoundaries.yMin,
                CoordinateBoundaries.xMax - CoordinateBoundaries.xMin,
                CoordinateBoundaries.yMax - CoordinateBoundaries.yMin);
        qtPink.split(500);
        
        qtGreen = new QuadTree(loader.edgesGreen, loader.nodes, "0");
        qtGreen.addCoords(CoordinateBoundaries.xMin,
                CoordinateBoundaries.yMin,
                CoordinateBoundaries.xMax - CoordinateBoundaries.xMin,
                CoordinateBoundaries.yMax - CoordinateBoundaries.yMin);
        qtGreen.split(500);
        
        colour = Colour.BLUE;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
              
        Graphics2D g2 = (Graphics2D) g;
        
        NodeData fn, tn;
        int type;
        double fnX, fnY, tnX, tnY;
        
        if(isMap == false) {
            map = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            mapG = (Graphics2D) map.getGraphics();
            mapG.setColor(Color.WHITE);
            mapG.fillRect(0, 0, getWidth(), getHeight());
            
            pressX = (press.x*k) - (xk*k) + CoordinateBoundaries.xMin; 
            pressY = (press.y*k) - (yk*k) + CoordinateBoundaries.yMin; 
            
            releaseX = (release.x*k) - (xk*k) + CoordinateBoundaries.xMin; 
            releaseY = (release.y*k) - (yk*k) + CoordinateBoundaries.yMin; 

            HashSet<String> trees;
            ArrayList<EdgeData> edges2 = new ArrayList<>();
            switch(colour) {
                case BLUE:
                    trees = qtBlue.getRoadsImproved(pressX, pressY, releaseX, releaseY);
                    for(String s : trees) {
                        edges2.addAll(qtBlue.getBranch(s).getEdges());
                    }
                    break;
                case PINK:
                    trees = qtPink.getRoadsImproved(pressX, pressY, releaseX, releaseY);
                    for(String s : trees) {
                        edges2.addAll(qtPink.getBranch(s).getEdges());
                    }
                    break;
                default:
                    trees = qtGreen.getRoadsImproved(pressX, pressY, releaseX, releaseY);
                    for(String s : trees) {
                        edges2.addAll(qtGreen.getBranch(s).getEdges());
                    }
                    break;
            }   

            if(zoomConstant < 0.10){
                RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                mapG.setRenderingHints(rh);
            }
            
            for (EdgeData ed : edges2) {
                fn = loader.nodes.get(ed.FNODE);
                tn = loader.nodes.get(ed.TNODE);
                type = ed.TYP;

                fnX = (((fn.getX() - CoordinateBoundaries.xMin) / k) + xk);
                fnY = (((fn.getY() - CoordinateBoundaries.yMin) / k) + yk); 
                tnX = (((tn.getX() - CoordinateBoundaries.xMin) / k) + xk);
                tnY = (((tn.getY() - CoordinateBoundaries.yMin) / k) + yk);

                //if these coordinates lies within the specified rectangle's bounds) 
//                if ((release.x > press.x &&  release.y > press.y)
//                    && (fnX < release.x + widthDiff && tnX < release.x + widthDiff)
//                    && (fnX > press.x && tnX > press.x)
//                    && (fnY > press.y && tnY > press.y)
//                    && (fnY < release.y + heightDiff && tnY < release.y + heightDiff)) {
                drawSpecified(fnX, fnY, tnX, tnY, type, ed, mapG);
//                }
            }
            roadList.removeAll(roadList);
            //System.out.println(edges2.size());
        }
        g2.drawImage(map, 0, 0, null);
        mapG.dispose();
        if (rect != null) {
            area.add(new Area(new Rectangle2D.Float(0, 0, getWidth(), getHeight())));
            g2.setColor(Color.BLACK.brighter());
            area.subtract(new Area(rect));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2.fill(area);
            drawRect(rect, g2);
        }
    }

    public void drawLine(Graphics2D g, Line2D line, Color color, float width) {
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        g.draw(line);
    }
    
    public void drawRect(Rectangle r, Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, dash, 0.0f));
        g2.drawRect(rect.x, rect.y, rect.width, rect.height);
    }
    
    public void assignCoords(Point2D.Double press, Point2D.Double release) {
        this.press = press;
        this.release = release;
        
        isMap = false;
      
        updateZoom(Math.abs(release.y - press.y) / getHeight());
        repaint();
    }

    private void drawSpecified(double fnX, double fnY, double tnX, double tnY, int type, EdgeData edge, Graphics2D g2) {
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
                drawLine(g2, line, Color.PINK, 1);
                break;
            case 1:
            case 31:
            case 41:
                drawLine(g2, line, Color.RED, 1);
                break;
            case 2:
            case 32:
                drawLine(g2, line, Color.GRAY, 1);
                break;
            case 8:
            case 48:
                drawLine(g2, line, Color.GREEN, 1);
                break;
            default:
                drawLine(g2, line, Color.BLUE, 1);
                break;
        }
        switch (type) {
            case 5:
            case 6:
                drawRoadNames(fnX, tnX, fnY, tnY, 0.0025, 12, edge, g2);
                break;
            case 1:
                drawRoadNames(fnX, tnX, fnY, tnY, 0.05, 16, edge, g2);
                break;
            case 4:
                drawRoadNames(fnX, tnX, fnY, tnY, 0.03, 14, edge, g2);
        }
        isMap = true;
    }
    
    public void drawRoadNames(double fnX, double tnX, double fnY, double tnY, double zoomLimit, int font, EdgeData edge, Graphics2D g2) {
        if(zoomConstant < zoomLimit) { 
            roadName = edge.VEJNAVN;
            for(int i = 0; i < roadList.size(); i++) {
                if(roadName.equals(roadList.get(i))) {
                    found = true;
                    break;
                }
            }
            if(found == false) {
                roadList.add(roadName);
                double xMid = (fnX + tnX)/2.0;
                double yMid = (fnY + tnY)/2.0;
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("TimesRoman", Font.PLAIN, font));
                g2.drawString(roadName,(float) xMid,(float) yMid);
            }
        }
        found = false;
    }
    
    public void defaultMap() {
        press = new Point2D.Double(0, 0);
        release = new Point2D.Double(850, 660);
        zoomConstant = 1;
        colour = colour.BLUE;
        resizeConstant = 1;
        yk = 0;
        xk = 0;
        setPreferredSize(new Dimension(850, 660));
        setVectorLastPress(0.0, 0.0);
        setVectorLastRelease(0.0, 0.0);
        isMap = false;
        repaint();
    }

    public void updateResize(double j) {
        resizeConstant = j;
        isMap = false;
        repaint();
    }

    public void updateZoom(double j) {
        zoomConstant = j;
        colour = colour.getColour(zoomConstant);
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
 
        setVectorLastPress(vectorLastPress.x + deltaX * factor,
                vectorLastPress.y + deltaY * factor);
        setVectorLastRelease(vectorLastRelease.x - deltaX * factor,
                vectorLastRelease.y - deltaY * factor);
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
 
        setVectorLastPress(vectorLastPress.x - deltaX * factor,
                vectorLastPress.y - deltaY * factor);
        setVectorLastRelease(vectorLastRelease.x + deltaX * factor,
                vectorLastRelease.y + deltaY * factor);
    }

    public Point2D.Double getPress() {
        return press;
    }

    public Point2D.Double getRelease() {
        return release;
    }

    public Point2D.Double getVectorLastPress() {
        return vectorLastPress;
    }

    public Point2D.Double getVectorLastRelease() {
        return vectorLastRelease;
    }

    public void setVectorLastPress(double x, double y) {
        this.vectorLastPress.setLocation(x, y);
        //System.out.println("LPrV = " + vectorLastPress);
    }

    public void setVectorLastRelease(double x, double y) {
        this.vectorLastRelease.setLocation(x, y);
        //System.out.println("LReV = " + vectorLastRelease);
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
    
    public void assignRect(Rectangle r) {
        rect = new Rectangle(r.x, r.y, r.width, r.height);
        repaint();
    }
    
    public void removeRect() {
        rect = null;
    }
    
    public String getRoadName(double x, double y) {        
        x = pressX + (releaseX - pressX)*x/INIT_WIDTH;
        y = pressY + (releaseY - pressY)*y/INIT_HEIGHT;
        EdgeData edge;
        switch(colour) {
            case BLUE:
                edge = qtBlue.getClosestRoad(x, y);
                break;
            case PINK:
                edge = qtPink.getClosestRoad(x, y);
                break;
            default:
                edge = qtGreen.getClosestRoad(x, y);
                break;
        }
        return edge.VEJNAVN;
    }

    @Override
    public void update(Observable o, Object arg) {
    } 

    public double getResizeConstant() {
        return resizeConstant;
    }
}
