/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Johann
 */
public class Projection {

    private final double mapHeight;
    private final double mapWidth;
    private final double midLat;
    private final double midLong;

    public Projection(double minLat, double maxLat, double minLong, double maxLong) {
        mapHeight = maxLat - minLat;
        mapWidth = maxLong - minLong;
        midLat = minLat + mapHeight / 2;
        midLong = minLong + mapWidth / 2;
    }

    public double mercatorX(double longitude) {
        return (longitude - midLong);
    }

    public double mercatorY(double latitude) {
        return Math.toDegrees(Math.log(Math.tan(Math.PI/4+Math.toRadians(latitude)/2)));
    }
}
