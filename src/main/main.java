package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/**
 * FUTURE ENHANCEMENT:
 * To extend the functionality of this application, a screen could be added to show a list of the machines that produce
 * each InHouse Part and the companies that produce each Outsourced part. The screens would also show details on each
 * machine and company. This Inventory Management System could further be extended by incorporating an ordering system
 * to allow managers/users to submit orders for new parts from their respective companies or in house machines.
 */

public class main extends Application {

    /**
     * Javadoc files located in software_I_proj/Javadoc folder
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Inventory inventory = new Inventory();
        loadSampleData(inventory);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MainScreen.fxml"));
        controller.MainScreenController controller = new controller.MainScreenController(inventory);
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    void loadSampleData(Inventory inventory) {
        Part part1 = new InHouse(1, "brakes", 15.00, 10, 3, 100, 101);
        inventory.addPart(part1);
        Part part2 = new InHouse(2, "wheels", 11.00, 16, 3, 100, 103);
        inventory.addPart(part2);
        Part part3 = new InHouse(3, "seat", 15.00, 10, 3, 100, 102);
        inventory.addPart(part3);
        inventory.addPart(new InHouse(4, "spokes", 20.00, 15, 3, 100, 104));
        inventory.addPart(new InHouse(5, "handlebars", 10.00, 5, 3, 100, 105));
        Part part6 = new Outsourced(6, "bell", 2.99, 10, 5, 100, "Mike's Bikes");
        inventory.addPart(part6);
        Part part7 = new Outsourced(7, "chain", 3.99, 9, 5, 100, "Orange Bicycles");
        inventory.addPart(part7);
        inventory.addPart(new Outsourced(8, "pedals", 2.99, 10, 5, 100, "The Bike Shop"));
        inventory.addPart(new Outsourced(9, "gears", 2.99, 10, 5, 100, "Mike's Bikes"));
        inventory.addPart(new Outsourced(10, "frame", 2.99, 10, 5, 100, "Bike Factory"));
        Product bike1 = new Product(652, "Giant Bike", 799.99, 20, 5, 100);
        bike1.addAssociatedPart(part1);
        bike1.addAssociatedPart(part2);
        inventory.addProduct(bike1);
        Product bike2 = new Product(598, "Tricycle", 199.99, 29, 5, 100);
        bike2.addAssociatedPart(part3);
        bike2.addAssociatedPart(part6);
        inventory.addProduct(bike2);
        Product bike3 = new Product(32, "Mountain Bike", 899.99, 30, 5, 100);
        bike3.addAssociatedPart(part7);
        inventory.addProduct(bike3);
        inventory.addProduct(new Product(410, "Road Bike", 599.99, 20, 5, 100));
        inventory.addProduct(new Product(51, "Electric Bike", 999.99, 9, 5, 100));

    }


}
