package csc180.townsend.ethan.finalcsc180;

import csc180.townsend.ethan.finalcsc180.Controller.DatabaseController;
import csc180.townsend.ethan.finalcsc180.Controller.Scraper.SongScraper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class SongApplication extends Application {
    //region Titles
    public static String loginTitle = "User Login";
    public static String signUpTitle = "User Register";
    public static String homeTitle = "User Profile";
    //endregion

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SongApplication.class.getResource("login-view.fxml")); // Load the FXML file
        Scene scene = new Scene(fxmlLoader.load()); // Remove fixed width and height
        stage.setTitle(loginTitle); // Set the title of the window
        stage.setScene(scene); // Set the scene of the window
        stage.setOnCloseRequest((WindowEvent event) -> { // When the window is closed
            Platform.exit(); // Exit the platform
            System.exit(0); // Exit the system
        });
        stage.show(); // Show the window
    }

    public static void main(String[] args) {
        if(DatabaseController.connect(true)){
            launch();
        } else {
            System.out.println("SQL Connection Failed - MAIN");
        }
    }
}
