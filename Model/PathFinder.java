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
    EdgeWeightedDigraph graph;
    AstarSP tree;
    HashMap<Point2D.Double, Integer> nodes;
    int count;    

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
    public void createGraph(int type, ArrayList<MapEdge> edges) {
        graph = null;
        EdgeWeightedDigraph grapht = new EdgeWeightedDigraph(edges.size());
        count = 0;
        nodes = new HashMap<>();
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
                        Integer i = nodes.get(xy1);
                        Integer j = nodes.get(xy2);

                        if (i == null) {
                            nodes.put(xy1, count);
                            grapht.addXY(xy1, count);
                            i = count++;
                        }

                        if (j == null) {
                            nodes.put(xy2, count);
                            grapht.addXY(xy2, count);
                            j = count++;
                        }

                        if (ed.getOneWay().equals("n") || ed.getMaxSpeed() == 0) {
                            break;
                        } else if (ed.getOneWay().equals("ft")) {
                            grapht.addEdge(new DirectedEdge(i, j, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else if (ed.getOneWay().equals("tf")) {
                            grapht.addEdge(new DirectedEdge(i, j, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else {
                            grapht.addEdge(new DirectedEdge(i, j, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                            grapht.addEdge(new DirectedEdge(j, i, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
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
                        Integer i;
                        Integer j;

                        if (nodes.get(xy1) == null) {
                            nodes.put(xy1, count);
                            i = count++;
                        } else {
                            i = nodes.get(xy1);
                        }

                        if (nodes.get(xy2) == null) {
                            nodes.put(xy2, count);
                            j = count++;
                        } else {
                            j = nodes.get(xy1);
                        }

                        if (ed.getOneWay().equals("n") || ed.getMaxSpeed() == 0) {
                            break;
                        } else if (ed.getOneWay().equals("ft")) {
                            grapht.addEdge(new DirectedEdge(i, j, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else if (ed.getOneWay().equals("tf")) {
                            grapht.addEdge(new DirectedEdge(i, j, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                        } else {
                            grapht.addEdge(new DirectedEdge(i, j, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                            grapht.addEdge(new DirectedEdge(j, i, ed.getLength() / ed.getMaxSpeed() / 3.6 + 10, ed));
                        }
                        break;
                }
            }

        }
        graph = grapht;
    }

    private ArrayList<MapEdge> createTree(MapEdge start, MapEdge end) {
        MapNode fn1 = DataLoader.nodes.get(start.getFNode());
        Point2D.Double xy1 = new Point2D.Double(fn1.getX(), fn1.getY());
        MapNode fn2 = DataLoader.nodes.get(end.getFNode());
        Point2D.Double xy2 = new Point2D.Double(fn2.getX(), fn2.getY());
        int idEnd = nodes.get(xy2);
        tree = new AstarSP(graph, nodes.get(xy1), idEnd);
        return shortestPath(idEnd);
        
    }

    private ArrayList<MapEdge> shortestPath(int id) {
        ArrayList<MapEdge> edges = new ArrayList<>();
        Iterable<DirectedEdge> list = tree.pathTo(id);
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
    public ArrayList<MapEdge> getShortestPath(int type, MapEdge start, MapEdge end){
        return createTree(start, end);
    }

}
