package csc180.townsend.ethan.finalcsc180.Controller.Validator;

/**
 * Created by Ethan Townsend on 7/6/2024
 * This class is used to return a boolean and a message from the validation methods.
 * This is used to return the result of the validation and a message to be displayed to the user.
 */
public class Validator {
    boolean isValid;
    String message;

    /**
     * Constructor for the Validator class
     * @param _isValid boolean value of the validation
     * @param _message message to be displayed to the user
     */
    public Validator(boolean _isValid, String _message){
        this.isValid = _isValid;
       this.message = _message;
    }

    /**
     * Returns the boolean value of the validation
     * @return boolean value of the validation
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Returns the message to be displayed to the user
     * @return message to be displayed to the user
     */
    public String getMessage() {
        return message;
    }


}
