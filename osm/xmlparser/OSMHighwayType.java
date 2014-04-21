package osm.xmlparser;

/**
 * These enums are types of highways found on the OpenStreetMaps wiki page.
 *
 * @see <a href="href="wiki.openstreetmap.org/wiki/Map_Features#Highway">
 * wiki.openstreetmap.org/wiki/Map_Features#Highway</a>
 * @author Sjúrður í Sandagerði
 */
public enum OSMHighwayType {
    // The principal tags for the road network, from most to least important 

    MOTORWAY("motorway"),
    TRUNK("trunk"),
    PRIMARY("primary"),
    SECONDARY("secondary"),
    TERTIARY("tertiary"),
    UNCLASSIFIED("unclassified"),
    RESIDENTIAL("residential"),
    SERVICE("service"),
    
    //Link roads 
    MOTORWAY_LINK("motorway_link"),
    TRUNK_LINK("trunk_link"),
    PRIMARY_LINK("primary_link"),
    SECONDARY_LINK("secondary_link"),
    TERTIARY_LINK("tertiary_link"),
    
    //Special road types
    LIVING_STREET("living_street"),
    PEDESTRIAN("pedestrian"),
    TRACK("track"),
    BUS_GUIDEWAY("bus_guideway"),
    RACEWAY("raceway"),
    ROAD("road"),
    
    // Paths
    FOOTWAY("footway"),
    BRIDLEWAY("bridleway"),
    STEPS("steps"),
    PATH("path"),
    CYCLEWAY("cycleway"),
    
    // Lifecycle
    PROPOSED("proposed"),
    CONSTRUCTION("construction"),
    
    // Other features
    BUS_STOP("bus_stop"),
    CROSSING("crossing"),
    EMERGENCY_ACCESS_POINT("emergency_access_point"),
    ESCAPE("escape"),
    GIVE_WAY("give_way"),
    PHONE("phone"),
    MINI_ROUNDABOUT("mini_roundabout"),
    MOTORWAY_JUNCTION("motorway_junction"),
    PASSING_PLACE("passing_place"),
    REST_AREA("rest_area"),
    SPEED_CAMERA("speed_camera"),
    STREET_LAMP("street_lamp"),
    SERVICES("services"),
    STOP("stop"),
    TRAFFIC_SIGNALS("traffic_signals"),
    TURNING_CIRCLE("turning_circle");
    
    private final String name;

    private OSMHighwayType(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Get OSMHighwayType from a string. Throws an IllegalArgumentException if no
     * OSMHighwayType is found.
     * @param name Value of the enum to convert.
     * @return Returns highway type
     * @see <a href="http://stackoverflow.com/a/2965252">
     * StackOverflow: Java - Convert String to enum</a>
     */
    public static OSMHighwayType fromString(String name) {
    if (name != null) {
      for (OSMHighwayType h : OSMHighwayType.values()) {
        if (name.equalsIgnoreCase(h.name)) {
          return h;
        }
      }
    }
    return null;
    //throw new IllegalArgumentException("No constant with name " + name + " found");
  }
}
