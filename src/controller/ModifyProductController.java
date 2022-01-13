package controller;

import javafx.scene.control.*;
import model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class ModifyProductController implements Initializable {

    @FXML
    private TextField IdField;
    @FXML
    private TextField NameField;
    @FXML
    private TextField CountField;
    @FXML
    private TextField PriceField;
    @FXML
    private TextField MinField;
    @FXML
    private TextField MaxField;
    @FXML
    private TextField SearchField;
    @FXML
    private TableView<Part> AllPartTable;
    @FXML
    private TableView<Part> AssocPartTable;
    @FXML
    private Button AddButton;
    @FXML
    private Button RemoveButton;
    @FXML
    private Button SaveButton;
    @FXML
    private Button CancelButton;
    private ObservableList<Part> assocPartList = FXCollections.observableArrayList();

    Inventory inventory;
    Product product;
    int index;
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    Alert error = new Alert(Alert.AlertType.ERROR);

    /**
     * @param product to be modified
     * @param inventory set the inventory to the inventory passed in
     * @param index of the product
     */
    public ModifyProductController(Product product, Inventory inventory, int index) {
        this.product = product;
        this.inventory = inventory;
        this.index = index;
    }

    /**
     * This function populates the top table with all parts in the inventory
     */
    private void populateAllPartTable () {
        AllPartTable.setItems(inventory.getAllParts());
    }

    /**
     * This function populates the bottom table with the product's associated parts
     */
    private void populateAssocPartTable () {
        assocPartList = product.getAllAssociatedParts();
        AssocPartTable.setItems(assocPartList);
    }

    /**
     * This function populates the text fields with the product's data
     */
    private void populateTextFields () {
        IdField.setText((Integer.toString(product.getId())));
        NameField.setText(product.getName());
        CountField.setText((Integer.toString(product.getStock())));
        PriceField.setText((Double.toString(product.getPrice())));
        MinField.setText((Integer.toString(product.getMin())));
        MaxField.setText((Integer.toString(product.getMax())));
    }

    /**
     * Initialize the Product Modify controller
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateTextFields();
        populateAllPartTable();
        populateAssocPartTable();
        IdField.setDisable(true);
    }

    /**
     * This function handles user input in the part search text field
     */
    @FXML
    private void partSearchHandler() {
        if (SearchField.getText().trim() != ""){
            try {
                ObservableList<Part> parts = FXCollections.observableArrayList();
                parts.add(inventory.lookupPart(Integer.parseInt(SearchField.getText())));
                AllPartTable.setItems(parts);
                AllPartTable.refresh();
                if (inventory.lookupPart(Integer.parseInt(SearchField.getText())) == null) {
                    error.setTitle("Error");
                    error.setHeaderText("No part found");
                    error.showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                AllPartTable.setItems(inventory.lookUpPart(SearchField.getText()));
                AllPartTable.refresh();
                if (AllPartTable.getItems().size() == 0) {
                    error.setTitle("Error");
                    error.setHeaderText("No part found");
                    error.showAndWait();
                    return;
                }
            }
        } else {
            AllPartTable.setItems(inventory.getAllParts());
        }
    }

    /**
     * This function adds the selected part as an associated part of the product and updates the bottom table
     */
    @FXML
    private void addButtonHandler () {
        Part selectedPart = AllPartTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null){
            error.setTitle("Error");
            error.setHeaderText("No part selected");
            error.showAndWait();
            return;
        }
        assocPartList.add(selectedPart);
        AssocPartTable.setItems(assocPartList);
    }

    /**
     * @param name of Item to perform action on
     * @param action to be performed on item
     * @return true if OK is clicked, otherwise return false
     */
    private boolean confirmed (String name, String action) {
        confirm.setTitle("Confirm action");
        confirm.setHeaderText("Are you sure you want to " + action + ": " + name);
        confirm.setContentText("Click ok to confirm");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This function removes an associated part from the product and the table
     */
    @FXML
    private void removeButtonHandler () {
        Part assocPart = AssocPartTable.getSelectionModel().getSelectedItem();
        if (confirmed(assocPart.getName(), "remove")) {
            assocPartList.remove(assocPart);
            AssocPartTable.setItems(assocPartList);
        };
    }

    /**
     * This function handles error checking for the min, max, and inventory fields. If all fields are ok, then the product
     * is added to the inventory and the inventory is passed into the MainScreenController and user is taken back to the
     * main screen.
     */
    @FXML
    private void saveButtonHandler () throws IOException {
        /* check to make sure all fields are entered in */
        if (NameField.getText().trim().length() < 1 || CountField.getText().trim().length() < 1 || MaxField.getText().trim().length() < 1 ||
                MinField.getText().trim().length() < 1) {
            error.setTitle("Error");
            error.setHeaderText("Please enter data in each field");
            error.showAndWait();
            return;
        }
        /* check integer fields */
        try {
            Integer.parseInt(MinField.getText().trim());
            Integer.parseInt(MaxField.getText().trim());
            Integer.parseInt(CountField.getText().trim());
        } catch (NumberFormatException e) {
            error.setTitle("Error");
            error.setHeaderText("Please valid numbers into min, max, and inv fields");
            error.showAndWait();
            return;
        }
        if (Integer.parseInt(MinField.getText().trim()) > Integer.parseInt(MaxField.getText().trim())) {
            error.setTitle("Error");
            error.setHeaderText("Min must be less than Max");
            error.showAndWait();
            return;
        }
        if (Integer.parseInt(CountField.getText().trim()) < Integer.parseInt(MinField.getText().trim()) ||
                Integer.parseInt(CountField.getText().trim()) > Integer.parseInt(MaxField.getText().trim())) {
            error.setTitle("Error");
            error.setHeaderText("Inventory must be between Min and Max");
            error.showAndWait();
            return;
        }
        /* check price field */
        try {
            Double.parseDouble(PriceField.getText().trim());
        } catch (NumberFormatException e) {
            error.setTitle("Error");
            error.setHeaderText("Please enter a valid price in dollar and cents format.");
            error.showAndWait();
            return;
        }

        Product product = new Product(Integer.parseInt(IdField.getText().trim()), NameField.getText().trim(), Double.parseDouble(PriceField.getText().trim()),
                Integer.parseInt(CountField.getText().trim()), Integer.parseInt(MinField.getText().trim()), Integer.parseInt(MaxField.getText().trim()));
        for (int i=0; i<assocPartList.size(); i++) {
            product.addAssociatedPart(assocPartList.get(i));
        }
        inventory.updateProduct(index, product);

        Stage stage = (Stage) SaveButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MainScreen.fxml"));
        MainScreenController controller = new MainScreenController(inventory);
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This function takes the user back to the main screen
     */
    @FXML
    private void cancelButtonHandler () throws IOException {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MainScreen.fxml"));
        MainScreenController controller = new MainScreenController(inventory);
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

}
