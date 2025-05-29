package com.example.tr.edu.erciyes.bm.group25321;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.io.PrintStream;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.example.tr.edu.erciyes.bm.group25321.TrafficLight.LightState;

public class TrafficSimulator2App extends Application {
    private final RoadModel model = new RoadModel();
    private final AnchorPane root = new AnchorPane();
    private CarController controller;
    private TrafficSimulatorView view;
    private Timeline trafficLightCycleTimeline;
    private Timeline currentQueueTimeline;
    private final long CYCLE_DURATION_SECONDS = 120L;
    private final long YELLOW_LIGHT_DURATION_SECONDS = 2L;
    private final long CAR_SPAWN_INTERVAL = 1L;
    private int currentTrafficLightIndex = 0;
    private double[] greenLightDurations = new double[4];
    private final double[][] INITIAL_CAR_CONFIG = new double[][] {
            {0.0, 0.0, 390.0, 80.0, 0.0, 30.0},   // Kuzey (yukarıdan aşağı)
            {1.0, 0.0, 420.0, 520.0, 0.0, -30.0}, // Güney (aşağıdan yukarı)
            {2.0, 0.0, 750.0, 280.0, -30.0, 0.0}, // Doğu (sağdan sola)
            {3.0, 0.0, 50.0, 320.0, 30.0, 0.0}    // Batı (soldan sağa)
    };
    private boolean isSimulationRunning = false;

    public TrafficSimulator2App() {
    }

    public void start(Stage stage) {
        this.controller = new CarController(this.model, this.root);
        this.view = new TrafficSimulatorView(this.root);
        Scene scene = new Scene(this.root, (double)800.0F, (double)600.0F);
        stage.setTitle("Trafik Simülasyonu");
        stage.setScene(scene);
        this.initializeCars(new int[]{10, 5, 5, 5});
        this.calculateGreenLightDurations();
        this.setupControlListeners();
        this.setupTrafficLightCycle();
        stage.show();
    }

    private void initializeCars(int[] carCounts) {
        this.root.getChildren().removeIf((node) -> node instanceof Rectangle);
        this.model.getQueue(0).clear();
        this.model.getQueue(1).clear();
        this.model.getQueue(2).clear();
        this.model.getQueue(3).clear();
        this.controller.clearCarViews();

        for(int i = 0; i < carCounts.length; ++i) {
            this.generateCars(i, carCounts[i], this.INITIAL_CAR_CONFIG[i][2], this.INITIAL_CAR_CONFIG[i][3], this.INITIAL_CAR_CONFIG[i][4], this.INITIAL_CAR_CONFIG[i][5]);
        }

    }

    private void setupControlListeners() {
        this.view.getStartButton().setOnAction((e) -> this.startSimulation());
        this.view.getPauseButton().setOnAction((e) -> this.pauseSimulation());
        this.view.getResetButton().setOnAction((e) -> this.resetSimulation());
        this.view.getRandomButton().setOnAction((e) -> this.randomCarCount()); // yeni ekledim
        this.view.getApplyCarCountsButton().setOnAction((e) -> this.applyNewCarCounts());
    }

