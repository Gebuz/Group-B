
package gui;

/**
 *
 * @author Johann
 */
public enum Colour {
    BLUE, PINK, GREEN;
    
    public Colour getColour(double zC){
        if      (zC  > 0.07)   return BLUE;
        else if (zC  > 0.007)  return PINK;
        else   /*zC <= 0.008*/ return GREEN;
    }
}
