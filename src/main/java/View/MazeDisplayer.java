package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
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
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

/**
 * this class represents a controller, who's job is to represent a maze
 * it will display the character, maze walls, path the character walked, etc..
 */
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
    private Stack<Integer[]> visited = new Stack<>();
    private boolean finish = false;

    /**
     * @param b ether the player finished or no
     * changes the boolean field to b.
     */
    public void setFinish(boolean b) {
        this.finish = b;
        if(finish){
            draw();
        }
    }

    /**
     * @param row new want to be player row
     * @param col new want to be player column
     * @return true if the player can move to (row,column) depending on the maze size,
     * and the maze's walls.
     */
    public boolean canMove(int row, int col){
        if (row < 0 || col < 0 || row > maze.length - 1 || col > maze[0].length - 1)
            return false;
        return(maze[row][col]==0);
    }


    /**
     * @return 2 doubles representing the dimensions of one cell in the maze
     * calculated by canvas size and amount of rows and columns in the maze.
     */
    public double[] getCellSize(){
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        int rows = maze.length - 20;
        int cols = maze[0].length - 20;

        double cellHeight = canvasHeight / rows;
        double cellWidth = canvasWidth / cols;
        return new double[]{cellHeight, cellWidth};
    }

    /**
     * draw the maze according to the fields. will draw the walls, character, treasure and walked path
     */
    private void draw() {
        if(maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth()-200;
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth+100, canvasHeight);

            drawTreasure(graphicsContext,cellHeight, cellWidth);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
        }
    }

    /**
     * @param graphicsContext
     * @param cellHeight
     * @param cellWidth
     * helper for draw that is responsible for drawing the player.
     */
    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth+100;
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


    /**
     * @param graphicsContext
     * @param cellHeight
     * @param cellWidth
     * @param rows
     * @param cols
     * helper function for draw that is responsible for drawing the maze walls.
     */
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
                if(maze[i][j] == 1){
                    double x = (j) * (cellWidth)+100;
                    double y = (i) * (cellHeight);
                    graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }

        }
        if(visited!=null){
            if(!visited.isEmpty()){
                for(Integer[] place: visited){
                    if(place[0]==playerRow && place[1]==playerCol)continue;
                    double x = place[1] * cellWidth+100;
                    double y = place[0] * cellHeight;
                    graphicsContext.drawImage(traceImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }


    /**
     * @param graphicsContext
     * @param cellHeight
     * @param cellWidth
     * helper function for draw that is responsible for drawing the treasure
     * depending on the maze goal location.
     */
    private void drawTreasure(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = treasureCol * cellWidth+100;
        double y = treasureRow * cellHeight;
        graphicsContext.setFill(Color.GREEN);
        if(finish){
            setImageFileNameTreasure("./resources/View/openTreasure.png");
        }
        else{
            setImageFileNameTreasure("./resources/View/treasure.png");
        }
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


    /**
     * @param sol queue of positions that represents the path to the goal
     * draws coins from the player to the treasure
     */
    public void drawSolution(Queue<Integer[]> sol) {
        Image img = null;
        try {
            img = new Image(new FileInputStream("./resources/View/coin.png"));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        double canvasHeight = getHeight();
        double canvasWidth = getWidth()-200;
        int rows = maze.length;
        int cols = maze[0].length;

        double cellHeight = canvasHeight / rows;
        double cellWidth = canvasWidth / cols;

        GraphicsContext graphicsContext = getGraphicsContext2D();
        //clear the canvas:
        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        for(Integer[] pos : sol){
            //System.out.println(pos);
            if(pos[0]==playerRow && pos[1]==playerCol)continue;
            double x = pos[1] * cellWidth+100;
            double y = pos[0] * cellHeight;
            graphicsContext.clearRect(x,y,cellWidth,cellHeight);
            graphicsContext.drawImage(img, x, y, cellWidth, cellHeight);

        }
        drawTreasure(graphicsContext,cellHeight, cellWidth);
        drawPlayer(graphicsContext, cellHeight, cellWidth);
        drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
    }

    /**
     * @param visited the stack containing the positions the player visited.
     */
    public void setVisited(Stack<Integer[]> visited) {
        this.visited = (Stack<Integer[]>)visited.clone();
        if(!this.visited.isEmpty())this.visited.pop();
    }


    /**
     * getters and setters.
     */
    public String getImageFileNameTrace() {
        return imageFileNameTrace.get();
    }

    public void setImageFileNameTrace(String imageFileNameTrace) {
        this.imageFileNameTrace.set(imageFileNameTrace);
    }

    public String getImageFileNameTreasure() {
        return imageFileNameTreasure.get();
    }

    public void setImageFileNameTreasure(String imageFileNameTreasure) { this.imageFileNameTreasure.set(imageFileNameTreasure); }

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

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) { this.imageFileNamePlayer.set(imageFileNamePlayer); }

    public void drawMaze(int[][] maze) {
        this.maze = maze;
        draw();
    }

    public double[] getPlayerLoc(){
        return new double[]{getPlayerRow()*getHeight(), getPlayerCol()*(getWidth()-200)+100};
    }
    public void setTreasureRow(int treasureRow) {
        this.treasureRow = treasureRow;
    }

    public void setTreasureCol(int treasureCol) {
        this.treasureCol = treasureCol;
    }
}
