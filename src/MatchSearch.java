import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class MatchSearch extends Application {
    private static final String DB_URL = "jdbc:mysql://cis244-prod.c28qsj4v6lea.us-east-2.rds.amazonaws.com:3306/Pricing Data";
    private static final String DB_USER = "Calculator";
    private static final String DB_PASSWORD = "";


    private Connection conn;
    private double inflationRate;

    {
        try {
            inflationRate = 1 + InflationAPI.APIcall();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MatchSearch.fxml"));
        MatchSearchController controller = new MatchSearchController();
        loader.setController(controller);
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Match Search");
        stage.show();

        conn = DriverManager.getConnection(DB_URL,
                DB_USER,
                DB_PASSWORD);

        controller.getSearchButton().setOnAction(event -> {
            String searchTerm = controller.getSearchField().getText();
            ArrayList<String> results = searchDatabase(searchTerm);
            controller.getResultsList().getItems().clear();
            controller.getResultsList().getItems().addAll(results);
            controller.getPriceValueLabel().setText("");
            controller.getAddButton().setDisable(true);
        });

        controller.getResultsList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                controller.getPriceValueLabel().setText(getPrice(newValue));
                controller.getAddButton().setDisable(false);
            }
        });

        controller.getAddButton().setOnAction(event -> {
            String selectedValue = controller.getResultsList().getSelectionModel().getSelectedItem();
            double selectedPrice = Double.parseDouble(getPrice(selectedValue));
            double total = Double.parseDouble(controller.getTotalValueLabel().getText());
            total += selectedPrice * inflationRate;
            controller.getTotalValueLabel().setText(String.format("%.2f", total));
            controller.getSelectedList().getItems().add(selectedValue);
        });

        controller.getRemoveButton().setOnMouseClicked(event -> {
            String selectedValue = controller.getSelectedList().getSelectionModel().getSelectedItem();
            if (selectedValue != null) {
                double selectedPrice = Double.parseDouble(getPrice(selectedValue));
                double total = Double.parseDouble(controller.getTotalValueLabel().getText());
                total -= selectedPrice * inflationRate;
                controller.getTotalValueLabel().setText(String.format("%.2f", total));
                controller.getSelectedList().getItems().remove(selectedValue);
            }
        });
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