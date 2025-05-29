package com.example.tr.edu.erciyes.bm.group25321;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class TrafficLight {
    private final ObjectProperty<Color> lightColor;
    private final ObjectProperty<LightState> currentState;
    private int trafficLightIndex;

    public TrafficLight(int index) {
        this.lightColor = new SimpleObjectProperty(Color.RED);
        this.currentState = new SimpleObjectProperty(TrafficLight.LightState.RED);
        this.trafficLightIndex = index;
        this.currentState.addListener((obs, oldState, newState) -> {
            switch (newState.ordinal()) {
                case 0 -> this.lightColor.set(Color.RED);
                case 1 -> this.lightColor.set(Color.YELLOW);
                case 2 -> this.lightColor.set(Color.LIMEGREEN);
            }

        });
    }

    public ObjectProperty<Color> lightColorProperty() {
        return this.lightColor;
    }

    public Color getLightColor() {
        return (Color)this.lightColor.get();
    }

    public ObjectProperty<LightState> currentStateProperty() {
        return this.currentState;
    }

    public void setCurrentState(LightState state) {
        this.currentState.set(state);
    }

    public int getTrafficLightIndex() {
        return this.trafficLightIndex;
    }

    public static enum LightState {
        RED,
        YELLOW,
        GREEN;

        private LightState() {
        }
    }
}
