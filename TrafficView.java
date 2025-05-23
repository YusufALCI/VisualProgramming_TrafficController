package com.example.demo2;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TrafficView {

    private Group root;

    // Trafik ışıkları (dikdörtgenler)
    private Rectangle northLight, southLight, westLight, eastLight;

    // Sayaçlar (kalan süre)
    private Text northTimerText, southTimerText, westTimerText, eastTimerText;

    // Kullanıcıdan araç sayısı alınan metin kutuları
    private TextField northCarCountInput, southCarCountInput, westCarCountInput, eastCarCountInput;

    // Başlat, duraklat, durdur butonları
    private Button startButton, pauseButton, stopButton;

    public TrafficView(Group root) {
        this.root = root;
        setupUI();
    }

    private void setupUI() {
        // Yolları çiz
        drawRoads();

        // Trafik ışıklarını oluştur ve root'a ekle
        northLight = createLight(555, 225);
        southLight = createLight(325, 335);
        westLight = createLight(535, 320);
        eastLight = createLight(340, 238.5);

        root.getChildren().addAll(northLight, southLight, westLight, eastLight);

        // Sayaç metinlerini oluştur ve root'a ekle
        northTimerText = createTimerText(280, 215);
        southTimerText = createTimerText(320, 345);
        westTimerText = createTimerText(210, 315);
        eastTimerText = createTimerText(345, 235);

        root.getChildren().addAll(northTimerText, southTimerText, westTimerText, eastTimerText);

        // Kullanıcıdan araç sayısı almak için TextField ve label ekle
        setupCarCountInputs();

        // Butonları ekle
        setupButtons();
    }

    private void drawRoads() {
        double verticalRoadX = 450;
        double horizontalRoadY = 250;
        double roadWidth = 300;
        double laneWidth = roadWidth / 6;

        DropShadow roadShadow = new DropShadow();
        roadShadow.setOffsetX(10);               // Yatay kayma
        roadShadow.setOffsetY(10);               // Dikey kayma
        roadShadow.setRadius(20);                // Blur yarıçapı (daha geniş)
        roadShadow.setSpread(0.3);               // Gölgede doluluk oranı (%30 daha dolu)
        roadShadow.setColor(Color.rgb(0, 0, 0, 0.7)); // Daha koyu ve daha opak


        // Dikey yol (Kuzey-Güney)
        Rectangle verticalRoad = new Rectangle(verticalRoadX, 0, roadWidth, 800);
        verticalRoad.setFill(Color.DARKSLATEGRAY);
        root.getChildren().add(verticalRoad);

        // Yatay yol (Batı-Doğu)
        Rectangle horizontalRoad = new Rectangle(0, horizontalRoadY, 1750, roadWidth);
        horizontalRoad.setFill(Color.DARKSLATEGRAY);
        root.getChildren().add(horizontalRoad);

        // Dikey şerit çizgileri
        for (int i = 1; i < 6; i++) {
            Line laneLine = new Line(verticalRoadX + i * laneWidth, 0, verticalRoadX + i * laneWidth, 1000);
            laneLine.setStroke(Color.WHITE);
            laneLine.setStrokeWidth(i == 3 ? 4 : 2); // Ortadaki çizgi kalın ve düz
            if (i != 3) laneLine.getStrokeDashArray().addAll(20.0, 20.0); // Diğerleri kesikli
            root.getChildren().add(laneLine);
        }

        // Yatay şerit çizgileri
        for (int i = 1; i < 6; i++) {
            Line laneLine = new Line(0, horizontalRoadY + i * laneWidth, 1750, horizontalRoadY + i * laneWidth);
            laneLine.setStroke(Color.WHITE);
            laneLine.setStrokeWidth(i == 3 ? 4 : 2); // Ortadaki çizgi kalın ve düz
            if (i != 3) laneLine.getStrokeDashArray().addAll(20.0, 20.0); // Diğerleri kesikli
            root.getChildren().add(laneLine);
        }
    }



    private Rectangle createLight(double x, double y) {
        Rectangle light = new Rectangle(x, y, 20, 20);
        light.setArcWidth(5);
        light.setArcHeight(5);
        light.setStroke(Color.BLACK);
        light.setStrokeWidth(1);
        light.setFill(Color.RED); // Başlangıçta kırmızı
        return light;
    }

    private Text createTimerText(double x, double y) {
        Text text = new Text(x, y, "10");
        text.setFill(Color.BLACK);
        return text;
    }

    private void setupCarCountInputs() {
        VBox inputBox = new VBox(5);
        inputBox.setLayoutX(1050);
        inputBox.setLayoutY(500);
        inputBox.setPadding(new Insets(10));
        inputBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999; -fx-border-radius: 5;");

        Label label = new Label("Araç Sayısı Giriniz:");
        inputBox.getChildren().add(label);

        northCarCountInput = new TextField("6");
        northCarCountInput.setPromptText("Kuzey");
        southCarCountInput = new TextField("6");
        southCarCountInput.setPromptText("Güney");
        westCarCountInput = new TextField("6");
        westCarCountInput.setPromptText("Batı");
        eastCarCountInput = new TextField("6");
        eastCarCountInput.setPromptText("Doğu");

        inputBox.getChildren().addAll(
                new Label("Kuzey:"), northCarCountInput,
                new Label("Güney:"), southCarCountInput,
                new Label("Batı:"), westCarCountInput,
                new Label("Doğu:"), eastCarCountInput
        );

        root.getChildren().add(inputBox);
    }

    private void setupButtons() {
        HBox buttonBox = new HBox(10);
        buttonBox.setLayoutX(1050);
        buttonBox.setLayoutY(760);

        startButton = new Button("Başlat");
        pauseButton = new Button("Duraklat");
        stopButton = new Button("Durdur");

        buttonBox.getChildren().addAll(startButton, pauseButton, stopButton);
        root.getChildren().add(buttonBox);
    }

    // Getterlar - Controller için

    public Rectangle getNorthLight() {
        return northLight;
    }

    public Rectangle getSouthLight() {
        return southLight;
    }

    public Rectangle getWestLight() {
        return westLight;
    }

    public Rectangle getEastLight() {
        return eastLight;
    }

    public Text getNorthTimerText() {
        return northTimerText;
    }

    public Text getSouthTimerText() {
        return southTimerText;
    }

    public Text getWestTimerText() {
        return westTimerText;
    }

    public Text getEastTimerText() {
        return eastTimerText;
    }

    public TextField getNorthCarCountInput() {
        return northCarCountInput;
    }

    public TextField getSouthCarCountInput() {
        return southCarCountInput;
    }

    public TextField getWestCarCountInput() {
        return westCarCountInput;
    }

    public TextField getEastCarCountInput() {
        return eastCarCountInput;
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getPauseButton() {
        return pauseButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public Group getRoot() {
        return root;
    }
}
