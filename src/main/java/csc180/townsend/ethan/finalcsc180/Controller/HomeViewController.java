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
    int user_id;
    String user_name;

    /**
     * Initialize the home view
     * This method is called when the view is loaded
     * It sets the current user's username and displays the top songs
     * It also populates the artist list and saves the user's preferences
     */
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

    @FXML
    private void onEditPreferencesButtonClick() {
        // Toggle the visibility and disable state of the artist list and top songs containers
        boolean isArtistListVisible = artistListScrollPane.isVisible();
        artistListScrollPane.setVisible(!isArtistListVisible);
        artistListScrollPane.setDisable(isArtistListVisible);
        topSongsScrollPane.setVisible(isArtistListVisible);
        topSongsScrollPane.setDisable(!isArtistListVisible);

        if (!isArtistListVisible) {
            // Populate the artist list dynamically
            selectedArtists.clear();
            populateArtistList();
        } else {
            // Save preferences and update the displayed top songs
            savePreferences();
            displayTopSongs(filterSongsByPreferences(SongScraper.getTopSongs(), user_id));
        }
    }
    //endregion

    /**
     * Populate the artist list with the user's preferred artists
     * This method is called when the view is loaded
     * It populates the artist list with the user's preferred artists
     */
    private void populateArtistList() {
        List<String> artists = SongScraper.getArtists(); // Get the list of artists
        artistListContainer.getChildren().clear(); // Clear any existing children

        // Get the user's current preferred artists
        List<Integer> preferredArtistIds = database.getPreferences(user_id); // Get the user's preferred artists
        List<String> preferredArtists = preferredArtistIds.stream()
                .map(database::findArtistName)
                .collect(Collectors.toList()); // Get the names of the preferred artists

        selectedArtists.addAll(preferredArtists); // Add the old selected artists to the new selected artists list

        for (String artist : artists) {
            CheckBox checkBox = new CheckBox(artist); // Create a new checkbox with the artist's name
            checkBox.setSelected(preferredArtists.contains(artist)); // Set the checkbox to selected if the artist is preferred
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    selectedArtists.add(artist); // Add the artist to the selected artists list
                } else {
                    selectedArtists.remove(artist); // Remove the artist from the selected artists list
                }
            });
            artistListContainer.getChildren().add(checkBox); // Add the checkbox to the artist list container
        }
    }

    /**
     * Save the user's selected artists to the database
     * This method is called when the user clicks the "Save Preferences" button
     * It removes the user's current preferred artists and adds the selected artists
     * to the database
     */
    private void savePreferences() {
        // Save the selected artists to the database
        database.removePerferredArtists(user_id); // Remove the user's current preferred artists
        for (String artist : selectedArtists) {
            int artistId = database.findArtistID(artist); // Find the artist's ID
            if (artistId != -1) {
                database.addPreferredArtist(artistId, user_name); // Add the artist to the user's preferred artists
            }
        }
    }

    /**
     * Display the top songs in the top songs container
     * This method is called when the view is loaded
     * It displays the top songs in the top songs container
     * @param topSongs The top songs to display
     */
    public void displayTopSongs(List<SongScraper.Song> topSongs) {
        topSongsContainer.getChildren().clear(); // Clear any existing children
        for (SongScraper.Song song : topSongs) {
            Label songLabel = new Label(song.getTitle()); // Create a new label with the song's title
            songLabel.setFont(Font.font("System", FontWeight.BOLD, 12)); // Set the font of the label
            topSongsContainer.getChildren().add(songLabel);

            //add artists with a tab
            for (String artist : song.getArtists()) {
                Label artistLabel = new Label("    " + artist);
                topSongsContainer.getChildren().add(artistLabel);
            }
        }
    }

    /**
     * Filter the top songs by the user's preferences
     * This method is called when the view is loaded
     * It filters the top songs by the user's preferences
     * @param topSongs The top songs to filter
     * @param user_id The user's ID
     * @return The filtered top songs
     */
    private List<SongScraper.Song> filterSongsByPreferences(List<SongScraper.Song> topSongs, int user_id) {
        List<SongScraper.Song> songList = new ArrayList<>();
        List<Integer> preferenceList = database.getPreferences(user_id);
        // if the preference is empty (have all songs avaliable)
        if(preferenceList == null || preferenceList.isEmpty()){
            return topSongs;
        }

        //otherwise, loop through and only display the user's preferred songs
        for(SongScraper.Song song : topSongs){
            List<String> artists = song.getArtists();
            for(String artist : artists) {
                int artist_id = database.findArtistID(artist);

                if(preferenceList.contains(artist_id)) {
                    songList.add(song);
                    break; // Ensure each song is only added once
                }
            }
        }
        return songList;
    }
}