package com.denfop.world.vein.noise;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

class Center {
    double x, y;

    public Center(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public static ArrayList<Center> generateCenters(int maxCenters, double minDist, int width, int height) {
        Random rnd = new Random();
        double cell = minDist;

        int gridW = (int) Math.ceil(width / cell) + 1;
        int gridH = (int) Math.ceil(height / cell) + 1;

        LinkedList<Center> centers = new LinkedList<>();
        LinkedList<Center>[][] grid = new LinkedList[gridW][gridH];

        for (int i = 0; i < gridW; i++)
            for (int j = 0; j < gridH; j++)
                grid[i][j] = new LinkedList<>();

        double minDistSq = minDist * minDist;

        int attempts = 0;
        while (centers.size() < maxCenters && attempts < maxCenters * 20) {
            double cx = rnd.nextDouble() * width;
            double cy = rnd.nextDouble() * height;

            int gx = (int) (cx / cell);
            int gy = (int) (cy / cell);

            boolean ok = true;

            for (int ix = gx - 1; ix <= gx + 1; ix++) {
                for (int iy = gy - 1; iy <= gy + 1; iy++) {
                    if (ix < 0 || iy < 0 || ix >= gridW || iy >= gridH) continue;

                    for (Center c : grid[ix][iy]) {
                        double dx = cx - c.x;
                        double dy = cy - c.y;
                        if (dx * dx + dy * dy < minDistSq) {
                            ok = false;
                            break;
                        }
                    }
                    if (!ok) break;
                }
                if (!ok) break;
            }

            if (ok) {
                Center nc = new Center(cx, cy);
                centers.add(nc);
                grid[gx][gy].add(nc);
            }
            attempts++;
        }

        return new ArrayList<>(centers);
    }

}