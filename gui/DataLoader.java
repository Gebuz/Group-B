package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import krakkit.CoordinateBoundaries;
import krakkit.EdgeData;
import krakkit.KrakLoader;
import krakkit.NodeData;

/**
 *
 * @author flemmingxu
 */
public class DataLoader {
    public final HashMap<Integer, NodeData> nodes;
    public final ArrayList<EdgeData> edges;

    public DataLoader() {
        String dir = "/Users/flemmingxu/NetBeansProjects/Group-B/";

        // For this example, we'll simply load the raw data into
        // ArrayLists.
        nodes = new HashMap<Integer, NodeData>();
        edges = new ArrayList<EdgeData>();

        // For that, we need to inherit from KrakLoader and override
        // processNode and processEdge. We do that with an 
        // anonymous class. 
        KrakLoader loader = new KrakLoader() {
            @Override
            public void processNode(NodeData nd) {
                nodes.put(nd.KDV, nd);
            }

            @Override
            public void processEdge(EdgeData ed) {
                edges.add(ed);
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
    }
}
