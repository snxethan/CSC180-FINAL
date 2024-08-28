package csc180.townsend.ethan.finalcsc180.Controller;

import csc180.townsend.ethan.finalcsc180.SongApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

import static csc180.townsend.ethan.finalcsc180.ChangeScene.changeScene;

public class LoginController {
    //region FXML variables
    @FXML
    public Label textDisplay;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    //endregion

    private final static DatabaseController database = new DatabaseController();

    /**
     * Login function
     * @param Event ActionEvent
     * @throws IOException IOException
     */
    public void login(ActionEvent Event) throws IOException {
        String username = usernameField.getText().trim(); // trim removes whitespace
        String password = passwordField.getText().trim(); // trim removes whitespace
        // mysql connection to user database
        if(database.connect(false)){
            // check if username and password are in the database
            if(database.loginUser(username,password)){
                changeScene(Event,"home-view.fxml", SongApplication.homeTitle); // change scene to home
            } else {
                //wrong credentials
                textDisplay.setText(DatabaseController.strUserNotFound + "\nWrong username or password!");
            }
        } else {
            // error connecting to database
            usernameField.setText("Error connecting to database");
        }
    }

    //region ActionEvent functions
    public void onSignUpLinkClick(ActionEvent Event) throws IOException {
        changeScene(Event,"signup-view.fxml", SongApplication.signUpTitle); // change scene to sign up
    }

    public void onLoginButtonClick(ActionEvent Event) throws IOException {
        login(Event); // login
    }
    //endregion
}