package Model;

import algorithms.search.Solution;

import java.io.File;
import java.util.Observer;
import java.util.Queue;
import java.util.Stack;

/**
 * Model Interface in the MVVM Architecture
 */
public interface IModel {
    /**
     * @param rows amount of rows in the binary maze
     * @param cols amount of cols in the binary maze
     */
    void generateMaze(int rows, int cols);

    /**
     * @return the binary maze that was generated
     */
    int[][] getMaze();

    /**
     * @param direction the direction to which the player tries to move his character
     */
    void updatePlayerLocation(MovementDirection direction);

    /**
     * @return row position of player
     */
    int getPlayerRow();

    /**
     * @return column position of player
     */
    int getPlayerCol();

    /**
     * @param o should be view model that will handle the notify messages from the model
     */
    void assignObserver(Observer o);

    /**
     * the model will try to solve the maze and should notify its observers when done
     */
    void solveMaze();

    /**
     * @return return the solution
     */
    Queue<Integer[]> getSolution();

    /**
     * @return return the start position of the player, depending of the maze
     */
    Integer[] getStartPos();

    /**
     * @return return the start position of the player, depending of the maze
     */
    Integer[] getGoalPos();

    /**
     * pre exit must take action not to leave threads behind
     */
    void stopServers();

    /**
     * @return the list of visited spots by the player
     */
    Stack<Integer[]> getVisited();

    /**
     * @param file that will be created on disc
     */
    void saveMaze(File file);

    /**
     * @param file that will be loaded from disc
     */
    void loadMaze(File file);
}
