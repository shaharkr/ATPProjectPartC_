package View;

import Model.IModel;
import Model.MovementDirection;
import Model.MyModel;
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

import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.ResourceBundle;

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
    @FXML
    Button solveMazeButton;

    @FXML
    BorderPane thirdBack;
    @FXML
    ScrollPane scrollPane;
    @FXML
    CheckBox musicOnOff;
    private boolean ctrlPressed =false;
    private boolean selectedMaze = false;
    private boolean selectedChar = false;
    private double mousePosX;
    private double mousePosY;


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
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);

        Background background = new Background(new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize));
        thirdBack.setBackground(background);
        this.startGame();
    }

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

    public void setOnCloseRequestServers(MouseEvent mouseEvent) {
        Stage primaryStage = (Stage) solveMazeButton.getScene().getWindow();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                exitGameAndServers(new ActionEvent());
            }
        });
    }

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
            case RIGHT -> {
                if(true){
                    mazeDisplayer.setTranslateX(mazeDisplayer.getTranslateX()-20);
                }
            }
            case LEFT -> {
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
        Main.turnMusicOn("./resources/Music/gtaMusic.mp3");
        try{
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("$$$$$$$ WOO HOO $$$$$$$");
            al.setHeaderText("");
            String s = "bla bla bla";
            al.setContentText(s);
            ImageView imgv = new ImageView(new Image(new FileInputStream(pathPlayer)));
            imgv.setFitWidth(400);
            imgv.setFitHeight(400);
            al.setGraphic(imgv);
            al.showAndWait();
            backToChoices(new ActionEvent());
        }
        catch (Exception e){

        }
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
            if(musicOnOff.isSelected())return;
            Main.turnMusicOn("./resources/Music/marioRemix.mp3");

        }
    }

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


    public void mouseHand(MouseEvent mouseEvent) {
        Scene scene = (Scene) solveMazeButton.getScene();
        scene.setCursor(Cursor.HAND);
    }

    public void mouseArrow(MouseEvent mouseEvent) {
        Scene scene = (Scene) solveMazeButton.getScene();
        scene.setCursor(Cursor.DEFAULT);
    }

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

    public void releaseCtrl(KeyEvent keyEvent) {
        if(keyEvent.getCode()== KeyCode.CONTROL){
            this.ctrlPressed = false;
        }
    }

    public void clickMaze(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
        double mouse_x = mouseEvent.getSceneX();
        double mouse_y = mouseEvent.getSceneY();
        double x = mazeDisplayer.getPlayerLoc()[0], y = mazeDisplayer.getPlayerLoc()[1];
        this.selectedMaze = true;
        this.selectedChar = x-20 <= mouse_x && x+20 >= mouse_x && y-20 <= mouse_y && y+20 >= mouse_y;
        this.mousePosX = mouse_x;
        this.mousePosY = mouse_y;
    }

    public void moveMaze(MouseEvent mouseEvent) {
        if(!this.selectedMaze && !this.selectedChar){
            return;
        }
        if(!this.selectedChar){
            this.moveCanvasMouse(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        }
        else{
            this.moveCharMouse(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        }
    }

    private void moveCanvasMouse(double sceneX, double sceneY) {
    }

    public void musicOnOOff(ActionEvent actionEvent) {
        if(musicOnOff.isSelected()){
            Main.media.pause();
        }
        else{
            Main.media.play();
        }
        mazeDisplayer.requestFocus();
    }

    private void moveCharMouse(double sceneX, double sceneY) {
        /*if(sceneX>mazeDisplayer.getPlayerLoc()[0]+5 && sceneY>mazeDisplayer.getPlayerLoc()[1]+5){
            viewModel.movePlayer(MovementDirection.DOWN_R);
        }
        else if(sceneX>mazeDisplayer.getPlayerLoc()[0]+5 && sceneY<mazeDisplayer.getPlayerLoc()[1]-5){
            viewModel.movePlayer(MovementDirection.UP_R);
        }
        else if(sceneX<mazeDisplayer.getPlayerLoc()[0]-5 && sceneY>mazeDisplayer.getPlayerLoc()[1]+5){
            viewModel.movePlayer(MovementDirection.DOWN_L);
        }
        else if(sceneX<mazeDisplayer.getPlayerLoc()[0]-5 && sceneY<mazeDisplayer.getPlayerLoc()[1]-5){
            viewModel.movePlayer(MovementDirection.UP_R);
        }
        else if(sceneX>mazeDisplayer.getPlayerLoc()[0]+5) {
            viewModel.movePlayer(MovementDirection.RIGHT);
        }
        else if(sceneX<mazeDisplayer.getPlayerLoc()[0]-5) {
            viewModel.movePlayer(MovementDirection.LEFT);
        }
        else if(sceneY>mazeDisplayer.getPlayerLoc()[0]+5) {
            viewModel.movePlayer(MovementDirection.DOWN);
        }
        else if(sceneY<mazeDisplayer.getPlayerLoc()[0]-5) {
            viewModel.movePlayer(MovementDirection.UP);
        }*/
        //mazeDisplayer.draw();
    }


/*    public void scrolling(ScrollEvent scrollEvent) {
        if(this.ctrlPressed)return;
        try{
            if(scrollEvent.getDeltaY()>0 && scrollPane.getVvalue()>0){
                mazeDisplayer.setHeight(mazeDisplayer.getHeight()+200);
            }
            else if(scrollEvent.getDeltaY()<0 && scrollPane.getVvalue()<scrollPane.getVmax()){
                mazeDisplayer.setHeight(mazeDisplayer.getHeight()-200);
            }
            if(scrollEvent.getDeltaX()>0 && scrollPane.getHvalue()>0){
                mazeDisplayer.setWidth(mazeDisplayer.getWidth()+200);
            }
            else if(scrollEvent.getDeltaX()<0 && scrollPane.getHvalue()<scrollPane.getHmax()){
                mazeDisplayer.setWidth(mazeDisplayer.getWidth()-200);
            }
        }
        catch (Exception e){e.printStackTrace();}

    }*/
}
