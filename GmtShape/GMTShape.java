package GmtShape;

import Model.Projection;

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

    public void applyMercatorProjection(Projection p) {
        for (int i = 0; i < xPoly.length; i++) {
            double x = xPoly[i];
            double y = yPoly[i];
            xPoly[i] = p.mercatorX(x);
            yPoly[i] = p.mercatorY(y);
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
