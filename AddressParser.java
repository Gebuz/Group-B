package mapofdenmark;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Group B
 */
public class AddressParser
{

    static String[] roadNameSpecialCasesWithI = new String[]{
        "A I Holms Vej",
        "H I Bies Gade",
        "H I Bies Plads",
        "H I Hansens Vej",
        "H I Park",
        "L I Brandes Allé",
        "N I Fjords Allé",
        "N I Møllers Plads",
        "Sønder i By",
        "Vägen I Dalen"
    };
    static String[] roadNameSpecialsCases = new String[]{
        "Brabrand Haveforening af 1967",
        "Godthåb 1952 Haveforening",
        "HAVEFORENING AF 1934",
        "Haveforening af 1941",
        "Haveforeningen af 10. maj 1918",
        "Haveforeningen af 1907",
        "Haveforeningen af 1918",
        "Haveforeningen af 1940",
        "Haveforeningen af 4. Juli 1917",
        "Haveforeningen af 4. Maj 1921",
        "Haveselskab af 1916",
        "Haveselskabet 1948"
    };

    /**
     * Takes an address and divides it into a String Array.
     *
     * @param str
     * @return Address divided into a String Array (street, building#, building
     * letter, floor, city)
     * @throws InputException
     */
    public String[] addressParser(String str) throws InputException
    {
        String[] output = new String[]{"", "", "", "", "", ""};
        String[] compileStrings = new String[]{
            "\\d",
            "(?i)(.*?)[\\s,]{1,2}i\\s(.*?)$",
            "(\\d{4})[\\s,]{0,2}(\\D*?)$",
            "(\\d{1,3})([a-zA-Z]?)[\\s,]{1,2}(\\d{1,2})[\\.]?[^\\d]",
            "[^\\d](\\d{1,3})([a-zA-Z]?)($|([\\s,]{1,2}\\D*$))",
            "(?i)(.*?)[\\s,]{1,2}i\\s(.*?)$"
        };

        String input;

        // Clean up bad input.
        System.out.println("Før: " + str);
        input = cleanUp(str);
        System.out.println("Eft: " + input);

        validateName(input);

        // Check special cases of road names with " i "
        if (input.toLowerCase().contains(" i ")) {
            for (String specialRoadName : roadNameSpecialCasesWithI) {
                if (input.toLowerCase().contains(specialRoadName.toLowerCase())) {
                    output[0] = specialRoadName;
                    input = input.substring(specialRoadName.length());
                }
            }
        }

        // Check special cases of road names with " i "
        if (input.toLowerCase().contains("haveforening")
                || input.toLowerCase().contains("haveselskab")) {
            for (String specialRoadName : roadNameSpecialsCases) {
                if (input.toLowerCase().contains(specialRoadName.toLowerCase())) {
                    output[0] = specialRoadName;
                    input = input.substring(specialRoadName.length());
                }
            }
        }

        Pattern pNumber = Pattern.compile(compileStrings[0]); // Tal
        Matcher mNumber = pNumber.matcher(input);

        // No numbers in input
        if (!mNumber.find()) { //1
            Pattern pRoadCity = Pattern.compile(compileStrings[1]);
            Matcher mRoadCity = pRoadCity.matcher(input);
            if (mRoadCity.find()) { //2
                if (output[0].equals("")) {
                    output[0] = mRoadCity.group(1);
                }
                output[5] = mRoadCity.group(2);
            } else {
                if (output[0].equals("") && output[5].equals("")) {
                    output[0] = input.replaceAll("^\\s", "") // removes spaces
                            .replaceAll("\\s$", "");;
                } else if (!output[0].equals("")) {
                    output[5] = input.replaceAll("^\\s", "") // removes spaces
                            .replaceAll("\\s$", "");;
                }
            }

            // Numbers in input.
        } else {

            // Looks for zipcode in input.
            Pattern pZipCodeCity = Pattern.compile(compileStrings[2]);
            Matcher mZipCodeCity = pZipCodeCity.matcher(input);
            if (mZipCodeCity.find()) { //3
                output[4] = mZipCodeCity.group(1); // Zipcode
                output[5] = mZipCodeCity.group(2) // City name
                        .replaceAll("^\\s", "") // removes spaces
                        .replaceAll("\\s$", "");
                String match = mZipCodeCity.group();
                int matchlen = match.length();
                int inputlen = input.length();
                input = input.substring(0, inputlen - matchlen);
            }

            // Looks for building number and floor.
            Pattern pHouseFloor = Pattern.compile(compileStrings[3]);
            Matcher mHouseFloor = pHouseFloor.matcher(input);

            if (mHouseFloor.find()) { //4
                String match = mHouseFloor.group();

                output[1] = mHouseFloor.group(1); // House number
                output[2] = mHouseFloor.group(2); // House letter
                output[3] = mHouseFloor.group(3); // Floor

                if (output[0].equals("")) {
                    output[0] = input.substring(0, input.indexOf(match)) // Road name
                            .replaceAll("^\\s", "") // Removes space.
                            .replaceAll("\\s$", "");
                }

                int matchlen = match.length();
                if (output[5].equals("")) { //5
                    // Set the City to be the rest of the string exceeding the match
                    output[5] = input.substring(input.indexOf(match) + matchlen)
                            .replaceAll("^\\s", "") // Removes space.
                            .replaceAll("\\s$", "");
                }
                return output;
            }

            // bulding number.
            Pattern pHouseNumber = Pattern.compile(compileStrings[4]);
            Matcher mHouseNumber = pHouseNumber.matcher(input);
            if (mHouseNumber.find()) { //6

                output[1] = mHouseNumber.group(1); // bulding number
                output[2] = mHouseNumber.group(2); // building letter
                if (output[5].equals("")) { //7
                    output[5] = mHouseNumber.group(3) // cityname
                            .replaceAll("^\\s", "") // Removes spaces
                            .replaceAll("\\s$", "");
                }

                String match = mHouseNumber.group();
                int matchlen = match.length();
                int inputlen = input.length();
                if (output[0].equals("")) {
                    output[0] = input.substring(0, inputlen - matchlen)
                            .replaceAll("^\\s", "") // Removes spaces
                            .replaceAll("\\s$", "");
                }
            } else {
                if (output[0].equals("")) {
                    Pattern pRoadCity = Pattern.compile(compileStrings[5]);
                    Matcher mRoadCity = pRoadCity.matcher(input);
                    if (mRoadCity.find()) {
                        output[0] = mRoadCity.group(1).replaceAll("^\\s", "") // Removes spaces
                                .replaceAll("\\s$", "");
                        output[5] = mRoadCity.group(2).replaceAll("^\\s", "") // Removes spaces
                                .replaceAll("\\s$", "");
                    } else {
                        output[0] = input.replaceAll("^\\s", "") // Removes spaces
                                .replaceAll("\\s$", "");
                    }
                }
            }
        }

        return output;
    }

