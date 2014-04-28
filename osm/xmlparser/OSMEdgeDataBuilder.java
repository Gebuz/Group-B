/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package osm.xmlparser;


public class OSMEdgeDataBuilder {
    private long FNODE;
    private long TNODE;
    private int TYPE;
    private int ID;
    private String NAME;
    private int MAXSPEED;
    private String ONE_WAY;

    public OSMEdgeDataBuilder() {
    }

    public OSMEdgeDataBuilder setFNODE(long FNODE) {
        this.FNODE = FNODE;
        return this;
    }

    public OSMEdgeDataBuilder setTNODE(long TNODE) {
        this.TNODE = TNODE;
        return this;
    }

    public OSMEdgeDataBuilder setTYPE(int TYPE) {
        this.TYPE = TYPE;
        return this;
    }

    public OSMEdgeDataBuilder setID(int ID) {
        this.ID = ID;
        return this;
    }

    public OSMEdgeDataBuilder setNAME(String NAME) {
        this.NAME = NAME;
        return this;
    }

    public OSMEdgeDataBuilder setMAXSPEED(int MAXSPEED) {
        this.MAXSPEED = MAXSPEED;
        return this;
    }

    public OSMEdgeDataBuilder setONE_WAY(String ONE_WAY) {
        this.ONE_WAY = ONE_WAY;
        return this;
    }

    public OSMEdgeData createOSMEdgeData() {
        return new OSMEdgeData(FNODE, TNODE, TYPE, ID, NAME, MAXSPEED, ONE_WAY);
    }
    
}
