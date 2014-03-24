/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

/**
 *
 * @author Johann
 */
public enum Colour {
    BLUE, PINK, GREEN;
    
    public Colour getColour(double zC){
        if(zC > 0.1) return BLUE;
        else if(zC > 0.01) return PINK;
        else return GREEN;
    }
}
