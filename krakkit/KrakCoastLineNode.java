
package krakkit;

import interfaces.MapNode;

/**
 *
 * @author Sjúrður í Sandagerði
 */
public class KrakCoastLineNode implements MapNode{

    private long ID;
    private double X;
    private double Y;

    public KrakCoastLineNode(long ID, double X, double Y) {
        this.ID = ID;
        this.X = X;
        this.Y = Y;
    }
    
    @Override
    public long getID() {
        return ID;
    }

    @Override
    public double getX() {
        return X;
    }

    @Override
    public double getY() {
        return Y;
    }

    @Override
    public void setY(double newY) {
        this.Y = newY;
    }
    
}
