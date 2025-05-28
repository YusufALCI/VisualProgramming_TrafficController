package com.example.demo5.controller;

import com.example.demo5.model.Car;
import com.example.demo5.model.RoadModel;
import com.example.demo5.view.CarView;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class CarController {

    private final RoadModel model;
    private final Pane simulationPane;
    private final List<CarView> carViews = new ArrayList<>();

    private AnimationTimer animationTimer;

    private int activeQueueIndex = -1; // Başlangıçta hiçbir kuyruk aktif değil.

    public CarController(RoadModel model, Pane simulationPane) {
        this.model = model;
        this.simulationPane = simulationPane;
    }

    public void addCar(Car car, int queueIndex) {
        model.addCarToQueue(car, queueIndex);
        CarView carView = new CarView(car);
        carViews.add(carView);
        simulationPane.getChildren().add(carView.getShape());
    }



    public void startSimulation() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateCars();
            }
        };
        animationTimer.start();
    }

    public void pauseSimulation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    public void stopSimulation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        simulationPane.getChildren().clear();
        carViews.clear();
        model.clearAllQueues();
    }

    // Yeni metod: aktif kuyruk indeksini ayarla
    public void setActiveQueue(int index) {
        this.activeQueueIndex = index;
    }

    private void updateCars() {
        for (CarView carView : carViews) {
            Car car = carView.getCar();
            int carQueueIndex = model.getQueueIndexOfCar(car);
            if (carQueueIndex == activeQueueIndex) {
                // Sadece aktif kuyruktaki arabalar hareket eder.
                car.move();
            }
            carView.updatePosition();
        }
    }
}
