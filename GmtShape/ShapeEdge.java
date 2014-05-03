/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GmtShape;

import interfaces.MapEdge;

/**
 *
 * @author Sjúrður í Sandagerði
 */
public class ShapeEdge implements MapEdge {
    private int ID;
    private long FNODE;
    private long TNODE;
    private double LENGTH = 0;
    private int TYPE = 200;
    private String NAME;
    private String ONEWAY = "";
    private int MAX_SPEED = 0;

    public ShapeEdge(int ID, long FNODE, long TNODE, String NAME) {
        this.ID = ID;
        this.FNODE = FNODE;
        this.TNODE = TNODE;
        this.NAME = NAME;
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
        return ONEWAY;
    }

    @Override
    public int getMaxSpeed() {
        return MAX_SPEED;
    }
    
}
