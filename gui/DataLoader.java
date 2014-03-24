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

    public final HashMap<Integer, NodeData> nodes;
    public final ArrayList<EdgeData> edgesBlue;
    public final ArrayList<EdgeData> edgesRed;
    public final ArrayList<EdgeData> edgesGreen;

    public DataLoader() {
        String dir = "";

        // For this example, we'll simply load the raw data into
        // ArrayLists.
        nodes = new HashMap<>();
        edgesBlue = new ArrayList<>();
        edgesRed = new ArrayList<>();
        edgesGreen = new ArrayList<>();

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
                        edgesRed.add(ed);
                        break;
                    case 1:
                    case 31:
                    case 41:
                        edgesRed.add(ed);
                        edgesBlue.add(ed);
                        break;
                    case 2:
                    case 32:
                        edgesRed.add(ed);
                        edgesBlue.add(ed);
                        break;
                    case 8:
                    case 48:
                        break;
                    default:
                        edgesRed.add(ed);
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
