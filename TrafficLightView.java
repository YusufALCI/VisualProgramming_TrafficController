package com.example.demo5.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TrafficLightView extends HBox {

    // Kuzey ışıkları
    private final Circle northRed = new Circle(10, Color.DARKRED);
    private final Circle northYellow = new Circle(10, Color.DARKGOLDENROD);
    private final Circle northGreen = new Circle(10, Color.DARKGREEN);
    private final Label northDurationLabel = new Label("0 s");

    // Güney ışıkları
    private final Circle southRed = new Circle(10, Color.DARKRED);
    private final Circle southYellow = new Circle(10, Color.DARKGOLDENROD);
    private final Circle southGreen = new Circle(10, Color.DARKGREEN);
    private final Label southDurationLabel = new Label("0 s");

    // Doğu ışıkları
    private final Circle eastRed = new Circle(10, Color.DARKRED);
    private final Circle eastYellow = new Circle(10, Color.DARKGOLDENROD);
    private final Circle eastGreen = new Circle(10, Color.DARKGREEN);
    private final Label eastDurationLabel = new Label("0 s");

    // Batı ışıkları
    private final Circle westRed = new Circle(10, Color.DARKRED);
    private final Circle westYellow = new Circle(10, Color.DARKGOLDENROD);
    private final Circle westGreen = new Circle(10, Color.DARKGREEN);
    private final Label westDurationLabel = new Label("0 s");

    public TrafficLightView() {
        setSpacing(20);
        setAlignment(Pos.CENTER);

        getChildren().addAll(
                createLightBox("Kuzey", northRed, northYellow, northGreen, northDurationLabel),
                createLightBox("Güney", southRed, southYellow, southGreen, southDurationLabel),
                createLightBox("Doğu", eastRed, eastYellow, eastGreen, eastDurationLabel),
                createLightBox("Batı", westRed, westYellow, westGreen, westDurationLabel)
        );
    }

    private VBox createLightBox(String labelText, Circle red, Circle yellow, Circle green, Label durationLabel) {
        Label label = new Label(labelText);
        VBox box = new VBox(5, label, red, yellow, green, durationLabel);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    public void setNorthLight(Color color) {
        updateLights(color, northRed, northYellow, northGreen);
    }

    public void setSouthLight(Color color) {
        updateLights(color, southRed, southYellow, southGreen);
    }

    public void setEastLight(Color color) {
        updateLights(color, eastRed, eastYellow, eastGreen);
    }

    public void setWestLight(Color color) {
        updateLights(color, westRed, westYellow, westGreen);
    }

    private void updateLights(Color activeColor, Circle red, Circle yellow, Circle green) {
        red.setFill(activeColor.equals(Color.RED) ? Color.RED : Color.DARKRED);
        yellow.setFill(activeColor.equals(Color.YELLOW) ? Color.YELLOW : Color.DARKGOLDENROD);
        green.setFill(activeColor.equals(Color.GREEN) ? Color.LIMEGREEN : Color.DARKGREEN);
    }

    // Süre label'larını güncelle
    public void setDurations(double northSeconds, double southSeconds, double eastSeconds, double westSeconds) {
        northDurationLabel.setText(String.format("%.0f s", northSeconds));
        southDurationLabel.setText(String.format("%.0f s", southSeconds));
        eastDurationLabel.setText(String.format("%.0f s", eastSeconds));
        westDurationLabel.setText(String.format("%.0f s", westSeconds));
    }
}
