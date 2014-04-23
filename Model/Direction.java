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
public enum Direction{
    N, W, S, E, NW, NE, SW, SE, None;
    
    static public boolean contains(Direction d, Direction e) {
        if      (d == NW &&  (e == N || e == W)) return true;
        else if (d == NE &&  (e == N || e == E)) return true;
        else if (d == SW &&  (e == S || e == W)) return true;
        else if (d == SE &&  (e == S || e == E)) return true;
        else                                     return false;
    }
    
    static public Direction minus(Direction d, Direction e) {
        if      ((d == NW &&  e == N) || (d == SW && e == S)) return W;
        else if ((d == NW &&  e == W) || (d == NE && e == E)) return N;
        else if ((d == SE &&  e == S) || (d == NE && e == N)) return E;
        else if ((d == SE &&  e == E) || (d == SW && e == W)) return S;
        else                                                   return d;
    }
    
    static public Direction plus(Direction d, Direction e) {
        if      ((d == N && e == W) || (d == W && e == N))  return NW;
        else if ((d == N && e == E) || (d == E && e == N))  return NE;
        else if ((d == S && e == W) || (d == W && e == S))  return NW;
        else if ((d == S && e == E) || (d == E && e == S))  return SE;
        else                                                 return d;
    }
    
    static public Direction opposite(Direction d) {
        if      (d == N)  return S;
        else if (d == W)  return E;
        else if (d == S)  return N;
        else if (d == E)  return W;
        else if (d == NW) return SE;
        else if (d == NE) return SW;
        else if (d == SW) return NE;
        else if (d == SE) return NW;
        else              return None;
    }
    
}
