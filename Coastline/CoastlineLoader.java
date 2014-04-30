package Coastline;

import interfaces.MapEdge;
import interfaces.MapNode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.me.jstott.jcoord.UTMRef;

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

    public static void main(String[] args) throws IOException, Exception {
        final ArrayList<MapEdge> edges = new ArrayList<>();
        final HashMap<Long, MapNode> nodes = new HashMap<>();

        CoastlineLoader cl = new CoastlineLoader(1000000, 3000000) {
            @Override
            public void processNode(CoastlineNode nd) {
                nodes.put(nd.getID(), nd);
            }

            @Override
            public void processEdge(CoastlineEdge ed) {
                edges.add(ed);
            }
        };

        cl.load("data/CoastLine 1 to 2.000.000.dat");

        System.out.println(nodes.size());
        System.out.println(edges.size());

        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;

        for (MapNode node : nodes.values()) {
            if (node.getX() < xmin) {
                xmin = node.getX();
            }
            if (node.getX() > xmax) {
                xmax = node.getX();
            }
            if (node.getY() < ymin) {
                ymin = node.getY();
            }
            if (node.getY() > ymax) {
                ymax = node.getY();
            }
        }

        System.out.println("xmin = " + xmin);
        System.out.println("xmax = " + xmax);
        System.out.println("ymin = " + ymin);
        System.out.println("ymax = " + ymax);


        UTMRef utmNewRef = new UTMRef(32, 'V', 442254.35659, 6049914.43018);
        System.out.println(utmNewRef.toLatLng().getLatitude());
        System.out.println(utmNewRef.toLatLng().getLongitude());
        UTMRef utmNewRef2 = new UTMRef(33, 'U', 892658.21706, 6402050.98297);
        System.out.println(utmNewRef2.toLatLng().getLatitude());
        System.out.println(utmNewRef2.toLatLng().getLongitude());
    }
}
