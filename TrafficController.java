package com.example.demo2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.scene.control.TextField;

public class TrafficController {

    private final TrafficModel model;
    private final TrafficView view;

    private Timeline animation;  // Araç hareket animasyonu
    private Timeline lightTimer; // Işık ve sayaç animasyonu

    private boolean isRunning = false;

    public TrafficController(TrafficModel model, TrafficView view) {
        this.model = model;
        this.view = view;

        setupButtonActions();
    }

    private void setupButtonActions() {
        view.getStartButton().setOnAction(e -> {
            if (!isRunning && model.getCars().isEmpty()) {
                startSimulation();  // ilk kez başlatıyorsa
            } else {
                resumeSimulation();  // duraklatmadan devam ettiriyorsa
            }
        });

        view.getPauseButton().setOnAction(e -> pauseSimulation());
        view.getStopButton().setOnAction(e -> stopSimulation());
    }
    private void resumeSimulation() {
        if (isRunning) return;  // zaten çalışıyorsa bir şey yapma
        animation.play();
        lightTimer.play();
        isRunning = true;
    }


    // Başlat: araçları oluştur ve animasyonu başlat
    private void startSimulation() {
        if (isRunning) return;

        // Önce var olan araçları temizle
        model.clearCars();

        // Kullanıcıdan araç sayılarını al
        int northCount = parseCount(view.getNorthCarCountInput());
        int southCount = parseCount(view.getSouthCarCountInput());
        int westCount = parseCount(view.getWestCarCountInput());
        int eastCount = parseCount(view.getEastCarCountInput());

        // Araçları oluşturup modele ekle
        createCarsForDirection(northCount, TrafficModel.Direction.NORTH_TO_SOUTH);
        createCarsForDirection(southCount, TrafficModel.Direction.SOUTH_TO_NORTH);
        createCarsForDirection(westCount, TrafficModel.Direction.WEST_TO_EAST);
        createCarsForDirection(eastCount, TrafficModel.Direction.EAST_TO_WEST);

        // Root'tan önceki araçları kaldır
        removeOldCarsFromView();

        // Yeni araçları root'a ekle
        addCarsToView();

        // Timer sıfırla
        model.resetTimer();

        // Işık renklerini güncelle
        updateLightColors();

        // Animasyon başlat
        startAnimations();

        isRunning = true;
    }

    private int parseCount(TextField field) {
        try {
            int val = Integer.parseInt(field.getText());
            return Math.max(0, val);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void createCarsForDirection(int count, TrafficModel.Direction direction) {
        for (int i = 0; i < count; i++) {
            Rectangle rect = new Rectangle(15, 30);
            rect.setArcWidth(5);
            rect.setArcHeight(5);
            rect.setFill(Color.BLUE);

            double speed = 2 + Math.random() * 2;

            TrafficModel.Car car = new TrafficModel.Car(rect, direction, speed);

            // Başlangıç konumu ve yönüne göre pozisyon ayarla
            setCarStartPosition(car, i);

            model.addCar(car);
        }
    }

    private void setCarStartPosition(TrafficModel.Car car, int index) {
        Rectangle rect = car.getShape();
        TrafficModel.Direction dir = car.getDirection();

        switch (dir) {
            case NORTH_TO_SOUTH:
                rect.setX(285);
                rect.setY(-30 * (index + 1));
                break;
            case SOUTH_TO_NORTH:
                rect.setX(315);
                rect.setY(600 + 30 * (index + 1));
                break;
            case WEST_TO_EAST:
                rect.setX(-40 * (index + 1));
                rect.setY(285);
                break;
            case EAST_TO_WEST:
                rect.setX(600 + 40 * (index + 1));
                rect.setY(315);
                break;
        }
    }

    private void removeOldCarsFromView() {
        Group root = view.getRoot();
        root.getChildren().removeIf(node -> node instanceof Rectangle && model.getCars().stream().noneMatch(car -> car.getShape() == node) == false);
        // Bu satır biraz karmaşık ama root'taki araç şekillerini temizlemek için.
    }

    private void addCarsToView() {
        Group root = view.getRoot();
        for (TrafficModel.Car car : model.getCars()) {
            root.getChildren().add(car.getShape());
        }
    }

    private void startAnimations() {
        animation = new Timeline(new KeyFrame(Duration.millis(30), e -> moveCars()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();

        lightTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            model.decreaseTimer();
            updateTimerTexts();

            if (model.getTimer() <= 0) {
                model.toggleLights();
                updateLightColors();
            }
        }));
        lightTimer.setCycleCount(Timeline.INDEFINITE);
        lightTimer.play();
    }

    private void moveCars() {
        for (TrafficModel.Car car : model.getCars()) {
            Rectangle rect = car.getShape();
            TrafficModel.Direction dir = car.getDirection();

            // Eğer ışık kırmızıysa araç geçmesin (basit kontrol)
            boolean canMove = canCarMove(dir);

            if (!canMove) continue;

            switch (dir) {
                case NORTH_TO_SOUTH:
                    rect.setY(rect.getY() + car.getSpeed());
                    if (rect.getY() > 600) rect.setY(-30);
                    break;
                case SOUTH_TO_NORTH:
                    rect.setY(rect.getY() - car.getSpeed());
                    if (rect.getY() < -30) rect.setY(600);
                    break;
                case WEST_TO_EAST:
                    rect.setX(rect.getX() + car.getSpeed());
                    if (rect.getX() > 600) rect.setX(-40);
                    break;
                case EAST_TO_WEST:
                    rect.setX(rect.getX() - car.getSpeed());
                    if (rect.getX() < -40) rect.setX(600);
                    break;
            }
        }
    }

    private boolean canCarMove(TrafficModel.Direction dir) {
        switch (dir) {
            case NORTH_TO_SOUTH:
                return model.getNorthLightColor() == Color.GREEN;
            case SOUTH_TO_NORTH:
                return model.getSouthLightColor() == Color.GREEN;
            case WEST_TO_EAST:
                return model.getWestLightColor() == Color.GREEN;
            case EAST_TO_WEST:
                return model.getEastLightColor() == Color.GREEN;
            default:
                return false;
        }
    }

    private void updateLightColors() {
        view.getNorthLight().setFill(model.getNorthLightColor());
        view.getSouthLight().setFill(model.getSouthLightColor());
        view.getWestLight().setFill(model.getWestLightColor());
        view.getEastLight().setFill(model.getEastLightColor());
    }

    private void updateTimerTexts() {
        int timerValue = model.getTimer();
        view.getNorthTimerText().setText(String.valueOf(timerValue));
        view.getSouthTimerText().setText(String.valueOf(timerValue));
        view.getWestTimerText().setText(String.valueOf(timerValue));
        view.getEastTimerText().setText(String.valueOf(timerValue));
    }




    private void pauseSimulation() {
        if (!isRunning) return;
        animation.pause();
        lightTimer.pause();
        isRunning = false;
    }

    private void stopSimulation() {
        if (!isRunning) return;
        animation.stop();
        lightTimer.stop();
        isRunning = false;

        // Araçları temizle ve view'dan kaldır
        for (TrafficModel.Car car : model.getCars()) {
            view.getRoot().getChildren().remove(car.getShape());
        }
        model.clearCars();

        updateTimerTexts();
    }



}
