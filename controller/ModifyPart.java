package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.InventoryData;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * QKM2
 * Jack Compton
 *
 * RUNTIME ERROR: ConcurrentModificationException was being thrown due to looping a list that was simultaneously being updated, fixed by including a break statement
 */

public class ModifyPart implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    public Label toggleLabel;

    @FXML
    private ToggleButton inHouseButton;

    @FXML
    private ToggleButton outsourcedButton;

    @FXML
    private TextField partId;

    @FXML
    private TextField partName;

    @FXML
    private TextField partStock;

    @FXML
    private TextField partPrice;

    @FXML
    private TextField partMax;

    @FXML
    private TextField partMin;

    @FXML
    private TextField partMachineId;

    // text label handler in GUI
    public void inHouseButton(ActionEvent actionEvent) {
        toggleLabel.setText("Machine ID");
    }
    public void outsourcedButton(ActionEvent actionEvent) {
        toggleLabel.setText("Company Name");
    }

    // catches the Outsourced Part object from MainMenu
    public void sendOutsourcedPart(Outsourced part) {
        outsourcedButton.setSelected(true);
        toggleLabel.setText("Company Name");
        partId.setText(String.valueOf(Integer.valueOf(part.getId())));
        partName.setText(String.valueOf(part.getName()));
        partStock.setText(String.valueOf(Integer.valueOf(part.getStock())));
        partPrice.setText(String.valueOf(Double.valueOf(part.getPrice())));
        partMax.setText(String.valueOf(Integer.valueOf(part.getMax())));
        partMin.setText(String.valueOf(Integer.valueOf(part.getMin())));
        partMachineId.setText(String.valueOf(part.getCompanyName()));
    }

    // catches the Inhouse Part object from MainMenu
    public void sendInhousePart(InHouse part) {
        inHouseButton.setSelected(true);
        toggleLabel.setText("Machine ID");
        partId.setText(String.valueOf(Integer.valueOf(part.getId())));
        partName.setText(String.valueOf(part.getName()));
        partStock.setText(String.valueOf(Integer.valueOf(part.getStock())));
        partPrice.setText(String.valueOf(Double.valueOf(part.getPrice())));
        partMax.setText(String.valueOf(Integer.valueOf(part.getMax())));
        partMin.setText(String.valueOf(Integer.valueOf(part.getMin())));
        partMachineId.setText(String.valueOf(Integer.valueOf(part.getMachineId())));
    }

    public void onActionSavePart(ActionEvent actionEvent){
        try {
            if (inHouseButton.isSelected()) {
                int id = Integer.parseInt(partId.getText());
                String name = partName.getText();
                double price = Double.parseDouble(partPrice.getText());
                int stock = Integer.parseInt(partStock.getText());
                int min = Integer.parseInt(partMin.getText());
                int max = Integer.parseInt(partMax.getText());
                int machineId = Integer.parseInt(partMachineId.getText());
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
                    InHouse selectedPart = new InHouse(id, name, price, stock, min, max, machineId);

                    // for loop designed to capture the correct index of getAllParts()
                    for (Part InHouse : InventoryData.getAllParts()) {
                        index++;
                        if(InHouse.getId() == id) {break;}
                    }
                    InventoryData.updatePart(index, selectedPart);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Part Updated!");
                    Optional<ButtonType> result = alert.showAndWait();
                }
            }
            if (outsourcedButton.isSelected()) {
                int id = Integer.parseInt(partId.getText());
                String name = partName.getText();
                double price = Double.parseDouble(partPrice.getText());
                int stock = Integer.parseInt(partStock.getText());
                int min = Integer.parseInt(partMin.getText());
                int max = Integer.parseInt(partMax.getText());
                String companyName = partMachineId.getText();
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
                    Outsourced selectedPart = new Outsourced(id, name, price, stock, min, max, companyName);

                    // for loop designed to capture the index of getAllParts()
                    for (Part Outsourced : InventoryData.getAllParts()) {
                        index++;
                        if (Outsourced.getId() == id) {break;}
                    }
                    InventoryData.updatePart(index, selectedPart);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Part Updated!");
                    Optional<ButtonType> result = alert.showAndWait();
                }
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

    @FXML
    public void onActionCancel(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
