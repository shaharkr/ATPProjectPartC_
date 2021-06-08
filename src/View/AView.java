package View;

import ViewModel.MyViewModel;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Observer;
import java.util.Properties;

public abstract class AView implements IView, Initializable {

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

    public File loadDialog(ActionEvent actionEvent){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze Objects", "*.maze")
        );
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }

    public void showConfig(ActionEvent actionEvent) throws Exception {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setTitle("Properties");
        al.setHeaderText("");
        File file = new File("resources\\config.properties");
        InputStream inputStream = new FileInputStream(file);
        Properties p = new Properties();
        p.load(inputStream);
        inputStream.close();
        String s = "\n\n\n\nNumber of users concurrently: "+p.getProperty("threadPoolSize")+"\n\n\nMaze generate algorithm: "+p.getProperty("mazeGeneratingAlgorithm")+"\n\n\nMaze solving algorithm: "+p.getProperty("mazeSearchingAlgorithm");
        al.setContentText(s);
        ImageView imgv = new ImageView(new Image(new FileInputStream("./resources/View/proper.png")));
        imgv.setFitWidth(400);
        imgv.setFitHeight(400);
        al.setGraphic(imgv);
        al.showAndWait();
    }

}
