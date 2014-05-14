package osm.xmlparser;

import Model.CoordinateBoundaries;
import Model.Projection;
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
public abstract class OSMParseHandler extends DefaultHandler {

    // Map the node IDs into this HashMap and give each node a new ID from 1 and up.
    private static HashMap<Long, Integer> idMap = new HashMap<>();
    private static int nodeIDCounter = 1;
    
    // Use boolean to choose which hashmap for ID mapping should be used.
    // This is so the class works for both OSMs standard .osm format where Nodes
    // come first in the file.
    // vs our format where Way elements come first.
    boolean mappingNodesFirst = true;
    
    public abstract void processNode(OSMNodeData nd);

    public abstract void processEdge(OSMEdgeData ed);

    boolean inWay = false; // Are we inside a Way element or not.
    boolean inNode = false; // Are we inside a Node element or not.
    boolean boundsFound = false;
    Queue<Long> nodeRefQueue = new LinkedList<>();

    // Temporary fields for each Way element.
    private int typ = 0;
    private int vejnr = 0;
    private int maxspeed = 0;
    private String oneway = "";
    private String vejnavn = "";
    
    // Ferry speed and type
    private int ferrySpeed = 2;
    private int ferryType = 80;

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
            
            if (mappingNodesFirst && idMap.isEmpty())
                mappingNodesFirst = false;            

            String id = attributes.getValue("id");
            if (id != null) {
                try {
                    vejnr = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    Logger.getLogger(OSMParseHandler.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        if (inWay) {
            if (localName.equalsIgnoreCase("nd")) {
                String ref = attributes.getValue("ref");
                if (ref != null) {
                    try {
                        nodeRefQueue.add(Long.parseLong(ref));
                    } catch (NumberFormatException ex) {
                        Logger.getLogger(OSMParseHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } else if (localName.equalsIgnoreCase("tag")) {
                String k = attributes.getValue("k");
                if (k != null) {
                    if (k.equals("highway")) {
                        String v = attributes.getValue("v");
                        int type = OSMHighwayTypeIndicator.getType(v);
                        if (type > 0) {
                            typ = type;
                            if (typ == 31) { // highway=motorway_link implies oneway=yes)
                                oneway = "yes";
                            }
                        } else {
                            typ = 5; // unclassified.
                            maxspeed = 40;
                        }
                        
                        
                    } else if (k.equals("route")) {
                        String v = attributes.getValue("v");
                        if (v.equalsIgnoreCase("ferry")){
                            typ = ferryType;
                            maxspeed = ferrySpeed;
                        }
                    } else if (k.equals("name")) {
                        String v = attributes.getValue("v");
                        if (v != null) {
                            vejnavn = v;
                        }
                    } else if (k.equals("maxspeed")) {
                        String v = attributes.getValue("v");
                        if (v != null) {
                            try {
                                if (v.contains(";")) {
                                    v = v.substring(0, v.indexOf(";"));
                                } else if (v.equalsIgnoreCase("DK:urban")) {
                                    v = "50";
                                } else if (v.equalsIgnoreCase("DK:rural")) {
                                    v = "80";
                                } else if (v.equals("*")) {
                                    v = "50"; // ??
                                } else if (v.contains("signal") && typ == 1) {
                                    v = "130";
                                } else if (v.contains("signal") && typ == 31) {
                                    v = "90";
                                }
                                
                                maxspeed = Integer.parseInt(v);

                            } catch (NumberFormatException ex) {
                                Logger.getLogger(OSMParseHandler.class.getName()).log(Level.SEVERE, null, ex);

                                maxspeed = 0;
                            }
                        }
                    } else if (k.equals("oneway")) {
                        String v = attributes.getValue("v");
                        if (v != null) {
                            if (v.equalsIgnoreCase("yes") || v.equalsIgnoreCase("true") || v.equalsIgnoreCase("1")) {
                                oneway = "ft";
                            } else if (v.equalsIgnoreCase("no") || v.equalsIgnoreCase("false") || v.equalsIgnoreCase("0")) {
                                oneway = "";
                            } else if (v.equalsIgnoreCase("-1") || v.equalsIgnoreCase("reverse")) {
                                oneway = "tf";
                            } else {
                                oneway = "";
                            }
                        }
                    } else if (k.equals("motor_vehicle") || k.equals("motorcar")) {
                        String v = attributes.getValue("v");
                        if (v != null) {
                            if (v.equalsIgnoreCase("yes")) {
                                oneway = ""; // Both directions
                            } else {
                                oneway = "n"; // No directions
                            }
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
        } else if (localName.equalsIgnoreCase("node")) {
            inNode = false;
        }
    }

    private void resetVariables() {
        typ = 0;
        vejnr = 0;
        vejnavn = "";
        oneway = "";
        maxspeed = 0;
    }

    private OSMNodeData getNodeInformation(Attributes attributes) {
        String idStr = attributes.getValue("id");
        String latStr = attributes.getValue("lat");
        String lonStr = attributes.getValue("lon");
        // Avoid null pointer reference!
        if (idStr != null && latStr != null && lonStr != null) {
            try {
                Long id = Long.parseLong(idStr);
                if (mappingNodesFirst && !idMap.containsKey(id))
                    idMap.put(id, nodeIDCounter);
                int mappedID = nodeIDCounter++;
                
                if (!mappingNodesFirst) {
                    mappedID = idMap.get(id);
                }
                
                double lat = Double.parseDouble(latStr);
                double lon = Double.parseDouble(lonStr);

                return new OSMNodeData(mappedID, lat, lon);
                
            } catch (NumberFormatException ex) {
                Logger.getLogger(OSMParseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return null;
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
        try {
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
        } catch (NumberFormatException ex) {
            Logger.getLogger(OSMParseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createEdges() {
        while (!nodeRefQueue.isEmpty()) {
            Long fn = nodeRefQueue.poll();
            if (!nodeRefQueue.isEmpty()) {
                Long tn = nodeRefQueue.peek();
                
                if (mappingNodesFirst && !idMap.isEmpty()) {
                    int mappedFN = idMap.get(fn);
                    int mappedTN = idMap.get(tn);
                    
                    OSMEdgeData edge = new OSMEdgeDataBuilder().setFNODE(mappedFN).setTNODE(mappedTN).setTYPE(typ).setID(vejnr).setNAME(vejnavn).setMAXSPEED(maxspeed).setONE_WAY(oneway).createOSMEdgeData();
                    processEdge(edge);
                    
                } else if (!mappingNodesFirst) {
                    if (!idMap.containsKey(tn)) {
                        idMap.put(tn, nodeIDCounter++);
                    }
                    if (!idMap.containsKey(fn)) {
                        idMap.put(fn, nodeIDCounter++);
                    }
                    
                    int mappedFN = idMap.get(fn);
                    int mappedTN = idMap.get(tn);
                    
                    OSMEdgeData edge = new OSMEdgeDataBuilder().setFNODE(mappedFN).setTNODE(mappedTN).setTYPE(typ).setID(vejnr).setNAME(vejnavn).setMAXSPEED(maxspeed).setONE_WAY(oneway).createOSMEdgeData();
                    processEdge(edge);
                }

            }
        }
    }
}
