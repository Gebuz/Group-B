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
 */
public class DataLoader {

    // Edges for roads and coastline
    public static HashMap<Long, MapNode> nodes = new HashMap<>();
    public static ArrayList<MapEdge> edgesBlue = new ArrayList<>();
    public static ArrayList<MapEdge> edgesPink = new ArrayList<>();
    public static ArrayList<MapEdge> edgesGreen = new ArrayList<>();
    
    // Shape objects for buildings
    public static HashMap<Integer, GMTShape> shapesBuilding = new HashMap<>();
    public static HashMap<Long, MapNode> shapeNodesBuilding = new HashMap<>();
    public static ArrayList<MapEdge> shapeEdgesBuilding = new ArrayList<>();
    
    // Shape objects for land polygons.
    public static HashMap<Integer, GMTShape> shapesLandPolygons = new HashMap<>();
    public static HashMap<Long, MapNode> shapeNodesLandPolygons = new HashMap<>();
    public static ArrayList<MapEdge> shapeEdgesLandPolygons = new ArrayList<>();
    
    public static boolean isOSM = false;
    public static final LoadingBar loadBar = new LoadingBar();
    private int lineCount = 0;
    private int progress = 0;

    public DataLoader(int bool) {
        String dir = "data/";
		loadBar.showLoadingBar();
        if (bool == 0) {

            // For that, we need to inherit from KrakLoader and override
            // processNode and processEdge. We do that with an 
            // anonymous class. 
            KrakLoader loader = new KrakLoader() {
                @Override
                public void processNode(KrakNodeData nd) {
                    if(lineCount == 24009) {
                        progress++;
                        loadBar.bar.setValue(progress);
                        lineCount = 0;
                    }
                    lineCount++;
                    nodes.put(nd.getID(), nd);
                }

                @Override
                public void processEdge(KrakEdgeData ed) {
                    if(lineCount == 24009) {                       
                        progress++;
                        loadBar.bar.setValue(progress);            
                        lineCount = 0;
                    }
                    lineCount++;
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
                loader.load(dir + "kdv_node_unload.txt",
                        dir + "kdv_unload.txt");
            } catch (IOException ex) {
                System.out.println("ERROR: Could not find kdv_node_unload.txt or kdv_unload.txt in specified directory " + dir);
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            }


            CoastlineLoader clPinkGreen = new CoastlineLoader(1000000, 2000000) {
                @Override
                public void processNode(CoastlineNode nd) {
                    if (nd != null) {
                        try {
                            double lonToUtm32 = GeoConvert.toUtmX(32, nd.getX(), nd.getY())[0];
                            double latToUtm32 = GeoConvert.toUtmX(32, nd.getX(), nd.getY())[1];
                            nd.setX(lonToUtm32);
                            nd.setY(latToUtm32);
                            if(lineCount == 24009) {                       
                                progress++;
                                loadBar.bar.setValue(progress);
                                lineCount = 0;
                            }
                            lineCount++;
                            nodes.put(nd.getID(), nd);
                        } catch (Exception ex) {
                            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                @Override
                public void processEdge(CoastlineEdge ed) {
                    if(lineCount == 24009) {                       
                        progress++;
                        loadBar.bar.setValue(progress);
                        lineCount = 0;
                    }
                    lineCount++;
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
                            if(lineCount == 24009) {                       
                                progress++;
                                loadBar.bar.setValue(progress);
                                lineCount = 0;
                            }
                            lineCount++;
                            nodes.put(nd.getID(), nd);
                        } catch (Exception ex) {
                            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                @Override
                public void processEdge(CoastlineEdge ed) {
                    if(lineCount == 24009) {                       
                        progress++;
                        loadBar.bar.setValue(progress);
                        lineCount = 0;
                    }
                    lineCount++;
                    edgesBlue.add(ed);
                }
            };

            try {
                clBlue.load(dir + "Coastline 1 to 250.000.dat");
                clPinkGreen.load(dir + "denmark-sweden merged coastline.dat");

            } catch (IOException ex) {
                System.out.println("ERROR: Could not find 'Coastline 1 to 250.000.dat' in specified directory " + dir);
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            }

            loadBar.bar.setValue(100);
            loadBar.bar.setString("Load complete!");
            loadBar.changeText("Now drawing map. Please wait...");
			
            CoordinateBoundaries.findBoundaries(nodes);
            MirrorCoordinates.MirrorY(nodes);


        } else { /* isOSM */

            OSMParseHandler ph = new OSMParseHandler() {
                @Override
                public void processNode(OSMNodeData nd) {
                    if(lineCount == 73221) {
                        progress++;
                        loadBar.bar.setValue(progress);
                        lineCount = 0;
                    }
                    lineCount++;
                    nodes.put(Long.valueOf(nd.getID()), nd);
                }

                @Override
                public void processEdge(OSMEdgeData ed) {
                    if(lineCount == 73221) {
                        progress++;
                        loadBar.bar.setValue(progress);
                        lineCount = 0;
                    }
                    lineCount++;
                    edgesGreen.add(ed);
                    switch (ed.getType()) {
                        case 6:
                            edgesPink.add(ed);
                            break;
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        //case 5:
                        case 31:
                        case 32:
                        case 41:
                        case 42:
                        case 80:
                        case 0:
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
            
            CoastlineLoader clPinkGreen = new CoastlineLoader(10000000, 20000000) {
                @Override
                public void processNode(CoastlineNode nd) {
                    if (nd != null) {
                        if(lineCount == 73221) {
                            progress++;
                            loadBar.bar.setValue(progress);
                            lineCount = 0;
                        }
                        lineCount++;
                        nodes.put(nd.getID(), nd);
                    }
                }

                @Override
                public void processEdge(CoastlineEdge ed) {
                    if(lineCount == 73221) {
                        progress++;
                        loadBar.bar.setValue(progress);
                        lineCount = 0;
                    }
                    lineCount++;
                    edgesPink.add(ed);
                    edgesGreen.add(ed);
                }
            };

            CoastlineLoader clBlue = new CoastlineLoader(30000000, 40000000) {
                @Override
                public void processNode(CoastlineNode nd) {
                    if (nd != null) {
                        if(lineCount == 73221) {
                            progress++;
                            loadBar.bar.setValue(progress);
                            lineCount = 0;
                        }
                        lineCount++;
                        nodes.put(nd.getID(), nd);
                    }
                }

                @Override
                public void processEdge(CoastlineEdge ed) {
                    if(lineCount == 73221) {
                        progress++;
                        loadBar.bar.setValue(progress);
                        lineCount = 0;
                    }
                    lineCount++;
                    edgesBlue.add(ed);
                }
            };

            try {
                clBlue.load(dir + "Coastline 1 to 250.000.dat");
                clPinkGreen.load(dir + "denmark-sweden merged coastline.dat");

            } catch (IOException ex) {
                System.out.println("ERROR: Could not find 'Coastline 1 to 250.000.dat' in specified directory " + dir);
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
            
            long startTime = System.currentTimeMillis();
            
            GMTParser gmtpBuildings = new GMTParser() {

                @Override
                public void processShape(GMTShape shape) {
                    shapesBuilding.put(shape.getID(), shape);
                }
            };
  
            try {
//                gmtpBuildings.load("data/gmt/landuseNew.gmt");
//                gmtpBuildings.load("data/gmt/naturalNew.gmt");
                gmtpBuildings.load("data/gmt/buildingsNew.gmt");

            } catch (FileNotFoundException ex) {
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            }

            GMTParser gmtpLandPolygons = new GMTParser() {

                @Override
                public void processShape(GMTShape shape) {
                    shapesLandPolygons.put(shape.getID(), shape);
                }
            };       
            
            try {
                gmtpLandPolygons.load("data/gmt/land_polygonDenmarkSweden.gmt");

            } catch (FileNotFoundException ex) {
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            }      
            
            for (GMTShape shape : shapesBuilding.values()) {
                shape.mirrorY(CoordinateBoundaries.yMin, CoordinateBoundaries.yMax);
                shape.applyMercatorProjection(p);
            }
            
            for (GMTShape shape : shapesLandPolygons.values()) {
                shape.mirrorY(CoordinateBoundaries.yMin, CoordinateBoundaries.yMax);
                shape.applyMercatorProjection(p);
            }
            
            CoordinateBoundaries.setxMin(p.mercatorX(CoordinateBoundaries.xMin));
            CoordinateBoundaries.setxMax(p.mercatorX(CoordinateBoundaries.xMax));
            CoordinateBoundaries.setyMin(p.mercatorY(CoordinateBoundaries.yMin));
            CoordinateBoundaries.setyMax(p.mercatorY(CoordinateBoundaries.yMax));
            
            for (GMTShape shape : shapesBuilding.values()) {
                
                int id = shape.getID();
                String name = shape.getTYPE();
                
                double[] centroid = shape.centroid();
                double x = centroid[0];
                double y = centroid[1];
                ShapeNode nd = new ShapeNode(GMTParser.nodeID, x, y);
                shapeNodesBuilding.put(nd.getID(), nd);
                shapeEdgesBuilding.add(new ShapeEdge(id, GMTParser.nodeID, GMTParser.nodeID, name));
                GMTParser.nodeID++;
            }

            //GMTParser.nodeID = 0;
            for (GMTShape shape : shapesLandPolygons.values()) {
                
                int id = shape.getID();
                String name = shape.getTYPE();
                
                for (int i = 0; i < shape.getxPoly().length - 1; i++) {
                    int fromID = GMTParser.nodeID++;
                    double fromX = shape.getxPoly()[i];
                    double fromY = shape.getyPoly()[i];
                    
                    int toID = GMTParser.nodeID++;
                    double toX = shape.getxPoly()[i+1];
                    double toY = shape.getyPoly()[i+1];
                    
                    ShapeNode fromNode = new ShapeNode(fromID, fromX, fromY);
                    ShapeNode toNode = new ShapeNode(toID, toX, toY);
                    
                    shapeNodesLandPolygons.put(fromNode.getID(), fromNode);
                    if (i == shape.getxPoly().length - 2) {
                        shapeNodesLandPolygons.put(toNode.getID(), toNode);
                    }
                    shapeEdgesLandPolygons.add(new ShapeEdge(id, fromID, toID, name));
                }
                
                // Add centroid as well.
                double[] centroid = shape.centroid();
                double x = centroid[0];
                double y = centroid[1];
                ShapeNode nd = new ShapeNode(GMTParser.nodeID, x, y);
                shapeNodesLandPolygons.put(nd.getID(), nd);
                shapeEdgesLandPolygons.add(new ShapeEdge(id, GMTParser.nodeID, GMTParser.nodeID, name));
                GMTParser.nodeID++;
                
                if (shape.getxPoly().length < 6) {
                    for (int i = 0; i < shape.getxPoly().length; i++) {
                        ShapeNode cornerNodes = new ShapeNode(GMTParser.nodeID, shape.getxPoly()[i], shape.getyPoly()[i]);
                        shapeNodesLandPolygons.put(cornerNodes.getID(), cornerNodes);
                        shapeEdgesLandPolygons.add(new ShapeEdge(id, GMTParser.nodeID, GMTParser.nodeID, name));
                        GMTParser.nodeID++;
                    }
                }
                
            }
            
            
            long endTime = System.currentTimeMillis();
            System.out.println("Total time to load buildings file was " + (endTime - startTime) + " milliseconds.");
            
        }
		
	long startTime = System.currentTimeMillis();
        PathFinder.createGraph(edgesGreen);
        long endTime = System.currentTimeMillis();
        System.out.println("Total time to create graph: " + (endTime - startTime) + " milliseconds.");
    }
}

