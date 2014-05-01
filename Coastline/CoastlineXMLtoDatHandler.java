package Coastline;

import interfaces.MapEdge;
import interfaces.MapNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class parses an OpenStreetMaps XML data file containing Way and Node
 * elements.
 *
 * @author Sjúrður í Sandagerði
 */
public class CoastlineXMLtoDatHandler extends DefaultHandler {

    static HashMap<Long, MapNode> nodes = new HashMap<>();
    
    boolean inWay = false; // Are we inside a Way element or not.
    boolean inNode = false; // Are we inside a Node element or not.
    
    // Borders
    double MIN_LON = 7.85;
    double MAX_LON = 15.48582;
    double MIN_LAT = 54.37361;
    double MAX_LAT = 57.82074;
    
    // Current way nodeRef queue
    Queue<Long> nodeRefQueue = new LinkedList<>();
    
    // Keep all Way nodeRef Queues in this list
    static ArrayList<Queue<MapEdge>> queues = new ArrayList<>();
    
    private int edgeID = 0;

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        if (localName.equalsIgnoreCase("node")) {
            inNode = true;
            CoastlineNode node = getNodeInformation(attributes);
            if (node != null) {
//                nodes.put(node.getID(), node);
                if (!outsideBorder(node.getY(), node.getX())) {
                    nodes.put(node.getID(), node);
                }
            }

        } else if (localName.equalsIgnoreCase("way")) {
            inWay = true;

            String id = attributes.getValue("id");
            if (id != null) {
                edgeID = Integer.parseInt(id);
            }
        }

        if (inWay) {
            if (localName.equalsIgnoreCase("nd")) {
                String ref = attributes.getValue("ref");
                if (ref != null) {
                    try {
                        long parseLong = Long.parseLong(ref);
                        nodeRefQueue.add(parseLong);
                    } catch (NumberFormatException ex) {
                        Logger.getLogger(Coastline.CoastlineXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (localName.equalsIgnoreCase("way")) {
            inWay = false;
            createEdges();
            nodeRefQueue.clear();

        } else if (localName.equalsIgnoreCase("node")) {
            inNode = false;
        }
    }

    private CoastlineNode getNodeInformation(Attributes attributes) {
        String idStr = attributes.getValue("id");
        String latStr = attributes.getValue("lat");
        String lonStr = attributes.getValue("lon");
        // Avoid null pointer reference!
        if (idStr != null && latStr != null && lonStr != null) {
            long id = Long.parseLong(idStr);

            try {
                double lat = Double.parseDouble(latStr);
                double lon = Double.parseDouble(lonStr);

                return new CoastlineNode(id, lon, lat);

            } catch (NumberFormatException ex) {
                Logger.getLogger(Coastline.CoastlineXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Coastline.CoastlineXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    private void createEdges() {
        Queue<MapEdge> tempQueue = new LinkedList<>();

        while (!nodeRefQueue.isEmpty()) {
            long fn = nodeRefQueue.poll();
            if (!nodeRefQueue.isEmpty()) {
                long tn = nodeRefQueue.peek();
                tempQueue.add(new CoastlineEdge(edgeID, fn, tn));
            }
        }

        queues.add(tempQueue);
    }

    /**
     * Is the current latitude longitude point outside of the borders.
     * @param lat Latitude
     * @param lon Longitude
     * @return Returns true if the point is outside of the borders.
     */
    private boolean outsideBorder(double lat, double lon) {
        return (Double.compare(lat, MAX_LAT) > 0 ||
                Double.compare(lat, MIN_LAT) < 0 ||
                Double.compare(lon, MAX_LON) > 0 ||
                Double.compare(lon, MIN_LON) < 0);
    }
}