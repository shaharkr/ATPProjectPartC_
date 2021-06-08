package Model;

import algorithms.search.Solution;

import java.io.File;
import java.util.Observer;
import java.util.Queue;
import java.util.Stack;

public interface IModel {
    void generateMaze(int rows, int cols);
    int[][] getMaze();
    void updatePlayerLocation(MovementDirection direction);
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer o);
    void solveMaze();
    Queue<Integer[]> getSolution();
    Integer[] getStartPos();
    Integer[] getGoalPos();
    void stopServers();
    Stack<Integer[]> getVisited();

    void saveMaze(File file);

    void loadMaze(File file);
}
