/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdressParser;

import java.util.HashMap;

/**
 *
 * @author Johan
 */
public class SearchName {

    public static String getRoadNumber(String input) {
        AddressParser parser = new AddressParser();
        String[] output;
        try {
            output = parser.addressParser(input);
        } catch (InputException ex) {
            throw new InputException();
        }
        return output[0];
    }

}
