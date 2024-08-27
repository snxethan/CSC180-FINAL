package csc180.townsend.ethan.finalcsc180.Controller;

import org.jsoup.select.Elements;

import java.sql.*;

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
    //endregion

    /**
     * Connects to the SQL database
     * @return true if connection is successful, false if not
     */
    public boolean connect() {
        String sql = "SELECT VERSION()"; // SQL query to test connection
        try(Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement(); // Create a statement object
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next()){ // If the query returns a result
                System.out.println(rs.getString(1)); // Print the result
                System.out.println("SQL Connection Successful"); // Print success message
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
            System.out.println("SQL Connection Failed - CONNECT"); // Print failure message
            return false;
        }
        return false;
    }

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
        // Check if the user already exists
        if (findUserUsername(_username).equals(_username)) {
            return strUserExists; // Return a message if the user already exists
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, _username.trim()); // Set the first parameter to the username
            pst.setString(2, _password.trim()); // Set the second parameter to the password
            pst.executeUpdate(); // Execute the query
            return strUserCreated;
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
            System.out.println("SQL Connection Failed - SIGN UP"); // Print failure message
            return "Error Creating User";
        }
    }

    public String findUserUsername(String _username){
        String sql = "SELECT user_name FROM users WHERE user_name = ?"; // SQL query to find a user by username
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1, _username); // Set the first parameter to the username
            ResultSet rs = pst.executeQuery(); // Execute the query
            if(rs.next()){
                return rs.getString("user_name"); // Return the username if the user is found
            } else {
                return strUserNotFound; // Return a message if the user is not found
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
            System.out.println("SQL Connection Failed - FIND USER"); // Print failure message
            return "Error Finding User";
        }
    }

    public int checkArtistExists(String artist_name){
        if(connect()) {
            String sql = "SELECT artist_id FROM artists WHERE artist_name = ?";
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, artist_name);
                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    return Integer.parseInt(rs.toString());
                } else {
                    return 0;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
                System.out.println("SQL Connection Failed - FIND USER"); // Print failure message
            } catch (NumberFormatException ignore){

            }
        } else {
            System.out.println("SQL Connection Failed - CHECK ARTIST EXISTS");
        }
        return 0;
    }

    public void addArtistToDatabase(String artist){
        if(connect()){
            String sql = "INSERT INTO artists(artist_name) VALUES (?)";
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, artist);
                pst.executeUpdate();
            }catch (SQLException e){
                System.out.println(e.getMessage() + "\n" + e.getSQLState()); // Print error message
                System.out.println("SQL Connection Failed - SIGN UP"); // Print failure message
            }
        }
    }
}