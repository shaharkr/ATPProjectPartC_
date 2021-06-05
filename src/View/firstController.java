package View;

import com.sun.media.jfxmediaimpl.platform.Platform;
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
import java.util.ResourceBundle;

public class firstController extends AController implements Initializable {
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

    public void exitGame(ActionEvent actionEvent) {
        System.exit(0);
    }
}
