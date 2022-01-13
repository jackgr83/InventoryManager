package controller;

import javafx.scene.control.*;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {

    @FXML
    private RadioButton InHouseButton;
    @FXML
    private RadioButton OutsourcedButton;
    @FXML
    private TextField IdField;
    @FXML
    private TextField NameField;
    @FXML
    private TextField CountField;
    @FXML
    private TextField PriceField;
    @FXML
    private TextField MaxField;
    @FXML
    private TextField MinField;
    @FXML
    private TextField MachineIdCompanyField;
    @FXML
    private Label MachineIdCompanyLabel;
    @FXML
    private Button CancelButton;
    @FXML
    private Button SaveButton;

    Inventory inventory;
    Alert error = new Alert(Alert.AlertType.ERROR);

    /**
     * @param inventory set the inventory to the inventory passed in
     */
    public AddPartController (Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * This function sets the label to "Machine ID" when the In-House radio button is clicked
     */
    @FXML
    void InHouseButtonHandler () {
        MachineIdCompanyLabel.setText("Machine ID");
        OutsourcedButton.setSelected(false);
    }

    /**
     * This function sets the label to "Company Name" when the Outsourced radio button is clicked
     */
    @FXML
    void OutsourcedButtonHandler () {
        MachineIdCompanyLabel.setText("Company Name");
        InHouseButton.setSelected(false);
    }

    /**
     * This function generates a unique part ID
     */
    private void generatePartId () {
        Random num = new Random();
        Integer randomNum = num.nextInt(1000);
        if (inventory.lookupPart(randomNum) == null) {
            IdField.setText(randomNum.toString());
            return;
        } else {
            generatePartId();
        }
    }

    /**
     * This function handles error checking for the min, max, and inventory fields. If all fields are ok, then the part
     * is added to the inventory and the inventory is passed into the MainScreenController and user is taken back to the
     * main screen.
     */
    @FXML
    public void saveButtonHandler () throws IOException {
        /* check to make sure all fields are entered in */
        if (NameField.getText().trim().length() < 1 || CountField.getText().trim().length() < 1 || MaxField.getText().trim().length() < 1 ||
                MinField.getText().trim().length() < 1 || MachineIdCompanyField.getText().trim().length() < 1) {
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

        if (InHouseButton.isSelected()) {
            /* check machine id */
            try {
                Integer.parseInt(MachineIdCompanyField.getText().trim());
            } catch (NumberFormatException e) {
                error.setTitle("Error");
                error.setHeaderText("Please enter a valid machine ID in number format");
                error.showAndWait();
                return;
            }
            inventory.addPart(new InHouse(Integer.parseInt(IdField.getText().trim()), NameField.getText().trim(),
                    Double.parseDouble(PriceField.getText().trim()), Integer.parseInt(CountField.getText().trim()),
                    Integer.parseInt(MinField.getText().trim()), Integer.parseInt(MaxField.getText().trim()), (Integer.parseInt(MachineIdCompanyField.getText().trim()))));
        } else {
            inventory.addPart(new Outsourced(Integer.parseInt(IdField.getText().trim()), NameField.getText().trim(),
                    Double.parseDouble(PriceField.getText().trim()), Integer.parseInt(CountField.getText().trim()),
                    Integer.parseInt(MinField.getText().trim()), Integer.parseInt(MaxField.getText().trim()), MachineIdCompanyField.getText().trim()));
        }

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
     * This function takes the user back to the main screen when the cancel button is clicked
     */
    @FXML
    public void cancelButtonHandler () throws IOException {

        Stage stage = (Stage) CancelButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MainScreen.fxml"));
        MainScreenController controller = new MainScreenController(inventory);
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the AddPartController
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        InHouseButton.setSelected(true);
        IdField.setDisable(true);
        generatePartId();
    }

}
