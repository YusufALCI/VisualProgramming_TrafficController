package com.example.tr.edu.erciyes.bm.group25321;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class TrafficSimulatorView {
    private final AnchorPane root;
    private final Map<Integer, TextField> carCountTextFields = new HashMap();
    private final List<TrafficLight> trafficLights = new ArrayList();
    private final List<Circle> trafficLightViews = new ArrayList();
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private Button randomButton;
    private Button applyCarCountsButton;

/*sfdsdfsdf*/
    private Label northDurationLabel;
    private Label southDurationLabel;
    private Label eastDurationLabel;
    private Label westDurationLabel;



    private void setupGreenLightLabels() {
        northDurationLabel = new Label("Kuzey: 0.0 sn");
        southDurationLabel = new Label("Güney: 0.0 sn");
        eastDurationLabel = new Label("Doğu: 0.0 sn");
        westDurationLabel = new Label("Batı: 0.0 sn");

        VBox labelBox = new VBox(5);
        labelBox.getChildren().addAll(northDurationLabel, southDurationLabel, eastDurationLabel, westDurationLabel);
        labelBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: black; -fx-border-width: 1;");
        labelBox.setLayoutX(1100);
        labelBox.setLayoutY(100); // ekranında boş uygun yere

        this.root.getChildren().add(labelBox);
    }

    public void updateGreenLightDurations(double north, double south, double east, double west) {
        northDurationLabel.setText(String.format("Kuzey: %.1f sn", north));
        southDurationLabel.setText(String.format("Güney: %.1f sn", south));
        eastDurationLabel.setText(String.format("Doğu: %.1f sn", east));
        westDurationLabel.setText(String.format("Batı: %.1f sn", west));
    }


    /*dfsfsdfsdfsd*/



    public TrafficSimulatorView(AnchorPane root) {
        this.root = root;
        this.drawIntersection();
        this.setupControlPanel();
        this.setupTrafficLights();
    }

    private void drawIntersection() {
        Line hRoad = new Line((double)0.0F, (double)300.0F, (double)800.0F, (double)300.0F);
        Line vRoad = new Line((double)400.0F, (double)0.0F, (double)400.0F, (double)600.0F);
        hRoad.setStroke(Color.GRAY);
        vRoad.setStroke(Color.GRAY);
        hRoad.setStrokeWidth((double)50.0F);
        vRoad.setStrokeWidth((double)50.0F);
        this.root.getChildren().addAll(new Node[]{hRoad, vRoad});
    }

    private void setupTrafficLights() {
        TrafficLight light0 = new TrafficLight(0);
        this.trafficLights.add(light0);
        Circle circle0 = new Circle(370.0, 265.0, 5.0);  // Yolu üst sınırına hizalı
        circle0.strokeProperty().set(Color.BLACK);
        circle0.fillProperty().bind(light0.lightColorProperty());
        this.trafficLightViews.add(circle0);
        TrafficLight light1 = new TrafficLight(1);
        this.trafficLights.add(light1);
        Circle circle1 = new Circle(430.0, 335.0, 5.0);  // Yolu alt sınırına hizalı
        circle1.strokeProperty().set(Color.BLACK);
        circle1.fillProperty().bind(light1.lightColorProperty());
        this.trafficLightViews.add(circle1);
        TrafficLight light2 = new TrafficLight(2);
        this.trafficLights.add(light2);
        Circle circle2 = new Circle(430.0, 265.0, 5.0);  // Yolu sağ kenarına hizalı
        circle2.strokeProperty().set(Color.BLACK);
        circle2.fillProperty().bind(light2.lightColorProperty());
        this.trafficLightViews.add(circle2);
        TrafficLight light3 = new TrafficLight(3);
        this.trafficLights.add(light3);
        Circle circle3 = new Circle(365.0, 335.0, 5.0);  // Yolu sol kenarına hizalı
        circle3.strokeProperty().set(Color.BLACK);
        circle3.fillProperty().bind(light3.lightColorProperty());
        this.trafficLightViews.add(circle3);
        this.root.getChildren().addAll(this.trafficLightViews);
    }






    private void setupControlPanel() {
        VBox controlPanel = new VBox((double)10.0F);
        controlPanel.setAlignment(Pos.TOP_LEFT);
        controlPanel.setPrefWidth((double)275.0F);
        controlPanel.setStyle("-fx-background-color: lightgray; -fx-padding: 10px;");
        HBox buttonBox = new HBox((double)10.0F);
        this.startButton = new Button("Başlat");
        this.pauseButton = new Button("Duraklat");
        this.resetButton = new Button("Sıfırla");
        this.randomButton = new Button("Random");
        buttonBox.getChildren().addAll(new Node[]{this.startButton, this.pauseButton, this.resetButton, this.randomButton});
        controlPanel.getChildren().add(buttonBox);
        String[] directions = new String[]{"Kuzey (0):", "Güney (1):", "Doğu (2):", "Batı (3):"};

        for(int i = 0; i < directions.length; ++i) {
            HBox inputRow = new HBox((double)5.0F);
            Label label = new Label(directions[i]);
            TextField textField = new TextField("5");
            textField.setPrefWidth((double)50.0F);
            this.carCountTextFields.put(i, textField);
            inputRow.getChildren().addAll(new Node[]{label, textField});
            controlPanel.getChildren().add(inputRow);
        }

        this.applyCarCountsButton = new Button("Araç Sayılarını Uygula");
        controlPanel.getChildren().add(this.applyCarCountsButton);
        AnchorPane.setTopAnchor(controlPanel, (double)10.0F);
        AnchorPane.setRightAnchor(controlPanel, (double)10.0F);
        this.root.getChildren().add(controlPanel);
        setupGreenLightLabels();

    }

    public Button getStartButton() {
        return this.startButton;
    }

    public Button getPauseButton() {
        return this.pauseButton;
    }

    public Button getResetButton() {
        return this.resetButton;
    }

    public Button getRandomButton() {return this.randomButton;}

    public Button getApplyCarCountsButton() {
        return this.applyCarCountsButton;
    }

    public Map<Integer, TextField> getCarCountTextFields() {
        return this.carCountTextFields;
    }

    public List<TrafficLight> getTrafficLights() {
        return this.trafficLights;
    }

    public List<Circle> getTrafficLightViews() {
        return this.trafficLightViews;
    }
}
