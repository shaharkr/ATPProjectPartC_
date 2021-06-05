package View;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class thirdController extends AController implements Initializable {
    public IMazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public static int playerRow=2;
    public static int playerCol=2;
    public static String pathWall = "./resources/View/classic.JPG";
    public static String pathPlayer = "./resources/View/benChar.png";
    private String pathTreasure = "./resources/View/treasure.jpg";

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //playerRow.textProperty().bind(updatePlayerRow);
        //playerCol.textProperty().bind(updatePlayerCol);
        mazeDisplayer.setImageFileNamePlayer(pathPlayer);
        mazeDisplayer.setImageFileNameWall(pathWall);
        mazeDisplayer.setImageFileNameTreasure(pathTreasure);
        mazeDisplayer.setImageFileNameTrace("./resources/View/bubble.png");
        generator = new MyMazeGenerator();
        try {
            Maze maze = generator.generate(playerRow,playerCol);
            mazeDisplayer.setThisMaze(maze);
            mazeDisplayer.setBeen(new Stack<Integer[]>());
            Integer[] firstPlace = new Integer[2];
            firstPlace[0] = maze.getStartPosition().getRowIndex();
            firstPlace[1] = maze.getStartPosition().getColumnIndex();
            mazeDisplayer.setPrevBeen(firstPlace);
            //mazeDisplayer.setPlaceBeen(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
            setPlayerPosition(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());
            mazeDisplayer.drawMaze(maze.getMaze());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
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
        int row = mazeDisplayer.getPlayerRow();
        int col = mazeDisplayer.getPlayerCol();

        switch (keyEvent.getCode()) {
            case NUMPAD8 -> row -= 1;
            case NUMPAD2 -> row += 1;
            case NUMPAD6 -> col += 1;
            case NUMPAD4 -> col -= 1;
            case NUMPAD7 -> {row-=1; col-=1;}
            case NUMPAD9 -> {row-=1; col+=1;}
            case NUMPAD1 -> {row+=1; col-=1;}
            case NUMPAD3 -> {row+=1; col+=1;}
            default -> {
                return;
            }
        }

        if(!mazeDisplayer.checkPossible(row,col))return;
        mazeDisplayer.setPlaceBeen(row, col);
        boolean finished = setPlayerPosition(row, col);
        keyEvent.consume();
        if(finished){
            mazeDisplayer.joinOtherCharacter();
        }

    }

    public boolean setPlayerPosition(int row, int col){
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
        boolean finished = mazeDisplayer.setPlayerPosition(row, col);

        return finished;
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
}
