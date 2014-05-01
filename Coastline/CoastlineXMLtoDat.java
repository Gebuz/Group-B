package Coastline;

import interfaces.MapEdge;
import interfaces.MapNode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Sjúrður í Sandagerði
 */
public class CoastlineXMLtoDat {

    public static void main(String[] args) {
        String dir = "data/";

        CoastlineXMLtoDatHandler clh = new CoastlineXMLtoDatHandler();

        try {
            XMLReader reader;
            reader = XMLReaderFactory.createXMLReader();

            reader.setContentHandler(clh);
            reader.parse(dir + "denmark-latest.osm coast line.xml");
            reader.parse(dir + "sweden-latest.osm coast line.xml");

        } catch (SAXException ex) {
            Logger.getLogger(CoastlineXMLtoDatHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CoastlineXMLtoDatHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            File file = new File(dir + "denmark-sweden merged coastline.dat");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            HashMap<Long, MapNode> nodesRef = CoastlineXMLtoDatHandler.nodes;

            for (Queue<MapEdge> queue : CoastlineXMLtoDatHandler.queues) {
                
                // Use boolean to keep track if this is a new segment we are 
                // saving to the file. This is to avoid empty segments in the
                // file. ">>>>"
                boolean newSegment = false;
                
                while (!queue.isEmpty()) {
                    MapEdge ed = queue.poll();

                    MapNode fn = nodesRef.get(ed.getFNode());
                    MapNode tn = nodesRef.get(ed.getTNode());

                    if (fn != null && tn != null) {
                        bw.write(fn.getX() + " " + fn.getY() + "\n");
                        newSegment = true;

                        if (queue.peek() == null) {
                            bw.write(tn.getX() + " " + tn.getY() + "\n");
                        }
                    }
                }
                if (newSegment)
                    bw.write(">\n");
            }

            bw.close();

        } catch (IOException ex) {
            Logger.getLogger(CoastlineXMLtoDatHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
