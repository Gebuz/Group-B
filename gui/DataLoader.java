package gui;

import Model.CoordinateBoundaries;
import Model.MirrorCoordinates;
import Model.Projection;
import interfaces.MapEdge;
import interfaces.MapNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import Coastline.CoastlineLoader;
import krakkit.GeoConvert;
import Coastline.CoastlineEdge;
import Coastline.CoastlineNode;
import GmtShape.GMTParser;
import GmtShape.GMTShape;
import GmtShape.ShapeEdge;
import GmtShape.ShapeNode;
import Model.PathFinder;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import krakkit.KrakEdgeData;
import krakkit.KrakLoader;
import krakkit.KrakNodeData;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import osm.xmlparser.OSMEdgeData;
import osm.xmlparser.OSMNodeData;
import osm.xmlparser.OSMParseHandler;

/**
 * 
 * @author flemmingxu
 * @autor Sjúrður í Sandagerði
 */
public class DataLoader {

    // Edges for roads and coastline
    public static HashMap<Integer, MapNode> nodes = new HashMap<>();
    public static ArrayList<MapEdge> edgesBlue = new ArrayList<>();
    public static ArrayList<MapEdge> edgesPink = new ArrayList<>();
    public static ArrayList<MapEdge> edgesGreen = new ArrayList<>();
    
    // Shape objects for buildings
    public static HashMap<Integer, GMTShape> shapes = new HashMap<>();
    public static HashMap<Integer, MapNode> shapeNodes = new HashMap<>();
    public static ArrayList<MapEdge> shapeEdges = new ArrayList<>();
    
    // Shape objects for land polygons.
    public static HashMap<Integer, GMTShape> shapesLandPolygons = new HashMap<>();
    public static HashMap<Integer, MapNode> landPolygonNodes = new HashMap<>();
    public static ArrayList<MapEdge> landPolygonEdges = new ArrayList<>();
    
    public static boolean isOSM = false;
    
    public static final LoadingBar loadBar = new LoadingBar();
    private int lineCount = 0;
    private int progress = 0;
    
    private String dir = "data/"; // directory where the data files are stored

