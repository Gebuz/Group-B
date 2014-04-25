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
    private int MAXSPEED = 0;
    private String ONE_WAY = "";
    private int ID; // Maybe use Way ID attribute.

    public OSMEdgeData(long FNODE, long TNODE, int TYPE, int ID, String NAME, int MAXSPEED, String ONE_WAY) {
        this.FNODE = FNODE;
        this.TNODE = TNODE;
        this.TYPE = TYPE;
        this.ID = ID;
        this.NAME = NAME;
        this.MAXSPEED = MAXSPEED;
        this.ONE_WAY = ONE_WAY;


        setSpeedFromRoadType();
    }

    @Override
    public String toString() {
        return ("FN = " + FNODE
                + ", TN = " + TNODE
                + ", TYPE = " + TYPE
                + ", ID = " + ID);
    }

    /**
     *
     * Incase the MAX SPEED has not been found in the OSM Data file then set the
     * speed according to the type of road.
     */
    private void setSpeedFromRoadType() {
        if (MAXSPEED == 0) {
            switch (TYPE) {
                case 1:
                    MAXSPEED = 130;
                    break;
                case 2:
                    MAXSPEED = 80;
                    break;
                case 3:
                    MAXSPEED = 50;
                    break;
                case 4:
                    MAXSPEED = 50;
                    break;
                case 6:
                    MAXSPEED = 50;
                    break;
                case 31:
                    MAXSPEED = 80;
                    break;
                case 32:
                    MAXSPEED = 50;
                    break;
                case 33:
                    MAXSPEED = 50;
                    break;
                case 34:
                    MAXSPEED = 50;
                    break;
                case 35:
                    MAXSPEED = 50;
                    break;
                case 10:
                    MAXSPEED = 30;
                    break;
                case 11:
                    MAXSPEED = 15;
                    break;
                case 8:
                    MAXSPEED = 6;
                    break;
                default:
                    MAXSPEED = 0;
                    break;
            }
        }
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

    @Override
    public int getMaxSpeed() {
        return MAXSPEED;
    }
}
