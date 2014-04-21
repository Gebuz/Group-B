package osm.xmlparser;

import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import krakkit.CoordinateBoundaries;
import krakkit.Projection;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This class is used to parse an XML file containing Node elements.
 *
 * @author Sjúrður í Sandagerði
 */
public class OSMNodeHandler extends DefaultHandler {

    private HashMap<Long, OSMNodeData> nodes = new HashMap<>();
    boolean boundsFound = false;
    Projection p;

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if (!boundsFound && localName.equalsIgnoreCase("bounds")) {
            getBounds(attributes);
            boundsFound = true;
        } else if (localName.equalsIgnoreCase("node")) {
            OSMNodeData node = getNodeInformation(attributes);
            if (node != null) {
                nodes.put(node.id, node);
            }
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

    private OSMNodeData getNodeInformation(Attributes attributes) {
        String idStr = attributes.getValue("id");
        String latStr = attributes.getValue("lat");
        String lonStr = attributes.getValue("lon");
        // Avoid null pointer reference!
        if (idStr != null && latStr != null && lonStr != null) {
            double lat = Double.parseDouble(latStr);
            return new OSMNodeData(Long.parseLong(idStr),
                    CoordinateBoundaries.yMax - p.mercatorY(lat)
                    + CoordinateBoundaries.yMin,
                    p.mercatorX(Double.parseDouble(lonStr)));
        } else {
            return null;
        }
    }

    public HashMap<Long, OSMNodeData> getNodes() {
        return nodes;
    }

    public boolean isBoundsFound() {
        return boundsFound;
    }

    public static void main(String[] args) throws Exception {
		// How to parse an xml file with this default handler.
        OSMNodeHandler np = new OSMNodeHandler();
        XMLReader reader;
        reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(np);
        reader.parse("denmark-latest.osm nodes.xml");
    }
}
