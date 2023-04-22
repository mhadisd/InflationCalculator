import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
public class MatchSearchController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> resultsList;

    @FXML
    private Label priceValueLabel;

    @FXML
    private Button addButton;

    @FXML
    private ListView<String> selectedList;

    @FXML
    private Label totalValueLabel;

    @FXML
    private Button removeButton;

    public TextField getSearchField() {
        return searchField;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public ListView<String> getResultsList() {
        return resultsList;
    }

    public Label getPriceValueLabel() {
        return priceValueLabel;
    }

    public Button getAddButton() {
        return addButton;
    }

    public ListView<String> getSelectedList
            () {
        return selectedList;
    }

    public Label getTotalValueLabel() {
        return totalValueLabel;
    }

    public Button getRemoveButton() {
        return removeButton;
    }
}