package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this); //Observe the Model for it's changes
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public void generateMaze(int rows, int cols) {
        model.generateMaze(rows, cols);
    }

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

    public int[][] getMaze() {
        return model.getMaze();
    }

    public int getPlayerRow() {
        return model.getPlayerRow();
    }

    public int getPlayerCol() {
        return model.getPlayerCol();
    }

    public Solution getSolution() {
        return model.getSolution();
    }

    public Integer[] getStartPos() {
        return model.getStartPos();
    }

    public Integer[] getGoalPos() {
        return model.getGoalPos();
    }
}
