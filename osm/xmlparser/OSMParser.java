/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package osm.xmlparser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Sjurdur
 */
public class OSMParser {

    public static void main(String[] args) throws Exception {
        OSMNodeHandler np = new OSMNodeHandler();
        OSMWayhandler wp = new OSMWayhandler();
        
        XMLReader reader;
        reader = XMLReaderFactory.createXMLReader();
        
        reader.setContentHandler(np);
        reader.parse("denmark-latest.osm nodes.xml");

        reader.setContentHandler(wp);
        reader.parse("denmark-latest.osm ways.xml");
    }
}