    private void randomCarCount() { // yeni ekledim
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int randomValue = random.nextInt(8) + 1; // 1-20 arası random sayı
            this.view.getCarCountTextFields().get(i).setText(String.valueOf(randomValue));
        }

    }
    private void startSimulation() {
        if (!this.isSimulationRunning) {
            this.isSimulationRunning = true;
            if (this.trafficLightCycleTimeline != null) {
                this.trafficLightCycleTimeline.play();
            }

            ((TrafficLight)this.view.getTrafficLights().get(this.currentTrafficLightIndex)).setCurrentState(LightState.GREEN);
            this.activateQueue(this.currentTrafficLightIndex);
        }

    }

    private void pauseSimulation() {
        if (this.isSimulationRunning) {
            this.isSimulationRunning = false;
            if (this.trafficLightCycleTimeline != null) {
                this.trafficLightCycleTimeline.pause();
            }

            if (this.currentQueueTimeline != null) {
                this.currentQueueTimeline.pause();
            }

            this.view.getTrafficLights().forEach((light) -> light.setCurrentState(LightState.RED));
        }

    }

    private void resetSimulation() {
        this.pauseSimulation();
        this.initializeCars(new int[]{10, 5, 5, 5});
        this.calculateGreenLightDurations();
        this.currentTrafficLightIndex = 0;
        this.view.getTrafficLights().forEach((light) -> light.setCurrentState(LightState.RED));
        if (this.trafficLightCycleTimeline != null) {
            this.trafficLightCycleTimeline.stop();
            this.trafficLightCycleTimeline = null;
        }

        this.setupTrafficLightCycle();
    }



    private void applyNewCarCounts() {
        this.pauseSimulation();
        int[] newCarCounts = new int[4];

        for(int i = 0; i < 4; ++i) {
            try {
                newCarCounts[i] = Integer.parseInt(((TextField)this.view.getCarCountTextFields().get(i)).getText());
                if (newCarCounts[i] < 0) {
                    newCarCounts[i] = 0;
                }
            } catch (NumberFormatException var4) {
                System.err.println("Geçersiz araç sayısı girişi için " + (i + 1) + ". yön: " + ((TextField)this.view.getCarCountTextFields().get(i)).getText() + ". Varsayılan değer 5 kullanılacak.");
                newCarCounts[i] = 5;
            }
        }

        this.initializeCars(newCarCounts);
        this.calculateGreenLightDurations();
    }

    private void calculateGreenLightDurations() {
        int totalCars = 0;

        for(int i = 0; i < 4; ++i) {
            totalCars += this.model.getQueue(i).size();
        }

        double totalGreenLightDuration = (double)112.0F;
        if (totalCars == 0) {
            for(int i = 0; i < 4; ++i) {
                this.greenLightDurations[i] = totalGreenLightDuration / (double)4.0F;
            }
        } else {
            for(int i = 0; i < 4; ++i) {
                int carsInQueue = this.model.getQueue(i).size();
                this.greenLightDurations[i] = (double)carsInQueue / (double)totalCars * totalGreenLightDuration;
                if (this.greenLightDurations[i] < (double)1.0F) {
                    this.greenLightDurations[i] = (double)1.0F;
                }
            }
        }

        PrintStream var10000 = System.out;
        String var10001 = String.format("%.2f", this.greenLightDurations[0]);
        var10000.println("Hesaplanan Yeşil Işık Süreleri (saniye): Kuzey: " + var10001 + ", Güney: " + String.format("%.2f", this.greenLightDurations[1]) + ", Doğu: " + String.format("%.2f", this.greenLightDurations[2]) + ", Batı: " + String.format("%.2f", this.greenLightDurations[3]));

        // Ekrandaki label'ları güncelle yeni ekledim
        this.view.updateGreenLightDurations(
                this.greenLightDurations[0],
                this.greenLightDurations[1],
                this.greenLightDurations[2],
                this.greenLightDurations[3]
        );
    }




    private void setupTrafficLightCycle() {
        if (this.trafficLightCycleTimeline != null) {
            this.trafficLightCycleTimeline.stop();
        }

        this.trafficLightCycleTimeline = new Timeline();
        this.trafficLightCycleTimeline.setCycleCount(-1);
        this.view.getTrafficLights().forEach((light) -> light.setCurrentState(LightState.RED));
        this.trafficLightCycleTimeline.getKeyFrames().add(new KeyFrame(Duration.millis((double)100.0F), (e) -> {
            if (this.isSimulationRunning) {
                if (((TrafficLight)this.view.getTrafficLights().get(this.currentTrafficLightIndex)).getLightColor() == Color.LIMEGREEN && (this.currentQueueTimeline == null || this.currentQueueTimeline.getStatus() == Status.STOPPED) && this.model.getQueue(this.currentTrafficLightIndex).isEmpty()) {
                    System.out.println("Kuyruk boşaldı, sonraki ışığa geçiliyor: " + this.currentTrafficLightIndex);
                    this.startNextTrafficLightPhase();
                }

            }
        }, new KeyValue[0]));
    }

    private void startNextTrafficLightPhase() {
        ((TrafficLight)this.view.getTrafficLights().get(this.currentTrafficLightIndex)).setCurrentState(LightState.YELLOW);
        if (this.currentQueueTimeline != null) {
            this.currentQueueTimeline.stop();
        }

        Timeline yellowToRedTransition = new Timeline(new KeyFrame[]{new KeyFrame(Duration.seconds((double)2.0F), (e) -> {
            ((TrafficLight)this.view.getTrafficLights().get(this.currentTrafficLightIndex)).setCurrentState(LightState.RED);
            this.currentTrafficLightIndex = (this.currentTrafficLightIndex + 1) % 4;
            ((TrafficLight)this.view.getTrafficLights().get(this.currentTrafficLightIndex)).setCurrentState(LightState.GREEN);
            this.activateQueue(this.currentTrafficLightIndex);
        }, new KeyValue[0])});
        yellowToRedTransition.play();
    }

    private void activateQueue(int index) {
        if (this.currentQueueTimeline != null) {
            this.currentQueueTimeline.stop();
        }

        if (this.model.getQueue(index).isEmpty()) {
            System.out.println("Aktive edilen kuyruk zaten boş: " + index + ", sonraki ışığa geçiliyor.");
            if (this.isSimulationRunning) {
            }

        } else {
            this.currentQueueTimeline = new Timeline(new KeyFrame[]{new KeyFrame(Duration.seconds((double)1.0F), (e) -> {
                if (!this.model.getQueue(index).isEmpty()) {
                    this.controller.activatePathForQueue(index);
                } else {
                    this.currentQueueTimeline.stop();
                }

            }, new KeyValue[0])});
            this.currentQueueTimeline.setCycleCount(-1);
            this.currentQueueTimeline.play();
        }
    }

    private void generateCars(int queueIndex, int carCount, double startX, double startY, double offsetX, double offsetY) {
        for (int i = carCount - 1; i >= 0; i--) {
            double x = startX + i * offsetX;
            double y = startY + i * offsetY;
            Car car = new Car(x, y);
            this.controller.addCar(car, queueIndex);
        }
    }




    public static void main(String[] args) {
        launch(args);
    }
}
