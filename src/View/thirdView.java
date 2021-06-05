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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

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
    public MazeDisplayer mazeDisplayer;
    public static int mazeRow=2;
    public static int mazeCol=2;
    public static String pathWall = "./resources/View/classic.JPG";
    public static String pathPlayer = "./resources/View/benChar.png";
    private String pathTreasure = "./resources/View/treasure.jpg";
    private StringProperty updatePlayerRow = new SimpleStringProperty();
    private StringProperty updatePlayerCol = new SimpleStringProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //playerRow.textProperty().bind(updatePlayerRow);
        //playerCol.textProperty().bind(updatePlayerCol);
        IModel model = new MyModel();
        viewModel=new MyViewModel(model);
        this.viewModel.assignObserver(this);

        mazeDisplayer.setImageFileNamePlayer(pathPlayer);
        mazeDisplayer.setImageFileNameWall(pathWall);
        mazeDisplayer.setImageFileNameTreasure(pathTreasure);
        mazeDisplayer.setImageFileNameTrace("./resources/View/bubble.png");
        viewModel.generateMaze(mazeRow,mazeCol);
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }


    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
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
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    private void showPlayerMove() {
        setUpdatePlayerRow(viewModel.getPlayerRow());
        setUpdatePlayerCol(viewModel.getPlayerCol());
        mazeDisplayer.setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    private void generateMaze(){
        Integer[] firstPlace = viewModel.getStartPos(), goalPos = viewModel.getGoalPos();
        showPlayerMove();
        mazeDisplayer.setTreasureRow(goalPos[0]);
        mazeDisplayer.setTreasureCol(goalPos[1]);
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }

    private void showSolution() {
        //mazeDisplayer.drawSolution(viewModel.getSolution());
    }

    public void solveMazeButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
        viewModel.solveMaze();
    }

    public void exitGameAndServers(ActionEvent actionEvent) {
        viewModel.stopServers();
        super.exitGame(actionEvent);
    }

}
