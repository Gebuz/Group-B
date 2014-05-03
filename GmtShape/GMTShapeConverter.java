package GmtShape;

import java.awt.geom.Path2D;

/**
 * This class is used to convert a GMTShape to a Path2D object that represents a
 * Shape in Swing.
 *
 * @author Sjúrður í Sandagerði
 */
public class GMTShapeConverter {

    private GMTShapeConverter() {}
    
    /**
     * Create a polygon from a (@link GMTShape) shape.
     *
     * @param shape
     * @return Returns a Path2D object containing the shape of the polygon.
     *
     * @see <a
     * href="http://stackoverflow.com/questions/15620590/polygons-with-double-coordinates">
     * Polygons with double coordinates - Stack Overflow</a>
     */
    public static Path2D createPolygon(GMTShape shape, double k, double xk, double yk, double xMin, double yMin, double zoomConstant, double resizeConstant, double pressX, double pressY) {
        Double[] xPoly = shape.getxPoly();
        Double[] yPoly = shape.getyPoly();

        Path2D path = new Path2D.Double();

        path.moveTo((((    (xPoly[0] - xMin)/k + xk) - pressX)/zoomConstant)/resizeConstant, ((((yPoly[0] - yMin)/k + yk) - pressY)/zoomConstant)/resizeConstant);

        for (int i = 1; i < xPoly.length; ++i) {
            path.lineTo(((((xPoly[i] - xMin)/k + xk) - pressX)/zoomConstant)/resizeConstant, ((((yPoly[i] - yMin)/k + yk) - pressY)/zoomConstant)/resizeConstant);
        }
        path.closePath();

        return path;
    }
}
