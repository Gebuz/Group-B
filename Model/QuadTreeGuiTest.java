package Model;

import interfaces.MapEdge;
import interfaces.MapNode;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import krakkit.KrakLoader;
import krakkit.KrakEdgeData;
import krakkit.KrakNodeData;

public class QuadTreeGuiTest {
    

    public static void main(String[] args) throws IOException {
        String dir = "";

        // For this example, we'll simply load the raw data into
        // ArrayLists.
        final HashMap<Integer, MapNode> nodes = new HashMap<>();
        final ArrayList<MapEdge> edges = new ArrayList<>();
        final QuadTree root;

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
                edges.add(ed);
            }
        };

        // If your machine slows to a crawl doing inputting, try
        // uncommenting this. 
        // Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        // Invoke the loader class.
        loader.load(dir + "kdv_node_unload.txt",
                dir + "kdv_unload.txt");

        CoordinateBoundaries.findBoundaries(nodes);

        // Spejlvend alle y værdier:
        Iterator<Map.Entry<Integer, MapNode>> it = nodes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, MapNode> e = it.next();
            MapNode nd = e.getValue();
            nd.setY(CoordinateBoundaries.yMax - nd.getY()
                    + CoordinateBoundaries.yMin);
        }

        System.out.println("XMAX = " + CoordinateBoundaries.xMax);
        System.out.println("XMIN = " + CoordinateBoundaries.xMin);
        System.out.println("YMAX = " + CoordinateBoundaries.yMax);
        System.out.println("YMIN = " + CoordinateBoundaries.yMin);

        root = new QuadTree(edges, nodes, "0");
        root.addCoords(CoordinateBoundaries.xMin,
                CoordinateBoundaries.yMin,
                CoordinateBoundaries.xMax - CoordinateBoundaries.xMin,
                CoordinateBoundaries.yMax - CoordinateBoundaries.yMin);
        root.split(500);

        final QuadTree branch = root.findNeighbor(root.getBranch("00"), Direction.E);
        System.out.println("\nRoot neighbour branch id (01) = " + branch.id);
        System.out.println("");

        final HashSet<String> trees = root.getAll();

        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.add(new JPanel() {
            public void drawLine(Graphics2D g, Line2D line, Color color, float width) {
                g.setColor(color);
                g.setStroke(new BasicStroke(width));
                g.draw(line);
            }

            @Override
            public void paintComponent(Graphics g) {
                int k = 550;
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                for (String s : trees) {
                    QuadTree qt = root.getBranch(s);
                }
            }
        }
        );
        frame.setSize(
                new Dimension(850, 670));
        frame.setVisible(
                true);
    }
}
