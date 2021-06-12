package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    public static MediaPlayer media;

    public static void turnMusicOn(String s) {
        Main.media.setMute(true);
        Media song = new Media(new File(s).toURI().toString());
        Main.media = new MediaPlayer(song);
        Main.media.setAutoPlay(true);
        Main.media.setCycleCount(MediaPlayer.INDEFINITE);
        Main.media.play();
    }

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
        Media song = new Media(new File("./resources/Music/startMario.mp3").toURI().toString());
        media = new MediaPlayer(song);
        media.setAutoPlay(true);
        media.setCycleCount(MediaPlayer.INDEFINITE);
        media.setOnEndOfMedia(() -> {
            media.setMute(true);
            Parent root2 = null;
            try {
                root2 = FXMLLoader.load(getClass().getResource("/choicesView.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene secondScene = new Scene(root2, 800, 600);
            primaryStage.setScene(secondScene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        });
        media.play();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
