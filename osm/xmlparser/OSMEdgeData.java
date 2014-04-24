/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package osm.xmlparser;

import interfaces.MapEdge;

/**
 *
 * @author Sjúrður í Sandagerði
 */
public class OSMEdgeData implements MapEdge {
    private long FNODE;
    private long TNODE;
    private double LENGTH = 0.0;
    private int TYPE;
    private String NAME;
    private int SPEED = 0;
    private String ONE_WAY = "";
    private int ID; // Maybe use Way ID attribute.

    public OSMEdgeData(long FNODE, long TNODE, int TYPE, int ID, String NAME) {
        this.FNODE = FNODE;
        this.TNODE = TNODE;
        this.TYPE = TYPE;
        this.ID = ID;
        this.NAME = NAME;
    }
    
    @Override
    public String toString(){
        return ("FN = " + FNODE + 
                ", TN = " + TNODE +
                ", TYPE = " + TYPE +
                ", ID = " + ID);
    }

    @Override
    public long getFNode() {
        return FNODE;
    }

    @Override
    public long getTNode() {
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
        return ONE_WAY;
    }
}
