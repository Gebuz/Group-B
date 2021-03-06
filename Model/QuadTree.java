package Model;

import interfaces.MapEdge;
import interfaces.MapNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * top left corner (x,y) and (x1, y1) bottom right corner (x2, y2)
 *
 * @author Johan
 */
public class QuadTree {

    private QuadTree nw, ne, sw, se;
    private final HashMap<Integer, MapNode> nodes;
    private ArrayList<MapEdge> edges;
    private double x, y, length, height;

    /**
     * ID unique to the QuadTree.
     */
    public final String id;

    /**
     * Creates a new QuadTree, should always be followed by a addCoords call
     * @param edges
     * @param nodes
     * @param id
     */
    public QuadTree(ArrayList edges, HashMap nodes, String id) {
        this.edges = edges;
        this.nodes = nodes;
        this.id = id;
    }

    /**
     * Set the top left XY-coordinates of the QuadTree rectangle as well as setting
     * the length and height of the rectangle.
     * @param x The x-coordinate of the top left corner
     * @param y The y-coordinate of the top left corner
     * @param length The length of the rectangle.
     * @param height The height of the rectangle.
     */
    public void addCoords(double x, double y, double length, double height) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.height = height;
    }

    /**
     * Split the QuadTree into SubQuadTrees. This method is called recursively
     * until the smallest QuadTree has less than 500 edges.
     */
    public void split(int size) {
        if (edges.size() > size) {
            double midx = x + length / 2;
            double midy = y + height / 2;

            ArrayList<MapEdge> enw = new ArrayList<>();
            ArrayList<MapEdge> ene = new ArrayList<>();
            ArrayList<MapEdge> esw = new ArrayList<>();
            ArrayList<MapEdge> ese = new ArrayList<>();

            for (MapEdge e : edges) {
                MapNode fn = nodes.get(e.getFNode());
                MapNode tn = nodes.get(e.getTNode());
                if (fn != null && tn != null) {
                    if (fn.getX() <= midx && fn.getY() <= midy) {
                        enw.add(e);
                    } else if (fn.getX() > midx && fn.getY() <= midy) {
                        ene.add(e);
                    } else if (fn.getX() <= midx && fn.getY() > midy) {
                        esw.add(e);
                    } else {
                        ese.add(e);
                    }

                    if (tn.getX() <= midx && tn.getY() <= midy 
                            && !(fn.getX() <= midx && fn.getY() <= midy)) 
                        { enw.add(e); 
                    } else if (tn.getX() > midx && tn.getY() <= midy 
                            && !(fn.getX() > midx && fn.getY() <= midy)) 
                        { ene.add(e); 
                    } else if (tn.getX() <= midx && tn.getY() > midy 
                            && !(fn.getX() <= midx && fn.getY() > midy)) 
                        { esw.add(e); 
                    } else if (tn.getX() > midx && tn.getY() > midy 
                            && !(fn.getX() > midx && fn.getY() > midy)) 
                        { ese.add(e); 
                    }
                }
            }
            
            nw = new QuadTree(enw, nodes, id + "0");
            nw.addCoords(x, y, length / 2, height / 2);
            nw.split(size);
            
            ne = new QuadTree(ene, nodes, id + "1");
            ne.addCoords(midx, y, length / 2, height / 2);
            ne.split(size);
            
            sw = new QuadTree(esw, nodes, id + "2");
            sw.addCoords(x, midy, length / 2, height / 2);
            sw.split(size);
            
            se = new QuadTree(ese, nodes, id + "3");
            se.addCoords(midx, midy, length / 2, height / 2);
            se.split(size);
            
            edges = null;

        }
    }

    /**
     * Get a branch by its id.
     * @param ID    The id of the branch/QuadTree we are looking for.
     * @return returns the QuadTree with the given id.
     */
    public QuadTree getBranch(String ID) {
        if (id.equals(ID)) {
            return this;
        }
        QuadTree qt = this;
        ID = ID.substring(1);
        while (ID.length() > 0 && qt.nw != null) {
            if ('0' == ID.charAt(0)) qt = qt.nw;
            else if ('1' == ID.charAt(0)) qt = qt.ne;
            else if ('2' == ID.charAt(0)) qt = qt.sw;
            else if ('3' == ID.charAt(0)) qt = qt.se;
            else throw new RuntimeException("|" + id + "|");
            
            ID = ID.substring(1);
        }
        return qt;
    }

    /**
     * Get the id of the QuadTree that contains the point (x, y).
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @return Returns the id of the QuadTree that contains the point (x, y).
     */
    public String getID(double x, double y) {
        if (nw == null) return this.id;
        if (nw.canZoom(x, y)) return nw.getID(x, y);
        if (ne.canZoom(x, y)) return ne.getID(x, y);
        if (sw.canZoom(x, y)) return sw.getID(x, y);
        if (se.canZoom(x, y)) return se.getID(x, y);
        return id;
    }
    
     /**
     * Gets the ID of all existing QuadTrees. For testing purposes only.
     * @return a HashSet containing the ID of all existing QuadTrees.
     */
    public HashSet<String> getAll() {
        HashSet<String> hs = new HashSet<>();
        if(nw != null) {
            hs.addAll(nw.getAll());
            hs.addAll(ne.getAll());
            hs.addAll(sw.getAll());
            hs.addAll(se.getAll());
        }
        hs.add(id);
        return hs;
    }

    private boolean canZoom(double x1, double y1) {
        return (    x1 >= x
                &&  y1 >= y
                &&  x1 <= x + length
                &&  y1 <= y + height);
    }

    private boolean canZoom(double x1, double y1, double x2, double y2) {
        return (    x1 > x
                &&  y1 > y
                &&  x2 <= x + length
                &&  y2 <= y + height);
    }

    /**
     * Outdated, use getRoadsImproved instead.
     * @param x1 The x coordinate of the top left corner of the
     * @param y1 The y coordinate of the top left corner of the 
     * @param x2 The x coordinate of the lower right corner of the 
     * @param y2 The y coordinate of the lower right corner of the
     * @return Returns an ArrayList&lt;{@link MapEdge}&gt; of all edges that
     * are inside the lowest QuadTree based on coordinates.
     */
    public ArrayList<MapEdge> getRoads(double x1, double y1, double x2, double y2) {
        if (nw.canZoom(x1, y1, x2, y2)) return nw.getRoads(x1, y1, x2, y2);
        if (ne.canZoom(x1, y1, x2, y2)) return ne.getRoads(x1, y1, x2, y2);
        if (sw.canZoom(x1, y1, x2, y2)) return sw.getRoads(x1, y1, x2, y2);
        if (se.canZoom(x1, y1, x2, y2)) return se.getRoads(x1, y1, x2, y2);
        return getEdges();
    }
    
     /**
     * Returns a HashSet with the ID of all QuadTrees needed to find the 
     * roads in the rectangle drawn by (x1,y1) and (x2, y2). 
     * @param x1 The x coordinate of the top left corner of the
     * @param y1 The y coordinate of the top left corner of the 
     * @param x2 The x coordinate of the lower right corner of the 
     * @param y2 The y coordinate of the lower right corner of the
     * @return Returns a HashSet with the ID of relevant QuadTrees.
     */

    public HashSet<String> getRoadsImproved(double x1, double y1, double x2, double y2) {
        x1 = outOfBoundsX(x1);
        y1 = outOfBoundsY(y1);
        x2 = outOfBoundsX(x2);
        y2 = outOfBoundsY(y2);
        HashSet<String> trees = new HashSet<>();
        
        if(x1 > x2) {
            double xtemp = x1;
            x1 = x2;
            x2 = xtemp;
        }
        if(y1 > y2) {
            double ytemp = y1;
            y1 = y2;
            y2 = ytemp;
        }
        String topLeft  = getID(x1, y1);
        String botRight = getID(x2, y2);
        int maxLength = topLeft.length();
        if (topLeft.equals(botRight) || isParent(topLeft, botRight)) {
            trees.add(topLeft);
            return trees;
        }
        if (isParent(botRight, topLeft)) {
            trees.add(botRight);
            return trees;
        }
        
        String botLeft = getID(x1, y2);
        if(botLeft.length() < maxLength) maxLength = botLeft.length();

        String farRight = getID(x2, y1);
        if(farRight.length() < maxLength) maxLength = farRight.length();
        if(topLeft.length() > maxLength) topLeft = topLeft.substring(0,maxLength);
        if(botLeft.length() > maxLength) botLeft = botLeft.substring(0,maxLength);
        if(farRight.length() > maxLength) farRight = farRight.substring(0,maxLength);
        String tempLeft = topLeft;
        while(true){
            String tempRight = tempLeft;
            
            if(farRight.length() > tempLeft.length()) farRight = farRight.substring(0,tempLeft.length());
            while(true){
                trees.add(tempRight);
                QuadTree qtRight = getBranch(tempRight);
                //System.out.println(qtRight.getEdges().size());
                if(farRight.equals(tempRight) || isParent(tempRight, farRight)) break;
                tempRight = findNeighbor(qtRight, Direction.E).id;
                
            }
            if(botLeft.equals(tempLeft) || isParent(tempLeft, botLeft)) break;
            tempLeft = findNeighbor(getBranch(tempLeft), Direction.S).id;
            farRight = findNeighbor(getBranch(farRight), Direction.S).id;
            if(tempLeft.length() < farRight.length()) farRight = farRight.substring(0,tempLeft.length());
            if(tempLeft.length() > farRight.length()) tempLeft = tempLeft.substring(0,farRight.length());
        }

        return trees;
    }

     /**
     * Find the neighbour QuadTree of a QuadTree qt in the Direction d.
     * @param qt    The QuadTree whose neighbour we want to find.
     * @param d     The {@link Direction} of the neighbour.
     * @return Returns the neighbour of the QuadTree in the specified Direction.
     */
    public QuadTree findNeighbor(QuadTree qt, Direction d) {
        
        if (qt.getDirection() == Direction.None) {
            throw new RuntimeException("FindNeighbor on root.");
        }
        
        if(d != Direction.N && d != Direction.S && d != Direction.W && d != Direction.E) 
            throw new RuntimeException("Can only find N, S, W, or E neighbors");
        
        String tempID = qt.id;
        int n = tempID.length()-1;
        while(n >= 0) {
            String s;
            if(d == Direction.N) s = getNorth(tempID.charAt(n));
            else if(d == Direction.S) s = getSouth(tempID.charAt(n));
            else if(d == Direction.W) s = getWest(tempID.charAt(n));
            else if(d == Direction.E) s = getEast(tempID.charAt(n));
            else throw new RuntimeException("tried to get direction of root.");
            tempID = tempID.substring(0, n) + s.substring(0, 1) + tempID.substring(n+1);
            if(s.contains("halt")) break;
            n--;
        }
        return getBranch(tempID);
    }
    
   /**
     * Find the road closest to a given point (x1, y1).
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @return Returns the MapEdge with the road closest to a given point.
     */
    public MapEdge getClosestRoad(double x, double y, boolean withName) {
        x = outOfBoundsX(x);
        y = outOfBoundsY(y);
        MapEdge ed;
        String ID = getID(x, y);
        double distance;
        ArrayList<MapEdge> a = getBranch(ID).getEdges();
        if(a.isEmpty()) a = this.getParent(getBranch(ID)).getEdges();
        
        ed = a.get(0);
        MapNode edfn = nodes.get(ed.getFNode());
        MapNode edtn = nodes.get(ed.getTNode());
        distance = distanceFromLine(edfn.getX(), edfn.getY(), edtn.getX(), edtn.getY(), x, y);
        
        for(MapEdge e: a){
            MapNode fn = nodes.get(e.getFNode());
            MapNode tn = nodes.get(e.getTNode());
            double d = distanceFromLine(fn.getX(), fn.getY(), tn.getX(), tn.getY(), x, y);
            if(withName) {
                if(d < distance && !(e.getName().equals(""))) {
                    distance = d;
                    ed = e;
                }
            }
            else {
                if(d < distance) {
                distance = d;
                ed = e;
                }
            }
        }
        return ed;
    }
    
    private double distanceFromLine(double ax, double ay, double bx, double by, double cx, double cy)
    {
	double distance;
        double r_numerator = (cx - ax) * (bx - ax) + (cy - ay) * (by - ay);
        double r_denomenator = (bx - ax) * (bx - ax) + (by - ay) * (by - ay);
        double r = r_numerator / r_denomenator;

        double px = ax + r * (bx - ax);
        double py = ay + r * (by - ay);
     
        double s = ((ay - cy) * (bx - ax) - (ax - cx) * (by - ay)) / r_denomenator;

        double distanceLine = Math.abs(s) * Math.sqrt(r_denomenator);


        // (xx,yy) is the point on the lineSegment closest to (cx,cy)

        double xx = px;
        double yy = py;

        if ((r >= 0) && (r <= 1)) {
            distance = distanceLine;
        } else {

            double dist1 = (cx - ax) * (cx - ax) + (cy - ay) * (cy - ay);
            double dist2 = (cx - bx) * (cx - bx) + (cy - by) * (cy - by);
            if (dist1 < dist2) {
                xx = ax;
                yy = ay;
                distance = Math.sqrt(dist1);
            } else {
                xx = bx;
                yy = by;
                distance = Math.sqrt(dist2);
            }

        }

        return distance;
    }


    /**
     * Get the parent of the current QuadTree.
     * @return Returns the parent of the current QuadTree.
     */
    private QuadTree getParent(QuadTree qt)
    {
        return getBranch(qt.id.substring(0, qt.id.length()-1));
    }
    
    private boolean isParent(QuadTree qtFather, QuadTree qtSon) {
        int i = qtFather.id.length();
        return qtFather.id.equals(qtSon.id.substring(0,i));
    }
    
    private boolean isParent(String father, String son) {
        int i = father.length();
        if (i > son.length()) return false;
        return father.equals(son.substring(0,i));
    }

    /**
     * Get the {@link Direction} of the current QuadTree.
     * @return Returns the Direction of the current QuadTree.
     */
    private Direction getDirection()
    {
        if      (id.length() == 1)   return Direction.None;
        else if (id.substring(id.length()-1).equals("0"))   return Direction.NW;
        else if (id.substring(id.length()-1).equals("1"))   return Direction.NE;
        else if (id.substring(id.length()-1).equals("2"))   return Direction.SW;
        else                    return Direction.SE;
    }
    
    private String getNorth(char a) {
        if ('0' == a) return "2, N";
        if ('1' == a) return "3, N";
        if ('2' == a) return "0, halt";
        return "1, halt";
    }
    
    private String getSouth(char a) {
        if ('0' == a) return "2, halt";
        if ('1' == a) return "3, halt";
        if ('2' == a) return "0, S";
        return "1, S";
    }
    
    private String getWest(char a) {
        if ('0' == a) return "1, W";
        if ('1' == a) return "0, halt";
        if ('2' == a) return "3, W";
        return "2, halt";
    }
    
    private String getEast(char a) {
        if ('0' == a) return "1, halt";
        if ('1' == a) return "0, E";
        if ('2' == a) return "3, halt";
        return "2, E";
    }

    /**
     * Gets the middle x-coordinate of the QuadTree.
     * @return returns the middle x-coordinate of the QuadTree.
     */
    public double getMidX() {
        return (x+length)/2;
    }

    /**
     * Gets the middle y-coordinate of the QuadTree.
     * @return returns the middle y-coordinate of the QuadTree.
     */
    public double getMidY() {
        return (y+height)/2;
    }

    /**
     * Gets the HashMap containing all nodes.
     * @return returns the HashMap containing all nodes.
     */
    public HashMap<Integer, MapNode> getNodes()
    {
        return nodes;
    }

    /**
     * Gets all the Edges in the QuadTree.
     * @return Returns all Edges in the QuadTree.
     */
    public ArrayList<MapEdge> getEdges()
    {
        if(nw == null) return edges; //if leaf
        ArrayList<MapEdge> a = new ArrayList<>();
        a.addAll(nw.getEdges());
        a.addAll(ne.getEdges());
        a.addAll(sw.getEdges());
        a.addAll(se.getEdges());
        return a;        
    }
    
    private double outOfBoundsX(double d){
        if(d > CoordinateBoundaries.xMax) return CoordinateBoundaries.xMax;
        else if(d < CoordinateBoundaries.xMin) return CoordinateBoundaries.xMin;
        else return d;
    }
    
    private double outOfBoundsY(double d){
        if(d > CoordinateBoundaries.yMax) return CoordinateBoundaries.yMax;
        else if(d < CoordinateBoundaries.yMin) return CoordinateBoundaries.yMin;
        else return d;        
    }
    
}
