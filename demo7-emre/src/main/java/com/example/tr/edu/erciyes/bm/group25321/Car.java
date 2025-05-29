package com.example.tr.edu.erciyes.bm.group25321;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.util.Random;
import javafx.scene.paint.Color;

public class Car {
    private double xCar;
    private double yCar;
    private Color color;
    private Direction targetDirection;
    private double currentSpeed;
    private final double maxSpeed;
    private boolean isMoving;
    private static final Random random = new Random();

    public Car(double xCar, double yCar) {
        this.xCar = xCar;
        this.yCar = yCar;
        this.color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        this.maxSpeed = (double)3.0F;
        this.currentSpeed = (double)0.0F;
        this.isMoving = false;
    }

    public double getxCar() {
        return this.xCar;
    }

    public double getyCar() {
        return this.yCar;
    }

    public Color getColor() {
        return this.color;
    }

    public Direction getTargetDirection() {
        return this.targetDirection;
    }

    public void setTargetDirection(Direction targetDirection) {
        this.targetDirection = targetDirection;
    }

    public double getCurrentSpeed() {
        return this.currentSpeed;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    public boolean isMoving() {
        return this.isMoving;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public void setPosition(double x, double y) {
        this.xCar = x;
        this.yCar = y;
    }

    public static enum Direction {
        STRAIGHT,
        LEFT,
        RIGHT;

        private Direction() {
        }
    }
}
