package csc180.townsend.ethan.finalcsc180.Controller.Validator;

public class UserValidation {

    /*
    * This method validates a password inputted by the user
    * @param password the password inputted by the user
     */
    public Validator validatePassword(String password){
        final int minUpper = 2; //minimum amount of times the password must have a lowercase
        final int minLower = 3; //minimum amount of times the password must have an uppercase
        final int minNumeric = 2; //minimum amount of times the password must have a number
        final int minSymbols = 1; //minimum amount of times the password must have symbols
        final int minLength = 8; //minimum length of the password in general

        if(password == null){
            return new Validator(false, "Password field is empty");
            //password is null
        }
        if(password.length() < minLength){
            return new Validator(false, "Password must be at least " + minLength + " characters long");
            //password length is too short
        }
        if(!password.matches(".*[A-Z].*")){
            return new Validator(false, "Password must contain at least " + minUpper + " uppercase letter(s)");
            //regex that matches an uppercase letter
        }
        if(!password.matches(".*[a-z].*")){
            return new Validator(false, "Password must contain at least " + minLower + " lowercase letter(s)");
            //regex that matches a lowercase letter
        }
        if(!password.matches(".*[0-9].*")){
            return new Validator(false, "Password must contain at least " + minNumeric + " number(s)");
            //regex that matches a number
        }
        if(!password.matches(".*[^a-zA-Z0-9].*")){
            return new Validator(false, "Password must contain at least " + minSymbols + " special character(s)");
            //regex that matches a special character
        }
        return new Validator(true, "Password is valid"); //otherwise password is valid
    }

    /*
    * This method validates a username inputted by the user
    * @param username the username inputted by the user
     */
    public Validator validateUsername(String username) {
        final int minLength = 5; //minimum length of the username in general

        if (username == null) {
            return new Validator(false, "Username field is empty");
            //username is null
        }
        if (username.length() < minLength) {
            return new Validator(false, "Username must be at least 5 characters long");
            //username length is too short
        }
        return new Validator(true, "Username is valid"); //otherwise username is valid
    }


}
