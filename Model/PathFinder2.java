package Model;

import Graph.DirectedEdge;
import Graph.EdgeWeightedDigraph;
import Graph.DijkstraSP;
import java.util.ArrayList;
import interfaces.MapEdge;
import interfaces.MapNode;
import gui.DataLoader;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PathFinder2 {

    private static final PathFinder2 pathFinder = new PathFinder2();
    private static EdgeWeightedDigraph graphCar;
    private static EdgeWeightedDigraph graphWalk;
    private static DijkstraSP tree;

    public static PathFinder2 getInstance() {
        return pathFinder;
    }

    public static void createGraph(ArrayList<MapEdge> edges) {
        graphCar = graphs(0, edges);
//        graphWalk = graphs(1, edges);
    }

    private static EdgeWeightedDigraph graphs(int type, ArrayList<MapEdge> edges) {
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(edges.size() * 5);  // Ved ikke hvorfor 5 er det rigtige tal.
        for (MapEdge ed : edges) {
            if (type == 0) {
                switch (ed.getType()) {
                    case 8:
                    case 48:
                        break;
                    default:
                        try {
                            MapNode fn = DataLoader.nodes.get(ed.getFNode());
                            MapNode tn = DataLoader.nodes.get(ed.getTNode());

                            Point2D.Double xy1 = new Point2D.Double(fn.getX(), fn.getY());
                            Point2D.Double xy2 = new Point2D.Double(tn.getX(), tn.getY());

                            // Get length of the edge by calculating the distance between
                            // the nodes. This is done because OSM data doesnt contain
                            // the length of each Edge segment.
                            double length;
                            if ((ed.getLength() - 0.00000001) < 0.0) {
                                length = xy1.distance(xy2);
                            } else {
                                length = ed.getLength();
                            }

                            int fnID = fn.getID();
                            int tnID = tn.getID();

                            if (ed.getOneWay().equals("n") || ed.getMaxSpeed() == 0) {
                                break;
                            } else if (ed.getOneWay().equals("ft")) {
                                graph.addEdge(new DirectedEdge(fnID, tnID, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                            } else if (ed.getOneWay().equals("tf")) {
                                graph.addEdge(new DirectedEdge(tnID, fnID, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                            } else {
                                graph.addEdge(new DirectedEdge(fnID, tnID, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                                graph.addEdge(new DirectedEdge(tnID, fnID, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                            }
                        } catch (RuntimeException ex) {
                            Logger.getLogger(PathFinder2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                }
            } else {
                switch (ed.getType()) {
                    case 1:
                    case 31:
                    case 41:
                        break;
                    default:
                        MapNode fn = DataLoader.nodes.get(ed.getFNode());
                        MapNode tn = DataLoader.nodes.get(ed.getTNode());

                        Point2D.Double xy1 = new Point2D.Double(fn.getX(), fn.getY());
                        Point2D.Double xy2 = new Point2D.Double(tn.getX(), tn.getY());

                        // Get length of the edge by calculating the distance between
                        // the nodes. This is done because OSM data doesnt contain
                        // the length of each Edge segment.
                        double length;
                        if ((ed.getLength() - 0.00000001) < 0.0) {
                            length = xy1.distance(xy2);
                        } else {
                            length = ed.getLength();
                        }

                        int fnID = fn.getID();
                        int tnID = tn.getID();
                        if (ed.getOneWay().equals("n") || ed.getMaxSpeed() == 0) {
                            break;
                        } else if (ed.getOneWay().equals("ft")) {
                            graph.addEdge(new DirectedEdge(fnID, tnID, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else if (ed.getOneWay().equals("tf")) {
                            graph.addEdge(new DirectedEdge(tnID, fnID, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else {
                            graph.addEdge(new DirectedEdge(fnID, tnID, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                            graph.addEdge(new DirectedEdge(tnID, fnID, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                        }
                        break;
                }
            }
        }
        System.out.println("ArrayList<MapEdge> edges size is = " + edges.size());
        System.out.println("Edges in the graph = " + graph.E());
        System.out.println("Vertices in the graph = " + graph.V());
        return graph;
    }

    private static ArrayList<MapEdge> createTree(int type, MapEdge start, MapEdge end) {

        MapNode fn = DataLoader.nodes.get(start.getFNode());
        MapNode tn = DataLoader.nodes.get(end.getTNode());
        int idStart = fn.getID();
        int idEnd = tn.getID();

        EdgeWeightedDigraph graph;

        if (type == 0) {
            graph = graphCar;
        } else {
            graph = graphWalk;
        }

        tree = new DijkstraSP(graph, idStart);
        return shortestPath(idEnd);

    }

    private static ArrayList<MapEdge> shortestPath(int id) {
        ArrayList<MapEdge> edges = new ArrayList<>();
        Iterable<DirectedEdge> list = tree.pathTo(id);
        tree = null; // Garbage control !
        if (list == null) {
            return edges;
        }
        for (DirectedEdge e : list) {
            edges.add(e.getEdge());
        }
        return edges;
    }

    public static ArrayList<MapEdge> getShortestPath(int type, MapEdge start, MapEdge end) {
        return createTree(type, start, end);
    }
}
