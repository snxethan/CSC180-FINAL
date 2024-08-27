package csc180.townsend.ethan.finalcsc180.Controller;

import csc180.townsend.ethan.finalcsc180.Controller.Validator.UserValidation;
import csc180.townsend.ethan.finalcsc180.Controller.Validator.Validator;
import csc180.townsend.ethan.finalcsc180.SongApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static csc180.townsend.ethan.finalcsc180.ChangeScene.changeScene;

public class SignupController {
    DatabaseController database = new DatabaseController();
    UserValidation userValidation = new UserValidation();

    //region FXML Variables
    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private Label textDisplay;
    //endregion

    //region Action Events
    public void onSignUpButtonClick(ActionEvent event) {
        if (validateUser()) {
            // If all fields are valid, sign up the user and add to database
            textDisplay.setText(database.signUpUser(usernameField.getText(), passwordField.getText()));
            if (textDisplay.getText().equals(DatabaseController.strUserCreated)) {
                try {
                    // Load the login view
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/csc180/townsend/ethan/finalcsc180/login-view.fxml"));
                    Parent root = loader.load();

                    // Get the controller and set the textDisplay label
                    LoginController loginController = loader.getController();
                    loginController.textDisplay.setText("User Created Successfully");

                    // Change the scene
                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle(SongApplication.loginTitle);
                    stage.show();
                } catch (IOException e) {
                    System.out.println(e.getMessage() + "Error changing scene");
                }
            } else if (textDisplay.getText().equals(DatabaseController.strUserExists)) {
                textDisplay.setText("This User already exists!");
            }
        }
    }

    public void onCancelButtonClick(ActionEvent event) throws IOException {
        changeScene(event, "login-view.fxml", SongApplication.loginTitle);
    }
    //endregion

    public boolean validateUser() {
        // Validate username
        Validator usernameValidation = userValidation.validateUsername(usernameField.getText());
        if (!usernameValidation.isValid()) {
            textDisplay.setText(usernameValidation.getMessage());
            return false;
        }

        // Validate password
        Validator passwordValidation = userValidation.validatePassword(passwordField.getText());
        if (!passwordValidation.isValid()) {
            textDisplay.setText(passwordValidation.getMessage());
            return false;
        }

        return true;
    }
}