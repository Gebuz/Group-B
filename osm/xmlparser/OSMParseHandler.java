package osm.xmlparser;

import Model.CoordinateBoundaries;
import Model.Projection;
import java.util.LinkedList;
import java.util.Queue;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class parses an OpenStreetMaps XML data file containing Way and Node
 * elements.
 *
 * @author Sjúrður í Sandagerði
 */
public abstract class OSMParseHandler extends DefaultHandler {

    public abstract void processNode(OSMNodeData nd);

    public abstract void processEdge(OSMEdgeData ed);
    
//    ArrayList<OSMEdgeData> osmEdges = new ArrayList<>();
    boolean inWay = false; // Are we inside a Way element or not.
    boolean inNode = false; // Are we inside a Node element or not.
    boolean boundsFound = false;
    Queue<Long> nodeRefQueue = new LinkedList<>();
    Projection p; // Maybe do projection later.
    // Temporary fields for each Way element.
    private int typ = 0;
    private int vejnr = 0;
    private String vejnavn = "";

    // Temporary fields for each Node element go here:
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        if (!boundsFound && localName.equalsIgnoreCase("bounds")) {
            getBounds(attributes);
            boundsFound = true;
        } else if (localName.equalsIgnoreCase("node")) {
            inNode = true;
            OSMNodeData node = getNodeInformation(attributes);
            if (node != null) {
                processNode(node);
            }

        } else if (localName.equalsIgnoreCase("way")) {
            inWay = true;

            String id = attributes.getValue("id");
            if (id != null) {
                vejnr = Integer.parseInt(id);
            }
        }

        if (inWay) {
            if (localName.equalsIgnoreCase("nd")) {
                String ref = attributes.getValue("ref");
                if (ref != null) {
                    nodeRefQueue.add(Long.parseLong(ref));
                }
            } else if (localName.equalsIgnoreCase("tag")) {
                String k = attributes.getValue("k");
                if (k != null) {
                    if (k.equals("highway")) {
                        String v = attributes.getValue("v");
                        int type = OSMHighwayTypeIndicator.getType(v);
                        if (type > 0) {
                            typ = type;
                        }
                    } else if (k.equals("name")) {
                        String v = attributes.getValue("v");
                        if (v != null) {
                            vejnavn = v;
                        }
                    }
                }
            }
        } else if (inNode) {
            // Insert code to parse the children of a Node element here.
        }


    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (localName.equalsIgnoreCase("way")) {
            inWay = false;
            createEdges();
            nodeRefQueue.clear();
            resetVariables();
        }
        
        else if (localName.equalsIgnoreCase("node")) {
            inNode = false;
        }
    }

    private void resetVariables() {
        typ = 0;
        vejnr = 0;
        vejnavn = "";
    }

    private OSMNodeData getNodeInformation(Attributes attributes) {
        String idStr = attributes.getValue("id");
        String latStr = attributes.getValue("lat");
        String lonStr = attributes.getValue("lon");
        // Avoid null pointer reference!
        if (idStr != null && latStr != null && lonStr != null) {
            long id = Long.parseLong(idStr);
            double lat = Double.parseDouble(latStr);
            double lon = Double.parseDouble(lonStr);
            
            return new OSMNodeData(id, CoordinateBoundaries.yMax - p.mercatorY(lat)
                    + CoordinateBoundaries.yMin,
                    p.mercatorX(lon));
        } else {
            return null;
        }
    }

    /**
     * Get the bounds from the XML element &lt;bounds&gt; and store it in the
     * CoordinateBoundaries class.
     *
     * @param attributes
     */
    private void getBounds(Attributes attributes) {
        String minLat = attributes.getValue("minlat");
        String maxLat = attributes.getValue("maxlat");
        String minLon = attributes.getValue("minlon");
        String maxLon = attributes.getValue("maxlon");
        if (minLat != null) {
            CoordinateBoundaries.setxMin(Double.parseDouble(minLon));
        }
        if (maxLat != null) {
            CoordinateBoundaries.setxMax(Double.parseDouble(maxLon));
        }
        if (minLon != null) {
            CoordinateBoundaries.setyMin(Double.parseDouble(minLat));
        }
        if (maxLon != null) {
            CoordinateBoundaries.setyMax(Double.parseDouble(maxLat));
        }

        p = new Projection(CoordinateBoundaries.yMin,
                CoordinateBoundaries.yMax, CoordinateBoundaries.xMin,
                CoordinateBoundaries.xMax);

        CoordinateBoundaries.setxMin(p.mercatorX(CoordinateBoundaries.xMin));
        CoordinateBoundaries.setxMax(p.mercatorX(CoordinateBoundaries.xMax));
        CoordinateBoundaries.setyMin(p.mercatorY(CoordinateBoundaries.yMin));
        CoordinateBoundaries.setyMax(p.mercatorY(CoordinateBoundaries.yMax));
    }

    private void createEdges() {
        while (!nodeRefQueue.isEmpty()) {
            long fn = nodeRefQueue.poll();
            if (!nodeRefQueue.isEmpty()) {
                long tn = nodeRefQueue.peek();
                OSMEdgeData edge = new OSMEdgeData(fn, tn, typ, vejnr, vejnavn);
                processEdge(edge);
            }
        }
    }
    
}