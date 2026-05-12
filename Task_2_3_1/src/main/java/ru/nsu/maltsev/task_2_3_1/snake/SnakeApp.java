package ru.nsu.maltsev.task_2_3_1.snake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SnakeApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                SnakeApp.class.getResource("/ru/nsu/maltsev/task_2_3_1/snake/view/game-view.fxml")
        );

        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Snake");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(event -> {
            Object controller = loader.getController();

            if (controller instanceof ru.nsu.maltsev.task_2_3_1.snake.controller.GameController) {
                ((ru.nsu.maltsev.task_2_3_1.snake.controller.GameController) controller).shutdown();
            }
        });

        root.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
