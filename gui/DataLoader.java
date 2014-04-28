package gui;

import Model.CoordinateBoundaries;
import Model.MirrorCoordinates;
import interfaces.MapEdge;
import interfaces.MapNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import krakkit.KrakEdgeData;
import krakkit.KrakLoader;
import krakkit.KrakNodeData;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import osm.xmlparser.OSMEdgeData;
import osm.xmlparser.OSMNodeData;
import osm.xmlparser.OSMParseHandler;

/**
 *
 * @author flemmingxu
 */
public class DataLoader {

    public static final HashMap<Long, MapNode> nodes = new HashMap<>();
    public static final ArrayList<MapEdge> edgesBlue = new ArrayList<>();
    public static final ArrayList<MapEdge> edgesPink = new ArrayList<>();
    public static final ArrayList<MapEdge> edgesGreen = new ArrayList<>();
    public static boolean isOSM = false;

    public DataLoader(int bool) {
        String dir = "data/";

        if (bool == 0) {

            // For that, we need to inherit from KrakLoader and override
            // processNode and processEdge. We do that with an 
            // anonymous class. 
            KrakLoader loader = new KrakLoader() {
                @Override
                public void processNode(KrakNodeData nd) {
                    nodes.put(nd.getID(), nd);
                }

                @Override
                public void processEdge(KrakEdgeData ed) {
                    edgesGreen.add(ed);
                    switch (ed.TYP) {
                        case 5:
                        case 6:
                            edgesPink.add(ed);
                            break;
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 31:
                        case 32:
                        case 41:
                        case 42:
                        case 80:
                            edgesPink.add(ed);
                            edgesBlue.add(ed);
                            break;
                    }
                }
            };
            try {
                // If your machine slows to a crawl doing inputting, try
                // uncommenting this. 
                // Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

                // Invoke the loader class.
                loader.load(dir + "kdv_node_unload.txt",
                        dir + "kdv_unload.txt");
            } catch (IOException ex) {
                System.out.println("ERROR: Could not find kdv_node_unload.txt or kdv_unload.txt in specified directory " + dir);
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            }

            CoordinateBoundaries.findBoundaries(nodes);
            MirrorCoordinates.MirrorY(nodes);

        } else { /* isOSM */

            OSMParseHandler ph = new OSMParseHandler() {
                @Override
                public void processNode(OSMNodeData nd) {
                    nodes.put(nd.getID(), nd);
                }

                @Override
                public void processEdge(OSMEdgeData ed) {
                    edgesGreen.add(ed);
                    switch (ed.getType()) {
                        case 5:
                        case 6:
                            edgesPink.add(ed);
                            break;
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 31:
                        case 32:
                        case 41:
                        case 42:
                        case 80:
                        case 0:
                            edgesPink.add(ed);
                            edgesBlue.add(ed);
                            break;
                    }
                }
            };

            try {
                XMLReader reader;
                reader = XMLReaderFactory.createXMLReader();

                reader.setContentHandler(ph);
                reader.parse(dir + "denmark-latest.osm parsed.xml");
                
                System.out.println(CoordinateBoundaries.xMin);
                System.out.println(CoordinateBoundaries.yMin);
            } catch (SAXException ex) {
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
