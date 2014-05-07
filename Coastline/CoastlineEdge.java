
package Coastline;

import interfaces.MapEdge;

/**
 *
 * @author Sjúrður í Sandagerði
 */
public class CoastlineEdge implements MapEdge {
    private int ID;
    private int FNODE;
    private int TNODE;
    private double LENGTH = 0;
    private int TYPE = 100;
    private String NAME = "";
    private String ONEWAY = "";
    private int MAX_SPEED = 0;

    public CoastlineEdge(int ID, int FNODE, int TNODE) {
        this.ID = ID;
        this.FNODE = FNODE;
        this.TNODE = TNODE;
    }
    
    @Override
    public int getFNode() {
        return FNODE;
    }

    @Override
    public int getTNode() {
        return TNODE;
    }

    @Override
    public double getLength() {
        return LENGTH;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getOneWay() {
        return ONEWAY;
    }

    @Override
    public int getMaxSpeed() {
        return MAX_SPEED;
    }
    
}
