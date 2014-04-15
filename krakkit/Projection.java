/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package krakkit;

/**
 *
 * @author Johann
 */
public class Projection {
    private final double height;
    private final double width;
    private final double midLat;
    private final double midLong;

    public Projection(double minLat, double maxLat, double minLong, double maxLong) {
        height = maxLat - minLat;
        width = maxLong - minLong;
        midLat = minLat + height/2;
        midLong = minLong + width/2;
    }

    public double mercatorX(double l) {
        return (l - midLong);
    }
    
    public double mercatorY(double l) {
        double rad = (l - midLat)*Math.PI/180;
        System.out.println(l - midLat);
        return Math.log(Math.tan(Math.PI/4 + rad/2))*180/Math.PI;

    }
    
}
