package com.example.demo5.view;

import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class TrafficSimulatorView extends BorderPane {

    private final AnchorPane simulationPane;
    private final TrafficLightView trafficLightView;
    private final ControlPanelView controlPanel;

    public TrafficSimulatorView() {
        simulationPane = new AnchorPane();
        simulationPane.setPrefSize(800, 600);
        simulationPane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");

        trafficLightView = new TrafficLightView();
        controlPanel = new ControlPanelView();
        controlPanel.setPrefWidth(250);

        setCenter(simulationPane);
        setRight(controlPanel);
        setTop(trafficLightView);

        setPadding(new Insets(10));
    }

    public AnchorPane getSimulationPane() {
        return simulationPane;
    }

    public ControlPanelView getControlPanel() {
        return controlPanel;
    }

    public TrafficLightView getTrafficLightView() {
        return trafficLightView;
    }
}
