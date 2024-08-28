package csc180.townsend.ethan.finalcsc180.Controller;

import csc180.townsend.ethan.finalcsc180.Controller.Scraper.SongScraper;
import csc180.townsend.ethan.finalcsc180.SongApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
    @FXML
    private ScrollPane artistListScrollPane, topSongsScrollPane;
    private boolean toggleEdit = true;
    //endregion

    private final DatabaseController database = new DatabaseController();
    private List<String> selectedArtists = new ArrayList<>();
    private List<SongScraper.Song> topSongs = new ArrayList<>();

    public void initialize() {
        // Set the text fields to the current user's information
        user_name = database.currentUserUsername;
        user_id = database.findUserID(user_name); // Find the user ID

        currentUsername.setText("'" + user_name + "'"); // Set the current username
        displayTopSongs(filterSongsByPreferences(SongScraper.getTopSongs(), user_id));
    }

    //region action events
    public void onLogoutButtonClick(ActionEvent event) throws IOException {
        // Change the scene to the login view
        changeScene(event, "login-view.fxml", SongApplication.homeTitle); // Change the scene to the login view
    }

    public void onEditPreferencesButtonClick() {
        // Toggle the visibility of the artist list and top songs containers
        boolean isArtistListVisible = artistListScrollPane.isVisible();
        artistListScrollPane.setVisible(!isArtistListVisible);
        topSongsScrollPane.setVisible(isArtistListVisible);

        if (!isArtistListVisible) {
            // Populate the artist list dynamically
            populateArtistList();
        } else {
            // Save preferences and update the displayed top songs
            for (String artist : selectedArtists) {
                database.addPreferredArtist(user_id, artist);
            }
            displayTopSongs(filterSongsByPreferences(SongScraper.getTopSongs(),user_id));
        }
    }
    //endregion

    private void populateArtistList() {
        List<String> artists = SongScraper.getArtists();
        artistListContainer.getChildren().clear(); // Clear any existing children

        for (String artist : artists) {
            CheckBox checkBox = new CheckBox(artist);
            checkBox.setSelected(selectedArtists.contains(artist));
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

    public void displayTopSongs(List<SongScraper.Song> topSongs) {
        topSongsContainer.getChildren().clear();
        for (SongScraper.Song song : topSongs) {
            Label songLabel = new Label(song.getTitle());
            songLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            topSongsContainer.getChildren().add(songLabel);

            //add artists with a tab
            for (String artist : song.getArtists()) {
                Label artistLabel = new Label("    " + artist);
                topSongsContainer.getChildren().add(artistLabel);
            }
        }
    }

    private List<SongScraper.Song> filterSongsByPreferences(List<SongScraper.Song> topSongs) {
        if (selectedArtists.isEmpty()) {
            return topSongs;
        }
        return topSongs.stream()
                .filter(song -> song.getArtists().stream().anyMatch(selectedArtists::contains))
                .collect(Collectors.toList());
    }
}