package osm.xmlparser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import krakkit.EdgeData;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This class parses an XML file containing Way elements.
 *
 * @author Sjúrður í Sandagerði
 */
public class OSMWayhandler extends DefaultHandler {

    ArrayList<OSMEdgeData> osmEdges = new ArrayList<>();
    boolean inWay = false; // Are we inside a Way element or not.
    Queue<Long> nodeReferences = new LinkedList<>();
    
    // Temporary fields for each Way element.
    private int typ = 0;
    private int vejnr = 0;
    private String vejnavn = "";

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        if (localName.equalsIgnoreCase("way")) {
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
                    nodeReferences.add(Long.parseLong(ref));
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
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (localName.equalsIgnoreCase("way")) {
            inWay = false;
            createEdges();
            nodeReferences.clear();
            resetVariables();
        }
    }

    private void resetVariables() {
        typ = 0;
        vejnr = 0;
        vejnavn = "";
    }

    private void createEdges() {
        while (!nodeReferences.isEmpty()) {
            long fn = nodeReferences.poll();
            if (!nodeReferences.isEmpty()) {
                long tn = nodeReferences.peek();
                OSMEdgeData edge = new OSMEdgeData(fn, tn, typ, vejnr, vejnavn);
                osmEdges.add(edge);
            }
        }
    }

    public ArrayList<OSMEdgeData> getEdges() {
        return osmEdges;
    }
    
    

    public static void main(String[] args) throws Exception {
        OSMWayhandler wp = new OSMWayhandler();
        XMLReader reader;
        reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(wp);
        reader.parse("denmark-latest.osm ways.xml");
//        reader.parse("faroe-islands-latest.osm ways.xml");
    }
}
