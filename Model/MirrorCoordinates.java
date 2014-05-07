package Model;

import interfaces.MapNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import krakkit.KrakNodeData;
 

/**
 * Mirror the coordinates of each KrakNodeData in a 
 * HashMap&lt;Integer, {@link KrakNodeData}&gt; based on the maximum coordinate 
 * found in the HashMap.
 * @author Sjúrður í Sandagerði
 */
public class MirrorCoordinates {
    
    /**
     * Mirror the Y coordinates based on the largest Y value found in the 
     * HashMap.
     * @param nodes HashMap whose KrakNodeData's y-coordinate to mirror.
     */
    public static void MirrorY(HashMap<Integer, MapNode> nodes) {
        
        // Just to make sure that the boundaries have been found.
        CoordinateBoundaries.findBoundaries(nodes);
        
        Iterator<Map.Entry<Integer, MapNode>> it = nodes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, MapNode> e = it.next();
            MapNode nd = e.getValue();
            nd.setY(CoordinateBoundaries.yMax - nd.getY() + 
                    CoordinateBoundaries.yMin);
        }

    }
}
