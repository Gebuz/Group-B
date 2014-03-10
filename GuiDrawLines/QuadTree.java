package GuiDrawLines;

import java.util.ArrayList;
import java.util.HashMap;
import krakkit.EdgeData;
import krakkit.NodeData;

/**
 * top left corner (x,y) and (x1, y1) bottom right corner (x2, y2)
 *
 * @author Johan
 */
public class QuadTree {

    private QuadTree nw, ne, sw, se;
    private final HashMap<Integer, NodeData> nodes;
    private final ArrayList<EdgeData> edges;
    private double x, y, length, height;
    public final int id;

    public QuadTree(ArrayList edges, HashMap nodes, int id) {
        this.edges = edges;
        this.nodes = nodes;
        this.id = id;
    }

    public void addCoords(double x, double y, double length, double height) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.height = height;
    }

    public void split() {
        int i = edges.size();
        if (i > 500) {
            double midx = x + length / 2;
            double midy = y + height / 2;

            ArrayList<EdgeData> enw = new ArrayList<>();
            ArrayList<EdgeData> ene = new ArrayList<>();
            ArrayList<EdgeData> esw = new ArrayList<>();
            ArrayList<EdgeData> ese = new ArrayList<>();

            for (EdgeData e : edges) {
                NodeData fn = nodes.get(e.FNODE);
                NodeData tn = nodes.get(e.TNODE);

                if (fn.X_COORD <= midx && fn.Y_COORD <= midy) {
                    enw.add(e);
                } else if (fn.X_COORD > midx && fn.Y_COORD <= midy) {
                    ene.add(e);
                } else if (fn.X_COORD <= midx && fn.Y_COORD > midy) {
                    esw.add(e);
                } else {
                    ese.add(e);
                }

                if (tn.X_COORD <= midx && tn.Y_COORD <= midy 
                        && !(fn.X_COORD <= midx && fn.Y_COORD <= midy)) 
                    { enw.add(e); 
                } else if (tn.X_COORD > midx && tn.Y_COORD <= midy 
                        && !(fn.X_COORD > midx && fn.Y_COORD <= midy)) 
                    { ene.add(e); 
                } else if (tn.X_COORD <= midx && tn.Y_COORD > midy 
                        && !(fn.X_COORD <= midx && fn.Y_COORD > midy)) 
                    { esw.add(e); 
                } else if (tn.X_COORD > midx && tn.Y_COORD > midy 
                        && !(fn.X_COORD > midx && fn.Y_COORD > midy)) 
                    { ese.add(e); 
                }
            }
            
            nw = new QuadTree(enw, nodes, 4 * id + 1);
            nw.addCoords(x, y, length / 2, height / 2);
            nw.split();
            
            ne = new QuadTree(ene, nodes, 4 * id + 2);
            ne.addCoords(midx, y, length / 2, height / 2);
            ne.split();
            
            sw = new QuadTree(esw, nodes, 4 * id + 3);
            sw.addCoords(x, midy, length / 2, height / 2);
            sw.split();
            
            se = new QuadTree(ese, nodes, 4 * id + 4);
            se.addCoords(midx, midy, length / 2, height / 2);
            se.split();

        }
    }

    //bliver ikke brugt, måske noget med at lade være med at gå ned i træet
    //når hele eller store dele af kortet skal tegnes.
    public double sizeMatch(double x1, double y1, double x2, double y2) {
        if (x1 > x
                || y1 > y
                || x2 < x + length
                || y2 < y + height) {
            return -1.0;
        }
        double sizematch = x2 / x;
        double temp = x1 / (x + length);
        if (temp > sizematch) {
            sizematch = temp;
        }
        temp = y2 / y;
        if (temp > sizematch) {
            sizematch = temp;
        }
        temp = y1 / (y + height);
        if (temp > sizematch) {
            sizematch = temp;
        }
        return sizematch;
    }

    public QuadTree getBranch(int id) {
        if (this.id == id) {
            return this;
        }
        String s = "";
        QuadTree qt = this;
        while (id > 0) {
            if (0 == (id - 1) % 4) {
                s = s + "0";
            } else if (1 == (id - 1) % 4) {
                s = s + "1";
            } else if (2 == (id - 1) % 4) {
                s = s + "2";
            } else {
                s = s + "3";
            }
            id = (id - 1) / 4;
        }
        while (s.length() > 0 && qt != null) {
            if ('0' == s.charAt(s.length() - 1)) {
                qt = qt.nw;
            }
            if ('1' == s.charAt(s.length() - 1)) {
                qt = qt.ne;
            }
            if ('2' == s.charAt(s.length() - 1)) {
                qt = qt.sw;
            }
            if ('3' == s.charAt(s.length() - 1)) {
                qt = qt.se;
            }
            s = s.substring(0, s.length() - 1);
        }
        if (qt == null) {
            qt = qt.getParent();
        }
        return qt;
    }

    public int getID(double x1, double y1) {
        if (nw.canZoom(x1, y1)) {
            return nw.getID(x1, y1);
        }
        if (ne.canZoom(x1, y1)) {
            return ne.getID(x1, y1);
        }
        if (sw.canZoom(x1, y1)) {
            return sw.getID(x1, y1);
        }
        if (se.canZoom(x1, y1)) {
            return se.getID(x1, y1);
        }
        return id;
    }

    private boolean canZoom(double x1, double y1) {
        return (    x1 > x
                ||  y1 > y
                ||  x1 < x + length
                ||  y1 < y + height);
    }

    private boolean canZoom(double x1, double y1, double x2, double y2) {
        return (x1 > x
                || y1 > y
                || x2 < x + length
                || y2 < y + height);
    }

    public ArrayList<EdgeData> getRoads(double x1, double y1, double x2, double y2) {
        if (nw.canZoom(x1, y1, x2, y2)) {
            return nw.getRoads(x1, y1, x2, y2);
        }
        if (ne.canZoom(x1, y1, x2, y2)) {
            return ne.getRoads(x1, y1, x2, y2);
        }
        if (sw.canZoom(x1, y1, x2, y2)) {
            return sw.getRoads(x1, y1, x2, y2);
        }
        if (se.canZoom(x1, y1, x2, y2)) {
            return se.getRoads(x1, y1, x2, y2);
        }
        return edges;
    }

    public ArrayList<EdgeData> getRoadsImproved(double x1, double y1, double x2, double y2) {
        int topLeft = getID(x1, y1);
        int botRight = getID(x2, y2);
        if (topLeft == botRight) {
            return getBranch(topLeft).edges;
        }
        ArrayList<EdgeData> zoomEdges = new ArrayList<>();
        //mangler resten af metoden.
        return zoomEdges;
    }

    private QuadTree findNeighbor(QuadTree qt, Direction d) {
        if (qt.getDirection() == Direction.None) {
            throw new RuntimeException("FindNeighbor on root.");
        }
        if (!Direction.contains(qt.getDirection(), d)) {
            QuadTree p = getParent();
            Direction e = Direction.minus(qt.getDirection(), Direction.opposite(d));
            Direction f = Direction.plus(d, e);
            if (e == Direction.NW) {
                return p.nw;
            }
            if (e == Direction.NE) {
                return p.ne;
            }
            if (e == Direction.SW) {
                return p.sw;
            }
            if (e == Direction.SE) {
                return p.se;
            }
        }
        QuadTree neighbor = this;
        QuadTree p = qt;
        String s = "";
        while (p.getDirection() != Direction.None && !Direction.contains(p.getDirection(), d)) { //skal stoppe ved fælles forældre (virker ikke)
            if (0 == (p.id - 1) % 4) {
                s = s + "0";
            } else if (1 == (p.id - 1) % 4) {
                s = s + "1";
            } else if (2 == (p.id - 1) % 4) {
                s = s + "2";
            } else {
                s = s + "3";
            }
        }
        if (d == Direction.N || d == Direction.S) {
            while (s.length() > 0 && neighbor != null) {
                if (0 == s.charAt(s.length() - 1)) {
                    neighbor = neighbor.sw;
                }
                if (0 == s.charAt(s.length() - 1)) {
                    neighbor = neighbor.se;
                }
                if (0 == s.charAt(s.length() - 1)) {
                    neighbor = neighbor.nw;
                }
                if (0 == s.charAt(s.length() - 1)) {
                    neighbor = neighbor.ne;
                }
                s = s.substring(0, s.length() - 1);
            }
        } else {
            while (s.length() > 0 && neighbor != null) {
                if (0 == s.charAt(s.length() - 1)) {
                    neighbor = neighbor.ne;
                }
                if (0 == s.charAt(s.length() - 1)) {
                    neighbor = neighbor.nw;
                }
                if (0 == s.charAt(s.length() - 1)) {
                    neighbor = neighbor.se;
                }
                if (0 == s.charAt(s.length() - 1)) {
                    neighbor = neighbor.sw;
                }
                s = s.substring(0, s.length() - 1);
            }
        }
        if (neighbor == null) {
            neighbor = neighbor.getParent();
        }
        return neighbor;
    }

    private QuadTree getParent()
    {
        return getBranch((id - 1) / 4);
    }

    private Direction getDirection()
    {
        if (id == 0) {
            return Direction.None;
        }
        if (id % 4 == 0) {
            return Direction.NW;
        }
        if (id % 4 == 1) {
            return Direction.NE;
        }
        if (id % 4 == 2) {
            return Direction.SW;
        }
        return Direction.SE;
    }

    public void doStuff(double x1, double y1, double x2, double y2)
    {
        if (x2 < x
                || y2 < y
                || x1 > x + length
                || y1 > y + length) {
            return;
        }
        //dostuff to data
        if (nw != null) {
            nw.doStuff(x1, y1, x2, y2);
        }
        if (ne != null) {
            ne.doStuff(x1, y1, x2, y2);
        }
        if (sw != null) {
            sw.doStuff(x1, y1, x2, y2);
        }
        if (se != null) {
            se.doStuff(x1, y1, x2, y2);
        }
    }

    public HashMap<Integer, NodeData> getNodes()
    {
        return nodes;
    }

    public ArrayList<EdgeData> getEdges()
    {
        return edges;
    }
}
