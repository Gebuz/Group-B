package Model;

import interfaces.MapNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The coordinate supremum of a HashMap&lt;Key, KrakNodeData&gt; or a 
 * Collection&lt;KrakNodeData&gt;.
 * 
 * @author Sjúrður í Sandagerði
 */
public class CoordinateBoundaries
{
    public static double yMax = Double.MIN_VALUE;
    public static double yMin = Double.MAX_VALUE;
    public static double xMax = Double.MIN_VALUE;
    public static double xMin = Double.MAX_VALUE;
    
    /**
     * Private constructor so the class cannot be instantiated.
     * 
     */
    private CoordinateBoundaries() {}
    
    /**
     * Find the coordinate supremum of a Map&lt;KrakNodeData&gt;.
     * @param nodes Map&lt;Integer, KrakNodeData&gt;
     */
    public static void findBoundaries(HashMap<Long, MapNode> nodes) {
        Iterator<Map.Entry<Long, MapNode>> it = nodes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, MapNode> e = it.next();
            MapNode nd = e.getValue();
            double x = nd.getX();
            double y = nd.getY();
            setXSupremum(x);
            setYSupremum(y);
        }
    }
    
    /**
     * Find the coordinate supremum of a List&lt;KrakNodeData&gt;.
     * @param nodes List of KrakNodeData.
     */
    public static void findBoundaries(List<MapNode> nodes) {
        for (MapNode nd : nodes) {
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

    public static void setyMax(double yMax) {
        CoordinateBoundaries.yMax = yMax;
    }

    public static void setyMin(double yMin) {
        CoordinateBoundaries.yMin = yMin;
    }

    public static void setxMax(double xMax) {
        CoordinateBoundaries.xMax = xMax;
    }

    public static void setxMin(double xMin) {
        CoordinateBoundaries.xMin = xMin;
    }
    
}
