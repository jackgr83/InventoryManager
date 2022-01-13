package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyPartController implements Initializable {

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
    Part part;
    int index;
    Alert error = new Alert(Alert.AlertType.ERROR);

    /**
     * @param part to be modified
     * @param inventory set the inventory to the inventory passed in
     * @param index of the part
     */
    public ModifyPartController(Part part, Inventory inventory, int index) {
        this.part = part;
        this.inventory = inventory;
        this.index = index;
    }

    /**
     * This function sets the label to "Machine ID" when the In-House radio button is clicked and erases the information
     * in the text field.
     *
     * RUNTIME ERROR:
     * I got a number format exception when trying to create a new in house part with empty number fields. I fixed this
     * error by adding dummy data into the integer fields until the part is saved.
     */
    @FXML
    void InHouseButtonHandler () {
        OutsourcedButton.setSelected(false);
        MachineIdCompanyLabel.setText("Machine ID");
        MachineIdCompanyField.setText("");
        if (part instanceof Outsourced) {
            try {
                /* sets machine ID to 1 as dummy data until part is saved */
                Part part = new InHouse(Integer.parseInt(IdField.getText().trim()), NameField.getText().trim(),
                        Double.parseDouble(PriceField.getText().trim()), Integer.parseInt(CountField.getText().trim()),
                        Integer.parseInt(MinField.getText().trim()), Integer.parseInt(MaxField.getText().trim()), 1);
                this.part = part;
            } catch (NumberFormatException e) {
                /* If number fields are empty, use dummy data until part is saved */
                Part part = new InHouse(Integer.parseInt(IdField.getText().trim()), NameField.getText().trim(),
                        10.99, 1,
                        1, 1, 1);
                this.part = part;
            }

        }
    }

    /**
     * This function sets the label to "Company Name" when the Outsourced radio button is clicked
     */
    @FXML
    void OutsourcedButtonHandler () {
        InHouseButton.setSelected(false);
        MachineIdCompanyLabel.setText("Company Name");
        MachineIdCompanyField.setText("");
        if (part instanceof InHouse) {
            try {
                Part part = new Outsourced(Integer.parseInt(IdField.getText().trim()), NameField.getText().trim(),
                        Double.parseDouble(PriceField.getText().trim()), Integer.parseInt(CountField.getText().trim()),
                        Integer.parseInt(MinField.getText().trim()), Integer.parseInt(MaxField.getText().trim()), MachineIdCompanyField.getText().trim());
                this.part = part;
            } catch (NumberFormatException e) {
                /* If integer fields are empty, use dummy data until part is saved */
                Part part = new Outsourced(Integer.parseInt(IdField.getText().trim()), NameField.getText().trim(),
                        10.99, 1,
                        1, 1, MachineIdCompanyField.getText().trim());
                this.part = part;
            }
        }
    }

    /**
     * This function handles error checking for the min, max, and inventory fields. If all fields are ok, then the part
     * is added to the inventory and the inventory is passed into the MainScreenController and user is taken back to the
     * main screen.
     *
     * RUNTIME ERROR:
     * Number format exception error. Added error handling to check if fields entered are in valid formats.
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
        /* check min max fields */
        if (Integer.parseInt(MinField.getText().trim()) > Integer.parseInt(MaxField.getText().trim())) {
            error.setTitle("Error");
            error.setHeaderText("Min must be less than Max");
            error.showAndWait();
            return;
        }
        /* check stock level */
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

        if (part instanceof InHouse) {
            /* check machine id */
            try {
                Integer.parseInt(MachineIdCompanyField.getText().trim());
            } catch (NumberFormatException e) {
                error.setTitle("Error");
                error.setHeaderText("Please enter a valid machine ID in number format");
                error.showAndWait();
                return;
            }
            updateInHousePart();
        } else {
            updateOutsourcedPart();
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
     * This function updates the part in the inventory
     */
    private void updateInHousePart() {
        inventory.updatePart(index, new InHouse(Integer.parseInt(IdField.getText().trim()), NameField.getText().trim(),
                Double.parseDouble(PriceField.getText().trim()), Integer.parseInt(CountField.getText().trim()),
                Integer.parseInt(MinField.getText().trim()), Integer.parseInt(MaxField.getText().trim()), Integer.parseInt(MachineIdCompanyField.getText().trim())));
    }

    /**
     * This function updates the part in the inventory
     */
    private void updateOutsourcedPart() {
        inventory.updatePart(index, new Outsourced(Integer.parseInt(IdField.getText().trim()), NameField.getText().trim(),
                Double.parseDouble(PriceField.getText().trim()), Integer.parseInt(CountField.getText().trim()),
                Integer.parseInt(MinField.getText().trim()), Integer.parseInt(MaxField.getText().trim()), MachineIdCompanyField.getText().trim()));
    }

    /**
     * Initialize the Modify Part and set the fields with inventory data
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        IdField.setDisable(true);
        if (part instanceof InHouse) {
            InHouse partIH = (InHouse) part;
            InHouseButton.setSelected(true);
            MachineIdCompanyLabel.setText("Machine ID");
            NameField.setText(partIH.getName());
            IdField.setText((Integer.toString(partIH.getId())));
            CountField.setText((Integer.toString(partIH.getStock())));
            PriceField.setText((Double.toString(partIH.getPrice())));
            MinField.setText((Integer.toString(partIH.getMin())));
            MaxField.setText((Integer.toString(partIH.getMax())));
            MachineIdCompanyField.setText((Integer.toString(partIH.getMachineID())));
        } else {
            OutsourcedButton.setSelected(true);
            MachineIdCompanyLabel.setText("Company Name");
            Outsourced partOS = (Outsourced) part;
            OutsourcedButton.setSelected(true);
            MachineIdCompanyLabel.setText("Company Name");
            NameField.setText(partOS.getName());
            IdField.setText((Integer.toString(partOS.getId())));
            CountField.setText((Integer.toString(partOS.getStock())));
            PriceField.setText((Double.toString(partOS.getPrice())));
            MinField.setText((Integer.toString(partOS.getMin())));
            MaxField.setText((Integer.toString(partOS.getMax())));
            MachineIdCompanyField.setText(partOS.getCompanyName());
        }

    }


}
