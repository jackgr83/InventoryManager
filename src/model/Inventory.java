package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     * @param part Part to be added
     */
    public static void addPart(Part part) {
        allParts.add(part);
    }

    /**
     * @param product Part to be added.
     */
    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    /**
     * @param partId Part to be searched.
     * @return p if the part is found, otherwise null
     *
     * LOGICAL ERROR:
     * When I entered the first number of the part ID into the search field it did not return any parts. This was
     * logically incorrect as the instructions state that any portion of the id can be returned. I corrected this by
     * using the .contains function as opposed to an equality operator.
     */
    public static Part lookupPart(int partId) {
        for (int i = 0; i < allParts.size(); i++) {
            Part p = allParts.get(i);
            if (String.valueOf(p.getId()).contains(String.valueOf(partId)))
                return p;
        }
        return null;
    }

    /**
     * @param productId Product to be searched.
     * @return p if the product is found, otherwise null
     *
     * LOGICAL ERROR:
     * When I entered the first number of the product ID into the search field it did not return any products. This was
     * logically incorrect as the instructions state that any portion of the id can be returned. I corrected this by
     * using the .contains function instead of the equality operator.
     */
    public static Product lookupProduct(int productId) {
        for (int i = 0; i < allProducts.size(); i++) {
            Product p = allProducts.get(i);
            if (String.valueOf(p.getId()).contains(String.valueOf(productId)))
                return p;
        }
        return null;

    }

    /**
     * @param partName Part name to be searched
     * @return part if the part's name is found
     */
    public static ObservableList<Part> lookUpPart(String partName) {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        for (Part part : getAllParts()) {
            if (part.getName().contains(partName)) {
                parts.add(part);
            }
        }
        return parts;
    }

    /**
     * @param productName Product name to be searched
     * @return product if the product's name is found
     */
    public static ObservableList<Product> lookUpProduct(String productName) {

        ObservableList<Product> products = FXCollections.observableArrayList();

        for (Product product : getAllProducts()){
            if(product.getName().contains(productName)){
                products.add(product);
            }
        }
        return products;
    }

    /**
     * @param index index of the selected part
     * @param selectedPart Part object to be modified
     * */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }
    /**
     * @param index index of the selected product
     * @param newProduct Product object to be modified
     * */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * @param selectedPart part to be deleted
     * @return True if part is deleted. If part is not found, returns false.
     */
    public static boolean deletePart(Part selectedPart) {
        for (Part p : allParts) {
            if (p.getId() == selectedPart.getId()) {
                allParts.remove(p);
                return true;
            }
        }
        return false;
    }

    /**
     * @param selectedProduct product to be deleted
     *@return True if product is deleted. If product is not found, returns false.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        for (Product p : allProducts) {
            if (p.getId() == selectedProduct.getId()) {
                allProducts.remove(p);
                return true;
            }
        }
        return false;
    }

    /**
     *@return all parts in list
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     *@return all products in list
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
