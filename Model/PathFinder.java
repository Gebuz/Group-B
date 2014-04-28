/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Graph.DirectedEdge;
import Graph.EdgeWeightedDigraph;
import Graph.SP;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.geom.Point2D;
import interfaces.MapEdge;
import interfaces.MapNode;
import gui.DataLoader;

/**
 * Vehicles:
 * 0 = car
 * 1 = walk
 * 
 * TODO: test
 *
 * @author Johan
 */
public class PathFinder {

    private static final PathFinder pathFinder = new PathFinder();
    EdgeWeightedDigraph graphCar;
    EdgeWeightedDigraph graphWalk;
    SP treeCar;
    SP treeWalk;
    HashMap<Point2D.Double, Integer> nodes;
    int count = 0;

    /**
     *
     */
    public PathFinder() {

    }

    /**
     *  gets the instance of PathFinder
     * @return 
     */
    public static PathFinder getInstance() {
        return pathFinder;
    }

    /**
     * Creates the graphs for an area.
     * @param edges list of edges in the area
     */
    public void addGraphs(ArrayList<MapEdge> edges) {
        graphCar = newGraph(0, edges);
        graphWalk = newGraph(1, edges);
    }

    private EdgeWeightedDigraph newGraph(int type, ArrayList<MapEdge> edges) {
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(edges.size());
        count = 0;
        for (MapEdge ed : edges) {
            if (type == 0) {
                switch (type) {
                    case 8:
                    case 48:
                        break;
                    default:
                        MapNode fn = DataLoader.nodes.get(ed.getFNode());
                        MapNode tn = DataLoader.nodes.get(ed.getTNode());
                        Point2D.Double xy1 = new Point2D.Double(fn.getX(), fn.getY());
                        Point2D.Double xy2 = new Point2D.Double(tn.getX(), tn.getY());
                        Integer i = nodes.get(xy1);
                        Integer j = nodes.get(xy2);

                        if (i == null) {
                            nodes.put(xy1, count);
                            i = count++;
                        }
                        if (i == null) {
                            nodes.put(xy2, count);
                            j = count++;
                        }
                        if (ed.getOneWay().equals("n") || ed.getMaxSpeed() == 0){
                            break;
                        } else if (ed.getOneWay().equals("ft")) {
                            graph.addEdge(new DirectedEdge(i, j, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else if (ed.getOneWay().equals("tf")) {
                            graph.addEdge(new DirectedEdge(i, j, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else {
                            graph.addEdge(new DirectedEdge(i, j, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                            graph.addEdge(new DirectedEdge(j, i, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                        }
                        break;
                }
            } else {
                switch (type) {
                    case 1:
                    case 31:
                    case 41:
                        break;
                    default:
                        MapNode fn = DataLoader.nodes.get(ed.getFNode());
                        MapNode tn = DataLoader.nodes.get(ed.getTNode());
                        Point2D.Double xy1 = new Point2D.Double(fn.getX(), fn.getY());
                        Point2D.Double xy2 = new Point2D.Double(tn.getX(), tn.getY());
                        Integer i = nodes.get(xy1);
                        Integer j = nodes.get(xy2);

                        if (i == null) {
                            nodes.put(xy1, count);
                            i = count++;
                        }
                        if (i == null) {
                            nodes.put(xy2, count);
                            j = count++;
                        }
                        if (ed.getOneWay().equals("ft")) {
                            graph.addEdge(new DirectedEdge(i, j, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else if (ed.getOneWay().equals("tf")) {
                            graph.addEdge(new DirectedEdge(i, j, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else {
                            graph.addEdge(new DirectedEdge(i, j, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                            graph.addEdge(new DirectedEdge(j, i, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                        }
                        break;
                }
            }

        }
        return graph;
    }

    /**
     * Creates the trees for an area.
     * @param ed root of the tree.
     */
    public void createTree(MapEdge ed) {
        treeCar = newTree(graphCar, ed);
        treeWalk = newTree(graphWalk, ed);
    }

    private SP newTree(EdgeWeightedDigraph graph, MapEdge ed) {
        MapNode fn = DataLoader.nodes.get(ed.getFNode());
        Point2D.Double xy = new Point2D.Double(fn.getX(), fn.getY());
        return new SP(graph, nodes.get(xy));
    }

    /**
     * 
     * @param type vehicle
     * @param ed end road
     * @return returns a list of ordered roads connecting the root (start)
     * to input ed.
     */
    public ArrayList<MapEdge> shortestPath(int type, MapEdge ed) {
        SP tree;
        if (type == 0) {
            tree = treeCar;
        } else {
            tree = treeWalk;
        }
        MapNode fn = DataLoader.nodes.get(ed.getFNode());
        Point2D.Double xy = new Point2D.Double(fn.getX(), fn.getY());
        Iterable<DirectedEdge> list = tree.pathTo(nodes.get(xy));
        ArrayList<MapEdge> edges = new ArrayList<>();
        for(DirectedEdge e: list){
            edges.add(e.getEdge());
        }
        return edges;
    }

}
