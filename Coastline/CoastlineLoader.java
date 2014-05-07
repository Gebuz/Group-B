package Coastline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sjúrður í Sandagerði
 */
public abstract class CoastlineLoader {

    private int edgeID;
    private int nodeID;
    private double x;
    private double y;
    Queue<Integer> nodeRefQueue = new LinkedList<>();

    /**
     * Create a new CoastlineLoader object.
     * 
     * @param startEdgeID Starting value for edge IDs
     * @param startNodeID Starting value for node IDs
     */
    public CoastlineLoader(int startEdgeID, int startNodeID) {
        this.edgeID = startEdgeID;
        this.nodeID = startNodeID;
    }

    public abstract void processNode(CoastlineNode nd);

    public abstract void processEdge(CoastlineEdge ed);

    public void load(String coastLineFile) throws IOException, Exception {
        BufferedReader br;
        br = new BufferedReader(new FileReader(coastLineFile));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains(">")) {
                while (!nodeRefQueue.isEmpty()) {
                    int fn = nodeRefQueue.poll();
                    if (!nodeRefQueue.isEmpty()) {
                        int tn = nodeRefQueue.peek();
                        processEdge(new CoastlineEdge(edgeID, fn, tn));
                        edgeID++;
                    }
                }
                nodeRefQueue.clear();
                continue;
            } else {
                String[] lineSplit = line.split("\\s+");
                try {
                    x = Double.parseDouble(lineSplit[0]);
                    y = Double.parseDouble(lineSplit[1]);
                } catch (NumberFormatException ex) {
                    Logger.getLogger(CoastlineLoader.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }

                double easting = x;
                double northing = y;

                processNode(new CoastlineNode(nodeID, easting, northing));

                nodeRefQueue.add(nodeID);
                nodeID++;
            }

        }


        br.close();
    }
}
