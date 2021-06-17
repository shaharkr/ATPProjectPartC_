package View;

import javafx.event.ActionEvent;

import java.io.FileNotFoundException;

/**
 * interface every view from the MVVM architecture must implement
 */
public interface IView {
    /**
     * @param pathToImage the image you want the player to see
     * @param title title of the dialog box
     * @param w width of dialog
     * @param h height of dialig
     * @throws FileNotFoundException if pathToImage doesnt exist
     * show an image to the player, ie: about menu, help menu, etc..
     */
    void showInfoWithImage(String pathToImage, String title,double w, double h)throws FileNotFoundException;

    /**
     * @param actionEvent every view must have an exit call, to request the view model
     * for the appropriate exit.
     */
    void exitGame(ActionEvent actionEvent);
}
