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
    private VBox artistListContainer, topSongsContainer;
    //endregion

    private final DatabaseController database = new DatabaseController();
    private List<String> selectedArtists = new ArrayList<>();
    private List<String> topSongs = new ArrayList<>();

    public void initialize() {
        // Get the current user's information
        String currentUserUsername = DatabaseController.currentUserUsername; // Get the current user's username
        String username = database.findUserUsername(currentUserUsername); // make sure the username is valid

        // Set the text fields to the current user's information
        currentUsername.setText("'" + username + "'"); // Set the current username

        displayTopSongs();
    }

    //region action events
    public void onLogoutButtonClick(ActionEvent event) throws IOException {
        // Change the scene to the login view
        changeScene(event, "login-view.fxml", SongApplication.homeTitle); // Change the scene to the login view
    }

    public void onEditPreferencesButtonClick() {
        // Toggle the visibility of the top songs container
        topSongsContainer.setVisible(!topSongsContainer.isVisible());

        // Populate the artist list dynamically
        populateArtistList();
    }
    //endregion

    private void populateArtistList() {
        List<String> artists = SongScraper.getArtists();
        artistListContainer.getChildren().clear(); // Clear any existing children

        for (String artist : artists) {
            CheckBox checkBox = new CheckBox(artist);
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    selectedArtists.add(artist);
                } else {
                    selectedArtists.remove(artist);
                }
                // Update the displayed top songs based on selected artists
                displayTopSongs();
            });
            artistListContainer.getChildren().add(checkBox);
        }
    }

    public void displayTopSongs() {
        //TODO: display all the scraped songs
        topSongs = SongScraper.getTopSongs();
        List<String> filteredSongs = filterSongsByPreferences(topSongs);

        topSongsContainer.getChildren().clear(); // Clear any existing children
        for (String song : filteredSongs){
            Label songLabel = new Label(song);
            topSongsContainer.getChildren().add(songLabel);
        }

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