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
    private static final String DB_URL = "jdbc:mysql://cis244-prod.c28qsj4v6lea.us-east-2.rds.amazonaws.com:3306/Login";
    private static final String DB_USER = "*";
    private static final String DB_PASSWORD = "*";
    private static final String SALT = "mysalt";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        LoginController controller = new LoginController();
        loader.setController(controller);
        Parent root = loader.load();
        loader.setController(controller);
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();

        controller.getLoginButton().setOnAction(event -> {
            String username = controller.getUsernameField().getText();
            String password = controller.getPasswordField().getText();
            if (isValidUser(username, password)) {
                // TODO: Handle successful login
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText(null);
                alert.setContentText("Welcome, " + username + "!");
                alert.showAndWait();
            } else {
                // TODO: Handle invalid login
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password. Please try again.");
                alert.showAndWait();
            }
        });

        controller.getCreateAccountButton().setOnAction(event -> {
            String username = controller.getUsernameField().getText();
            String password = controller.getPasswordField().getText();
            if (addUser(username, password)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account Created");
                alert.setHeaderText(null);
                alert.setContentText("Your account has been created. Please log in.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account Creation Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to create account. Please try again.");
                alert.showAndWait();
            }
        });
    }


    private boolean isValidUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                String inputHashedPassword = hashPassword(password);
                return hashedPassword.equals(inputHashedPassword);
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Error validating user credentials: " + ex.getMessage());
            return false;
        }
    }

    private boolean addUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String hashedPassword = hashPassword(password);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted == 1;
        } catch (SQLException ex) {
            System.out.println("Error adding user to database: " + ex.getMessage());
            return false;
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(SALT.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error hashing password: " + ex.getMessage());
            return "";
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

}