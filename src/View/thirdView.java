package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Stack;

public class thirdView extends AView implements Observer {
    private MyViewModel viewModel;
    public static File fileLoaded;
    public MazeDisplayer mazeDisplayer;
    public static int mazeRow=15;
    public static int mazeCol=15;
    public static String pathWall = "./resources/View/classic.JPG";
    public static String pathPlayer = "./resources/View/benChar.png";
    private String pathTreasure = "./resources/View/treasure.png";
    @FXML
    Button solveMazeButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //playerRow.textProperty().bind(updatePlayerRow);
        //playerCol.textProperty().bind(updatePlayerCol);
        this.startGame();
    }

    public void startGame(){
        IModel model = new MyModel();
        viewModel=new MyViewModel(model);
        viewModel.assignObserver(this);
        mazeDisplayer.setImageFileNamePlayer(pathPlayer);
        mazeDisplayer.setImageFileNameWall(pathWall);
        mazeDisplayer.setImageFileNameTreasure(pathTreasure);
        mazeDisplayer.setImageFileNameTrace("./resources/View/bubble.png");
        mazeDisplayer.setImageFileNameTreasure("./resources/View/treasure.png");
        mazeDisplayer.setFinish(false);
        if(fileLoaded!=null){
            viewModel.loadMaze(fileLoaded);
            fileLoaded=null;
        }
        else{
            viewModel.generateMaze(mazeRow,mazeCol);
        }
    }

    public void keyPressed(KeyEvent keyEvent) throws IOException, InterruptedException {
        viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }


    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            case "maze created" -> generateMaze();
            case "maze solved" -> showSolution();
            case "player moved" -> showPlayerMove();
            case "new visited" -> setVisited();
            case "player finished" -> playerMadeIt();
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    private void playerMadeIt() {
        mazeDisplayer.setFinish(true);
    }

    private void setVisited() {
        mazeDisplayer.setVisited(viewModel.getVisited());
    }

    private void showPlayerMove() {
        mazeDisplayer.setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    private void generateMaze(){
        Integer[] goalPos = viewModel.getGoalPos();
        mazeDisplayer.setTreasureRow(goalPos[0]);
        mazeDisplayer.setTreasureCol(goalPos[1]);
        mazeDisplayer.setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }

    private void showSolution() {
        mazeDisplayer.drawSolution(viewModel.getSolution());
    }

    public void solveMazeButton(ActionEvent actionEvent) {
        viewModel.solveMaze();
        mazeDisplayer.requestFocus();

    }

    public void exitGameAndServers(ActionEvent actionEvent) {
        viewModel.stopServers();
        super.exitGame(actionEvent);
    }

    public void saveMaze(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze Objects", "*.maze")
        );
        File file = fileChooser.showSaveDialog(stage);
        viewModel.saveMaze(file);
    }

    public void loadMaze(ActionEvent actionEvent) {
        File file = this.loadDialog(actionEvent);
        if (file != null) {
            mazeDisplayer.setFinish(false);
            viewModel.loadMaze(file);
        }
    }
}
