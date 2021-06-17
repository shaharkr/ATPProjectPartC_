package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.Stack;

/**
 * Class to implement the view-model in MVVM Architecture.
 * it is both observable-being observed by View
 * and observer-observing the Model.
 */
public class MyViewModel extends Observable implements Observer {
    private IModel model;

    /**
     * @param model the model from MVVM architecture
     * assigns itself as an observer of model.
     */
    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this); //Observe the Model for it's changes
    }

    /**
     * @param o should be a View instance, observing the view-model.
     */
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    /**
     * @param o
     * @param arg states what has been changed
     * notifies the View of the update
     */
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    /**
     * @param rows of binary maze
     * @param cols of binary maze
     * passes the generating request to the model.
     */
    public void generateMaze(int rows, int cols) {
        model.generateMaze(rows, cols);
    }

    /**
     * passes the solving request to the model.
     */
    public void solveMaze(){
        model.solveMaze();
    }

    /**
     * @param direction where the player wants to move his character
     * passes the moving request to the model.
     */
    public void movePlayerByDirection(MovementDirection direction){
        model.updatePlayerLocation(direction);

    }

    /**
     * @param keyEvent key pressed by the player
     * creates a direction enum of the key pressed and sends it to model via updatePlayerLocation.
     */
    public void movePlayer(KeyEvent keyEvent) {
        MovementDirection direction;
        switch (keyEvent.getCode()) {
            case NUMPAD8 -> direction = MovementDirection.UP;
            case NUMPAD2 -> direction = MovementDirection.DOWN;
            case NUMPAD6 -> direction = MovementDirection.RIGHT;
            case NUMPAD4 -> direction = MovementDirection.LEFT;
            case NUMPAD7 -> direction = MovementDirection.UP_L;
            case NUMPAD9 -> direction = MovementDirection.UP_R;
            case NUMPAD1 -> direction = MovementDirection.DOWN_L;
            case NUMPAD3 -> direction = MovementDirection.DOWN_R;
            default -> {
                return;
            }
        }
        model.updatePlayerLocation(direction);
    }

    public void movePlayer(MovementDirection direction) {
        model.updatePlayerLocation(direction);
    }

    /**
     * next methods are made for view - model transformation, and only send the request from view to model
     * or send the information from model to view
     */
    public int[][] getMaze() {
        return model.getMaze();
    }

    public int getPlayerRow() {
        return model.getPlayerRow();
    }

    public int getPlayerCol() {
        return model.getPlayerCol();
    }

    public Queue<Integer[]> getSolution() {
        return model.getSolution();
    }

    public Integer[] getStartPos() {
        return model.getStartPos();
    }

    public Integer[] getGoalPos() {
        return model.getGoalPos();
    }

    public void stopServers() {
        model.stopServers();
    }

    public Stack<Integer[]> getVisited() {
        return model.getVisited();
    }

    public void saveMaze(File file) {
        model.saveMaze(file);
    }

    public void loadMaze(File file) {
        model.loadMaze(file);
    }
}
