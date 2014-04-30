/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Graph.DirectedEdge;
import Graph.EdgeWeightedDigraph;
import Graph.DijkstraSP;
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
    DijkstraSP tree;
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
     * Creates the graphs for an area.
     *
     * @param type vehicle type
     * @param edges list of edges in the area
     */

    public void newGraph(int type, ArrayList<MapEdge> edges) {
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
                            i = count++;
                        }

                        if (j == null) {
                            nodes.put(xy2, count);
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

    /**
     * Creates the trees for an area.
     *
     * @param ed root of the tree.
     */

    public void newTree(MapEdge ed) {
        MapNode fn = DataLoader.nodes.get(ed.getFNode());
        Point2D.Double xy = new Point2D.Double(fn.getX(), fn.getY());
        tree = new DijkstraSP(graph, nodes.get(xy));
    }

    /**
     *
     * @param ed end road
     * @return returns a list of ordered roads connecting the root (start) to
     * input ed.
     */
    public ArrayList<MapEdge> shortestPath(MapEdge ed) {
        MapNode fn = DataLoader.nodes.get(ed.getFNode());
        Point2D.Double xy = new Point2D.Double(fn.getX(), fn.getY());
        ArrayList<MapEdge> edges = new ArrayList<>();
        Iterable<DirectedEdge> list = tree.pathTo(nodes.get(xy));
        if (list == null) {
            return edges;
        }
        for (DirectedEdge e : list) {
            edges.add(e.getEdge());
        }
        return edges;
    }
    
    public ArrayList<MapEdge> getShortestPath(int type, MapEdge start, MapEdge end){
        newGraph(type, DataLoader.edgesGreen);
        newTree(start);
        return shortestPath(end);
    }

}
