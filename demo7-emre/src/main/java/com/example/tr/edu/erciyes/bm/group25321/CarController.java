package com.example.tr.edu.erciyes.bm.group25321;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import javafx.animation.PathTransition;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class CarController {
    private final RoadModel model;
    private final Pane root;
    private final Map<Car, Rectangle> carViews = new HashMap();

    public CarController(RoadModel model, Pane root) {
        this.model = model;
        this.root = root;
    }

    public void addCar(Car car, int queueIndex) {
        Queue<Car> queue = this.model.getQueue(queueIndex);
        queue.add(car);
        Rectangle carRect = new Rectangle((double)20.0F, (double)10.0F);
        carRect.setFill(car.getColor());
        carRect.setX((double)0.0F);
        carRect.setY((double)0.0F);
        carRect.setTranslateX(car.getxCar());
        carRect.setTranslateY(car.getyCar());
        this.carViews.put(car, carRect);
        this.root.getChildren().add(carRect);
    }

    public void activatePathForQueue(int queueIndex) {
        Queue<Car> queue = this.model.getQueue(queueIndex);
        if (!queue.isEmpty()) {
            Car car = (Car)queue.poll();
            Rectangle carRect = (Rectangle)this.carViews.get(car);
            if (carRect != null) {
                Path path = new Path();
                double startX = carRect.getTranslateX();
                double startY = carRect.getTranslateY();
                path.getElements().add(new MoveTo(startX, startY));
                switch (queueIndex) {
                    case 0 -> path.getElements().add(new LineTo(startX, (double)650.0F));
                    case 1 -> path.getElements().add(new LineTo(startX, (double)-50.0F));
                    case 2 -> path.getElements().add(new LineTo((double)-50.0F, startY));
                    case 3 -> path.getElements().add(new LineTo((double)850.0F, startY));
                    default -> throw new IllegalArgumentException("Geçersiz kuyruk indexi: " + queueIndex);
                }

                PathTransition transition = new PathTransition();
                transition.setNode(carRect);
                transition.setDuration(Duration.seconds((double)3.0F));
                transition.setPath(path);
                transition.setOnFinished((e) -> {
                    this.root.getChildren().remove(carRect);
                    this.carViews.remove(car);
                });
                transition.play();
            }
        }
    }

    public void clearCarViews() {
        this.carViews.clear();
    }
}
