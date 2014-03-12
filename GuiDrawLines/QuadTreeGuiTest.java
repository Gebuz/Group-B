package GuiDrawLines;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.IOException;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import krakkit.CoordinateBoundaries;
import krakkit.KrakLoader;
import krakkit.EdgeData;
import krakkit.NodeData;

public class QuadTreeGuiTest
{

    public static void main(String[] args) throws IOException
    {
        String dir = "";

        // For this example, we'll simply load the raw data into
        // ArrayLists.
        final HashMap<Integer, NodeData> nodes = new HashMap<Integer, NodeData>();
        final ArrayList<EdgeData> edges = new ArrayList<EdgeData>();

        // For that, we need to inherit from KrakLoader and override
        // processNode and processEdge. We do that with an 
        // anonymous class. 
        KrakLoader loader = new KrakLoader()
        {
            @Override
            public void processNode(NodeData nd)
            {
                nodes.put(nd.KDV, nd);
            }

            @Override
            public void processEdge(EdgeData ed)
            {
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
        Iterator<Map.Entry<Integer, NodeData>> it = nodes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, NodeData> e = it.next();
            NodeData nd = e.getValue();
            nd.setY(CoordinateBoundaries.yMax - nd.getY() + 
                    CoordinateBoundaries.yMin);
        }

        System.out.println("XMAX = " + CoordinateBoundaries.xMax);
        System.out.println("XMIN = " + CoordinateBoundaries.xMin);
        System.out.println("YMAX = " + CoordinateBoundaries.yMax);
        System.out.println("YMIN = " + CoordinateBoundaries.yMin);
        
        QuadTree root = new QuadTree(edges, nodes, "0");
        root.addCoords( CoordinateBoundaries.xMin,
                        CoordinateBoundaries.yMin, 
                        CoordinateBoundaries.xMax-CoordinateBoundaries.xMin, 
                        CoordinateBoundaries.yMax-CoordinateBoundaries.yMin);
        root.split();

        final QuadTree branch = root.findNeighbor(root.getBranch("00"), Direction.E);
        System.out.println("\nRoot neighbour branch id = " + branch.id);
        System.out.println("");
        
        final ArrayList<EdgeData> edges2 = branch.getEdges();
        
        System.out.println("\nID for branch 0 = " + root.getBranch("0").id);
        System.out.println("Size of edges in branch 0 = " + root.getBranch("0").getEdges().size());
        System.out.println("\nID for branch 00 = " + root.getBranch("00").id);
        System.out.println("Size of edges in branch 00 = " + root.getBranch("00").getEdges().size());
        System.out.println("\nID for branch 01 = " + root.getBranch("01").id);
        System.out.println("Size of edges in branch 01 = " + root.getBranch("01").getEdges().size());
        System.out.println("\nID for branch 02 = " + root.getBranch("02").id);
        System.out.println("Size of edges in branch 02 = " + root.getBranch("02").getEdges().size());
        System.out.println("\nID for branch 03 = " + root.getBranch("03").id);
        System.out.println("Size of edges in branch 03 = " + root.getBranch("03").getEdges().size());
        

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.add(new JPanel()
        {
            public void drawLine(Graphics2D g, Line2D line, Color color, float width)
            {
                g.setColor(color);
                g.setStroke(new BasicStroke(width));
                g.draw(line);
            }

            public void paintComponent(Graphics g)
            {
                int k = 550;
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                for (EdgeData ed : edges2) {
                    NodeData fn = nodes.get(ed.FNODE);
                    NodeData tn = nodes.get(ed.TNODE);
                    int type = ed.TYP;

                    double fnX = (fn.getX() - CoordinateBoundaries.xMin) / k;
                    double fnY = (fn.getY() - CoordinateBoundaries.yMin) / k;
                    double tnX = (tn.getX() - CoordinateBoundaries.xMin) / k;
                    double tnY = (tn.getY() - CoordinateBoundaries.yMin) / k;

                    Line2D line = new Line2D.Double(fnX, fnY, tnX, tnY);
                    switch (type) {
                        case 5:
                        case 6:
                            drawLine(g2, line, Color.PINK, 1);
                            break;
                        case 1:
                        case 31:
                        case 41:
                            drawLine(g2, line, Color.RED, 1);
                            break;
                        case 2:
                        case 32:
                            drawLine(g2, line, Color.GRAY, 1);
                            break;
                        case 8:
                        case 48:
                            drawLine(g2, line, Color.GREEN, 1);
                            break;
                        default:
                            drawLine(g2, line, Color.BLUE, 1);
                            break;
                    }

                }
            }
        });
        frame.setSize(new Dimension(850, 670));
        frame.setVisible(true);
    }
}