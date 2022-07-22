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
import main.Main;
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
 *LOGICAL ERROR: AddProduct was not correctly showing parts added to the bottom list, corrected by initializing a "product build" list instead of trying to use associatedParts
 */

public class AddProduct implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableView<Part> partToProductTable;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partStockCol;

    @FXML
    private TableColumn<Part, Double> partPricePerUnitCol;

    @FXML
    private TableColumn<Part, Integer> partIdProductCol;

    @FXML
    private TableColumn<Part, String> partNameProductCol;

    @FXML
    private TableColumn<Part, Integer> partStockProductCol;

    @FXML
    private TableColumn<Part, Double> partPricePerUnitProductCol;

    @FXML
    private TextField searchPartText;

    @FXML
    private TextField productName;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField productStock;

    @FXML
    private TextField productMin;

    @FXML
    private TextField productMax;

    // TEMPORARY "PRODUCT BUILD" LIST
    private static ObservableList<Part> partToProduct = FXCollections.observableArrayList();
    public static ObservableList<Part> getAllPartToProduct() {return partToProduct;}

    public void onActionCancel(ActionEvent actionEvent) throws IOException {
        partToProduct.clear();
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionSaveProduct(ActionEvent actionEvent) {
        try {
            int id = Main.generateUniqueID();
            // ENSURES PRODUCT IDs ARE SEPARATELY UNIQUE FROM PART IDs (+1000)
            id = id + 900;

            String name = productName.getText();
            double price = Double.parseDouble(productPrice.getText());
            int stock = Integer.parseInt(productStock.getText());
            int min = Integer.parseInt(productMin.getText());
            int max = Integer.parseInt(productMax.getText());

            // LOGIC HANDLING OF INPUT VALUES
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
                InventoryData.addProduct(newProduct);

                // SWITCH OCCURS HERE TO PLACE THE TEMPORARY "PRODUCT BUILD" partToProduct LIST INTO A UNIQUE PRODUCT'S associatedParts
                for (int i = 0; i < partToProduct.size();) {
                    newProduct.addAssociatedPart(partToProduct.get(i));
                    i++;
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product Added!");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Format Error");
            alert.setContentText("Please enter correct values in the text fields!");
            alert.showAndWait();
        }
    }

    public void onActionRemovePart(ActionEvent actionEvent) {
        Part selection = partToProductTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Part to remove!");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to remove this Part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                partToProduct.remove(selection);
            }
        }
    }

    public void onActionAddPart(ActionEvent actionEvent) {
        Part selection = partTableView.getSelectionModel().getSelectedItem();

        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Part to add!");
            alert.showAndWait();
        }
        else {
            partToProduct.add(selection);
        }
    }

    public void onActionSearchPart(ActionEvent actionEvent) {
        String search = searchPartText.getText();

        if (search.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter text to Search!");
            alert.showAndWait();
        }
        if (!search.isEmpty()) {
            for (Part part : InventoryData.getAllParts()) {
                if (part.getName().contains(search) || Integer.toString(part.getId()).contains(search)) {
                    partTableView.getSelectionModel().select(part);
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partTableView.setItems(InventoryData.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partToProductTable.setItems(AddProduct.getAllPartToProduct());
        partIdProductCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameProductCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockProductCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitProductCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
