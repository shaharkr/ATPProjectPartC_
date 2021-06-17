package View;

import Model.IModel;
import Model.MovementDirection;
import Model.MyModel;
import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.print.attribute.standard.DialogOwner;
import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * the third and main view presenting the game itself
 */
public class thirdView extends AView implements Observer {
    private static MyViewModel viewModel;
    public static File fileLoaded;
    public MazeDisplayer mazeDisplayer;
    public static int mazeRow=15;
    public static int mazeCol=15;
    public static String pathWall = "./resources/View/classic.JPG";
    public static String pathPlayer = "./resources/View/benChar.png";
    public static String pathBack = "./resources/View/classicBack.jpg";
    private String pathTreasure = "./resources/View/treasure.png";
    private double prevX=-1;
    private double prevY=-1;
    @FXML
    Button solveMazeButton;
    @FXML
    BorderPane thirdBack;
    @FXML
    CheckBox musicOnOff;
    private boolean ctrlPressed =false;
    private boolean selectedMaze = false;
    private boolean selectedChar = false;
    private double mousePosX;
    private double mousePosY;


    /**
     * @param url
     * @param resourceBundle
     * initializes the view and sets the background according to the players selection
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //playerRow.textProperty().bind(updatePlayerRow);
        //playerCol.textProperty().bind(updatePlayerCol);
        Image img = null;
        try {
            img = new Image(new FileInputStream(pathBack));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true , true, true, true);

        Background background = new Background(new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize));
        thirdBack.setBackground(background);
        this.startGame();
    }

    /**
     * helper for initialize, and the control of a new game.
     * sets the maze itself, the character and maze
     */
    public void startGame(){
        mazeDisplayer.setScaleY(1.0);
        mazeDisplayer.setScaleX(1.0);
        mazeDisplayer.setTranslateY(0);
        mazeDisplayer.setTranslateX(0);
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
        if(musicOnOff.isSelected())return;
        Main.turnMusicOn("./resources/Music/marioRemix.mp3");
    }

