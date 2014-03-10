package krakkit;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The coordinate supremum of a HashMap&lt;Key, NodeData&gt; or a 
 * Collection&lt;NodeData&gt;.
 * 
 * @author Sjúrður í Sandagerði
 */
public class CoordinateBoundaries
{
    public static double yMax = Double.MIN_VALUE;
    public static double yMin = Double.MAX_VALUE;
    public static double xMax = Double.MIN_VALUE;
    public static double xMin = Double.MAX_VALUE;
    
    private CoordinateBoundaries() {
    }
    
    /**
     * Find the coordinate supremum of a Map&lt;NodeData&gt;.
     * @param nodes Map&lt;Integer, NodeData&gt;
     */
    public static void findBoundaries(Map<Integer, NodeData> nodes) {
        Iterator<Map.Entry<Integer, NodeData>> it = nodes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, NodeData> e = it.next();
            NodeData nd = e.getValue();
            double x = nd.getX();
            double y = nd.getY();
            setXSupremum(x);
            setYSupremum(y);
        }
    }
    
    /**
     * Find the coordinate supremum of a List&lt;NodeData&gt;.
     * @param nodes List of NodeData.
     */
    public static void findBoundaries(List<NodeData> nodes) {
        for (NodeData nd : nodes) {
            double x = nd.getX();
            double y = nd.getY();
            setXSupremum(x);
            setYSupremum(y);
        }
    }
    
    private static void setXSupremum(double x) {
        if (x > xMax) xMax = x;
        if (x < xMin) xMin = x;
    }
    
    private static void setYSupremum(double y) {
      if (y > yMax) yMax = y;
      if (y < yMin) yMin = y;
    }
    
}
