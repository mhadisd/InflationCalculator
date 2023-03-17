import java.sql.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MatchSearch extends Application {

    private Connection conn;

    @Override
    public void start(Stage stage) throws Exception {
        // Connect to the database
        String dbUrl = "jdbc:mysql://database-1.c28qsj4v6lea.us-east-2.rds.amazonaws.com:3306/Pricing Data";
        String username = "*";
        String password = "*";
        conn = DriverManager.getConnection(dbUrl, username, password);

        // Set up the UI
        Label titleLabel = new Label("Match Search");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label searchLabel = new Label("Enter a search term:");
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        ListView<String> resultsList = new ListView<>();
        Label priceLabel = new Label("Price:");
        Label priceValueLabel = new Label();
        Button addButton = new Button("Add to List");
        ListView<String> selectedList = new ListView<>();
        Label totalLabel = new Label("Total:");
        Label totalValueLabel = new Label("0.00");

        GridPane searchGrid = new GridPane();
        searchGrid.setHgap(10);
        searchGrid.setVgap(10);
        searchGrid.setAlignment(Pos.CENTER);
        searchGrid.add(searchLabel, 0, 0);
        searchGrid.add(searchField, 1, 0);
        searchGrid.add(searchButton, 2, 0);

        HBox priceBox = new HBox(10);
        priceBox.setAlignment(Pos.CENTER);
        priceBox.getChildren().addAll(priceLabel, priceValueLabel);

        HBox addButtonBox = new HBox(10);
        addButtonBox.setAlignment(Pos.CENTER);
        addButtonBox.getChildren().add(addButton);

        VBox resultsBox = new VBox(10);
        resultsBox.setPadding(new Insets(10));
        resultsBox.getChildren().addAll(resultsList, priceBox, addButtonBox);

        VBox selectedBox = new VBox(10);
        selectedBox.setPadding(new Insets(10));
        selectedBox.getChildren().addAll(selectedList, totalLabel, totalValueLabel);

        HBox mainBox = new HBox(10);
        mainBox.setPadding(new Insets(10));
        mainBox.getChildren().addAll(resultsBox, selectedBox);

        VBox rootBox = new VBox(10);
        rootBox.setAlignment(Pos.CENTER);
        rootBox.getChildren().addAll(titleLabel, searchGrid, mainBox);

        // Set up the search button action
        searchButton.setOnAction(event -> {
            String searchTerm = searchField.getText();
            ArrayList<String> results = searchDatabase(searchTerm);
            resultsList.getItems().clear();
            resultsList.getItems().addAll(results);
            priceValueLabel.setText("");
            addButton.setDisable(true);
        });

        // Set up the results list selection action
        resultsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                priceValueLabel.setText(getPrice(newValue));
                addButton.setDisable(false);
            }
        });
        // Set up the add button action
        addButton.setOnAction(event -> {
            String selectedValue = resultsList.getSelectionModel().getSelectedItem();
            double selectedPrice = Double.parseDouble(getPrice(selectedValue));
            double total = Double.parseDouble(totalValueLabel.getText());
            total += selectedPrice * 0.05;
            totalValueLabel.setText(String.format("%.2f", total));
            selectedList.getItems().add(selectedValue);
        });

        // Set up the stage
        Scene scene = new Scene(rootBox, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Match Search");
        stage.show();
    }

    private ArrayList<String> searchDatabase(String searchTerm) {
        ArrayList<String> results = new ArrayList<>();
        try {
            String query = "SELECT * FROM WMT_Grocery_202209 WHERE PRODUCT_NAME LIKE '%" + searchTerm + "%'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("PRODUCT_NAME");
                String price = rs.getString("PRICE_RETAIL");
                results.add(name + " (" + price + ")");
            }
        } catch (SQLException ex) {
            System.out.println("Error searching database: " + ex.getMessage());
        }
        return results;
    }

    private String getPrice(String value) {
        String[] parts = value.split("\\(");
        String name = parts[0].trim();
        String query = "SELECT PRICE_RETAIL FROM WMT_Grocery_202209 WHERE PRODUCT_NAME = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("PRICE_RETAIL");
            }
        } catch (SQLException ex) {
            System.out.println("Error getting price from database: " + ex.getMessage());
        }
        return "";
    }

    public static void main(String[] args) {
        launch(args);
    }
}