package csc180.townsend.ethan.finalcsc180;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ChangeScene {
    /**
     * Changes the scene of the window to the FXML file specified
     * @param event The event that triggered the scene change
     * @param strFXMLFileName The name of the FXML file to load
     * @param windowTitle The title of the window
     * @throws IOException If the FXML file cannot be loaded
     */
    public static void changeScene(Event event, String strFXMLFileName, String windowTitle) throws IOException {
        URL url = new File("src/main/resources/csc180/townsend/ethan/finalcsc180/" + strFXMLFileName).toURI().toURL();
        Parent root = FXMLLoader.load(url); // Load the FXML file
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the current window
        Scene scene = new Scene(root); // Ensure dynamic resizing

        // Optional: Set a preferred size for the scene if needed
        scene.getRoot().autosize();

        stage.setTitle(windowTitle); // Set the title of the window
        stage.setScene(scene); // Set the scene of the window

        // Optional: Set initial size if not set in FXML

        stage.show(); // Show the window
    }
}