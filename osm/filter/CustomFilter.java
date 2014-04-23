package osm.filter;

import osm.xmlparser.OSMNodeData;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Filter an OSM file from any country into two separate XML files. One
 * containing all Way elements containing tags with specified attributes. The
 * other containing all the Node elements that are references in the filtered
 * Way elements. This class runs through the input OSM file twice to avoid
 * keeping all Nodes in memory. On the other hand all the NodeReference (&lt;nd
 * ref"long"/&gt;) values are kept in memory in a HashSet to avoid duplicates.
 *
 * This class only works if the structure of the input OSM file is as stated on
 * OpenStreetMaps wiki page.
 *
 * @see <a href="http://wiki.openstreetmap.org/wiki/.osm">
 * wiki.openstreetmap.org/wiki/.osm</a>
 *
 * @author Sjúrður í Sandagerði
 */
public class CustomFilter {

    static HashSet<Long> nodeReferences = new HashSet<>();

    /**
     * Skip past all the elements that are nested under a specified element,
     * including the specified element. Calls reader.next() until the endElement
     * of the specified element is reached.
     *
     * @param tagName The name of the element to skip.
     * @param reader XMLReader.
     * @throws XMLStreamException
     */
    public static void skipElementAndChildren(String tagName, XMLEventReader reader) throws XMLStreamException {
        // Ignore all the elements that are inside this element
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isEndElement()) {
                if (event.asEndElement().getName().getLocalPart().equals(tagName)) {
                    reader.next();
                    break;
                }
            }
        }
    }

    /**
     * Create a Node element containing the three attributes; id, lat, lon.
     * Currently not used.
     *
     * @param node OSMModeData object containing the information for this Node
     * element.
     * @param ef XMLEventFactory used to create the event objects.
     * @return Returns the "node" XMLEvent
     */
    public static XMLEvent createNodeElement(OSMNodeData node, XMLEventFactory ef) {
        String id = String.valueOf(node.getID());
        String lon = String.valueOf(node.getX());
        String lat = String.valueOf(node.getY());
        Attribute attId = ef.createAttribute("id", id);
        Attribute attLat = ef.createAttribute("lat", lat);
        Attribute attLon = ef.createAttribute("lon", lon);
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(attId);
        attributes.add(attLat);
        attributes.add(attLon);
        Iterator iter = attributes.iterator();
        XMLEvent event = ef.createStartElement(new QName("node"), iter, null);
        return event;
    }

    /**
     * Filter out unnecessary attributes. This method creates a new XMLEvent
     * containing only the attributes that are in the attributesToKeep array.
     *
     * @param startElement The element whose attributes to filter.
     * @param ef XMLEventFactory, used to create a new start element with the
     * new attributes.
     * @param attributesToKeep This array works as the filter. Only save the
     * attributes that are in this array.
     * @return Returns a new XMLEvent with the new attributes.
     */
    public static XMLEvent filterAttributes(StartElement startElement,
            XMLEventFactory ef, String[] attributesToKeep) {

        String tagName = startElement.getName().getLocalPart();
        Iterator nameSpaces = startElement.getNamespaces();
        Iterator it = startElement.getAttributes();
        // Array to include all the attributes to be kept.
        ArrayList<Attribute> attArray = new ArrayList<>();

        while (it.hasNext()) {
            Attribute att = (Attribute) it.next();
            for (String attributeToGet : attributesToKeep) {
                String attName = att.getName().getLocalPart();
                if (attName.equals(attributeToGet)) {
                    attArray.add(att);
                    break;
                }
            }
        }
        XMLEvent event = ef.createStartElement(
                new QName(tagName), attArray.iterator(), nameSpaces);

        return event;
    }

    /**
     * Check if an element should be ignored or not, based on an array of
     * specified attribute values.
     *
     * @param startElement The element whose attributes to look up.
     * @param nameOfElement Name of the element.
     * @param nameOfAttribute Name of the attribute whose value to filter.
     * @param attributeValues Array of attributes values. If the element does
     * not contain this attribute value then ignore this element.
     * @return Returns true if the element should be ignored and false
     * otherwise.
     */
    public static boolean ignoreElementWithAttributeValue(
            StartElement startElement, String nameOfElement,
            String nameOfAttribute, String[] attributeValues) {

        String tagName = startElement.getName().getLocalPart();
        boolean found = true;
        if (tagName.equals(nameOfElement)) {
            Attribute att = startElement.getAttributeByName(new QName(nameOfAttribute));
            if (found) {
                for (String tagToGet : attributeValues) {
                    if (att != null && att.getValue().equalsIgnoreCase(tagToGet)) {
                        found = false;
                        break;
                    }
                }
            }
        }

        return found;
    }

    public static void main(String[] args) throws FileNotFoundException,
            XMLStreamException {

        boolean firstPass = true;
        
        FileOutputStream foutS = new FileOutputStream(MyInputFile.fileUrl + " parsed.xml");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(foutS));

        try (InputStream inFirstPass = new FileInputStream(MyInputFile.fileUrl); 
             InputStream inSecondPass = new FileInputStream(MyInputFile.fileUrl); 
                BufferedWriter out = bw) {

            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLEventFactory ef = XMLEventFactory.newInstance();

            XMLEventReader reader = factory.createXMLEventReader(MyInputFile.fileUrl, inFirstPass);
            XMLEventWriter writer = xof.createXMLEventWriter(out);

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartDocument()) {
                    writer.add(event);
                    writer.add(ef.createCharacters("\n"));

                } else if (event.isStartElement()) {

                    StartElement s = event.asStartElement();
                    String tagName = s.getName().getLocalPart();
                    if (tagName.equals("osm")) {
                        writer.add(event);

                        // Get the bounds for the map and save them to global variables.
                    } else if (tagName.equals("bounds")) {
                        writer.add(event);

                    } else if (tagName.equals("node")) {
                        // ignore all nodes on first pass.
                        skipElementAndChildren("node", reader);

                        // If the element is a Way element then only save it if it 
                        // contains the "highway" tag.
                    } else if (tagName.equals("way")) {
                        event = filterAttributes(s, ef, WhatToKeep.attributesToKeep);

                        // Read all elements between the Way Start and End element.
                        // Check if the Way contains the tag Highway.
                        // Ignore all the nodes that are inside this node element

                        ArrayList<XMLEvent> wayEvents = new ArrayList<>();
                        ArrayList<Long> wayNodeRef = new ArrayList<>();

                        wayEvents.add(event); // Add start element.

                        boolean highwayTagFound = false;
                        boolean coastLineTagFound = false;

                        while (reader.hasNext()) {

                            XMLEvent wayEvent = reader.nextEvent();
                            wayEvents.add(wayEvent);

                            if (wayEvent.isStartElement()) {
                                StartElement startElem = wayEvent.asStartElement();
                                if (startElem.getName().getLocalPart().equals("nd")) {
                                    Attribute attNodeRef = startElem.getAttributeByName(new QName("ref"));
                                    if (attNodeRef != null) {
                                        long nodeRefID = Long.parseLong(attNodeRef.getValue());
                                        wayNodeRef.add(nodeRefID);
                                    }
                                }

                                Attribute k = startElem.getAttributeByName(new QName("k"));
                                Attribute v = startElem.getAttributeByName(new QName("v"));
                                if (k != null && (k.getValue().equalsIgnoreCase("highway"))) {
                                    if (v != null) {
                                        highwayTagFound = true;
                                        for (String nonos : WhatToKeep.highwayToSkip) {
                                            if (v.getValue().equalsIgnoreCase(nonos)) {
                                                highwayTagFound = false;
                                                break;
                                            }
                                        }
                                    }
                                } else if (v != null && v.getValue().equalsIgnoreCase("coastline")) {
                                    coastLineTagFound = true;
                                }

                                if (startElem.getName().getLocalPart().equalsIgnoreCase("tag")
                                        && ignoreElementWithAttributeValue(startElem, "tag", "k", WhatToKeep.tagsToKeep)) {
                                    wayEvents.remove(wayEvent);

                                    // Skip twice to get rid of newline and whitespace 
                                    // where the element was deleted
                                    reader.next();
                                    reader.next();
                                }
                            } else if (wayEvent.isEndElement()) {
                                if (wayEvent.asEndElement().getName().getLocalPart().equals("way")) {
                                    if (reader.hasNext()) {
                                        wayEvents.add(reader.nextEvent());
                                        break;
                                    }
                                }
                            }
                        }

                        if (highwayTagFound || coastLineTagFound) {
                            for (XMLEvent e : wayEvents) {
                                writer.add(e);
                            }
                            nodeReferences.addAll(wayNodeRef);
                        }

                    } else if (tagName.equals("relation")) {
                        reader.close(); // Nothing more to read on the first pass.
                        break;
                    }
                } else if (event.isEndElement()) {
                    writer.add(event);
                } else {
                    writer.add(event);
                }
            }

            inFirstPass.close();

            firstPass = false;

            reader = factory.createXMLEventReader(MyInputFile.fileUrl, inSecondPass);
            
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartDocument()) {

                    continue; // Skip the StartDocument element <?xml ...>
                } else if (event.isStartElement()) {
                    StartElement s = event.asStartElement();
                    String tagName = s.getName().getLocalPart();

                    if (tagName.equals("osm")) {
                        continue;
                    } else if (tagName.equals("bounds")) {
                        continue;
                    } else if (tagName.equals("node")) {
                        Attribute idAttribute = s.getAttributeByName(new QName("id"));
                        long id = 0;
                        if (idAttribute != null) // avoid null pointer reference.
                        {
                            id = Long.parseLong(idAttribute.getValue());
                        }

                        // Check if the Node is in the nodeReferences.
                        if (nodeReferences.contains(id)) {
                            nodeReferences.remove(id);
                            event = filterAttributes(s, ef, WhatToKeep.attributesToKeep);
                            writer.add(event);

                            while (reader.hasNext()) {
                                XMLEvent nodeEvent = reader.nextEvent();
                                writer.add(nodeEvent);

                                if (nodeEvent.isEndElement()) {
                                    EndElement endElement = nodeEvent.asEndElement();
                                    if (endElement.getName().getLocalPart().equalsIgnoreCase("node")) {
                                        break;
                                    }
                                }
                            }
                        } else {
                            skipElementAndChildren("node", reader);
                        }

                    } else if (tagName.equalsIgnoreCase("way") || tagName.equalsIgnoreCase("relation")) {
                        break;
                    } else {
                        writer.add(event);
                    }
                } else if (event.isEndElement()) {
                    EndElement end = event.asEndElement();
                    String tagName = end.getName().getLocalPart();
                    if (tagName.equalsIgnoreCase("node")
                            || tagName.equalsIgnoreCase("tag")) {
                        writer.add(event);
                    } else {
                        continue;
                    }
                } else {
                    writer.add(event);
                }
            }

            writer.add(ef.createEndElement(new QName("osm"), null));
            writer.add(ef.createEndDocument());

            reader.close();

            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
