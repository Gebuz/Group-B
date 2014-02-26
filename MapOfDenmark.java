/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.of.denmark;

/**
 *
 * @author Johan Lund Neergaard (jnee)
 */
public class MapOfDenmark {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*String[] input = new String[5];
        input[0] = "Rued Langgaards Vej";
        input[1] = "Rued Langgaards Vej 7, 5. sal, København S";
        input[2] = "Rued Langgaards Vej 7 2300 København S";
        input[3] = "Rued Langgaards Vej 7, 5.";
        input[4] = "Rued Langgaards Vej 7A København S";
        input[5] = "Rued Langgaards Vej i København";

        for (String sInput : input) {
            String[] output = parseAdress(sInput);
            System.out.println("input: " + sInput);
            String sAllOut = output[0];
            for (int i = 1; i < 6; i++) {
                sAllOut += "#" + output[i];
            }
            System.out.println("output: " + sAllOut);
        }*/
    }

    private void print(String input,String[] output) {
        String sAllOut = output[0];
        for (int i = 1; i < 6; i++) {
            sAllOut += "#" + output[i];
        }
        System.out.println("input: " + input);
        System.out.println("output: " + sAllOut);
    }

    private void validate(String[] array) throws InputException {
        boolean acceptable = true;
        if (array[0].equals("")) {
            throw new InputException("Invalid adress.");
        } else {
            validateName(array[0]);
        }
        validateName(array[5]);
        validateNumber(array[1], 1, 3);
        validateNumber(array[3], 1, 2);
        validateNumber(array[4], 4, 4);
        validateCharacter(array[2]);
    }

    private void validateName(String name) throws InputException {
        boolean acceptable = true;
        if (name.replaceAll("[a-zA-ZåæøÅÆØ0éÉäÄüÜöÖèÈ0-9\\(\\)'\\-,\\./\\s]", "").equals("")) {
        } else {
            throw new InputException("Invalid name.");
        }
    }

    private void validateNumber(String number, int low, int high) throws InputException {
        if (number.replaceAll("[0-9]", "").equals("")) {
        } else {
            throw new InputException("Invalid number.");
        }
        if (number.length() >= low && number.length() <= high) {
        } else {
            throw new InputException("Invalid number.");
        }
    }

    private void validateCharacter(String character) throws InputException {
        if (character.replaceAll("[a-zA-ZåæøÅÆØ0éÉäÄüÜöÖèÈ]", "").equals("")) {
        } else {
            throw new InputException("Invalid charcter.");
        }
    }
}
