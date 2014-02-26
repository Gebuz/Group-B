/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package map.of.denmark;

/**
 *
 * @author Johann
 */
public class InputException extends Exception {

    /**
     * Creates a new instance of <code>InputException</code> without detail
     * message.
     */
    public InputException() {
    }

    public InputException(String message) {
        super(message);
    }

    public InputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputException(Throwable cause) {
        super(cause);
    }

    public InputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String newMethod() {
        return "What is love, baby don't hurt me";
    }


}
