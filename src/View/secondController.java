package View;


import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;

public class secondController extends AController implements Initializable {
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
        pathPlayer = "./resources/View/shaharChar.png";
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
        pathWall = "./resources/View/ice.JPG";
    }

    public void selectionPressWood(ActionEvent actionEvent) {
        if(classicWallSelector.isSelected())
            classicWallSelector.setSelected(false);
        if(lavaWallSelector.isSelected())
            lavaWallSelector.setSelected(false);
        if(iceWallSelector.isSelected())
            iceWallSelector.setSelected(false);
        woodWallSelector.setSelected(true);
        pathWall ="./resources/View/wood.JPG";
    }

    public void selectionPressLava(ActionEvent actionEvent) {
        if(woodWallSelector.isSelected())
            woodWallSelector.setSelected(false);
        if(classicWallSelector.isSelected())
            classicWallSelector.setSelected(false);
        if(iceWallSelector.isSelected())
            iceWallSelector.setSelected(false);
        lavaWallSelector.setSelected(true);
        pathWall = "./resources/View/lava.JPG";
    }

    public void SubmittedConfig(ActionEvent actionEvent) throws Exception {
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

        thirdController.playerCol=cols;
        thirdController.playerRow=rows;
        thirdController.pathPlayer = pathPlayer;
        thirdController.pathWall = pathWall;
        Parent root3 = FXMLLoader.load(getClass().getResource("gameBoardView.fxml"));
        Scene gameScene = new Scene(root3, 800, 600);
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(gameScene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

    }


}