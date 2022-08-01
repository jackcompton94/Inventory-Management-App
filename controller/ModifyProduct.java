package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.InventoryData;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * QKM2
 * Jack Compton
 *
 * LOGICAL ERROR: unable to view a correctly modified product due to trying to update associatedParts directly, fixed by using partsToProduct to swap the lists with .setItems()
 */

public class ModifyProduct implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableView<Part> associatedPartsTable;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partStockCol;

    @FXML
    private TableColumn<Part, Double> partPricePerUnitCol;

    @FXML
    private TableColumn<Product, Integer> partIdProductCol;

    @FXML
    private TableColumn<Product, String> partNameProductCol;

    @FXML
    private TableColumn<Product, Integer> partStockProductCol;

    @FXML
    private TableColumn<Product, Double> partPricePerUnitProductCol;

    @FXML
    private TextField searchPartText;

    @FXML
    private TextField productId;

    @FXML
    private TextField productName;

    @FXML
    private TextField productStock;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField productMax;

    @FXML
    private TextField productMin;

    // temporary "product build" list
    private static ObservableList<Part> partToProduct = FXCollections.observableArrayList();
    public static ObservableList<Part> getAllPartToProduct() {return partToProduct;}

    public void onActionSearchPart(ActionEvent actionEvent) {
        String search = searchPartText.getText();

        partTableView.getSelectionModel().clearSelection();

        // user input check
        if (search.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter text to Search!");
            alert.showAndWait();
        }

        // loops through the arrayList to find a match (or multiple matches)
        if (!search.isEmpty()) {
            for (Part part : InventoryData.getAllParts()) {
                if (part.getName().contains(search) || Integer.toString(part.getId()).contains(search)) {
                    partTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                    partTableView.getSelectionModel().select(part);
                }
            }

            // end of loop confirmation for if a match is found
            if (!partTableView.getSelectionModel().getSelectedItems().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Part(s) found!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No Part(s) found!");
                alert.showAndWait();
            }
        }
    }

    public void sendProduct(Product product) {
        productId.setText(String.valueOf(Integer.valueOf(product.getId())));
        productName.setText(String.valueOf(product.getName()));
        productStock.setText(String.valueOf(Integer.valueOf(product.getStock())));
        productPrice.setText(String.valueOf(Double.valueOf(product.getPrice())));
        productMax.setText(String.valueOf(Integer.valueOf(product.getMax())));
        productMin.setText(String.valueOf(Integer.valueOf(product.getMin())));

        // catches the associatedParts of a Product and swap to the partsToProduct to allow a dynamic temporary "build list"
        associatedPartsTable.setItems(product.getAllAssociatedParts());
        partToProduct.addAll(product.getAllAssociatedParts());
    }

    public void onActionAddPart(ActionEvent actionEvent) {
        Part selection = partTableView.getSelectionModel().getSelectedItem();

        // user input check
        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Part to add!");
            alert.showAndWait();
        }

        // adds the temporary list back into the Products associatedParts
        else {
            partToProduct.add(selection);
            associatedPartsTable.setItems(partToProduct);
        }
    }

    public void onActionRemovePart(ActionEvent actionEvent) {
        Part selection = associatedPartsTable.getSelectionModel().getSelectedItem();

        // user input check
        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Part to remove!");
            alert.showAndWait();
        }

        // confirmation of deletions
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to remove this Part?");
            Optional<ButtonType> result = alert.showAndWait();

            // dynamically removing a Part while saving the latest arrayList in the associatedParts
            if (result.get() == ButtonType.OK) {
                partToProduct.remove(selection);
                associatedPartsTable.setItems(partToProduct);
            }
        }
    }

    public void onActionSaveProduct(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(productId.getText());
            String name = productName.getText();
            double price = Double.parseDouble(productPrice.getText());
            int stock = Integer.parseInt(productStock.getText());
            int min = Integer.parseInt(productMin.getText());
            int max = Integer.parseInt(productMax.getText());
            int index = -1;

            // input value logic check
            if (min > max){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Format Error");
                alert.setContentText("\"Min\" cannot be a greater value than \"Max\"");
                alert.showAndWait();
            }
            if (stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Format Error");
                alert.setContentText("\"Inventory\" must be between the values of \"Max\" and \"Min\"");
                alert.showAndWait();
            }
            else {
                Product newProduct;
                newProduct = new Product(id, name, price, stock, min, max);

                // // for loop designed to capture the correct index of getAllProducts()
                for (Product product : InventoryData.getAllProducts()) {
                    index++;
                    if (product.getId() == id) {break;}
                }
                InventoryData.updateProduct(index, newProduct);

                for (int i = 0; i < partToProduct.size();) {
                    newProduct.addAssociatedPart(partToProduct.get(i));
                    i++;
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product Updated!");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }

        // prevents program from attempting to save null/invalid text fields
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Format Error");
            alert.setContentText("Please enter correct values in the text fields!");
            alert.showAndWait();
        }
    }

    public void onActionCancel(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partTableView.setItems(InventoryData.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partIdProductCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameProductCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockProductCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitProductCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // resets partsToProduct list each time ModifyProduct is loaded to prevent an overflow of memory from previous modified products
        partToProduct.clear();
    }
}
