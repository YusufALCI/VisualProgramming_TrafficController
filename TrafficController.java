package com.example.demo5.controller;

import com.example.demo5.model.RoadModel;
import com.example.demo5.view.TrafficLightView;
import javafx.scene.layout.Pane;

public class TrafficController {

    private final RoadModel roadModel;
    private final CarController carController;
    private final TrafficLightController trafficLightController;

    public TrafficController(RoadModel roadModel, Pane simulationPane, TrafficLightView trafficLightView) {
        this.roadModel = roadModel;
        this.carController = new CarController(roadModel, simulationPane);
        this.trafficLightController = new TrafficLightController(trafficLightView);
    }

    public void setGreenDurations(double[] durations) {
        trafficLightController.setGreenLightDurations(durations);
    }

    public void start() {
        trafficLightController.start();
        carController.startSimulation();
    }

    public void pause() {
        trafficLightController.stop();
        carController.pauseSimulation();
    }

    public void stop() {
        trafficLightController.stop();
        carController.stopSimulation();
    }

    // Trafik ışığı yönü değiştiğinde aktif aracı güncelle
    public void setActiveQueue(int index) {
        carController.setActiveQueue(index);
        trafficLightController.setActiveQueue(index);
    }

    public CarController getCarController() {
        return carController;
    }
}
