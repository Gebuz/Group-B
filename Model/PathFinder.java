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
 * TODO:
 * add multiple fields for multiple vehicles (both graphs and trees).
 * make NewGraph private and make a new method that creates all graphs needed.
 * fix weight of edges.
 * fix oneway.
 * unit test
 * @author Johan
 */
public class PathFinder {
    private static final PathFinder pathFinder = new PathFinder();
    EdgeWeightedDigraph graph;
    SP tree;
    HashMap<Point2D.Double, Integer> nodes;
    int count = 0;
    
    public PathFinder(){

    }
    
    public static PathFinder getInstance() {
        return pathFinder;
    }
    
    public void NewGraph(ArrayList<MapEdge> edges){
        graph = new EdgeWeightedDigraph(edges.size());
        count = 0;
        for(MapEdge ed: edges) {
            MapNode fn = DataLoader.nodes.get(ed.getFNode());
            MapNode tn = DataLoader.nodes.get(ed.getTNode());
            Point2D.Double xy1 = new Point2D.Double(fn.getX(), fn.getY());
            Point2D.Double xy2 = new Point2D.Double(tn.getX(), tn.getY());
            Integer i = nodes.get(xy1);
            Integer j = nodes.get(xy2);
            
            if(i == null) {
                nodes.put(xy1, count);
                i = count++;
            }
            if(i == null) {
                nodes.put(xy2, count);
                j = count++;
            }
            if(ed.getOneWay().equals("ft")){
                graph.addEdge(new DirectedEdge(i, j, ed.getLength()/ed.getType(), ed));
            } else if (ed.getOneWay().equals("tf")) {
                graph.addEdge(new DirectedEdge(i, j, ed.getLength()/ed.getType(), ed));
            } else {
                graph.addEdge(new DirectedEdge(i, j, ed.getLength()/ed.getType(), ed));
                graph.addEdge(new DirectedEdge(j, i, ed.getLength()/ed.getType(), ed));
            }
        }
    }
    
    public void NewTree(MapEdge ed){
        MapNode fn = DataLoader.nodes.get(ed.getFNode());
        Point2D.Double xy = new Point2D.Double(fn.getX(), fn.getY());
        tree = new SP(graph, nodes.get(xy));
    }
    
    public Iterable<DirectedEdge> shortestPath(MapEdge ed){
        MapNode fn = DataLoader.nodes.get(ed.getFNode());
        Point2D.Double xy = new Point2D.Double(fn.getX(), fn.getY());
        return tree.pathTo(nodes.get(xy));
    }
}
