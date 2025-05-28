package com.example.demo5.model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class RoadModel {
    private final List<List<Car>> queues = new ArrayList<>();

    private Color northLightColor = Color.RED;
    private Color southLightColor = Color.RED;
    private Color westLightColor = Color.RED;
    private Color eastLightColor = Color.RED;

    public RoadModel() {
        for (int i = 0; i < 4; i++) {
            queues.add(new ArrayList<>());
        }
    }

    public void addCarToQueue(Car car, int queueIndex) {
        if (queueIndex < 0 || queueIndex >= queues.size()) return;
        queues.get(queueIndex).add(car);
    }

    public void clearAllQueues() {
        for (List<Car> queue : queues) {
            queue.clear();
        }
    }

    /**
     * Verilen aracın hangi kuyrukta olduğunu bulur.
     * Kuyruk yoksa -1 döner.
     */
    public int getQueueIndexOfCar(Car car) {
        for (int i = 0; i < queues.size(); i++) {
            if (queues.get(i).contains(car)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Kuyruk listesini döner.
     */
    public List<List<Car>> getQueues() {
        return queues;
    }

    public Color getNorthLightColor() {
        return northLightColor;
    }

    public void setNorthLightColor(Color northLightColor) {
        this.northLightColor = northLightColor;
    }

    public Color getSouthLightColor() {
        return southLightColor;
    }

    public void setSouthLightColor(Color southLightColor) {
        this.southLightColor = southLightColor;
    }

    public Color getWestLightColor() {
        return westLightColor;
    }

    public void setWestLightColor(Color westLightColor) {
        this.westLightColor = westLightColor;
    }

    public Color getEastLightColor() {
        return eastLightColor;
    }

    public void setEastLightColor(Color eastLightColor) {
        this.eastLightColor = eastLightColor;
    }
}
