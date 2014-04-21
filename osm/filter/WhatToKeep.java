package osm.filter;

/**
 * Attributes and Tags to keep.
 *
 * @author Sjúrður í Sandagerði
 */
public class WhatToKeep {

    // Array of attributes keys.
    public static final String[] attributesToKeep = new String[]{
        "id",
        "lat",
        "lon"
    };
    
    // Array of attributes values.
    public static final String[] tagsToKeep = new String[]{
        "addr:city",
        "addr:country",
        "addr:housenumber",
        "addr:postcode",
        "addr:street",
        "alt_name",     // Alternative name
        "highway",      // Type of road
        "name",         // Name of the road
        "name:da",      // Name localized to danish
        "maxspeed",
        "minspeed",
        "oneway",       // Oneway, yes, no, -1
        "short_name"
    };
    
    public static final String[] highwayToSkip = new String[]{
        "construction",
        "turning_circle",
        "traffic_signals",
        "stop",
        "street_lamp",
        "give_way",
        "crossing",
        "bus_stop",
        "emergency_access_point",
        "escape",
        "speed_camera"
    };
}
