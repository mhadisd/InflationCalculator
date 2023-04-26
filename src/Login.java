import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

public class Login extends Application {

    // Constants for connecting to the database and hashing passwords
    private static final String DB_URL = "jdbc:mysql://cis244-prod.c28qsj4v6lea.us-east-2.rds.amazonaws.com:3306/Login";
    private static final String DB_USER = "loginagent";
    private static final String DB_PASSWORD = "";
    private static final String SALT = "mysalt";

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load the FXML file for the login screen and create a new instance of the LoginController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        LoginController controller = new LoginController();

        // Set the LoginController as the controller for the FXML file and load the root node
        loader.setController(controller);
        Parent root = loader.load();

        // Set the title and size of the login screen and show it
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();

        // Handle the login button click event
        controller.getLoginButton().setOnAction(event -> {
            String username = controller.getUsernameField().getText();
            String password = controller.getPasswordField().getText();
            if (isValidUser(username, password)) {
                // TODO: Handle successful login

                // Display an information dialog indicating a successful login
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText(null);
                alert.setContentText("Welcome, " + username + "!");
                alert.showAndWait();

                // Launch the MatchSearch application
                MatchSearch matchSearch = new MatchSearch();
                try {
                    matchSearch.start(new Stage());
                } catch (Exception e) {
                    System.out.println("Error launching Inflation Calculator" + e.getMessage());
                }
            } else {
                // TODO: Handle invalid login
                // Display an error dialog indicating an invalid login
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password. Please try again.");
                alert.showAndWait();
            }
        });

        // Handle the create account button click event
        controller.getCreateAccountButton().setOnAction(event -> {
            String username = controller.getUsernameField().getText();
            String password = controller.getPasswordField().getText();
            if (addUser(username, password)) {
                // Display an information dialog indicating a successful account creation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account Created");
                alert.setHeaderText(null);
                alert.setContentText("Your account has been created. Please log in.");
                alert.showAndWait();
            } else {
                // Display an error dialog indicating a failed account creation
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account Creation Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to create account. Please try again.");
                alert.showAndWait();
            }
        });
    }

    // Method to validate a user's credentials
    private boolean isValidUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                String inputHashedPassword = hashPassword(password);
                // Compare the hashed password from the database with the hashed input password
                return hashedPassword.equals(inputHashedPassword);
            } else {
                // If the username does not exist in the database, return false
                return false;
            }
        } catch (SQLException ex) {
            // Print an error message if there was an error validating the user's credentials
            System.out.println("Error validating user credentials: " + ex.getMessage());
            return false;
        }
    }

    // Method to add a new user to the database
    private boolean addUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String hashedPassword = hashPassword(password);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            int rowsInserted = stmt.executeUpdate();
            // If a row was successfully inserted into the database, return true
            return rowsInserted == 1;
        } catch (SQLException ex) {
            // Print an error message if there was an error adding the user to the database
            System.out.println("Error adding user to database: " + ex.getMessage());
            return false;
        }
    }

    // Method to hash a password using the SHA-256 algorithm and a salt
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(SALT.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            // Encode the hashed password as a Base64 string
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException ex) {
            // Print an error message if there was an error hashing the password
            System.out.println("Error hashing password: " + ex.getMessage());
            return "";
        }
    }

    // Main method to launch the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}
