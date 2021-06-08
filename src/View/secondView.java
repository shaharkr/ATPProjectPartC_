package View;


import algorithms.mazeGenerators.IMazeGenerator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
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
    String pathWall = "./resources/View/classic.JPG";
    String pathPlayer = "./resources/View/benChar.png";
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
        iceWallSelector.setSelected(true);
        pathWall = "./resources/View/iron.JPG";
    }

    public void selectionPressWood(ActionEvent actionEvent) {
        if(classicWallSelector.isSelected())
            classicWallSelector.setSelected(false);
        if(lavaWallSelector.isSelected())
            lavaWallSelector.setSelected(false);
        if(iceWallSelector.isSelected())
            iceWallSelector.setSelected(false);
        woodWallSelector.setSelected(true);
        pathWall ="./resources/View/stone.JPG";
    }

    public void selectionPressLava(ActionEvent actionEvent) {
        if(woodWallSelector.isSelected())
            woodWallSelector.setSelected(false);
        if(classicWallSelector.isSelected())
            classicWallSelector.setSelected(false);
        if(iceWallSelector.isSelected())
            iceWallSelector.setSelected(false);
        lavaWallSelector.setSelected(true);
        pathWall = "./resources/View/brik.JPG";
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
        Parent root3 = FXMLLoader.load(getClass().getResource("gameBoardView.fxml"));
        Scene gameScene = new Scene(root3, 1000, 800);
        Stage primaryStage = (Stage) shaharSelect.getScene().getWindow();
        primaryStage.setMaximized(false);
        primaryStage.setScene(gameScene);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }
}