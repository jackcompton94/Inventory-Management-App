package main;
/**
 * QKM2
 * Jack Compton
 *
 * JAVA DOCS LOCATED IN JavaDocFiles
 *
 * FUTURE ENHANCEMENT: adding an "Exceptions" class to "model" in order to clean up repeated code by defining if(exceptionOccurs)
 * FUTURE ENHANCEMENT: connecting to a live database with another application used to BUY items from the inventory created in this application
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static int n;
    private static int m;
    
    // placed in Main so that 'n' can be exclusively incremented and safe from ever creating a duplicate ID
    public static int generateUniqueID() {
        ++n;
        m = n + 100;
        return m;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setTitle("Inventory Management");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String [] args) {
        launch(args);}
}