    /**
     * Removes all unnecessary information.
     *
     * @param str
     * @return
     */
    public String cleanUp(String str)
    {
        str = str.replaceAll("[\\s]?\\b(?i:etage|sal|kld)\\b\\.?,?", ""); // (?i:) case insensitive
        str = str.replaceAll("[\\s]?\\b(?i:tv|th|mf)\\b\\.?,?", ""); // Manglede et "\b"
        str = str.replaceAll("[\\s]?\\b(?i:st)\\b\\.?,?", "0"); // Manglede et "\b"
        str = str.replaceAll("--+", "-");
        str = str.replaceAll("\\.\\.+", "\\.");
        str = str.replaceAll("\\s+,", ",");
        str = str.replaceAll(",,+", ",");

        return str;
    }

    //Prints input and output.
    private void print(String input, String[] output) throws IOException
    {
        String sAllOut = output[0];
        for (int i = 1; i < 6; i++) {
            sAllOut += "#" + output[i];
        }

        System.out.println("input: " + input);
        try {
            boolean exists = validate(output[0]);
        } catch (InputException ex) {
            System.out.println("input: " + input);
            System.out.println(ex.getMessage());

        }
        System.out.println("output: " + sAllOut);
        System.out.println("");
    }

    //Validates the input.
    private void validateName(String name) throws InputException
    {
        //System.out.println("Name = " + name);
        if (name.equals("") || !name.replaceAll( //1
                "[a-zA-ZåæøÅÆØ0éÉäÄüÜöÖèÈâÂôÔêÊîÎûÛ0-9\\(\\)'\\-,\\./\\s`´:\\&]", "").equals("")) {
            throw new InputException("Invalid address.");
        }
    }

    //Validates the road name. Should be changed to use binary search.
    private boolean validate(String road) throws IOException, InputException
    {
        if (road == null ||road.equals(""))
            return false;
        InputStream fis;
        BufferedReader br;
        String line;

        fis = new FileInputStream("C:\\Users\\Sjurdur\\Desktop\\road_names.txt");
        br = new BufferedReader(new InputStreamReader(fis, "LATIN1"));
        while ((line = br.readLine()) != null) {
            if (road.equalsIgnoreCase(line)) {
                br.close();
                return true;
            }
        }
        br.close();
        throw new InputException("Could not find " + road + "");
    }

    /**
     *
     * @param args
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     * @throws addressparser.InputException
     */
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException, InputException
    {
        String[] inputList = new String[]{
            "Rued Langgaards Vej 7A, 5. kld, 2300 København S",
            "Rued Langgaards Vej 7 2300 København S",
            "Rued Langgaards Vej kld 2300 København S",
            "Rued Langgaards Vej 7, 5. kld, København S",
            "Rued Langgaards Vej 7, kld",
            "Rued Langgaards Vej 7A kld København S",
            "Rued Langgaards Vej 7A kld",
            "Rued langgaards Vej kld København",
            "Rued Langgaards Vej",
            "!%&/",
            "2300 København",
            "Rued Langgaards Vej i København"
        };

        for (String input : inputList) {
            AddressParser ap = new AddressParser();
            String[] output;
            try {
                output = ap.addressParser(input);
                ap.print(input, output);
            } catch (InputException ex) {
                System.out.println(ex);
                System.out.println();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
//        AddressParser ap2 = new AddressParser();
//        InputStream fis;
//        BufferedReader br;
//        String line;
//
//        fis = new FileInputStream("C:\\Users\\Sjurdur\\Desktop\\road_names.txt");
//        br = new BufferedReader(new InputStreamReader(fis, "LATIN1"));
//        while ((line = br.readLine()) != null) {
//            String[] output;
//            output = ap2.addressParser(line);
//            ap2.print(line, output);
//        }
    }
}
