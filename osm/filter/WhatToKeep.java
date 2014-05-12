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
        "route",        // Ferry routes
        "name",         // Name of the road
        "name:da",      // Name localized to danish
        "maxspeed",
        "minspeed",
        "oneway",       // Oneway, yes, no, -1
        "short_name",
        "motor_vehicle",// ="yes" The ferry allows motor vehicles.
        "motorcar",     // ="yes" The ferry allows motorcars.
        "foot"          // ="yes". The ferry does allow foot passengers.
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
        "speed_camera",
        "proposed",
        "raceway",
       	"bus_guideway",
        //"unclassified"
    };
    
    // What ferry routes to keep for Denmark. This list is manually picked from 
    // the "denmark-latest.osm" file with timestamp="2014-04-12T20:55:01Z"
    public static final String[] ferryRoutesToKeep = new String[]{
        "Agersøfærgen",
        "Allinge - Christiansø",
        "Anholt - Grenå",
        "Assens - Baagø",
        "Ballebro - Hardeshøj",
        "Ballen - Kalundborg",
        "Bandhold - Askø",
        "Barsø Landing - Barsø",
        "Birkholm posten",
        "Branden - Fur",
        "Bøjden - Fynshav",
        "Egense - Hals",
        "Esbjerg - Fanø",
        "Frederikshavn - Læsø",
        "Fåborg - Avernakø",
        "Fåborg - Bjørnø",
        "Fåborg - Lyø",
        "Gudhjem - Ertholmene",
        "Havnebussen",
        "Havnsø - Nekselø",
        "Havnsø - Sejerø",
        "Helsingør - Helsingborg",
        "Hov - Sælvig",
        "Hov - Tunø",
        "Hundested - Rørvig",
        "Hvalpsund - Sundøre",
        "Kalvehave - Lindholm",
        "Kolby Kås - Kalundborg",
        "Kragenæs - Fejø",
        "Kragenæs - Femø",
        "Kulhuse - Sølager",
        "København - Hven",
        "Køge - Rønne",
        "Lodshuse - Enebærodde",
        "Lyø - Avernakø",
        "Marstal - Rudkøbing",
        "Mors - Thy (Feggesund)",
        "Mors - Thy (Næssund)",
        "Nordre Skanse",
        "Nyhavn - Flakfortet",
        "Odden - Ebeltoft",
        "Odden - Århus",
        "Omøfærgen",
        "Orø - Hammer Bakke",
        "Orø - Holbæk",
        "Rudkøbing - Strynø",
        "Rødby - Puttgarden",
        "Rønbjerg - Livø",
        "Skarø - Drejø",
        "Snaptun - Endelave",
        "Snaptun - Hjarnø",
        "Spodsbjerg - Tårs",
        "Struer - Venø",
        "Stubbekøbing - Bogø",
        "Svendborg - Hjortø",
        "Svendborg - Skarø",
        "Svendborg - Ærøskøbing",
        "Sylt - Rømø (List - Havneby)",
        "Søby - Faaborg",
        "Søby - Fynshav",
        "Thyborøn - Agger",
        "Ålborg - Egholm",
        "Årøsund - Årø",
        "Øksnerodde - Fænø"
    };
}
