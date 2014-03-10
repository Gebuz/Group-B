/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapofdenmark.GuiDrawLines;

/**
 *
 * @author Johann
 */
public enum Direction{
    N, W, S, E, NW, NE, SW, SE, None;
    
    static public boolean contains(Direction d, Direction e) {
        if (d == NW && (e == N || e == W)) return true;
        if (d == NE && (e == N || e == E)) return true;
        if (d == SW && (e == S || e == W)) return true;
        if (d == SE && (e == S || e == E)) return true;
        return false;
    }
    
    static public Direction minus(Direction d, Direction e) {
        if((d == NW && e == N) || (d == SW && e == S)) return W;
        if((d == NW && e == W) || (d == NE && e == E)) return N;
        if((d == SE && e == S) || (d == NE && e == N)) return E;
        if((d == SE && e == E) || (d == SW && e == W)) return S;
        return d;
    }
    
    static public Direction plus(Direction d, Direction e) {
        if((d == N && e == W) || (d == W && e == N)) return NW;
        if((d == N && e == E) || (d == E && e == N)) return NE;
        if((d == S && e == W) || (d == W && e == S)) return NW;
        if((d == S && e == E) || (d == E && e == S)) return SE;
        return d;
    }
    
    static public Direction opposite(Direction d) {
        if(d == N) return S;
        if(d == W) return E;
        if(d == S) return N;
        if(d == E) return W;
        if(d == NW) return SE;
        if(d == NE) return SW;
        if(d == SW) return NE;
        if(d == SE) return NW;
        return None;
    }
    
}
