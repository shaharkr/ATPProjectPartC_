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
    private int treasureRow = 0;
    private int treasureCol = 0;
    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameTreasure = new SimpleStringProperty();
    StringProperty imageFileNameTrace = new SimpleStringProperty();



    public String getImageFileNameTrace() {
        return imageFileNameTrace.get();
    }

    public StringProperty imageFileNameTraceProperty() {
        return imageFileNameTrace;
    }

    public void setImageFileNameTrace(String imageFileNameTrace) {
        this.imageFileNameTrace.set(imageFileNameTrace);
    }



    public String getImageFileNameTreasure() {
        return imageFileNameTreasure.get();
    }

    public StringProperty imageFileNameTreasureProperty() {
        return imageFileNameTreasure;
    }

    public void setImageFileNameTreasure(String imageFileNameTreasure) {
        this.imageFileNameTreasure.set(imageFileNameTreasure);
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
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

            drawTreasure(graphicsContext,cellHeight, cellWidth);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
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
        /*for(Integer[] place: been){
            double x = place[1] * cellWidth;
            double y = place[0] * cellHeight;
            graphicsContext.drawImage(traceImage, x, y, cellWidth, cellHeight);
        }*/
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
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
        double x = treasureCol * cellWidth;
        double y = treasureRow * cellHeight;
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
        /*for(Integer[] i : been){
            double x = i[1] * cellWidth;
            double y = i[0] * cellHeight;
            graphicsContext.clearRect(x,y,cellWidth,cellHeight);
            graphicsContext.drawImage(otherImage, x, y, cellWidth, cellHeight);

        }*/
    }

    public void setTreasureRow(int treasureRow) {
        this.treasureRow = treasureRow;
    }

    public void setTreasureCol(int treasureCol) {
        this.treasureCol = treasureCol;
    }
}
