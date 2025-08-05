package com.denfop.world.vein.noise;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

class Center {
    double x, y;
    public Center(double x, double y) { this.x = x; this.y = y; }


    public static ArrayList<Center> generateCenters(int maxCenters, double minDist, int width, int height) {
        Random rnd = new Random();
        LinkedList<Center> centers = new LinkedList<>();

        int attempts = 0;
        while (centers.size() < maxCenters && attempts < maxCenters * 10) {
            double cx = rnd.nextDouble() * width;
            double cy = rnd.nextDouble() * height;

            boolean tooClose = false;
            for (Center c : centers) {
                double dist = Math.hypot(cx - c.x, cy - c.y);
                if (dist < minDist) {
                    tooClose = true;
                    break;
                }
            }
            if (!tooClose) {
                centers.add(new Center(cx, cy));
            }
            attempts++;
        }
        return new ArrayList<>(centers);
    }
}