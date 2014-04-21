/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package osm.xmlparser;

import krakkit.EdgeData;

/**
 *
 * @author Sjurdur
 */
public class OSMEdgeData {
    public long FNODE;
    public long TNODE;
    public double LENGTH = 0.0;
    public int TYP;
    public String VEJNAVN;
    public int SPEED = 0;
    public String ONE_WAY = "";
    public int VEJNR; // Maybe use Way ID attribute.

    public OSMEdgeData(long FNODE, long TNODE, int TYP, int VEJNR, String VEJNAVN) {
        this.FNODE = FNODE;
        this.TNODE = TNODE;
        this.TYP = TYP;
        this.VEJNR = VEJNR;
        this.VEJNAVN = VEJNAVN;
    }
    
    @Override
    public String toString(){
        return ("FN = " + FNODE + 
                ", TN = " + TNODE +
                ", TYPE = " + TYP +
                ", VEJNR = " + VEJNR);
    }
    
    public EdgeData toED(){
        String s = FNODE + " " + TNODE + " " + LENGTH + " " + TYP + " " + VEJNAVN
                + " " + SPEED + " " + ONE_WAY + " " + VEJNR;
        return new EdgeData(s, 0);
    }
}
