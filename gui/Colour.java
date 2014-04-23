
package gui;

/**
 *
 * @author Johann
 */
public enum Colour {
    BLUE, PINK, GREEN;
    
    public Colour getColour(double zC){
        if      (zC  > 0.1)   return BLUE;
        else if (zC  > 0.01)  return PINK;
        else   /*zC <= 0.01*/ return GREEN;
    }
}
