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
public class CoastlineXMLtoGMT {

    public static void main(String[] args) {
        String dir = "data/";
        int id = 0;
        CoastlineXMLtoDatHandler clh = new CoastlineXMLtoDatHandler();

        try {
            XMLReader reader;
            reader = XMLReaderFactory.createXMLReader();

            reader.setContentHandler(clh);
            reader.parse(dir + "denmark-latest.osm coast line.xml");
            //reader.parse(dir + "sweden-latest.osm coast line.xml");

        } catch (SAXException ex) {
            Logger.getLogger(CoastlineXMLtoDatHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CoastlineXMLtoDatHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            File file = new File(dir + "denmark coastline.gmt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            HashMap<Integer, MapNode> nodesRef = CoastlineXMLtoDatHandler.nodes;
            
            // Write starting segment.
            bw.write(">\n");
            bw.write("# @D" + id + "\n");
            bw.write("# @P" + "\n");
            id++;
            Double savedFirstX = 0.0;
            Double savedFirstY = 0.0;
            boolean saved = false;
            for (int i = 0; i < CoastlineXMLtoDatHandler.queues.size()-1; i++) {
                Queue<MapEdge> queue = CoastlineXMLtoDatHandler.queues.get(i);
                Queue<MapEdge> queueNext = CoastlineXMLtoDatHandler.queues.get(i+1);
                
                Double firstX = nodesRef.get(queue.peek().getFNode()).getX();
                Double firstY = nodesRef.get(queue.peek().getFNode()).getY();
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
                            Double tnX = tn.getX();
                            Double tnY = tn.getY();
                            
                            MapNode nextQueue = nodesRef.get(queueNext.peek().getFNode());
                            Double nextX = nextQueue.getX();
                            Double nextY = nextQueue.getY();
                            if (tnX.compareTo(nextX) == 0 && tnY.compareTo(nextY) == 0) {
                                System.out.println(id);
                                
                                if (!saved) {
                                    savedFirstX = firstX;
                                    savedFirstY = firstY;
                                    saved = true;
                                }
                                newSegment = false;
                                break;
                            }                            
                            
                            if (firstX.compareTo(tnX) != 0 || firstY.compareTo(tnY) != 0) {
                                if (!saved)
                                    bw.write(firstX + " " + firstY + "\n");
                                else 
                                    bw.write(savedFirstX + " " + savedFirstY + "\n");
                            }
                        }
                    }
                }
                if (newSegment) {
                    bw.write(">\n");
                    bw.write("# @D" + id + "\n");
                    bw.write("# @P" + "\n");
                    id++;
                    saved = false;
                }

            }

            bw.close();

        } catch (IOException ex) {
            Logger.getLogger(CoastlineXMLtoDatHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
