package controller;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

import model.Inventory;
import model.Part;
import model.Product;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Optional;

public class MainScreenController implements Initializable {

    @FXML
    private TextField PartSearch;

    @FXML
    private TableView<Part> PartTableView;

    @FXML
    private TableColumn<Part, Integer> PartIDColumn;

    @FXML
    private TableColumn<Part, String> PartNameColumn;

    @FXML
    private TableColumn<Part, Integer> PartQuantityColumn;

    @FXML
    private TableColumn<Part, Double> PartPriceColumn;

    @FXML
    private Button PartAddButton;

    @FXML
    private Button PartModifyButton;

    @FXML
    private TextField ProductSearch;

    @FXML
    private TableView<Product> ProductTableView;

    @FXML
    private TableColumn<Product, Integer> ProductIDColumn;

    @FXML
    private TableColumn<Product, String> ProductNameColumn;

    @FXML
    private TableColumn<Product, Integer> ProductQuantityColumn;

    @FXML
    private TableColumn<Product, Double> ProductPriceColumn;

    @FXML
    private Button ProductAddButton;

    @FXML
    private Button ProductModifyButton;

    Inventory inventory;
    Alert error = new Alert(Alert.AlertType.ERROR);
    Alert confirm = new Alert(AlertType.CONFIRMATION);

    /**
     * @param inventory set the inventory to the inventory passed in
     */
    public MainScreenController(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Exits the application
     */
    @FXML
    void exitButtonHandler() {
        System.exit(0);
    }

    /**
     * Take user to the Add Part screen
     */
    @FXML
    void partAddHandler() throws IOException {
        Stage stage = (Stage) PartAddButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AddPart.fxml"));
        AddPartController controller = new AddPartController(inventory);
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Take user to the Add Product screen
     */
    @FXML
    void productAddHandler() throws IOException {
        Stage stage = (Stage) ProductAddButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AddProduct.fxml"));
        AddProductController controller = new AddProductController(inventory);
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Take user to the Part Modify screen
     *
     * RUNTIME ERROR:
     * I got a null pointer exception when trying to call the part modify screen when no part was selected. I fixed this
     * error by adding in error handling to show an alert when nothing is selected.
     */
    @FXML
    void partModifyHandler() throws IOException {
        Part selectedPart = PartTableView.getSelectionModel().getSelectedItem();
        if (selectedPart == null){
            error.setTitle("Error");
            error.setHeaderText("No part selected");
            error.showAndWait();
            return;
        }
        int index = PartTableView.getSelectionModel().getSelectedIndex();
        Stage stage = (Stage) PartModifyButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ModifyPart.fxml"));
        ModifyPartController controller = new ModifyPartController(selectedPart, inventory, index);
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Take user to the Product Modify screen
     *
     * RUNTIME ERROR:
     * I got a load exception and a null pointer exception when calling this function. I figured out that
     * this error was caused by trying to load items from the fxml document that had not yet been given IDs in the fxml
     * document. I fixed this error by adding a "fx:id" attribute to each item in the fxml document.
     */
    @FXML
    void productModifyHandler() throws IOException {
        Product selectedProduct = ProductTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null){
            error.setTitle("Error");
            error.setHeaderText("No product selected");
            error.showAndWait();
            return;
        }
        int index = ProductTableView.getSelectionModel().getSelectedIndex();
        Stage stage = (Stage) ProductModifyButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ModifyProduct.fxml"));
        ModifyProductController controller = new ModifyProductController(selectedProduct, inventory, index);
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Confirm delete of part/product
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
     * Delete part
     */
    @FXML
    void partDeleteHandler() throws IOException {
        Part partSelected = PartTableView.getSelectionModel().getSelectedItem();
        if (partSelected == null){
            error.setTitle("Error");
            error.setHeaderText("No part selected");
            error.showAndWait();
            return;
        }
        if (confirmed(PartTableView.getSelectionModel().getSelectedItem().getName(), "delete")) {
            inventory.deletePart(partSelected);
            PartTableView.refresh();
        }
    }

    /**
     * Delete product
     */
    @FXML
    void productDeleteHandler() throws IOException {
        Product productSelected = ProductTableView.getSelectionModel().getSelectedItem();
        if (productSelected == null){
            error.setTitle("Error");
            error.setHeaderText("No product selected");
            error.showAndWait();
            return;
        }
        if (productSelected.getAllAssociatedParts().size() != 0) {
            error.setTitle("Error");
            error.setHeaderText("Cannot delete product with associated parts");
            error.showAndWait();
            return;
        }
        if (confirmed(ProductTableView.getSelectionModel().getSelectedItem().getName(), "delete")) {
            inventory.deleteProduct(productSelected);
            ProductTableView.refresh();
        }

    }

    /**
     * This function handles user input in the part search text field
     *
     * LOGICAL ERROR:
     * When I was searching for a part by its ID, the error handling appeared to not be working. The error did not alert
     * the user when an ID was not entered that did not match any parts. I fixed this error by checking the output of
     * the inventory.lookupPart function instead of the size of the Part Table itself.
     *
     */
    @FXML
    void partSearchHandler() {
        if (PartSearch.getText().trim() != ""){
            ObservableList<Part> parts =  FXCollections.observableArrayList();
            try {
                parts.add(inventory.lookupPart(Integer.parseInt(PartSearch.getText())));
                PartTableView.setItems(parts);
                PartTableView.refresh();
                if (inventory.lookupPart(Integer.parseInt(PartSearch.getText())) == null) {
                    error.setTitle("Error");
                    error.setHeaderText("No part found");
                    error.showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                PartTableView.setItems(inventory.lookUpPart(PartSearch.getText()));
                PartTableView.refresh();
                if (PartTableView.getItems().size() == 0) {
                    error.setTitle("Error");
                    error.setHeaderText("No part found");
                    error.showAndWait();
                    return;
                }
            }
        } else {
            PartTableView.setItems(inventory.getAllParts());
        }
    }

    /**
     * This function handles user input in the product search text field
     */
    @FXML
    void productSearchHandler() {
        if (ProductSearch.getText().trim() != ""){
            try {
                ObservableList<Product> products = FXCollections.observableArrayList();
                products.add(inventory.lookupProduct(Integer.parseInt(ProductSearch.getText())));
                ProductTableView.setItems(products);
                ProductTableView.refresh();
                if (inventory.lookupProduct(Integer.parseInt(ProductSearch.getText())) == null) {
                    error.setTitle("Error");
                    error.setHeaderText("No product found");
                    error.showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                ProductTableView.setItems(inventory.lookUpProduct(ProductSearch.getText()));
                ProductTableView.refresh();
                if (ProductTableView.getItems().size() == 0) {
                    error.setTitle("Error");
                    error.setHeaderText("No product found");
                    error.showAndWait();
                    return;
                }
            }
        } else {
            ProductTableView.setItems(inventory.getAllProducts());
        }
    }

    /**
     * Populate part and product tables with inventory data
     */
    private void populateTables () {
        PartTableView.setItems(inventory.getAllParts());
        ProductTableView.setItems(inventory.getAllProducts());
    }

    /**
     * Initialize the Main Screen
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateTables();

    }
}
