package mapofdenmark.krakkit;

/**
 * An object storing the raw node data from the krak data file.
 */
public class NodeData {
	final int ARC;
	public final int KDV;
	final int KDV_ID;
	public final double X_COORD;
	public final double Y_COORD;

	/**
	 * Parses node data from line, throws an IOException
	 * if something unexpected is read
	 * @param line The source line from which the NodeData fields are parsed
	 */
	public NodeData(String line) {
		DataLine dl = new DataLine(line);
                double MIN_X = 442254.35659;
                double MAX_Y = 6402050.98297;
		ARC = dl.getInt();
		KDV = dl.getInt();
		KDV_ID = dl.getInt();
		X_COORD = dl.getDouble() - MIN_X;
		Y_COORD = MAX_Y - dl.getDouble();
	}

	/**
	 * Returns a string representing the node data in the same format as used in the
	 * kdv_node_unload.txt file.
	 */
	public String toString() {
		return ARC + "," + KDV + "," + KDV_ID + "," + X_COORD + "," + Y_COORD;
	}
}
