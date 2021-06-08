package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class firstView extends AView {
    @FXML
    Button startButton;
    public void startGame(ActionEvent actionEvent) throws IOException {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("choicesView.fxml"));
        Scene secondScene = new Scene(root, 800, 600);
        primaryStage.setScene(secondScene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //startButton.getStylesheets().add("myView.css");
    }

    /*    <center>
        <Pane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="463.0" prefWidth="684.0" xmlns="http://javafx.com/javafx/16" xmlns:fx="http://javafx.com/fxml/1" >
            <children>
                <Button fx:id="startButton" layoutX="600.0" layoutY="450.0" mnemonicParsing="false" onAction="#startGame" prefHeight="39.0" prefWidth="86.0" text="Lets Start!" stylesheets="@myViewCSS.css" BorderPane.alignment="BOTTOM_RIGHT"/>
                <Button fx:id="exitButton" layoutX="0.0" layoutY="450.0" mnemonicParsing="false" onAction="#exitGame" prefHeight="39.0" prefWidth="86.0" text="Exit" stylesheets="@myViewCSS.css" BorderPane.alignment="BOTTOM_LEFT"/>
            </children>
        </Pane>
    </center>
</BorderPane>*/
}
