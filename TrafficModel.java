package com.example.demo2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class TrafficModel {

    // Araçların hareket yönleri
    public enum Direction {
        NORTH_TO_SOUTH,
        SOUTH_TO_NORTH,
        WEST_TO_EAST,
        EAST_TO_WEST
    }

    // Araç sınıfı (görsel şekil, yön, hız içerir)
    public static class Car {
        private Rectangle shape;
        private Direction direction;
        private double speed;

        public Car(Rectangle shape, Direction direction, double speed) {
            this.shape = shape;
            this.direction = direction;
            this.speed = speed;
        }

        public Rectangle getShape() {
            return shape;
        }

        public Direction getDirection() {
            return direction;
        }

        public double getSpeed() {
            return speed;
        }
    }

    private final List<Car> cars = new ArrayList<>();

    // Işık renkleri (başlangıçta Kuzey ve Güney kırmızı, Doğu ve Batı yeşil)
    private Color northLightColor = Color.RED;
    private Color southLightColor = Color.RED;
    private Color westLightColor = Color.GREEN;
    private Color eastLightColor = Color.GREEN;

    private int timer = 10;  // Işık süresi (saniye cinsinden)

    // Araç ekle
    public void addCar(Car car) {
        cars.add(car);
    }

    // Araçları temizle (örneğin, simülasyon yeniden başlatılırken)
    public void clearCars() {
        cars.clear();
    }

    public List<Car> getCars() {
        return cars;
    }

    // Işık renkleri getter/setter
    public Color getNorthLightColor() {
        return northLightColor;
    }

    public void setNorthLightColor(Color color) {
        northLightColor = color;
    }

    public Color getSouthLightColor() {
        return southLightColor;
    }

    public void setSouthLightColor(Color color) {
        southLightColor = color;
    }

    public Color getWestLightColor() {
        return westLightColor;
    }

    public void setWestLightColor(Color color) {
        westLightColor = color;
    }

    public Color getEastLightColor() {
        return eastLightColor;
    }

    public void setEastLightColor(Color color) {
        eastLightColor = color;
    }

    // Timer işlemleri
    public int getTimer() {
        return timer;
    }

    public void resetTimer() {
        timer = 10;
    }

    public void decreaseTimer() {
        if (timer > 0) timer--;
    }

    // Işıkları değiştir (kuzey-güney ile doğu-batı değiş tokuş)
    public void toggleLights() {
        if (northLightColor == Color.GREEN) {
            northLightColor = southLightColor = Color.RED;
            westLightColor = eastLightColor = Color.GREEN;
        } else {
            northLightColor = southLightColor = Color.GREEN;
            westLightColor = eastLightColor = Color.RED;
        }
        resetTimer();
    }

    // Yönde kaç araç var sayısını döner
    private int getCarCountForDirection(Direction direction) {
        int count = 0;
        for (Car car : cars) {
            if (car.getDirection() == direction) {
                count++;
            }
        }
        return count;
    }

    public int getNorthCarCount() {
        return getCarCountForDirection(Direction.NORTH_TO_SOUTH);
    }

    public int getSouthCarCount() {
        return getCarCountForDirection(Direction.SOUTH_TO_NORTH);
    }

    public int getWestCarCount() {
        return getCarCountForDirection(Direction.WEST_TO_EAST);
    }

    public int getEastCarCount() {
        return getCarCountForDirection(Direction.EAST_TO_WEST);
    }
}
