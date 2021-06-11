package View;


import algorithms.mazeGenerators.IMazeGenerator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class secondView extends AView{
    @FXML
    RadioButton shaharSelect;
    @FXML
    RadioButton benSelect;
    @FXML
    TextField RowTextField;
    @FXML
    TextField ColTextField;
    @FXML
    Label RowColError;
    @FXML
    javafx.scene.image.ImageView benChar;
    @FXML
    javafx.scene.image.ImageView shaharChar;
    @FXML
    RadioButton classicWallSelector;
    @FXML
    RadioButton woodWallSelector;
    @FXML
    RadioButton lavaWallSelector;
    @FXML
    RadioButton iceWallSelector;
    @FXML
    RadioButton ironWallSelector;
    @FXML
    RadioButton classicBackSelector;
    @FXML
    RadioButton moonBackSelector;
    @FXML
    RadioButton beachBackSelector;
    @FXML
    RadioButton forestBackSelector;
    @FXML
    RadioButton desertBackSelector;

    String pathWall = "./resources/View/classic.JPG";
    String pathPlayer = "./resources/View/benChar.png";
    String pathBack = "./resources/View/classicBack.jpg";
    public IMazeGenerator generator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ColTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                RowColError.setText("");

            }
        });
        RowTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                RowColError.setText("");

            }
        });
        Main.media.setOnEndOfMedia(()->{});
        Main.turnMusicOn("./resources/Music/marioShort.mp3");
    }

    public void WritingTextBoxConfig(ActionEvent actionEvent) {
        RowColError.setText("");
    }

    public void selectionPressBen(ActionEvent actionEvent) {
        if (shaharSelect.isSelected())
            shaharSelect.setSelected(false);
        benSelect.setSelected(true);
        pathPlayer = "./resources/View/benChar.png";
    }

    public void selectionPressShahar(ActionEvent actionEvent) {
        if (benSelect.isSelected())
            benSelect.setSelected(false);
        shaharSelect.setSelected(true);
        pathPlayer = "./resources/View/saraChar.png";
    }

    public void selectionPressClassic(ActionEvent actionEvent) {
        if(woodWallSelector.isSelected())
            woodWallSelector.setSelected(false);
        if(lavaWallSelector.isSelected())
            lavaWallSelector.setSelected(false);
        if(iceWallSelector.isSelected())
            iceWallSelector.setSelected(false);
        if(ironWallSelector.isSelected())
            ironWallSelector.setSelected(false);
        classicWallSelector.setSelected(true);
        pathWall = "./resources/View/classic.JPG";
    }

    public void selectionPressIce(ActionEvent actionEvent) {
        if(woodWallSelector.isSelected())
            woodWallSelector.setSelected(false);
        if(lavaWallSelector.isSelected())
            lavaWallSelector.setSelected(false);
        if(classicWallSelector.isSelected())
            classicWallSelector.setSelected(false);
        if(ironWallSelector.isSelected())
            ironWallSelector.setSelected(false);
        iceWallSelector.setSelected(true);
        pathWall = "./resources/View/ice.JPG";
    }

    public void selectionPressWood(ActionEvent actionEvent) {
        if(classicWallSelector.isSelected())
            classicWallSelector.setSelected(false);
        if(lavaWallSelector.isSelected())
            lavaWallSelector.setSelected(false);
        if(iceWallSelector.isSelected())
            iceWallSelector.setSelected(false);
        if(ironWallSelector.isSelected())
            ironWallSelector.setSelected(false);
        woodWallSelector.setSelected(true);
        pathWall ="./resources/View/levena.JPG";
    }

    public void selectionPressLava(ActionEvent actionEvent) {
        if(woodWallSelector.isSelected())
            woodWallSelector.setSelected(false);
        if(classicWallSelector.isSelected())
            classicWallSelector.setSelected(false);
        if(iceWallSelector.isSelected())
            iceWallSelector.setSelected(false);
        if(ironWallSelector.isSelected())
            ironWallSelector.setSelected(false);
        lavaWallSelector.setSelected(true);
        pathWall = "./resources/View/lava.JPG";
    }

    public void selectionPressIron(ActionEvent actionEvent) {
        if(woodWallSelector.isSelected())
            woodWallSelector.setSelected(false);
        if(classicWallSelector.isSelected())
            classicWallSelector.setSelected(false);
        if(iceWallSelector.isSelected())
            iceWallSelector.setSelected(false);
        if(lavaWallSelector.isSelected())
            lavaWallSelector.setSelected(false);
        ironWallSelector.setSelected(true);
        pathWall = "./resources/View/iron.JPG";
    }

    public void selectionPressClassicBack(ActionEvent actionEvent) {
        if(moonBackSelector.isSelected())
            moonBackSelector.setSelected(false);
        if(beachBackSelector.isSelected())
            beachBackSelector.setSelected(false);
        if(forestBackSelector.isSelected())
            forestBackSelector.setSelected(false);
        if(desertBackSelector.isSelected())
            desertBackSelector.setSelected(false);
        classicBackSelector.setSelected(true);
        pathBack = "./resources/View/classicBack.jpg";
    }

    public void selectionPressMoon(ActionEvent actionEvent) {
        if(classicBackSelector.isSelected())
            classicBackSelector.setSelected(false);
        if(beachBackSelector.isSelected())
            beachBackSelector.setSelected(false);
        if(forestBackSelector.isSelected())
            forestBackSelector.setSelected(false);
        if(desertBackSelector.isSelected())
            desertBackSelector.setSelected(false);
        moonBackSelector.setSelected(true);
        pathBack = "./resources/View/moon.png";
    }

    public void selectionPressBeach(ActionEvent actionEvent) {
        if(moonBackSelector.isSelected())
            moonBackSelector.setSelected(false);
        if(classicBackSelector.isSelected())
            classicBackSelector.setSelected(false);
        if(forestBackSelector.isSelected())
            forestBackSelector.setSelected(false);
        if(desertBackSelector.isSelected())
            desertBackSelector.setSelected(false);
        beachBackSelector.setSelected(true);
        pathBack = "./resources/View/beach.jpg";
    }

    public void selectionPressForest(ActionEvent actionEvent) {
        if(moonBackSelector.isSelected())
            moonBackSelector.setSelected(false);
        if(beachBackSelector.isSelected())
            beachBackSelector.setSelected(false);
        if(classicBackSelector.isSelected())
            classicBackSelector.setSelected(false);
        if(desertBackSelector.isSelected())
            desertBackSelector.setSelected(false);
        forestBackSelector.setSelected(true);
        pathBack = "./resources/View/forest.png";
    }

    public void selectionPressDesert(ActionEvent actionEvent) {
        if(moonBackSelector.isSelected())
            moonBackSelector.setSelected(false);
        if(beachBackSelector.isSelected())
            beachBackSelector.setSelected(false);
        if(forestBackSelector.isSelected())
            forestBackSelector.setSelected(false);
        if(classicBackSelector.isSelected())
            classicBackSelector.setSelected(false);
        desertBackSelector.setSelected(true);
        pathBack = "./resources/View/desert.jpg";
    }

    public void SubmittedConfig(ActionEvent actionEvent)throws Exception{
        if (RowTextField.getText().equals("") || ColTextField.getText().equals("")) {
            RowColError.setText("Rows and Cols cannot be Empty");
            return;
        }
        int rows, cols;
        try{
            rows = Integer.parseInt(RowTextField.getText());
            cols = Integer.parseInt(ColTextField.getText());
            if (rows <= 1 || cols <= 1) {
                RowColError.setText("Rows and Cols must be larger than 1");
                return;
            }
        }
        catch (Exception e){
            RowColError.setText("Rows and Cols must be integers");
            return;
        }

        thirdView.mazeCol=cols;
        thirdView.mazeRow=rows;
        setScene(actionEvent);
    }

    public void loadMaze(ActionEvent actionEvent) throws Exception{
        File file =this.loadDialog(actionEvent);
        if(file!=null){
            thirdView.fileLoaded=file;
            setScene(actionEvent);
        }
    }

    private void setScene(ActionEvent actionEvent) throws Exception {
        thirdView.pathPlayer = pathPlayer;
        thirdView.pathWall = pathWall;
        thirdView.pathBack = pathBack;
        Parent root3 = FXMLLoader.load(getClass().getResource("/gameBoardView.fxml"));
        Scene gameScene = new Scene(root3, 1000, 800);
        Stage primaryStage = (Stage) shaharSelect.getScene().getWindow();
        primaryStage.setMaximized(false);
        primaryStage.setScene(gameScene);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }

}