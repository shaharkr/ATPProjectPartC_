package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * first view the player sees,
 * background shows until song stops to play.
 */
public class firstView extends AView {

    public void startGame(ActionEvent actionEvent) throws IOException {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/choicesView.fxml"));
        Scene secondScene = new Scene(root, 800, 600);
        primaryStage.setScene(secondScene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}
