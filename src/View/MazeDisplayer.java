package View;

import algorithms.mazeGenerators.Maze;
import javafx.animation.PauseTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class MazeDisplayer extends Canvas {
    private int[][] maze;
    // player position:
    private int playerRow = 0;
    private int playerCol = 0;
    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameTreasure = new SimpleStringProperty();
    // players positioning
    private Stack<Integer[]> been;

    public Integer[] getPrevBeen() {
        return prevBeen;
    }

    public void setPrevBeen(Integer[] prevBeen) {
        this.prevBeen = prevBeen;
    }

    private Integer[] prevBeen;

    public Stack<Integer[]> getBeen() {
        return been;
    }

    public void setBeen(Stack<Integer[]> been) {
        this.been = been;
    }


    public String getImageFileNameTrace() {
        return imageFileNameTrace.get();
    }

    public StringProperty imageFileNameTraceProperty() {
        return imageFileNameTrace;
    }

    public void setImageFileNameTrace(String imageFileNameTrace) {
        this.imageFileNameTrace.set(imageFileNameTrace);
    }

    StringProperty imageFileNameTrace = new SimpleStringProperty();
    private Maze thisMaze;

    public String getImageFileNameTreasure() {
        return imageFileNameTreasure.get();
    }

    public StringProperty imageFileNameTreasureProperty() {
        return imageFileNameTreasure;
    }

    public void setImageFileNameTreasure(String imageFileNameTreasure) {
        this.imageFileNameTreasure.set(imageFileNameTreasure);
    }

    public Maze getThisMaze() {
        return thisMaze;
    }

    public void setThisMaze(Maze thisMaze) {
        this.thisMaze = thisMaze;
    }



    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public boolean setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;

        draw();
        if(row == thisMaze.getGoalPosition().getRowIndex() && col == thisMaze.getGoalPosition().getColumnIndex()){
            return true;
        }
        return false;
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public void drawMaze(int[][] maze) {
        this.maze = maze;
        draw();
    }

    private void draw() {
        if(maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            drawTreasure(graphicsContext,cellHeight, cellWidth);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
        }
    }


    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

        Image wallImage = null;
        Image traceImage = null;
        try{
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
            traceImage = new Image(new FileInputStream(getImageFileNameTrace()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double x = j * cellWidth;
                double y = i * cellHeight;
                if(maze[i][j] == 1){
                    //if it is a wall:
                    if(wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }

        }
        for(Integer[] place: been){
            double x = place[1] * cellWidth;
            double y = place[0] * cellHeight;
            graphicsContext.drawImage(traceImage, x, y, cellWidth, cellHeight);
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        if(getPlayerCol()==thisMaze.getGoalPosition().getColumnIndex() && getPlayerRow()==thisMaze.getGoalPosition().getRowIndex()){
            setImageFileNameTreasure("./resources/View/openTreasure.jpg");
            drawTreasure(graphicsContext,cellHeight,cellWidth);
            return;
        }
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    private void drawTreasure(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = (thisMaze.getGoalPosition().getColumnIndex()) * cellWidth;
        double y = (thisMaze.getGoalPosition().getRowIndex()) * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image treasureImage = null;
        try {
            treasureImage = new Image(new FileInputStream(getImageFileNameTreasure()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no treasure image file");
        }
        if(treasureImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(treasureImage, x, y, cellWidth, cellHeight);
    }

    public boolean checkPossible(int row, int col){
        try{if(maze[row][col]==0)
            return true;}
        catch (Exception e){
            return false;}
        return false;
    }

    public void setPlaceBeen(int row, int column) {
        if(!been.isEmpty() && row==been.peek()[0] && column==been.peek()[1]) {
            been.pop();
        }
        else{
            been.push(prevBeen);
        }
        Integer[] place = new Integer[2];
        place[0] = row;
        place[1] = column;
        prevBeen = place;
    }

    public void joinOtherCharacter() throws FileNotFoundException, InterruptedException {

        GraphicsContext graphicsContext = getGraphicsContext2D();
        StringProperty other = new SimpleStringProperty();
        if(imageFileNamePlayer.get()=="./resources/View/benChar.png")other.set("./resources/View/shaharChar.png");
        else other.set("./resources/View/benChar.png");
        Image otherImage = new Image(new FileInputStream(other.get()));

        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        int rows = maze.length;
        int cols = maze[0].length;
        double cellHeight = canvasHeight / rows;
        double cellWidth = canvasWidth / cols;
        for(Integer[] i : been){
            double x = i[1] * cellWidth;
            double y = i[0] * cellHeight;
            graphicsContext.clearRect(x,y,cellWidth,cellHeight);
            graphicsContext.drawImage(otherImage, x, y, cellWidth, cellHeight);

        }
    }
}
