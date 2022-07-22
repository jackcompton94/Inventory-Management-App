package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * QKM2
 * Jack Compton
 *
 * RUNTIME ERROR: NullPointerException was being thrown from Search and Delete methods, resolved with try/catch
 */

public class MainMenu implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partStockCol;

    @FXML
    private TableColumn<Part, Double> partPricePerUnitCol;

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Integer> productStockCol;

    @FXML
    private TableColumn<Product, Double> productPricePerUnitCol;

    @FXML
    private TextField searchPartText;

    @FXML
    private TextField searchProductText;

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

    @FXML
    public void onActionSearchProduct(ActionEvent actionEvent) {
        String search = searchProductText.getText();

        if (search.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter text to Search!");
            alert.showAndWait();
        }
        if (!search.isEmpty()) {
            for (Product product : InventoryData.getAllProducts()) {
                if (product.getName().contains(search) || Integer.toString(product.getId()).contains(search)) {
                    productTableView.getSelectionModel().select(product);
                }
            }
        }
    }

    @FXML
    public void onActionAddPart(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void onActionAddProduct(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void onActionModifyPart(ActionEvent actionEvent) throws IOException {
        Part selection = partTableView.getSelectionModel().getSelectedItem();

        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Part to modify!");
            alert.showAndWait();
        }
        else if (selection instanceof Outsourced) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
            loader.load();

            // SENDS Outsourced OBJECT TO ModifyPart
            ModifyPart ModifyOutsourcedPartController = loader.getController();
            ModifyOutsourcedPartController.sendOutsourcedPart((Outsourced) partTableView.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else if (selection instanceof InHouse) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
            loader.load();

            // SENDS Inhouse OBJECT TO ModifyPart
            ModifyPart ModifyPartController = loader.getController();
            ModifyPartController.sendInhousePart((InHouse) partTableView.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    public void onActionModifyProduct(ActionEvent actionEvent) throws IOException {
        Product selection = productTableView.getSelectionModel().getSelectedItem();

        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Product to modify!");
            alert.showAndWait();
        }
        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
            loader.load();

            // SENDS Product OBJECT TO ModifyProduct
            ModifyProduct ModifyProductController = loader.getController();
            ModifyProductController.sendProduct(productTableView.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    public void onActionDeletePart(ActionEvent actionEvent) {
        Part selection = partTableView.getSelectionModel().getSelectedItem();

        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Part to delete!");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this Part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                InventoryData.deletePart(selection);
            }
        }
    }

    @FXML
    public void onActionDeleteProduct(ActionEvent actionEvent) {
        try {
            Product selection = productTableView.getSelectionModel().getSelectedItem();

            if (!selection.getAllAssociatedParts().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete a Product with Associated Parts!");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this Product?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    InventoryData.deleteProduct(selection);
                }
            }
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Product to delete!");
            alert.showAndWait();
        }
    }

    @FXML
    public void onActionExit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to close the application?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partTableView.setItems(InventoryData.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setItems(InventoryData.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
