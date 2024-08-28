package csc180.townsend.ethan.finalcsc180.Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {

    static String currentUserUsername; // Stores the username of the currently logged in user
    //region sql connection info
    static final String url = System.getenv("SONG_DB_URL");
    static final String user = "root";
    static final String password = "test";
    //endregion

    //region sout messages
    static final String strUserExists = "User already exists!";
    static final String strUserCreated = "User successfully Created!";
    static final String strUserFound = "User Found";
    static final String strUserNotFound = "User Not Found";
    static final String strArtistNotFound = "Artist Not Found";
    //endregion

    /**
     * Connects to the SQL database
     * @return true if connection is successful, false if not
     */
    public static boolean connect(boolean wantDisplay) {
        String sql = "SELECT VERSION()"; // SQL query to test connection
        try(Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement(); // Create a statement object
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next()) { // If the query returns a result
                if (wantDisplay)
                {
                System.out.println(rs.getString(1)); // Print the result
                System.out.println("SQL Connection Successful"); // Print success message
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
            System.out.println("SQL Connection Failed - CONNECT"); // Print failure message
            return false;
        }
        return false;
    }

    //region sign up & login
    /**
     * Logs in a user
     * @param _username the username of the user
     * @param _password the password of the user
     * @return true if the user is found, false if not
     */
    public boolean loginUser(String _username, String _password) {
        String sql = "SELECT * FROM users WHERE user_name = ? AND user_password = ?"; // SQL query to find a user by username and password
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = conn.prepareStatement(sql)) { // Create a prepared statement object
            pst.setString(1, _username.trim()); // Set the first parameter to the username
            pst.setString(2, _password.trim()); // Set the second parameter to the password

            // Print the SQL query and parameters
            System.out.println("Executing query: " + sql);
            System.out.println("With parameters: " + _username + ", " + _password);

            ResultSet rs = pst.executeQuery(); // Execute the query
            if (rs.next()) {
                System.out.println(strUserFound + " " + rs.getInt("user_id")); // Print success message
                currentUserUsername = _username.trim(); // Set the current user's username
                return true;
            } else {
                System.out.println(strUserNotFound); // Print failure message
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
            System.out.println("SQL Connection Failed - USER LOGIN"); // Print failure message
            return false;
        }
    }

    /**
     * Signs up a user
     * @param _username the username of the user
     * @param _password the password of the user
     * @return a string message
     */
    public String signUpUser(String _username, String _password) {
        String sql = "INSERT INTO users(user_name, user_password) VALUES(?,?)"; // SQL query to insert a new user
        // see if the user already exists
        if(validateUserName(_username).equals(strUserExists)){
            return strUserExists;
        }

        // if not, create the user
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, _username.trim()); // Set the first parameter to the username
            pst.setString(2, _password.trim()); // Set the second parameter to the password
            pst.executeUpdate(); // Execute the query
            return strUserCreated;
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
            System.out.println("SQL Connection Failed - SIGN UP USER"); // Print failure message
            return "Error Creating User";
        }
    }
    //endregion

    //region validate user & artist

    /**
     * Validates the artist and ensures they exist
     * @param artist_name the name of the arist
     * @return the artist id or 0 if they don't exist
     */
    public int validateArtist(String artist_name){
        if(connect(false)) {
            String sql = "SELECT artist_id FROM artists WHERE artist_name = ?";
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, artist_name);
                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    return rs.getInt("artist_id");
                } else {
                    return -1;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
                System.out.println("SQL Connection Failed - FIND USER"); // Print failure message
            } catch (NumberFormatException ignore){

            }
        } else {
            System.out.println("SQL Connection Failed - CHECK ARTIST EXISTS");
        }
        return -1;
    }

    /**
     * validates the user name
     * @param _username the username to validate
     * @return a string message, strUserExists if the user exists, strUserNotFound if the user doesn't
     */
    public String validateUserName(String _username){
        String sql = "SELECT user_name FROM users WHERE user_name = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, _username);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return strUserExists;
            } else {
                return strUserNotFound;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
            System.out.println("SQL Connection Failed - FIND USER NAME"); // Print failure message
            return "Error Finding User";
        }
    }
    //endregion

    //region find id

    /**
     * finds the user's id
     * @param _username the username of the user
     * @return the user's id
     */
    public int findUserID(String _username){
        String sql = "SELECT user_id FROM users WHERE user_name = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, _username);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return rs.getInt("user_id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
            System.out.println("SQL Connection Failed - FIND USER ID"); // Print failure message
            return -1;
        }
    }

    /**
     * finds the artist's id
     * @param artist the name of the artist
     * @return the artist's id
     */
    public int findArtistID(String artist){
        String sql = "SELECT artist_id FROM artists WHERE artist_name = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, artist);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return rs.getInt("artist_id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
            System.out.println("SQL Connection Failed - FIND ARTIST ID"); // Print failure message
            return -1;
        }
    }

    /**
     * finds the artist names
     * @param artist_id the id of the artist
     * @return the name of the artist
     */
    public String findArtistName(int artist_id){
        String sql = "SELECT artist_name FROM artists WHERE artist_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, artist_id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return rs.getString("artist_name");
            } else {
                return strArtistNotFound;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
            System.out.println("SQL Connection Failed - FIND ARTIST NAME"); // Print failure message
            return "Error Finding Artist";
        }
    }

    /**
     * displays all artists names
     * @return a list of all artist names
     */
    public List<String> returnAllArtists(){
        String sql = "SELECT artist_name FROM artists";
        List<String> artistNames = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = conn.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                artistNames.add(rs.getString("artist_name"));
            }
            return artistNames;
        }catch (SQLException e){
            System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
            System.out.println("SQL Connection Failed - RETURN ALL ARTISTS"); // Print failure message
            return null;
        }
    }
    //endregion

    //region add artist & preferred artist
    /**
     * Adds an artist to the database
     * @param artist the name of the artist
     */
    public void addArtistToDatabase(String artist){
        if(connect(false)){
            String sql = "INSERT INTO artists(artist_name) VALUES (?)";
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, artist);
                pst.executeUpdate();
            }catch (SQLException e){
                System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
                System.out.println("SQL Connection Failed - ADD ARTIST TO DATABASE"); // Print failure message
            }
        }
    }

    /**
     * Adds a preferred artist connecting to the user id & artist id
     * @param artist_id the id of the artist
     * @param user_name the name of the user
     */
    public void addPreferredArtist(int artist_id, String user_name){
        if(connect(false)){
            //get user id:
            int userID = findUserID(user_name);
            //get artist id:
            int artistID = findArtistID(findArtistName(artist_id));

            //add to database
            String sql = "INSERT INTO users_preferredartists(user_id, artist_id) VALUES (?, ?)";
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, userID);
                pst.setInt(2, artistID);
                pst.executeUpdate();
                System.out.println("Added " + findArtistName(artist_id) + " to " + user_name + "'s preferred artists");
            }catch (SQLException e){
                System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
                System.out.println("SQL Connection Failed - ADD PREFERRED ARTIST"); // Print failure message
            }
        }
    }

    /**
     * Removes a preferred artist from the user
     * @param user_id the id of the user
     */
    public void removePerferredArtists(int user_id){
        if(connect(false)){
            String sql = "DELETE FROM users_preferredartists WHERE user_id = ?";
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, user_id);
                pst.executeUpdate();
            }catch (SQLException e){
                System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
                System.out.println("SQL Connection Failed - REMOVE PREFERRED ARTIST"); // Print failure message
            }
        }
    }

    /**
     * Gets the preferred artists of the user
     * @param user_id the id of the user
     * @return a list of the preferred artists
     */
    public List<Integer> getPreferences(int user_id){
        List<Integer> preferredArtist = new ArrayList<>();
        if(connect(false)){
            String sql = "SELECT artist_id FROM users_preferredartists WHERE user_id = ?";
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, user_id);
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    preferredArtist.add(rs.getInt("artist_id"));
                }
                return preferredArtist;
            }catch(SQLException e){
                System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
                System.out.println("SQL Connection Failed - SIGN UP"); // Print failure message
            }
        }
        return null;
    }
    //endregion
}