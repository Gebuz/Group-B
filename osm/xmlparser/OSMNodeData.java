
package osm.xmlparser;

import krakkit.NodeData;
import krakkit.Projection;

/**
 * Open Street Maps Node Data class.
 * 
 * @author Sjúrður í Sandagerði
 */
public class OSMNodeData{

    public final long    id;    // Unique id
    public final double lat;    // Latitude
    public final double lon;    // Longitude

    public OSMNodeData(long id, double lat, double lon) {
        this.id  = id;
        this.lat = lat;
        this.lon = lon;
    }

    public double getX() {
        return lon;
    }

    public double getY() {
        return lat;        
    }
    
    public NodeData toND(Projection pro){
        return new NodeData(pro.mercatorX(lon), pro.mercatorY(lat), id);
    }
}