    /**
     * @param mouseEvent
     * makes sure that when the player closes the app by clicking [X] it exits servers
     */
    public void setOnCloseRequestServers(MouseEvent mouseEvent) {
        Stage primaryStage = (Stage) solveMazeButton.getScene().getWindow();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                exitGameAndServers(new ActionEvent());
            }
        });
    }

    /**
     * @param keyEvent
     * @throws IOException
     * @throws InterruptedException
     * handles the key pressed on action, sends it to view-model if necessary,
     * handles it in-place if no logic is involved.(ctrl, arrows)
     */
    public void keyPressed(KeyEvent keyEvent) throws IOException, InterruptedException {
        if(keyEvent.getCode()== KeyCode.CONTROL){
            this.ctrlPressed = true;
            return;
        }
        viewModel.movePlayer(keyEvent);
        if(mazeDisplayer.getScaleY()==1 && mazeDisplayer.getScaleX()==1) {
            mazeDisplayer.setTranslateY(0);
            mazeDisplayer.setTranslateX(0);
            return;
        }
        switch (keyEvent.getCode()){
            case UP -> {
                if(mazeDisplayer.getHeight()>50){
                    mazeDisplayer.setTranslateY(mazeDisplayer.getTranslateY()+20);

                }
            }
            case DOWN -> {
                if(true){
                    mazeDisplayer.setTranslateY(mazeDisplayer.getTranslateY()-20);
                }
            }
            case LEFT -> {
                if(true){
                    mazeDisplayer.setTranslateX(mazeDisplayer.getTranslateX()-20);
                }
            }
            case RIGHT -> {
                if(mazeDisplayer.getWidth()>200){
                    mazeDisplayer.setTranslateX(mazeDisplayer.getTranslateX()+20);
                }
            }
        }
        keyEvent.consume();
    }


    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    /**
     * @param o
     * @param arg
     * handles the notifications from the view-model,
     * when view-model notifies about actions the model finished executing.
     */
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

    /**
     * activated when the player reached the treasure,
     * turns on music and shows a dialog before returning to 2nd scene.
     */
    private void playerMadeIt(){
        mazeDisplayer.setFinish(true);
        Main.turnMusicOn("./resources/Music/gtaMusic.mp3");
        try{
            showInfoWithImage("./resources/View/finish.jpg", "Success",650, 400);
            backToChoices(new ActionEvent());
        }
        catch (Exception e){

        }
    }

    /**
     * update the visited stack of maze-displayer.
     */
    private void setVisited() {
        mazeDisplayer.setVisited(viewModel.getVisited());
    }

    /**
     * helper for update when view-model says the player moved successfully.
     */
    private void showPlayerMove() {
        mazeDisplayer.setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    /**
     * helper for update when view-model says the maze was generated
     */
    private void generateMaze(){
        Integer[] goalPos = viewModel.getGoalPos();
        mazeDisplayer.setTreasureRow(goalPos[0]);
        mazeDisplayer.setTreasureCol(goalPos[1]);
        mazeDisplayer.setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }

    /**
     * helper for update when the view-model says the solution was generated
     */
    private void showSolution() {
        mazeDisplayer.drawSolution(viewModel.getSolution());
    }

    /**
     * @param actionEvent
     * on-action for the solve button, asks the viw-model to solve the maze.
     */
    public void solveMazeButton(ActionEvent actionEvent) {
        viewModel.solveMaze();
        mazeDisplayer.requestFocus();

    }

    /**
     * @param actionEvent
     * exit the game through the models exit.
     */
    public void exitGameAndServers(ActionEvent actionEvent) {
        viewModel.stopServers();
        super.exitGame(actionEvent);
    }


    /**
     * @param actionEvent
     * on action for the save button, saves the current maze bytes to disc
     */
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

    /**
     * @param actionEvent
     * on action for the load button, open choice dialog and loads the selected maze
     * for the player(updates maze displayer).
     */
    public void loadMaze(ActionEvent actionEvent) {
        File file = this.loadDialog(actionEvent);
        if (file != null) {
            mazeDisplayer.setFinish(false);
            viewModel.loadMaze(file);
            if(musicOnOff.isSelected())return;
            Main.turnMusicOn("./resources/Music/marioRemix.mp3");

        }
    }

    /**
     * @param scrollEvent
     * on action for the control+scroll which aallows player wo zoom in or out of the maze.
     */
    public void zooming(ScrollEvent scrollEvent) {
        if(!ctrlPressed)return;
        if(scrollEvent.getDeltaY()>0){
            mazeDisplayer.setScaleY(mazeDisplayer.getScaleY()*1.05);
            mazeDisplayer.setScaleX(mazeDisplayer.getScaleX()*1.05);
        }
        else{
            if(mazeDisplayer.getScaleY()*0.95<=1 || mazeDisplayer.getScaleX()*0.95<=1){
                mazeDisplayer.setScaleY(1);
                mazeDisplayer.setScaleX(1);
                return;
            }
            mazeDisplayer.setScaleY(mazeDisplayer.getScaleY()*0.95);
            mazeDisplayer.setScaleX(mazeDisplayer.getScaleX()*0.95);
        }

    }


    /**
     * @param mouseEvent
     * turns the mouse from arrow to hand when mouse is on the maze
     */
    public void mouseHand(MouseEvent mouseEvent) {
        Scene scene = (Scene) solveMazeButton.getScene();
        scene.setCursor(Cursor.HAND);
    }

    /**
     * @param mouseEvent
     * turns the mouse to arrow when not on the maze.
     */
    public void mouseArrow(MouseEvent mouseEvent) {
        Scene scene = (Scene) solveMazeButton.getScene();
        scene.setCursor(Cursor.DEFAULT);
    }

    /**
     * @param actionEvent
     * @throws IOException
     * on action for the back button, returns to second scene.
     */
    public void backToChoices(ActionEvent actionEvent) throws IOException {
        Main.media.setMute(true);
        Stage primaryStage = (Stage) solveMazeButton.getScene().getWindow();;
        Parent root = FXMLLoader.load(getClass().getResource("/choicesView.fxml"));
        Scene secondScene = new Scene(root, 800, 600);
        primaryStage.setMaximized(false);
        primaryStage.setScene(secondScene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * @param keyEvent
     * when releasing the ctrl this method makes sure there will be no zooming
     * when scrolling.
     */
    public void releaseCtrl(KeyEvent keyEvent) {
        if(keyEvent.getCode()== KeyCode.CONTROL){
            this.ctrlPressed = false;
        }
    }

    /**
     * @param mouseEvent
     * requests focus for the maze displayer.
     */
    public void clickMaze(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void moveMaze(MouseEvent mouseEvent) {
        if(!this.selectedMaze && !this.selectedChar){
            return;
        }
        if(!this.selectedChar){
            this.moveCanvasMouse(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        }
    }

    private void moveCanvasMouse(double sceneX, double sceneY) {
    }

    /**
     * @param actionEvent
     * on action for the music checkbox, turns it on and off.
     */
    public void musicOnOOff(ActionEvent actionEvent) {
        if(musicOnOff.isSelected()){
            Main.media.pause();
        }
        else{
            Main.media.play();
        }
        mazeDisplayer.requestFocus();
    }

    /**
     * @param mouseEvent
     * drag the character, depending on the previous mouse location and current mouse location
     */
    public void mouseDrag2(MouseEvent mouseEvent){
        if(this.prevX==-1)
            prevX=mouseEvent.getX();
        if(this.prevY==-1)
            prevY=mouseEvent.getY();
        if(mouseEvent.getY()<this.prevY-10) {
            if(mazeDisplayer.canMove(mazeDisplayer.getPlayerRow()-1,mazeDisplayer.getPlayerCol())) {
                viewModel.movePlayerByDirection(MovementDirection.UP);
                return;
            }
        }
        if(mouseEvent.getY()>this.prevY+10) {
            if(mazeDisplayer.canMove(mazeDisplayer.getPlayerRow()+1, mazeDisplayer.getPlayerCol())) {
                viewModel.movePlayerByDirection(MovementDirection.DOWN);
                return;
            }
        }
        if(mouseEvent.getX()>this.prevX+10) {
            if(mazeDisplayer.canMove(mazeDisplayer.getPlayerRow(), mazeDisplayer.getPlayerCol()+1)) {
                viewModel.movePlayerByDirection(MovementDirection.RIGHT);
                return;
            }
        }
        if(mouseEvent.getX()<this.prevX-10) {
            if(mazeDisplayer.canMove(mazeDisplayer.getPlayerRow(), mazeDisplayer.getPlayerCol()-1)) {
                viewModel.movePlayerByDirection(MovementDirection.LEFT);

            }
        }
    }
}
