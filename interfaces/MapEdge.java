
package interfaces;

/**
 *
 * @author Sjúrður í Sandagerði
 */
public interface MapEdge {

    /**
     *
     * @return
     */
    long getFNode();

    /**
     *
     * @return
     */
    long getTNode();

    /**
     *
     * @return
     */
    double getLength();

    /**
     * 
     * @return Returns an integer representing the type of this Edge.
     */
    int getType();
    
    /**
     *
     * @return
     */
    String getName();

    /**
     *
     * @return
     */
    int getID();
}
