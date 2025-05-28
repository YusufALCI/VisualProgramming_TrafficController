package com.example.demo5.controller;

import com.example.demo5.view.TrafficLightView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class TrafficLightController {

    private final TrafficLightView trafficLightView;
    private Timeline timeline;
    private double[] greenDurations; // saniye cinsinden

    private int currentActive = 0; // aktif yön indeksi
    private double currentRemainingTime; // o anki yeşil süresinden kalan saniye

    private AnimationTimer countdownTimer; // geri sayım için timer

    public TrafficLightController(TrafficLightView trafficLightView) {
        this.trafficLightView = trafficLightView;
    }

    public void setGreenLightDurations(double[] durations) {
        this.greenDurations = durations;
        trafficLightView.setDurations(durations[0], durations[1], durations[2], durations[3]);
        setupTimeline();
    }

    private void setupTimeline() {
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline();

        double totalDuration = 0;

        for (int i = 0; i < 4; i++) {
            int directionIndex = i;
            double duration = greenDurations[i];
            totalDuration += duration;

            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(totalDuration), e -> {
                        setActiveLight(directionIndex);
                    })
            );
        }

        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void setActiveLight(int index) {
        currentActive = index;
        currentRemainingTime = greenDurations[index];

        trafficLightView.setNorthLight(index == 0 ? Color.GREEN : Color.RED);
        trafficLightView.setSouthLight(index == 1 ? Color.GREEN : Color.RED);
        trafficLightView.setEastLight(index == 2 ? Color.GREEN : Color.RED);
        trafficLightView.setWestLight(index == 3 ? Color.GREEN : Color.RED);

        startCountdown();
    }

    private void startCountdown() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        countdownTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double elapsedSeconds = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                currentRemainingTime -= elapsedSeconds;
                if (currentRemainingTime < 0) currentRemainingTime = 0;

                updateDurationLabels();
            }
        };
        countdownTimer.start();
    }

    private void updateDurationLabels() {
        double north = (currentActive == 0) ? currentRemainingTime : 0;
        double south = (currentActive == 1) ? currentRemainingTime : 0;
        double east = (currentActive == 2) ? currentRemainingTime : 0;
        double west = (currentActive == 3) ? currentRemainingTime : 0;

        trafficLightView.setDurations(north, south, east, west);
    }

    public void start() {
        if (timeline != null) {
            timeline.playFromStart();
        }
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        trafficLightView.setNorthLight(Color.RED);
        trafficLightView.setSouthLight(Color.RED);
        trafficLightView.setEastLight(Color.RED);
        trafficLightView.setWestLight(Color.RED);

        trafficLightView.setDurations(0, 0, 0, 0);
    }

    public void setActiveQueue(int index) {
        if (timeline != null) {
            timeline.stop();
        }
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        setActiveLight(index);
    }
}
