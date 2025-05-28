package com.example.demo5.model;

public class TrafficTimingCalculator {
    public double[] calculateGreenTimes(int totalCycleTime, int[] vehicleCounts) {
        double totalVehicles = 0;
        for (int count : vehicleCounts) {
            totalVehicles += count;
        }

        double[] times = new double[4];
        if (totalVehicles == 0) return times;

        double ratio = totalCycleTime / totalVehicles;
        for (int i = 0; i < 4; i++) {
            times[i] = vehicleCounts[i] * ratio;
        }

        return times;
    }
}
