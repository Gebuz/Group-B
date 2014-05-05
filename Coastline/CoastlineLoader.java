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
    private long nodeID;
    private double x;
    private double y;
    Queue<Long> nodeRefQueue = new LinkedList<>();

    public CoastlineLoader(int startEdgeID, long startNodeID) {
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
                    long fn = nodeRefQueue.poll();
                    if (!nodeRefQueue.isEmpty()) {
                        long tn = nodeRefQueue.peek();
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

                double easting = x; //GeoConvert.toUtmX(32, x, y)[0];
                double northing = y; //GeoConvert.toUtmX(32, x, y)[1];

                processNode(new CoastlineNode(nodeID, easting, northing));

                nodeRefQueue.add(nodeID);
                nodeID++;
            }

        }


        br.close();
    }
}
