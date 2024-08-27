package csc180.townsend.ethan.finalcsc180.Controller;

import csc180.townsend.ethan.finalcsc180.Controller.Scraper.SongScraper;
import csc180.townsend.ethan.finalcsc180.SongApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static csc180.townsend.ethan.finalcsc180.ChangeScene.changeScene;

public class HomeViewController {
    //region FXML Variables
    @FXML
    private Label currentUsername;
    @FXML
    private VBox artistListContainer;
    //endregion

    private final DatabaseController database = new DatabaseController();
    private List<String> selectedArtists = new ArrayList<>();

    public void initialize() {
        // Get the current user's information
        String currentUserUsername = DatabaseController.currentUserUsername; // Get the current user's username
        String username = database.findUserUsername(currentUserUsername); // make sure the username is valid

        // Set the text fields to the current user's information
        currentUsername.setText("'" + username + "'"); // Set the current username
    }

    //region action events
    public void onLogoutButtonClick(ActionEvent event) throws IOException {
        // Change the scene to the login view
        changeScene(event, "login-view.fxml", SongApplication.homeTitle); // Change the scene to the login view
    }

    public void onEditPreferencesButtonClick() {
        // Populate the artist list dynamically
        populateArtistList();
    }
    //endregion

    private void populateArtistList() {
        // Example list of artists
        List<String> artists = List.of("Drake", "Travis Scott", "Kanye West", "Ariana Grande");

        artistListContainer.getChildren().clear(); // Clear any existing children

        for (String artist : artists) {
            CheckBox checkBox = new CheckBox(artist);
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    selectedArtists.add(artist);
                } else {
                    selectedArtists.remove(artist);
                }
            });
            artistListContainer.getChildren().add(checkBox);
        }
    }

    public void displayTopSongs() {
//        try {
//            List<String> topSongs = SongScraper.getTop200Songs();
//            List<String> filteredSongs = filterSongsByPreferences(topSongs);
//            artistListContainer.getChildren().clear();
//            for (String song : filteredSongs) {
//                Label songLabel = new Label(song);
//                artistListContainer.getChildren().add(songLabel);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private List<String> filterSongsByPreferences(List<String> topSongs) {
        if (selectedArtists.isEmpty()) {
            return topSongs;
        }
        return topSongs.stream()
                .filter(song -> selectedArtists.stream().anyMatch(song::contains))
                .collect(Collectors.toList());
    }
}