/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package osm.xmlparser;


public class OSMEdgeDataBuilder {
    private long FNODE;
    private long TNODE;
    private int TYP;
    private int VEJNR;
    private String VEJNAVN;

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

    public OSMEdgeDataBuilder setTYP(int TYP) {
        this.TYP = TYP;
        return this;
    }

    public OSMEdgeDataBuilder setVEJNR(int VEJNR) {
        this.VEJNR = VEJNR;
        return this;
    }

    public OSMEdgeDataBuilder setVEJNAVN(String VEJNAVN) {
        this.VEJNAVN = VEJNAVN;
        return this;
    }

    public OSMEdgeData createOSMEdgeData() {
        return new OSMEdgeData(FNODE, TNODE, TYP, VEJNR, VEJNAVN);
    }
    
}
