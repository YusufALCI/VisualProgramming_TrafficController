package com.example.tr.edu.erciyes.bm.group25321;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.util.LinkedList;
import java.util.Queue;

public class RoadModel {
    public final Queue<Car> queue1 = new LinkedList();
    public final Queue<Car> queue2 = new LinkedList();
    public final Queue<Car> queue3 = new LinkedList();
    public final Queue<Car> queue4 = new LinkedList();

    public RoadModel() {
    }

    public Queue<Car> getQueue(int index) {
        Queue var10000;
        switch (index) {
            case 0 -> var10000 = this.queue1;
            case 1 -> var10000 = this.queue2;
            case 2 -> var10000 = this.queue3;
            case 3 -> var10000 = this.queue4;
            default -> throw new IllegalArgumentException("Invalid queue index: " + index);
        }

        return var10000;
    }
}
