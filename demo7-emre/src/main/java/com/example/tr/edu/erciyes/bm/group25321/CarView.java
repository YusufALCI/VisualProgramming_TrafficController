package com.example.tr.edu.erciyes.bm.group25321;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class CarView extends Pane {
    private final Rectangle carShape;

    public CarView(double width, double height) {
        this.carShape = new Rectangle(width, height);
        this.carShape.setArcWidth((double)10.0F);
        this.carShape.setArcHeight((double)10.0F);
        this.getChildren().add(this.carShape);
    }

    public Rectangle getCarShape() {
        return this.carShape;
    }
}
