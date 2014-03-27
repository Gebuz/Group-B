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
    
    private HashMap<Integer, EdgeData> roadMap;
    private HashSet<Integer> roadListHashSet = new HashSet<>();
    private boolean roadOn = true;
    
    public final int k = 550;
    private double resizeConstant = 1, zoomConstant = 1;
    private double xk = 0, yk = 0;
    public final double ratio;
    public final float dash[] = {7.0f};
    
    private Rectangle rect;
    private Point2D.Double press, release, vectorLastPress, vectorLastRelease;
        
    private boolean isMap = false;
    private Graphics2D mapG;
    private BufferedImage map;
    
    private final Area area;
    
    public final int INIT_WIDTH = 850;
    public final int INIT_HEIGHT = 660;
    
    double pressX, pressY, releaseX, releaseY;

    public MapPanel() {  
        area = new Area();
        
        loader = new DataLoader();
        vectorLastPress = new Point2D.Double(0.0, 0.0);
        vectorLastRelease = new Point2D.Double(0.0, 0.0);
        press = new Point2D.Double(0, 0);  
        release = new Point2D.Double(INIT_WIDTH, INIT_HEIGHT);
        setPreferredSize(new Dimension(INIT_WIDTH, INIT_HEIGHT));
        ratio = release.x / release.y;
        
        roadMap = loader.roadMap;
        
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
            
            double widthDiff  = (getWidth() * resizeConstant - INIT_WIDTH)  * zoomConstant;
            double heightDiff = (getHeight()* resizeConstant - INIT_HEIGHT) * zoomConstant;
            
            pressX = (press.x*k) - (xk*k) + CoordinateBoundaries.xMin; 
            pressY = (press.y*k) - (yk*k) + CoordinateBoundaries.yMin; 
            
            releaseX = (release.x*k) - (xk*k) + widthDiff*k + CoordinateBoundaries.xMin; 
            releaseY = (release.y*k) - (yk*k) + heightDiff*k + CoordinateBoundaries.yMin;  

            // If press or release is outside the boundaries for the quadtrees
            // set press or release to the boundaries so not all quadtrees are
            // returned.
            if (pressX   < CoordinateBoundaries.xMin)   pressX = CoordinateBoundaries.xMin;
            if (pressY   < CoordinateBoundaries.yMin)   pressY = CoordinateBoundaries.yMin;
            if (releaseX > CoordinateBoundaries.xMax) releaseX = CoordinateBoundaries.xMax;
            if (releaseY > CoordinateBoundaries.yMax) releaseY = CoordinateBoundaries.yMax;
            
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

                drawSpecified(fnX, fnY, tnX, tnY, type, ed, mapG);
            }
            roadListHashSet.clear();

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
            case 42:
                drawLine(g2, line, Color.MAGENTA, 1);
                break;
            case 8:
            case 48:
                drawLine(g2, line, Color.GREEN, 1);
                break;
            case 11:
                drawLine(g2, line, Color.GRAY, 1);
                break;
            case 3:
            case 4:
                drawLine(g2, line, Color.BLUE, 1);
                break;
            case 80:
                drawLine(g2, line, Color.ORANGE, 1);
                break;
            default:
                drawLine(g2, line, Color.BLACK, 1);
                break;
        }
        if(roadOn) {
            switch (type) {
                case 5:
                case 6:
                    drawRoadNames(fnX, tnX, fnY, tnY, 0.0025, 11, edge, g2);
                    break;
                case 1:
                    drawRoadNames(fnX, tnX, fnY, tnY, 0.05, 14, edge, g2);
                    break;
                case 4:
                    drawRoadNames(fnX, tnX, fnY, tnY, 0.03, 12, edge, g2);
            }
        }
        isMap = true;
    }
    
    public void drawLine(Graphics2D g2, Line2D line, Color color, float width) {
        g2.setColor(color);
        if(color == Color.ORANGE) 
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, dash, 0.0f));
        else 
            g2.setStroke(new BasicStroke(width));
        g2.draw(line);
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
      
        updateZoom(Math.abs(release.y - press.y) / INIT_HEIGHT);
        repaint();
    }


    /**
     * Source: Stack Overflow forum
     * @see http://stackoverflow.com/a/14376141
     */
    public BufferedImage createStringImage(Graphics g, String s) {
        int w = g.getFontMetrics().stringWidth(s) + 5;
        int h = g.getFontMetrics().getHeight();

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageGraphics = image.createGraphics();
        imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        imageGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        imageGraphics.setColor(Color.BLACK);
        imageGraphics.setFont(g.getFont());
        imageGraphics.drawString(s, 0, h - g.getFontMetrics().getDescent());
        imageGraphics.dispose();

        return image;
    }
    
    /**
     * Source: Stack Overflow forum
     * @see http://stackoverflow.com/a/14376141
     */
    private void drawString(Graphics2D g, String s, double tx, double ty, double theta) { //double rotx, double roty) {
        AffineTransform aff = AffineTransform.getRotateInstance(theta, tx, ty);
        aff.translate(tx, ty);

        Graphics2D g2D = ((Graphics2D) g);
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        BufferedImage stringImage = createStringImage(g, s);
        aff.translate(-stringImage.getWidth()/2, 0); // Move image so it is centered around tx, ty
        g2D.drawImage(stringImage, aff, this);
    }
    
    /** 
     * Source: Wiki Code
     * @see http://wikicode.wikidot.com/get-angle-of-line-between-two-points
     * 
     * Determines the angle of a straight line drawn between point one and two. 
     * The number returned, which is a double in degrees, tells us how much we have to 
     * rotate a horizontal line clockwise for it to match the line between the two points. 
     * If you prefer to deal with angles using radians instead of degrees, 
     * just change the last line to: "return Math.atan2(yDiff, xDiff);" 
     */ 
    private static double GetAngleOfLineBetweenTwoPoints(double x1, double y1, double x2, double y2) { 
        double xDiff = x2 - x1; 
        double yDiff = y2 - y1; 
        return Math.toDegrees(Math.atan2(yDiff, xDiff)); 
    }
    
    public void drawRoadNames(double fnX, double tnX, double fnY, double tnY, double zoomLimit, int fontSize, EdgeData edge, Graphics2D g2) {
                                    // Ignore roadnames that are the empty string.    
        if(zoomConstant < zoomLimit && !edge.VEJNAVN.equals("")) {
            boolean found;
            int roadNum = edge.VEJNR;
            String roadName = edge.VEJNAVN;
            double roadLength = edge.LENGTH;
 
            found = roadListHashSet.contains(roadNum);

            double xMid = (fnX + tnX)/2.0;
            double yMid = (fnY + tnY)/2.0;
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Helvetica", Font.PLAIN, fontSize));

            double degrees = GetAngleOfLineBetweenTwoPoints(fnX, fnY, tnX, tnY);

            // Flip the angle 180 degrees if the string would be written 
            // upside down.
            if      (degrees < -90) degrees += 180;
            else if (degrees >  90) degrees -= 180;

            double radians = Math.toRadians(degrees);

            if(xMid < getWidth() && xMid > 0 && yMid < getHeight() && yMid > 0) {
                // Using epsilon precision instead of == when checking equality
                // of two doubles.
                double epsilon = 1E-5;
                if(!found && roadLength + epsilon > roadMap.get(roadNum).LENGTH 
                          && roadLength - epsilon < roadMap.get(roadNum).LENGTH) 
                {
                    roadListHashSet.add(roadNum);
                    drawString(g2, roadName, xMid, yMid, radians);
                }
                else if(!found && roadLength < roadMap.get(roadNum).LENGTH) {
                    roadListHashSet.add(roadNum);
                    drawString(g2, roadName, xMid, yMid, radians);
                }
            }
        }
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
        double deltaX = (release.x - press.x) * factor;
        double deltaY = (release.y - press.y) * factor;
        press.setLocation(press.x + deltaX, press.y + deltaY);
        release.setLocation(release.x - deltaX, release.y - deltaY);
 
        assignCoords(press, release);
 
        setVectorLastPress(vectorLastPress.x + deltaX,
                vectorLastPress.y + deltaY);
        setVectorLastRelease(vectorLastRelease.x - deltaX,
                vectorLastRelease.y - deltaY);
    }

    /**
     * Center zoom out by a certain factor.
     *
     * @param factor How much to zoom out each time. The closer the number is to
     * zero, the slower the zoom will be.
     */
    public void zoomOut(double factor) {
        isMap = false;
        double deltaX = (release.x - press.x) * factor;
        double deltaY = (release.y - press.y) * factor;
        press.setLocation(press.x - deltaX, press.y - deltaY);
        release.setLocation(release.x + deltaX, release.y + deltaY);
 
        assignCoords(press, release);
 
        setVectorLastPress(vectorLastPress.x - deltaX,
                vectorLastPress.y - deltaY);
        setVectorLastRelease(vectorLastRelease.x + deltaX,
                vectorLastRelease.y + deltaY);
    }
    
    public void zoomInRelativeToMouse(double factor, double mousePosX, double mousePosY) { 
        
        double relativeFactor = factor * zoomConstant;
        
        double pressPositionX = (press.x - 0) * relativeFactor;
        double pressPositionY = (press.y - 0) * relativeFactor;
        double releasePositionX = (release.x - INIT_WIDTH)  * relativeFactor;
        double releasePositionY = (release.y - INIT_HEIGHT) * relativeFactor;
        
        // vector from press to mousePosition
        double pcX = (mousePosX - press.x)* relativeFactor;
        double pcY = (mousePosY - press.y)* relativeFactor;
        
        
        // vector from release to mousePosition
        double rcX = (mousePosX - release.x)* relativeFactor;
        double rcY = (mousePosY - release.y)* relativeFactor;

        double moveVectorX = pcX + rcX + (pressPositionX + releasePositionX);
        double moveVectorY = pcY + rcY + (pressPositionY + releasePositionY);
                
        zoomIn(factor);
        changeX(-moveVectorX);
        changeY(-moveVectorY);
    }
   
    public void zoomOutRelativeToMouse(double factor, double mousePosX, double mousePosY) {
        
        double relativeFactor = factor * zoomConstant;
        
        double pressPositionX = (press.x - 0) * relativeFactor;
        double pressPositionY = (press.y - 0) * relativeFactor;
        double releasePositionX = (release.x - INIT_WIDTH)  * relativeFactor;
        double releasePositionY = (release.y - INIT_HEIGHT) * relativeFactor;
        // vector from press to mousePosition
        double pcX = (mousePosX - press.x)* relativeFactor;
        double pcY = (mousePosY - press.y)* relativeFactor;
        
        // vector from release to mousePosition
        double rcX = (mousePosX - release.x)* relativeFactor;
        double rcY = (mousePosY - release.y)* relativeFactor;

        double moveVectorX = pcX + rcX + (pressPositionX + releasePositionX);
        double moveVectorY = pcY + rcY + (pressPositionY + releasePositionY);
        
        zoomOut(factor);
        changeX(moveVectorX);
        changeY(moveVectorY);
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
    }

    public void setVectorLastRelease(double x, double y) {
        this.vectorLastRelease.setLocation(x, y);
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
    
    public void roadSwitch() {
        if(roadOn) roadOn = false;
        else roadOn = true;
        isMap = false;
        repaint();
    }
    
    public String getRoadName(double x, double y) {
        double widthDiff  = (getWidth()  * resizeConstant - INIT_WIDTH)  * zoomConstant;
        double heightDiff = (getHeight() * resizeConstant - INIT_HEIGHT) * zoomConstant;
        
        x = pressX +  ((releaseX-widthDiff*k) - pressX)*x/INIT_WIDTH;
        y = pressY +  ((releaseY-heightDiff*k) - pressY)*y/INIT_HEIGHT;
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
