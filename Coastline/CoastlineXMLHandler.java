package Coastline;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import krakkit.GeoConvert;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class parses an OpenStreetMaps XML data file containing Way and Node
 * elements.
 *
 * @author Sjúrður í Sandagerði
 */
public abstract class CoastlineXMLHandler extends DefaultHandler {

    public abstract void processNode(CoastlineNode nd);

    public abstract void processEdge(CoastlineEdge ed);

    boolean inWay = false; // Are we inside a Way element or not.
    boolean inNode = false; // Are we inside a Node element or not.
    Queue<Integer> nodeRefQueue = new LinkedList<>();
    private int edgeID = 0;

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        if (localName.equalsIgnoreCase("node")) {
            inNode = true;
            CoastlineNode node = getNodeInformation(attributes);
            if (node != null) {
                processNode(node);
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
                        int parseInt = Integer.parseInt(ref);
                        nodeRefQueue.add(parseInt);
                    } catch (NumberFormatException ex) {
                        Logger.getLogger(CoastlineXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
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
            int id = Integer.parseInt(idStr);

            try {
                double lat = Double.parseDouble(latStr);
                double lon = Double.parseDouble(lonStr);

                double easting = GeoConvert.toUtmX(32, lon, lat)[0];
                double northing = GeoConvert.toUtmX(32, lon, lat)[1];

                return new CoastlineNode(id, easting, northing);
            } catch (NumberFormatException ex) {
                Logger.getLogger(CoastlineXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CoastlineXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    private void createEdges() {
        while (!nodeRefQueue.isEmpty()) {
            int fn = nodeRefQueue.poll();
            if (!nodeRefQueue.isEmpty()) {
                int tn = nodeRefQueue.peek();
                processEdge(new CoastlineEdge(edgeID, fn, tn));
            }
        }
    }
}
