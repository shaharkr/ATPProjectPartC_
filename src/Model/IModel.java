package Model;

import algorithms.search.Solution;

import java.util.Observer;
import java.util.Stack;

public interface IModel {
    void generateMaze(int rows, int cols);
    int[][] getMaze();
    void updatePlayerLocation(MovementDirection direction);
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer o);
    void solveMaze();
    Solution getSolution();
    Integer[] getStartPos();
    Integer[] getGoalPos();
    void stopServers();
}
