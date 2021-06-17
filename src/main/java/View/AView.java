package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Observer;
import java.util.Properties;

/**
 * abstract class that implements method from the interface IView
 * created because some methods are the same for all views, prevent code duplication.
 */
public abstract class AView implements IView, Initializable {

    /**
     * @param actionEvent every view must have an exit call, to request the view model
     */
    public void exitGame(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * next 3 methods are made for showing information that the player may request in the menuBar.
     */
    public void aboutCreatorsMenu(ActionEvent actionEvent) throws FileNotFoundException {
        showInfoWithImage("./resources/View/aboutCr.png","The Creators",600,360);
    }
    public void aboutAlgoMenu(ActionEvent actionEvent) throws FileNotFoundException {
        showInfoWithImage("./resources/View/aboutAlgs.png","The Algorithms", 600,360);
    }
    public void helpMenu(ActionEvent actionEvent) throws FileNotFoundException {
        showInfoWithImage("./resources/View/instructions.png", "Instructions", 600,360);
    }

    /**
     * @param pathToImage the image you want the player to see
     * @param title       title of the dialog box
     * @param w           width of dialog
     * @param h           height of dialig
     * @throws FileNotFoundException
     */
    public void showInfoWithImage(String pathToImage, String title,double w, double h) throws FileNotFoundException {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setTitle(title);
        al.setHeaderText("");
        al.getDialogPane().setPrefSize(w, h);
        Image img = null;
        try {
            img = new Image(new FileInputStream(pathToImage));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DialogPane dp = al.getDialogPane();
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true , true, true, true);
        Background background = new Background(new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize));
        dp.setBackground(background);
        dp.setMaxHeight(h);
        dp.setMaxWidth(w);
        ImageView imgv = new ImageView(new Image(new FileInputStream("./resources/View/empty.png")));
        al.setGraphic(imgv);
        al.showAndWait();
    }

    /**
     * @param actionEvent
     * @return the file that was loaded from the disc using the dialog
     */
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

    /**
     * @param actionEvent
     * @throws Exception
     * shows the configurations details that may changed by player in scene 2.
     */
    public void showConfig(ActionEvent actionEvent) throws Exception {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setTitle("Properties");
        al.setHeaderText("");
        File file = new File("resources\\config.properties");
        InputStream inputStream = new FileInputStream(file);
        Properties p = new Properties();
        p.load(inputStream);
        inputStream.close();
        String s = "\n\n\n\nNumber of users concurrently:\n\n\t\t\t\t"+ Configurations.getInstance().getThreadPoolSize() +"\n\n\nMaze generate algorithm:\n\n\t\t"+Configurations.getInstance().getMazeGeneratingAlgorithm()+"\n\n\nMaze solving algorithm:\n\n\t\t"+Configurations.getInstance().getMazeSearchingAlgorithm();
        Text t = new Text();
        t.setText(s);
        t.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        al.setContentText(t.getText());
        ImageView imgv = new ImageView(new Image(new FileInputStream("./resources/View/proper.png")));
        imgv.setFitWidth(400);
        imgv.setFitHeight(400);
        al.setGraphic(imgv);
        DialogPane dp = al.getDialogPane();
        al.showAndWait();
    }

}
