
package Coastline;

import interfaces.MapNode;

/**
 *
 * @author Sjúrður í Sandagerði
 */
public class CoastlineNode implements MapNode{

    private int ID;
    private double X;
    private double Y;

    public CoastlineNode(int ID, double X, double Y) {
        this.ID = ID;
        this.X = X;
        this.Y = Y;
    }
    
    @Override
    public int getID() {
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
    
    @Override
    public void setX(double newX) {
        this.X = newX;
    }
    
}
