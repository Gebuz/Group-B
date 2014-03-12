package krakkit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
 

/**
 * Mirror the coordinates of each NodeData in a 
 * HashMap&lt;Integer, {@link NodeData}&gt; based on the maximum coordinate 
 * found in the HashMap.
 * @author Sjúrður í Sandagerði
 */
public class MirrorCoordinates {
    
    /**
     * Mirror the Y coordinates based on the largest Y value found in the 
     * HashMap.
     * @param nodes HashMap whose NodeData's y-coordinate to mirrir.
     */
    public static void MirrorY(HashMap<Integer, NodeData> nodes) {
        
        // Just to make sure that the boundaries have been found.
        CoordinateBoundaries.findBoundaries(nodes);
        
        Iterator<Map.Entry<Integer, NodeData>> it = nodes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, NodeData> e = it.next();
            NodeData nd = e.getValue();
            nd.setY(CoordinateBoundaries.yMax - nd.getY() + 
                    CoordinateBoundaries.yMin);
        }

    }
}
