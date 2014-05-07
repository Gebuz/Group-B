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
    private int FNODE;
    private int TNODE;
    private double LENGTH = 0;
    private int TYPE = 200;
    private String NAME;
    private String ONEWAY = "";
    private int MAX_SPEED = 0;

    public ShapeEdge(int ID, int FNODE, int TNODE, String NAME) {
        this.ID = ID;
        this.FNODE = FNODE;
        this.TNODE = TNODE;
        this.NAME = NAME;
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
