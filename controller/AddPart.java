package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;
import model.InHouse;
import model.InventoryData;
import model.Outsourced;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * QKM2
 * Jack Compton
 *
 * LOGICAL ERROR: NumberFormatException was fixed by try/catch in onActionSavePart()
 */

public class AddPart implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    public Label toggleLabel;

    @FXML
    private ToggleButton inHouseButton;

    @FXML
    private ToggleButton outsourcedButton;

    @FXML
    private TextField partName;

    @FXML
    private TextField partStock;

    @FXML
    private TextField partPrice;

    @FXML
    private TextField partMin;

    @FXML
    private TextField partMax;

    @FXML
    private TextField partMachineId;

    // text label handler in GUI
    public void inHouseButton(ActionEvent actionEvent) {
        toggleLabel.setText("Machine ID");
    }
    public void outsourcedButton(ActionEvent actionEvent) {
        toggleLabel.setText("Company Name");
    }

    public void onActionSavePart(ActionEvent actionEvent) throws IOException {
        try {
            int id = Main.generateUniqueID();

            // checking inhouse/outsource button selection to save correct part type
            if (inHouseButton.isSelected()) {
                String name = partName.getText();
                double price = Double.parseDouble(partPrice.getText());
                int stock = Integer.parseInt(partStock.getText());
                int min = Integer.parseInt(partMin.getText());
                int max = Integer.parseInt(partMax.getText());
                int machineId = Integer.parseInt(partMachineId.getText());

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
                    InventoryData.addPart(new InHouse(id, name, price, stock, min, max, machineId));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Part Added!");
                    Optional<ButtonType> result = alert.showAndWait();
                }
            }
            if (outsourcedButton.isSelected()) {
                String name = partName.getText();
                double price = Double.parseDouble(partPrice.getText());
                int stock = Integer.parseInt(partStock.getText());
                int min = Integer.parseInt(partMin.getText());
                int max = Integer.parseInt(partMax.getText());
                String companyName = partMachineId.getText();

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
                    InventoryData.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Part Added!");
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
