package View;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class AController {
    public void exitGame(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void aboutCreatorsMenu(ActionEvent actionEvent) throws FileNotFoundException {
        showInfoWithImage("./resources/View/benChar.png");
    }
    public void aboutAlgoMenu(ActionEvent actionEvent) throws FileNotFoundException {
        showInfoWithImage("./resources/View/benChar.png");
    }
    public void helpMenu(ActionEvent actionEvent) throws FileNotFoundException {
        showInfoWithImage("./resources/View/benChar.png");
    }
    public void showInfoWithImage(String pathToImage) throws FileNotFoundException {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setTitle("About");
        ImageView imgv = new ImageView(new Image(new FileInputStream(pathToImage)));
        imgv.setFitWidth(260);
        imgv.setFitHeight(260);
        al.setGraphic(imgv);
        al.showAndWait();
    }
}
