package com.example.demo5.view;

import com.example.demo5.model.Car;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CarView {
    private final Car car;
    private final Rectangle shape;

    public CarView(Car car) {
        this.car = car;
        this.shape = car.getShape();
        shape.setFill(Color.BLUE);
    }

    public Rectangle getShape() {
        return shape;
    }

    public void updatePosition() {
        shape.setX(car.getX());
        shape.setY(car.getY());
    }

    public Car getCar() {
        return car;
    }
}
