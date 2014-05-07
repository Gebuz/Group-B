package GmtShape;

import Model.Projection;
import java.util.logging.Level;
import java.util.logging.Logger;
import krakkit.GeoConvert;

/**
 *
 * @author Sjúrður í Sandagerði
 */
public class GMTShape {

    private int ID;
    private String NAME;
    private String TYPE;
    private Double[] xPoly, yPoly;

    public GMTShape(int ID, String NAME, String TYPE, Double[] xPoly, Double[] yPoly) {
        this.ID = ID;
        this.NAME = NAME;
        this.TYPE = TYPE;
        this.xPoly = xPoly;
        this.yPoly = yPoly;
    }

    /**
     * Apply Mercator projection to each coordinate in this shape.
     * @param p Mercator projection object.
     */
    public void applyMercatorProjection(Projection p) {
        for (int i = 0; i < xPoly.length; i++) {
            double x = xPoly[i];
            double y = yPoly[i];
            xPoly[i] = p.mercatorX(x);
            yPoly[i] = p.mercatorY(y);
        }
    }
    
    /**
     * Apply UTM32 projection to each coordinate in this shape.
     * This method uses a modified version of GeoConvert.toUTM's method to fit
     * with the overlapping of more than one UTM zone.
     * 
     * @param zone The UTM zone to convert to.
     */
    public void applyUTMProjection(int zone) {
        for (int i = 0; i < xPoly.length; i++) {
            try {
                double x = xPoly[i];
                double y = yPoly[i];
                double[] xy = GeoConvert.toUtmX(zone, x, y);
                xPoly[i] = xy[0];
                yPoly[i] = xy[1];
            } catch (Exception ex) {
                Logger.getLogger(GMTShape.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void mirrorY(double yMin, double yMax) {
        for (int i = 0; i < yPoly.length; i++) {
            double y = yPoly[i];
            yPoly[i] = yMax - y + yMin;
        }
    }

    /**
     * Get the center point of all points in this shape.
     * @return Returns the center point {x, y}.
     */
    public double[] centroid() {
        double[] centroid = new double[]{0, 0};
        
        int numOfPoints = xPoly.length;
        for (int i = 0; i < numOfPoints; i++) {
            double x = xPoly[i];
            double y = yPoly[i];
            centroid[0] += x;
            centroid[1] += y;
        }
        centroid[0] /= numOfPoints;
        centroid[1] /= numOfPoints;
        
        return centroid;
    }

    public int getID() {
        return ID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getTYPE() {
        return TYPE;
    }

    public Double[] getxPoly() {
        return xPoly;
    }

    public Double[] getyPoly() {
        return yPoly;
    }

    @Override
    public String toString() {
        return (NAME + " - " + TYPE + " " + xPoly.length);
    }
}
