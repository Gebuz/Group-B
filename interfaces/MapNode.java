package interfaces;

/**
 * This interface is meant to represent a Node. Each node should contain a set
 * of coordinates (x, y) and an ID.
 *
 * @author Sjúrður í Sandagerði
 */
public interface MapNode {

    /**
     * Each node should contain a unique ID and have this method return that ID.
     *
     * @return Returns a unique id for this node.
     */
    long getID();

    /**
     *
     * @return Returns the X coordinate of this node.
     */
    double getX();

    /**
     *
     * @return Returns the Y coordinate of this node.
     */
    double getY();

    /**
     * Overwrite the Y coordinate with the value newY. This method is used if
     * there is a need to edit the coordinates, by e.g. mirroring the
     * coordinates around the x-axis.
     *
     * @param newY New value for the Y-coordinate.
     */
    void setY(double newY);

    /**
     * Overwrite the X coordinate with the value newX. This method is used if
     * there is a need to edit the coordinates, by e.g. mirroring the
     * coordinates around the y-axis.
     *
     * @param newX New value for the X-coordinate.
     */
    void setX(double newX);
}
