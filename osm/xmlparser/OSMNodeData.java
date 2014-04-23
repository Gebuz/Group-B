
package osm.xmlparser;

import interfaces.MapNode;

/**
 * Open Street Maps Node Data class.
 * 
 * @author Sjúrður í Sandagerði
 */
public class OSMNodeData implements MapNode {

    private long    id;    // Unique id
    private double lat;    // Latitude
    private double lon;    // Longitude

    public OSMNodeData(long id, double lat, double lon) {
        this.id  = id;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public double getX() {
        return lon;
    }

    @Override
    public double getY() {
        return lat;        
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setY(double newLat) {
        this.lat = newLat;
    }
}
