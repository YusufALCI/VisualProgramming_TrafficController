package com.example.demo5.model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class JuntionManager {
    private final Queue<Car>[] queues = new Queue[]{new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>()};

    public void generateCars() {
        Random random = new Random();
        int[] carCounts = {5, 10, 15, 20};

        for (int i = 0; i < queues.length; i++) {
            queues[i].clear();
            for (int j = 0; j < carCounts[i]; j++) {
                Car.Direction direction = switch (i) {
                    case 0 -> Car.Direction.NORTH;
                    case 1 -> Car.Direction.SOUTH;
                    case 2 -> Car.Direction.EAST;
                    case 3 -> Car.Direction.WEST;
                    default -> Car.Direction.NORTH; // default bir değer
                };
                queues[i].add(new Car(random.nextInt(100), random.nextInt(100), 1.0, direction));
            }
        }
    }


    public double[] calculateGreenTimes(int totalCycleTime) {
        generateCars();
        double[] times = new double[4];
        double totalCars = 0;

        for (Queue<Car> queue : queues) {
            totalCars += queue.size();
        }

        if (totalCars == 0) return new double[]{0, 0, 0, 0};

        double ratio = totalCycleTime / totalCars;
        for (int i = 0; i < 4; i++) {
            times[i] = queues[i].size() * ratio;
        }

        return times;
    }
}
