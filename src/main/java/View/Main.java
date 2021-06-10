package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Solving Maze");
        Parent root = FXMLLoader.load(getClass().getResource("/myView.fxml"));

        Scene firstScene = new Scene(root, 800, 600);
        //firstScene.getStylesheets().add("myView.css");
        primaryStage.setScene(firstScene);

        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
        Thread.sleep(4000);
        Parent root2 = FXMLLoader.load(getClass().getResource("/choicesView.fxml"));
        Scene secondScene = new Scene(root2, 800, 600);
        primaryStage.setScene(secondScene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
