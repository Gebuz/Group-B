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
 * fix newGraph and newTree to match.
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
            if(ed.ONE_WAY.equals("ft")){
                graph.addEdge(new DirectedEdge(i, j, ed.DRIVETIME));
            } else if (ed.ONE_WAY.equals("tf")) {
                graph.addEdge(new DirectedEdge(j, i, ed.DRIVETIME));
            } else {
                graph.addEdge(new DirectedEdge(i, j, ed.DRIVETIME));
                graph.addEdge(new DirectedEdge(j, i, ed.DRIVETIME));
            }
        }
    }
    
    public void NewTree(MapEdge e){
        MapNode fn = DataLoader.nodes.get(e.getFNode());
        Point2D.Double xy = new Point2D.Double(fn.getX(), fn.getY());
        tree = new SP(graph, nodes.get(xy));
    }
    
    public Iterable<DirectedEdge> shortestPath(MapEdge e){
        MapNode fn = DataLoader.nodes.get(e.getFNode());
        Point2D.Double xy = new Point2D.Double(fn.getX(), fn.getY());
        return tree.pathTo(nodes.get(xy));
    }
}
