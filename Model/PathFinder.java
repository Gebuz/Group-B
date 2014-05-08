/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Graph.DirectedEdge;
import Graph.EdgeWeightedDigraph;
import Graph.AstarSP;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.geom.Point2D;
import interfaces.MapEdge;
import interfaces.MapNode;
import gui.DataLoader;

/**
 * Vehicles: 0 = car 1 = walk
 *
 * TODO: test
 *
 * @author Johan
 */
public class PathFinder {

    private static final PathFinder pathFinder = new PathFinder();
    private static EdgeWeightedDigraph graphCar;
    private static EdgeWeightedDigraph graphWalk;
    private static AstarSP tree;
    private static HashMap<Point2D.Double, Integer> nodesCar;
    private static HashMap<Point2D.Double, Integer> nodesWalk;
    private static int count;

    /**
     *
     */
    public PathFinder() {
    }

    /**
     * gets the instance of PathFinder
     *
     * @return
     */
    public static PathFinder getInstance() {
        return pathFinder;
    }

    /**
     *
     * @param type vehicle type
     * @param edges all edges
     */
    public static void createGraph(ArrayList<MapEdge> edges) {
        graphCar = Graphs(0, edges);
//        graphWalk = Graphs(1, edges);
    }

    private static EdgeWeightedDigraph Graphs(int type, ArrayList<MapEdge> edges) {
        // Der er for mange vertices tror jeg.
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(edges.size() * 2); // at most 2*size because of two way streets.
        count = 0;
        if (type == 0) {
            nodesCar = new HashMap<>();
        } else {
            nodesWalk = new HashMap<>();
        }
        for (MapEdge ed : edges) {
            if (type == 0) {
                switch (ed.getType()) {
                    case 8:
                    case 48:
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

                        Integer i;
                        Integer j;

                        if (nodesCar.get(xy1) == null) {
                            nodesCar.put(xy1, count);
                            graph.addXY(xy1, count);
                            i = count++;
                        } else {
                            i = nodesCar.get(xy1);
                        }


                        if (nodesCar.get(xy2) == null) {
                            nodesCar.put(xy2, count);
                            graph.addXY(xy2, count);
                            j = count++;
                        } else {
                            j = nodesCar.get(xy2);
                        }

                        if (ed.getOneWay().equals("n") || ed.getMaxSpeed() == 0) {
                            break;
                           
                        } else if (ed.getOneWay().equals("ft")) {
                            graph.addEdge(new DirectedEdge(i, j, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else if (ed.getOneWay().equals("tf")) {
                            graph.addEdge(new DirectedEdge(j, i, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else {
                            graph.addEdge(new DirectedEdge(i, j, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                            graph.addEdge(new DirectedEdge(j, i, length / ed.getMaxSpeed() / 3.6 + 10, ed));
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

                        Integer i;
                        Integer j;

                        if (nodesWalk.get(xy1) == null) {
                            nodesWalk.put(xy1, count);
                            graph.addXY(xy1, count);
                            i = count++;
                        } else {
                            
                            i = nodesWalk.get(xy1);
                        }


                        if (nodesWalk.get(xy2) == null) {
                            nodesWalk.put(xy2, count);
                            graph.addXY(xy2, count);
                            j = count++;
                        } else {
                            
                            j = nodesWalk.get(xy2);
                        }
                        
                        if (ed.getOneWay().equals("n") || ed.getMaxSpeed() == 0) {
                            break;
                        } else if (ed.getOneWay().equals("ft")) {
                            graph.addEdge(new DirectedEdge(i, j, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else if (ed.getOneWay().equals("tf")) {
                            graph.addEdge(new DirectedEdge(j, i, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else {
                            graph.addEdge(new DirectedEdge(i, j, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                            graph.addEdge(new DirectedEdge(j, i, length / ed.getMaxSpeed() / 3.6 + 10, ed));
                        }
                        break;
                }
            }

        }
        System.out.println("ArrayList<MapEdge> edges size is = " + edges.size());
        System.out.println("Edges in the graph = " + graph.E());
        System.out.println("Vertices in the graph = " + graph.V());
        System.out.println(nodesCar.size());
        return graph;
    }

    private static ArrayList<MapEdge> createTree(int type, MapEdge start, MapEdge end) {
        MapNode fn1 = DataLoader.nodes.get(start.getFNode());
        Point2D.Double xy1 = new Point2D.Double(fn1.getX(), fn1.getY());
        MapNode fn2 = DataLoader.nodes.get(end.getFNode());
        Point2D.Double xy2 = new Point2D.Double(fn2.getX(), fn2.getY());
        Integer idStart;
        Integer idEnd;
        EdgeWeightedDigraph graph;
        // HER GÅR DET GALT. Se Controlleren!
        if (type == 0) {
            graph = graphCar;
            idStart = nodesCar.get(xy1);
            idEnd = nodesCar.get(xy2);
        } else {
            graph = graphWalk;
            idStart = nodesWalk.get(xy1);
            idEnd = nodesWalk.get(xy2);
        }
        tree = new AstarSP(graph, idStart, idEnd);
        return shortestPath(idEnd);

    }

    private static ArrayList<MapEdge> shortestPath(int id) {
        ArrayList<MapEdge> edges = new ArrayList<>();
        Iterable<DirectedEdge> list = tree.pathTo(id);
        tree = null; // Help garbage control.
        if (list == null) {
            return edges;
        }
        for (DirectedEdge e : list) {
            edges.add(e.getEdge());
        }
        return edges;
    }

    /**
     *
     * @param type
     * @param start
     * @param end
     * @return
     */
    public static ArrayList<MapEdge> getShortestPath(int type, MapEdge start, MapEdge end) {
        return createTree(type, start, end);
    }
}
