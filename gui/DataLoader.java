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
import krakkit.MirrorCoordinates;

/**
 *
 * @author flemmingxu
 */
public class DataLoader {

    public static final HashMap<Integer, NodeData> nodes = new HashMap<>();
    public static final ArrayList<EdgeData> edgesBlue = new ArrayList<>();
    public static final ArrayList<EdgeData> edgesPink = new ArrayList<>();
    public static final ArrayList<EdgeData> edgesGreen = new ArrayList<>();

    public DataLoader() {
        String dir = "";     

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
    }
}
