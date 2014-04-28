package krakkit;

import osm.xmlparser.*;
import java.util.LinkedList;
import java.util.Queue;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

/**
 * This class parses an OpenStreetMaps XML data file containing Way and Node
 * elements.
 *
 * @author Sjúrður í Sandagerði
 */
public abstract class CoastLineHandler extends DefaultHandler {

    public abstract void processNode(OSMNodeData nd);

    public abstract void processEdge(OSMEdgeData ed);
    
//    ArrayList<OSMEdgeData> osmEdges = new ArrayList<>();
    boolean inWay = false; // Are we inside a Way element or not.
    boolean inNode = false; // Are we inside a Node element or not.

    Queue<Long> nodeRefQueue = new LinkedList<>();
    
    // Temporary fields for each Way element.
    private int typ = 0;
    private int vejnr = 0;
    private int maxspeed = 0;
    private String oneway = "";
    private String name = "";


    // Temporary fields for each Node element go here:
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        if (localName.equalsIgnoreCase("node")) {
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
                    if (k.equals("name")) {
                        String v = attributes.getValue("v");
                        if (v != null) {
                            name = v;
                        }
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
            resetVariables();
        }
        
        else if (localName.equalsIgnoreCase("node")) {
            inNode = false;
        }
    }

    private void resetVariables() {
        typ = 0;
        vejnr = 0;
        name = "";
        oneway = "";
        maxspeed = 0;
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
            
            // Using JCoord to convert lat lon to UTM32
            LatLng latlon = new LatLng(lat, lon);
            UTMRef utm = latlon.toUTMRef();
            lat = utm.getNorthing();
            lon = utm.getEasting();
            
            return new OSMNodeData(id, lat, lon);
        } else {
            return null;
        }
    }

    private void createEdges() {
        while (!nodeRefQueue.isEmpty()) {
            long fn = nodeRefQueue.poll();
            if (!nodeRefQueue.isEmpty()) {
                long tn = nodeRefQueue.peek();
                OSMEdgeData edge = new OSMEdgeDataBuilder().setFNODE(fn).setTNODE(tn).setTYPE(typ).setID(vejnr).setNAME(name).setMAXSPEED(maxspeed).setONE_WAY(oneway).createOSMEdgeData();
                processEdge(edge);
            }
        }
    }
    
}
