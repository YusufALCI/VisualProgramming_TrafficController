package com.example.demo2;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TrafikSimulasyonu extends Application {

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();

        TrafficModel model = new TrafficModel();
        TrafficView view = new TrafficView(root);
        TrafficController controller = new TrafficController(model, view);

        Scene scene = new Scene(root, 600, 600);

        primaryStage.setTitle("Trafik Işıkları Simülasyonu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
