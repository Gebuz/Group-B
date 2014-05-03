
package osm.xmlparser;

import static osm.xmlparser.OSMHighwayType.RESIDENTIAL;
import static osm.xmlparser.OSMHighwayType.SERVICE;
import static osm.xmlparser.OSMHighwayType.UNCLASSIFIED;

/**
 * Translate the value of a highway to an Integer that is similar to the type
 * list used in the Krak dataset.
 * @author Sjúrður í Sandagerði
 */
public class OSMHighwayTypeIndicator {
    
    private OSMHighwayTypeIndicator(){}
    
    /**
     * 
     * @param highwayValue The value that is associated to a highway tag in OSM.
     * @return Returns an integer indicating what type the highway is. If the
     * highway type is specified then return 0 as a default value, and if no
     * highway enum is found then this method returns -1.
     */
    public static int getType(String highwayValue){
        OSMHighwayType ht = OSMHighwayType.fromString(highwayValue.toLowerCase());
        if (ht != null)
        switch (ht) {
            case MOTORWAY: 
                return 1;
            case TRUNK:
                return 2;
            case PRIMARY:
                return 3;
            case SECONDARY:
                return 4;
            case TERTIARY:
            case RESIDENTIAL:
            case SERVICE:
                return 6;
            case UNCLASSIFIED:
                return 5;
            case MOTORWAY_LINK:
                return 31;
            case TRUNK_LINK:
                return 32;
            case PRIMARY_LINK:
                return 33;
            case SECONDARY_LINK:
                return 34;
            case TERTIARY_LINK:
                return 35;
            case TRACK:
                return 10;
            case PEDESTRIAN:
                return 11;
            case FOOTWAY:
            case BRIDLEWAY:
            case STEPS:
            case PATH:
                return 8;
            default:
                return 99;
        }
        
        return -1;
    }
}
