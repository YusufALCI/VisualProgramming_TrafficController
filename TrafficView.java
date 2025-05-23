package com.example.demo2;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
        northLight = createLight(255, 225);
        southLight = createLight(325, 335);
        westLight = createLight(225, 320);
        eastLight = createLight(330, 250);

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
        // Dikey yol (Kuzey-Güney)
        Line verticalRoad = new Line(300, 0, 300, 600);
        verticalRoad.setStrokeWidth(80);
        verticalRoad.setStroke(Color.LIGHTGRAY);
        root.getChildren().add(verticalRoad);

        // Yatay yol (Batı-Doğu)
        Line horizontalRoad = new Line(0, 300, 600, 300);
        horizontalRoad.setStrokeWidth(80);
        horizontalRoad.setStroke(Color.LIGHTGRAY);
        root.getChildren().add(horizontalRoad);

        // Orta çizgiler (yol çizgileri)
        for (int i = 0; i < 10; i++) {
            Line vDash = new Line(295, i * 60 + 10, 305, i * 60 + 30);
            vDash.setStroke(Color.WHITE);
            vDash.setStrokeWidth(3);
            root.getChildren().add(vDash);

            Line hDash = new Line(i * 60 + 10, 295, i * 60 + 30, 305);
            hDash.setStroke(Color.WHITE);
            hDash.setStrokeWidth(3);
            root.getChildren().add(hDash);
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
        inputBox.setLayoutX(700);
        inputBox.setLayoutY(20);
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
        buttonBox.setLayoutX(700);
        buttonBox.setLayoutY(300);

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
