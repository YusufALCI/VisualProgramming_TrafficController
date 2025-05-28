package com.example.demo5.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Car {
    private final Rectangle shape;
    private double speed;
    private Direction direction;  // Yeni eklendi

    public enum Direction {
        NORTH, SOUTH, EAST, WEST
    }

    public Car(double x, double y, double speed, Direction direction) {
        this.speed = speed;
        this.direction = direction;
        this.shape = new Rectangle(20, 10, Color.BLUE);
        shape.setX(x);
        shape.setY(y);
    }



    public Rectangle getShape() {
        return shape;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getX() {
        return shape.getX();
    }

    public void setX(double x) {
        shape.setX(x);
    }

    public double getY() {
        return shape.getY();
    }

    public void setY(double y) {
        shape.setY(y);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    // Hareket metodu
    public void move() {
        switch (direction) {
            case NORTH -> setY(getY() - speed);  // yukarı doğru (y azalır)
            case SOUTH -> setY(getY() + speed);  // aşağı doğru (y artar)
            case EAST  -> setX(getX() + speed);  // sağa doğru (x artar)
            case WEST  -> setX(getX() - speed);  // sola doğru (x azalır)
        }
    }
}
