
package Coastline;

import interfaces.MapNode;

/**
 *
 * @author Sjúrður í Sandagerði
 */
public class CoastlineNode implements MapNode{

    private long ID;
    private double X;
    private double Y;

    public CoastlineNode(long ID, double X, double Y) {
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

    public void setY(double newY) {
        this.Y = newY;
    }
    
    public void setX(double newX) {
        this.X = newX;
    }
    
}
