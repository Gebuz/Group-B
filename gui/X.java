package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import krakkit.CoordinateBoundaries;
import krakkit.EdgeData;
import krakkit.KrakLoader;
import krakkit.NodeData;

public class X
{

    public static void main(String[] args) throws IOException {
        MapPanel panel = new MapPanel();
        MapView map = new MapView("Super duper map", panel);
        Controller controller = new Controller(map);
    }
//        String dir = "/Users/flemmingxu/NetBeansProjects/Group-B/";
//
//        // For this example, we'll simply load the raw data into
//        // ArrayLists.
//        final HashMap<Integer, NodeData> nodes = new HashMap<Integer, NodeData>();
//        final ArrayList<EdgeData> edges = new ArrayList<EdgeData>();
//
//        // For that, we need to inherit from KrakLoader and override
//        // processNode and processEdge. We do that with an 
//        // anonymous class. 
//        KrakLoader loader = new KrakLoader()
//        {
//            @Override
//            public void processNode(NodeData nd) { nodes.put(nd.KDV, nd); }
//
//            @Override
//            public void processEdge(EdgeData ed) { edges.add(ed); }
//        };
//        
//        // If your machine slows to a crawl doing inputting, try
//        // uncommenting this. 
//        // Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
//
//        // Invoke the loader class.
//        loader.load(dir + "kdv_node_unload.txt",
//                dir + "kdv_unload.txt");
//
//        CoordinateBoundaries.findBoundaries(nodes);
//
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
//        frame.add(new JPanel()
//        {
//            public void drawLine(Graphics2D g, Line2D line, Color color, float width)
//            {
//                g.setColor(color);
//                g.setStroke(new BasicStroke(width));
//                g.draw(line);
//            }
//
//            public void paintComponent(Graphics g)
//            {
//                int k = 550;
//                super.paintComponent(g);
//                Graphics2D g2 = (Graphics2D) g;
//                for (int i = 1; i < 812301; i++) {
//                    EdgeData ed = edges.get(i);
//                    NodeData fn = nodes.get(ed.FNODE);
//                    NodeData tn = nodes.get(ed.TNODE);
//                    int type = ed.TYP;
//
//                    double fnX = (fn.X_COORD - CoordinateBoundaries.xMin) / k;
//                    double fnY = (CoordinateBoundaries.yMax - fn.Y_COORD) / k;
//                    double tnX = (tn.X_COORD - CoordinateBoundaries.xMin) / k;
//                    double tnY = (CoordinateBoundaries.yMax - tn.Y_COORD) / k;
//
//                    Line2D line = new Line2D.Double(fnX, fnY, tnX, tnY);
//                    switch (type) {
//                        case 5:
//                        case 6:
//                            drawLine(g2, line, Color.PINK, 1);
//                            break;
//                        case 1:
//                        case 31:
//                        case 41:
//                            drawLine(g2, line, Color.RED, 1);
//                            break;
//                        case 2:
//                        case 32:
//                        drawLine(g2, line, Color.GRAY, 1);
//                            break;
//                        case 8:
//                        case 48:
//                            drawLine(g2, line, Color.GREEN, 1);
//                            break;
//                        default:
//                            drawLine(g2, line, Color.BLUE, 1);
//                            break;
//                    }
//
//                }
//            }
//        });
//        frame.setSize(new Dimension(850, 670));
//        frame.setVisible(true);
//    }
}