package GmtShape;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to parse a GMT file.
 *
 * @author Sjúrður í Sandagerði
 */
public abstract class GMTParser {

    static int id = 0;
    static String name = "";
    static String type = "";
    
    // Boundaries for the current file.
    public static double xMin = 0.0;
    public static double xMax = 0.0;
    public static double yMin = 0.0;
    public static double yMax = 0.0;

    // keep track of the last nodeID
    public static int nodeID = 0;
    
    public abstract void processShape(GMTShape shape);

    public static String[] parseColumnHeader(String line) {
        String tempLine = line;
        try {
            tempLine = line.substring(4);
        } catch (Exception ex) {
            Logger.getLogger(GMTParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (tempLine.contains("|")) {
            String[] output = tempLine.split("\\|", -1);
            output[1] = output[1].replaceAll("\"", "");
            return output;
        } else {
            String[] output = new String[3];
            output[0] = tempLine;
            output[2] = "landPolygon";
            return output;
        }
        
    }

    public static boolean isPolygon(String line) {
        return line.startsWith("# @P");
    }

    public static void resetVariables() {
        id = 0;
        name = "";
        type = "";
    }

    public static Double[] toDoubleArray(ArrayList<Double> arraylist) {
        Double[] d = new Double[arraylist.size()];
        return arraylist.toArray(d);
    }

    public void load(String file) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

        String line;
        
        boolean insidePolygon = false;
        
        ArrayList<Double> xPoly = new ArrayList<>();
        ArrayList<Double> yPoly = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (line.startsWith(">")) {
                if (!xPoly.isEmpty()) {
                    processShape(new GMTShape(id, name, type, toDoubleArray(xPoly), toDoubleArray(yPoly)));
                }
                xPoly.clear();
                yPoly.clear();
                resetVariables();
                insidePolygon = false;
            } else if (line.contains("# @R")) {
                try {
                    line = line.substring(4);
                    String[] boundaries = line.trim().split("/");
                    if (boundaries.length == 4) {
                        xMin = Double.parseDouble(boundaries[0]);
                        xMax = Double.parseDouble(boundaries[1]);
                        yMin = Double.parseDouble(boundaries[2]);
                        yMax = Double.parseDouble(boundaries[3]);
                    }
                    
                } catch (NumberFormatException ex) {
                    Logger.getLogger(GMTParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (line.contains("# @D")) {

                String[] headers = parseColumnHeader(line);
                try {
                    GMTParser.id = Integer.parseInt(headers[0]);
                    
                } catch (NumberFormatException ex) {
                    Logger.getLogger(GMTParser.class.getName()).log(Level.SEVERE, null, ex);

                }
                GMTParser.name = headers[1];
                GMTParser.type = headers[2];
            } else if (isPolygon(line)) {
                insidePolygon = true;
                continue;
            }

            if (insidePolygon) {
                String[] coordinates = line.split("\\s");
                try {
                    double x = Double.parseDouble(coordinates[0]);
                    double y = Double.parseDouble(coordinates[1]);
                    xPoly.add(x);
                    yPoly.add(y);
                } catch (NumberFormatException ex) {
                    Logger.getLogger(GMTParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        // Process the last shape in the file as well.
        processShape(new GMTShape(id, name, type, toDoubleArray(xPoly), toDoubleArray(yPoly)));
        
    }
}
