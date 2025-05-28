package com.example.demo5;

import com.example.demo5.controller.CarController;
import com.example.demo5.controller.TrafficController;
import com.example.demo5.model.Car;
import com.example.demo5.model.RoadModel;
import com.example.demo5.model.TrafficTimingCalculator;
import com.example.demo5.view.TrafficSimulatorView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class TrafficSimulator2App extends Application {

    private final RoadModel model = new RoadModel();
    private TrafficSimulatorView simulatorView;
    private TrafficController controller;
    private CarController carController;
    private final TrafficTimingCalculator timingCalculator = new TrafficTimingCalculator();

    @Override
    public void start(Stage primaryStage) {
        simulatorView = new TrafficSimulatorView();
        carController = new CarController(model, simulatorView.getSimulationPane());
        controller = new TrafficController(model, simulatorView.getSimulationPane(), simulatorView.getTrafficLightView());


        Scene scene = new Scene(simulatorView, 800, 650);
        primaryStage.setTitle("Trafik Simülasyonu");
        primaryStage.setScene(scene);

        drawIntersection();

        bindButtonEvents();

        primaryStage.show();
    }

    private void drawIntersection() {
        Line horizontalRoad = new Line(0, 300, 800, 300);
        horizontalRoad.setStroke(Color.GRAY);
        horizontalRoad.setStrokeWidth(40);

        Line verticalRoad = new Line(400, 0, 400, 600);
        verticalRoad.setStroke(Color.GRAY);
        verticalRoad.setStrokeWidth(40);

        simulatorView.getSimulationPane().getChildren().addAll(horizontalRoad, verticalRoad);
    }

    private void bindButtonEvents() {
        simulatorView.getControlPanel().setOnStart(() -> {
            System.out.println("Simülasyon Başlatıldı");

            controller.stop(); // Önce durdur ve temizle
            clearSimulation();
            drawIntersection();

            int kuzeyCount = simulatorView.getControlPanel().getKuzeyAracSayisi();
            int guneyCount = simulatorView.getControlPanel().getGuneyAracSayisi();
            int doguCount = simulatorView.getControlPanel().getDoguAracSayisi();
            int batiCount = simulatorView.getControlPanel().getBatiAracSayisi();

            int[] vehicleCounts = new int[]{kuzeyCount, guneyCount, doguCount, batiCount};
            int totalCycleTime = 60;

            double[] greenDurations = timingCalculator.calculateGreenTimes(totalCycleTime, vehicleCounts);
            controller.setGreenDurations(greenDurations);

            // Araçları CarController üzerinden ekle
            generateCars(0, kuzeyCount, 400, -40, 30, 2);  // Kuzeyden aşağı
            generateCars(1, guneyCount, 420, 650, 30, 2);  // Güneyden yukarı
            generateCars(2, doguCount, 850, 280, 30, 2);   // Doğudan sola
            generateCars(3, batiCount, -40, 320, 60, 2);   // Batıdan sağa

            controller.start();
        });

        simulatorView.getControlPanel().setOnPause(() -> {
            System.out.println("Simülasyon Duraklatıldı");
            controller.pause();
        });

        simulatorView.getControlPanel().setOnStop(() -> {
            System.out.println("Simülasyon Durduruldu");
            controller.stop();
            clearSimulation();
            drawIntersection();
        });
    }

    private void generateCars(int queueIndex, int carCount, double startX, double startY, double distance, double speed) {
        for (int i = 0; i < carCount; i++) {
            double x = startX;
            double y = startY;
            Car.Direction direction = null;
            switch (queueIndex) {
                case 0 -> {
                    y = startY + i * distance;
                    direction = Car.Direction.NORTH;
                }
                case 1 -> {
                    y = startY - i * distance;
                    direction = Car.Direction.SOUTH;
                }
                case 2 -> {
                    x = startX - i * distance;
                    direction = Car.Direction.EAST;
                }
                case 3 -> {
                    x = startX + i * distance;
                    direction = Car.Direction.WEST;
                }
            }
            Car car = new Car(x, y, speed, direction);
            carController.addCar(car, queueIndex);  // Burada controller değil carController kullanıldı
        }
    }

    private void clearSimulation() {
        simulatorView.getSimulationPane().getChildren().clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