    public DataLoader(int bool) {

        loadBar.showLoadingBar();

        if (bool == 0) {
            loadKrak();
            
        } else { /* isOSM */
            loadOSM();
            
        }

        PathFinder.createGraph(edgesGreen);
    }

/**
     * Load the Krak data.
     */
    private void loadKrak() {
        // For that, we need to inherit from KrakLoader and override
        // processNode and processEdge. We do that with an 
        // anonymous class. 
        KrakLoader loader = new KrakLoader() {
            @Override
            public void processNode(KrakNodeData nd) {
                
                loadingbarCounter(24009);
                
                nodes.put(nd.getID(), nd);
            }

            @Override
            public void processEdge(KrakEdgeData ed) {
                
                loadingbarCounter(24009);
                
                edgesGreen.add(ed);
                switch (ed.TYP) {
                    case 5:
                    case 6:
                        edgesPink.add(ed);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 31:
                    case 32:
                    case 41:
                    case 42:
                    case 80:
                        edgesPink.add(ed);
                        edgesBlue.add(ed);
                        break;
                }
            }
        };
        try {
            // If your machine slows to a crawl doing inputting, try
            // uncommenting this. 
            // Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

            // Invoke the loader class.
            loader.load(dir + "kdv_node_unload.txt", dir + "kdv_unload_fixedStorebaeltsbroen.txt");
        } catch (IOException ex) {
            System.out.println("ERROR: Could not find kdv_node_unload.txt or "
                    + "kdv_unload.txt in specified directory " + dir);
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        loadBar.bar.setValue(100);
        loadBar.bar.setString("Load complete!");
        loadBar.changeText("Now drawing map. Please wait...");
        
        // Find the boundaries for all nodes.
        CoordinateBoundaries.findBoundaries(nodes);
        MirrorCoordinates.MirrorY(nodes);
        
        loadLandPolygons(dir + "gmt/land_polygon_split_final.gmt", shapesLandPolygons);

        String[] shapefiles = new String[]{
            dir + "gmt/buildings.gmt",
//            dir + "gmt/landuse.gmt",
//            dir + "gmt/natural.gmt"
        };

//        loadShapes(shapefiles, shapes);

        //Mirror coordinates for the shapes and apply UTM32 projection
        for (GMTShape shape : shapes.values()) {
            shape.applyUTMProjection(32);
            shape.mirrorY(CoordinateBoundaries.yMin, CoordinateBoundaries.yMax);
        }
        for (GMTShape shape : shapesLandPolygons.values()) {
            shape.applyUTMProjection(32);
            shape.mirrorY(CoordinateBoundaries.yMin, CoordinateBoundaries.yMax);
        }

        // Create edges and nodes based on the centroid of each shape
        createEdgesAndNodesFromShapes(shapes, shapeNodes, shapeEdges);

        // Create edges and nodes for each land polygon.
        createEdgesAndNodesFromLandPolygons(shapesLandPolygons, 
                landPolygonNodes, landPolygonEdges);
    }
    
    /**
     * Load the OSM data.
     */
    private void loadOSM() {
        OSMParseHandler ph = new OSMParseHandler() {
            @Override
            public void processNode(OSMNodeData nd) {
                loadingbarCounter(73221);
                nodes.put(nd.getID(), nd);
            }

            @Override
            public void processEdge(OSMEdgeData ed) {
                loadingbarCounter(73221);
                edgesGreen.add(ed);
                switch (ed.getType()) {
                    case 0:
                    case 4:
                    case 5:
                    case 6:
                        edgesPink.add(ed);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 31:
                    case 32:
                    case 41:
                    case 42:
                    case 80:
                        edgesPink.add(ed);
                        edgesBlue.add(ed);
                        break;
                }
            }
        };

        try {
            XMLReader reader;
            reader = XMLReaderFactory.createXMLReader();

            reader.setContentHandler(ph);
            reader.parse(dir + "denmark-latest.osm parsed.xml");

        } catch (SAXException ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }



        loadBar.bar.setValue(100);
        loadBar.bar.setString("Load complete!");
        loadBar.changeText("Now drawing map. Please wait...");

        CoordinateBoundaries.findBoundaries(nodes);
        MirrorCoordinates.MirrorY(nodes);

        Projection p = new Projection(CoordinateBoundaries.yMin,
                CoordinateBoundaries.yMax, CoordinateBoundaries.xMin,
                CoordinateBoundaries.xMax);

        for (MapNode nd : nodes.values()) {
            double lon = nd.getX();
            double lat = nd.getY();
            nd.setX(p.mercatorX(lon));
            nd.setY(p.mercatorY(lat));
        }

        loadLandPolygons(dir + "gmt/land_polygon_split_final.gmt", shapesLandPolygons);

        String[] shapefiles = new String[]{
            dir + "gmt/buildings.gmt",
//            dir + "gmt/landuse.gmt",
//            dir + "gmt/natural.gmt"
        };

//        loadShapes(shapefiles, shapes);

        //Mirror coordinates for the shapes and apply Mercator projection.
        for (GMTShape shape : shapes.values()) {
            shape.mirrorY(CoordinateBoundaries.yMin, CoordinateBoundaries.yMax);
            shape.applyMercatorProjection(p);
        }
        for (GMTShape shape : shapesLandPolygons.values()) {
            shape.mirrorY(CoordinateBoundaries.yMin, CoordinateBoundaries.yMax);
            shape.applyMercatorProjection(p);
        }

        // Reset the coordinate boundaries to be based on Mercator projection.
        CoordinateBoundaries.setxMin(p.mercatorX(CoordinateBoundaries.xMin));
        CoordinateBoundaries.setxMax(p.mercatorX(CoordinateBoundaries.xMax));
        CoordinateBoundaries.setyMin(p.mercatorY(CoordinateBoundaries.yMin));
        CoordinateBoundaries.setyMax(p.mercatorY(CoordinateBoundaries.yMax));

        // Create edges and nodes based on the centroid of each shape
        createEdgesAndNodesFromShapes(shapes, shapeNodes, shapeEdges);

        // Create edges and nodes for each land polygon.
        createEdgesAndNodesFromLandPolygons(shapesLandPolygons, 
                landPolygonNodes, landPolygonEdges);
        
    }

    

    /**
     * Load a land polygon file.
     *
     * @param file Land polygon file in the format of '.gmt'.
     */
    private void loadLandPolygons(String file, final HashMap<Integer, GMTShape> landPolygons) {
        GMTParser gmtpLandPolygons = new GMTParser() {
            @Override
            public void processShape(GMTShape shape) {
                if (shape.getxPoly().length > 0) {
                    landPolygons.put(shape.getID(), shape);
                }
            }
        };

        try {
            gmtpLandPolygons.load(file);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Load shape files.
     *
     * Currently we have the following files: 'buildings.gmt' 'natural.gmt'
     * 'landuse.gmt'
     *
     * @param files List of shape files store in .gmt format.
     */
    private void loadShapes(String[] files, final HashMap<Integer, GMTShape> shapes) {
        GMTParser gmtpBuildings = new GMTParser() {
            @Override
            public void processShape(GMTShape shape) {
                shapes.put(shape.getID(), shape);
            }
        };

        try {
            for (String file : files) {
                gmtpBuildings.load(file);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Currently not used since we changed to land polygons.
     *
     * @param lowDetailedFile A lower detailed coastline file to be used to
     * lower zoom levels.
     * @param highDetailedFile A higher detailed coastline file to be used when
     * zooming up close.
     */
    private void osmLoadCoastline(String lowDetailedFile, String highDetailedFile) {
        CoastlineLoader clPinkGreen = new CoastlineLoader(10000000, 20000000) {
            @Override
            public void processNode(CoastlineNode nd) {
                if (nd != null) {
                    loadingbarCounter(73221);

                    nodes.put(nd.getID(), nd);
                }
            }

            @Override
            public void processEdge(CoastlineEdge ed) {
                loadingbarCounter(73221);
                
                edgesPink.add(ed);
                edgesGreen.add(ed);
            }
        };

        CoastlineLoader clBlue = new CoastlineLoader(30000000, 40000000) {
            @Override
            public void processNode(CoastlineNode nd) {
                if (nd != null) {
                    loadingbarCounter(73221);
                    
                    nodes.put(nd.getID(), nd);
                }
            }

            @Override
            public void processEdge(CoastlineEdge ed) {
                loadingbarCounter(73221);
                
                edgesBlue.add(ed);
            }
        };

        try {
            clBlue.load(lowDetailedFile);
            clPinkGreen.load(highDetailedFile);

        } catch (IOException ex) {
            System.out.println("ERROR: Either could not find '"
                    + lowDetailedFile + "' or ");
            System.out.println("could not find '" + highDetailedFile + "'.");
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Currently not used since we changed to land polygons.
     * 
     * Load the coastline data for Krak. Krak has its own coastline loader
     * because we need to apply the WGS84 to UTM32 projection.
     * @param lowDetailedFile A lower detailed coastline file to be used to
     * lower zoom levels.
     * @param highDetailedFile A higher detailed coastline file to be used when
     * zooming up close.
     */
    private void krakLoadCoastline(String lowDetailedFile, String highDetailedFile) {
        CoastlineLoader clPinkGreen = new CoastlineLoader(1000000, 2000000) {
            @Override
            public void processNode(CoastlineNode nd) {
                if (nd != null) {
                    try {
                        double lonToUtm32 = GeoConvert.toUtmX(32, nd.getX(), nd.getY())[0];
                        double latToUtm32 = GeoConvert.toUtmX(32, nd.getX(), nd.getY())[1];
                        nd.setX(lonToUtm32);
                        nd.setY(latToUtm32);
                        
                        loadingbarCounter(24009);
                        
                        nodes.put(nd.getID(), nd);
                    } catch (Exception ex) {
                        Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void processEdge(CoastlineEdge ed) {
                loadingbarCounter(24009);
                
                edgesPink.add(ed);
                edgesGreen.add(ed);
            }
        };

        CoastlineLoader clBlue = new CoastlineLoader(3000000, 4000000) {
            @Override
            public void processNode(CoastlineNode nd) {
                if (nd != null) {
                    try {
                        double lonToUtm32 = GeoConvert.toUtmX(32, nd.getX(), nd.getY())[0];
                        double latToUtm32 = GeoConvert.toUtmX(32, nd.getX(), nd.getY())[1];
                        nd.setX(lonToUtm32);
                        nd.setY(latToUtm32);
                        
                        loadingbarCounter(24009);
                        
                        nodes.put(nd.getID(), nd);
                    } catch (Exception ex) {
                        Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void processEdge(CoastlineEdge ed) {
                loadingbarCounter(24009);
                
                edgesBlue.add(ed);
            }
        };

        try {
            clBlue.load(lowDetailedFile);
            clPinkGreen.load(highDetailedFile);

        } catch (IOException ex) {
            System.out.println("ERROR: Either could not find '"
                    + lowDetailedFile + "' or ");
            System.out.println("could not find '" + highDetailedFile + "'.");
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Helper method for the progress bar counter.
     * @param lineCount 
     */
    private void loadingbarCounter(int countlimit) {
        if (lineCount == countlimit) {
            progress++;
            loadBar.bar.setValue(progress);
            lineCount = 0;
        }
        lineCount++;
    }
    
    /**
     * Helper method to create MapEdges and MapNodes from a collection of 
     * GMTShape objects based on the centroid of each shape.
     * 
     * @param shapes Collection of GMTShape objects.
     * @param nodes HashMap to store the output nodes in.
     * @param edges ArrayList to store the output edges in.
     */
    private void createEdgesAndNodesFromShapes(HashMap<Integer, GMTShape> shapes
            , HashMap<Integer, MapNode> nodes, ArrayList<MapEdge> edges) {
        // Create edges and nodes based on the centroid of each shape
        for (GMTShape shape : shapes.values()) {
            if (shape.getxPoly().length > 0) {
                int id = shape.getID();
                String name = shape.getTYPE();

                double[] centroid = shape.centroid();
                double x = centroid[0];
                double y = centroid[1];
                ShapeNode nd = new ShapeNode(GMTParser.nodeID, x, y);
                nodes.put(nd.getID(), nd);
                edges.add(new ShapeEdge(id, GMTParser.nodeID, GMTParser.nodeID, name));
                GMTParser.nodeID++;
            }
        }
    }
    
     /**
     * Helper method to create MapEdges and MapNodes from a collection of 
     * GMTShapes that are Land Polygons.
     * 
     * @param shapes Collection of GMTShape objects.
     * @param nodes HashMap to store the output nodes in.
     * @param edges ArrayList to store the output edges in.
     */
    private void createEdgesAndNodesFromLandPolygons(
            HashMap<Integer, GMTShape> landPolygons, 
            HashMap<Integer, MapNode> nodes, 
            ArrayList<MapEdge> edges) {
        // Create edges and nodes for each land polygon.
        for (GMTShape shape : landPolygons.values()) {
            if (shape.getxPoly().length > 0) {
                int id = shape.getID();
                String name = shape.getTYPE();

                for (int i = 0; i < shape.getxPoly().length - 1; i++) {
                    int fromID = GMTParser.nodeID++;
                    double fromX = shape.getxPoly()[i];
                    double fromY = shape.getyPoly()[i];

                    int toID = GMTParser.nodeID++;
                    double toX = shape.getxPoly()[i + 1];
                    double toY = shape.getyPoly()[i + 1];

                    ShapeNode fromNode = new ShapeNode(fromID, fromX, fromY);
                    ShapeNode toNode = new ShapeNode(toID, toX, toY);

                    nodes.put(fromNode.getID(), fromNode);
                    if (i == shape.getxPoly().length - 2) {
                        nodes.put(toNode.getID(), toNode);
                    }
                    edges.add(new ShapeEdge(id, fromID, toID, name));
                }

                // Add centroid of each land polygon as well for better
                // accuracy.
                double[] centroid = shape.centroid();
                double x = centroid[0];
                double y = centroid[1];
                ShapeNode nd = new ShapeNode(GMTParser.nodeID, x, y);
                nodes.put(nd.getID(), nd);
                edges.add(new ShapeEdge(id, GMTParser.nodeID, GMTParser.nodeID, name));
                GMTParser.nodeID++;

                // Adding each corner as a point as well for small polygons.
                // This seems to help avoid the disappearing polygons.
                if (shape.getxPoly().length > 0) {
                    for (int i = 0; i < shape.getxPoly().length; i++) {
                        ShapeNode cornerNodes = new ShapeNode(GMTParser.nodeID, shape.getxPoly()[i], shape.getyPoly()[i]);
                        nodes.put(cornerNodes.getID(), cornerNodes);
                        edges.add(new ShapeEdge(id, GMTParser.nodeID, GMTParser.nodeID, name));
                        GMTParser.nodeID++;
                    }
                }
            }
        }
    }
}
