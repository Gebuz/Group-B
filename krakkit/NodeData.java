package krakkit;

/**
 * An object storing the raw node data from the krak data file.
 */
public class NodeData {

    final int ARC;
    public final int KDV;
    final long KDV_ID;
    private double X_COORD;
    private double Y_COORD;

    /**
     * Parses node data from line, throws an IOException if something unexpected
     * is read
     *
     * @param line The source line from which the NodeData fields are parsed
     */
    public NodeData(String line) {
        DataLine dl = new DataLine(line);
        ARC = dl.getInt();
        KDV = dl.getInt();
        KDV_ID = (long) dl.getInt();
        X_COORD = dl.getDouble();
        Y_COORD = dl.getDouble();
    }
    
    public NodeData(double x, double y, long id) {
        X_COORD = x;
        Y_COORD = y;
        KDV_ID = id;
        ARC = 0;
        KDV = 0;
    }

    /**
     * Returns a string representing the node data in the same format as used in
     * the kdv_node_unload.txt file.
     */
    public String toString() {
        return ARC + "," + KDV + "," + KDV_ID + "," + X_COORD + "," + Y_COORD;
    }

    public double getX() {
        return X_COORD;
    }

    public double getY() {
        return Y_COORD;
    }

    public void setX(double X_COORD) {
        this.X_COORD = X_COORD;
    }
    
    public void setY(double Y_COORD) {
        this.Y_COORD = Y_COORD;
    }
}
