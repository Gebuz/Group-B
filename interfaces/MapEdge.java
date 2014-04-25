
package interfaces;

/**
 * 
 * @author Sjúrður í Sandagerði
 */
public interface MapEdge {

    /**
     * 
     * @return Returns the From-node.
     */
    long getFNode();

    /**
     *
     * @return Returns the To-node.
     */
    long getTNode();

    /**
     *
     * @return Returns the length of this edge, that is the distance between the 
     * from-node and the to-node.
     */
    double getLength();

    /**
     * 
     * @return Returns an integer representing the type of this Edge.
     */
    int getType();
    
    /**
     *
     * @return Returns the name of this edge. If this edge represents a road
     * then this would be the name of that given road.
     */
    String getName();

    /**
     *
     * @return Returns the unique ID for this edge.
     */
    int getID();
    
    /**
     * This method should return the value saying whether this edge is a one_way
     * edge, or both. A none-specified value should be assigned for those edges
     * where this method does not apply.
     * 
     * @return 
     */
    String getOneWay();
    
    /**
     * This method should return the maximum speed for this edge. A non-specified 
     * value should be assigned for those edges where maximum speed is not 
     * applicable.
     * @return Returns the maximum speed.
     */
    int getMaxSpeed();
}
