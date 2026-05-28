package com.denfop.world.vein.noise;

import net.minecraft.util.RandomSource;

import java.util.*;

public class EDTVoronoi {


    private static void edt1d(double[] f, int n, double[] d, int[] idx) {
        int[] v = new int[n];
        double[] z = new double[n + 1];
        int k = 0;

        v[0] = 0;
        z[0] = Double.NEGATIVE_INFINITY;
        z[1] = Double.POSITIVE_INFINITY;

        for (int q = 1; q < n; q++) {
            double s;
            while (true) {
                int vk = v[k];
                s = ((f[q] + q * (double) q) - (f[vk] + vk * (double) vk)) / (2.0 * (q - vk));
                if (s > z[k]) break;
                if (--k < 0) {
                    k = 0;
                    break;
                }
            }
            k++;
            v[k] = q;
            z[k] = s;
            z[k + 1] = Double.POSITIVE_INFINITY;
        }

        k = 0;
        for (int q = 0; q < n; q++) {
            while (z[k + 1] < q) k++;
            int p = v[k];
            d[q] = (q - p) * (double) (q - p) + f[p];
            idx[q] = p;
        }
    }


    public static Result computeEDTWithLabels(List<Center> centers, int width, int height) {
        final double INF = 1e30;


        int[][] seedIndexByCoord = new int[height][width];
        for (int y = 0; y < height; y++) Arrays.fill(seedIndexByCoord[y], -1);

        for (int i = 0; i < centers.size(); i++) {
            int xi = (int) Math.round(centers.get(i).x);
            int yi = (int) Math.round(centers.get(i).y);
            if (xi >= 0 && xi < width && yi >= 0 && yi < height) {
                seedIndexByCoord[yi][xi] = i;
            }
        }


        double[][] vertDist = new double[height][width];
        int[][] vertIdxY = new int[height][width];

        double[] f = new double[height];
        double[] d = new double[height];
        int[] iy = new int[height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                f[y] = (seedIndexByCoord[y][x] >= 0) ? 0.0 : INF;
            }
            edt1d(f, height, d, iy);

            for (int y = 0; y < height; y++) {
                vertDist[y][x] = d[y];
                vertIdxY[y][x] = iy[y];
            }
        }


        double[][] distSq = new double[height][width];
        int[][] nearestIdx = new int[height][width];

        double[] fx = new double[width];
        double[] dx = new double[width];
        int[] ix = new int[width];

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {
                fx[x] = vertDist[y][x];
            }

            edt1d(fx, width, dx, ix);

            for (int x = 0; x < width; x++) {
                int sx = ix[x];
                int sy = vertIdxY[y][sx];
                distSq[y][x] = dx[x];

                int centerIdx = -1;
                if (sx >= 0 && sx < width && sy >= 0 && sy < height) {
                    centerIdx = seedIndexByCoord[sy][sx];
                }
                nearestIdx[y][x] = centerIdx;
            }
        }

        return new Result(distSq, nearestIdx);
    }


    public static Map<Integer, List<Pixel>> collectCenterPixels(
            Result edt, int width, int height, RandomSource random, double maxShellRadius) {
        Map<Integer, List<Pixel>> centerPixels = new HashMap<>();
        double[][] distSq = edt.distSq;
        int[][] nearest = edt.nearestIndex;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int idx = nearest[y][x];
                if (idx < 0) continue;

                double minDistSq = distSq[y][x];
                double shell = random.nextDouble() * maxShellRadius;
                double thrSq = shell * shell;

                if (minDistSq <= thrSq) {
                    centerPixels
                            .computeIfAbsent(idx, k -> new LinkedList<>())
                            .add(new Pixel(x, y, Math.sqrt(minDistSq)));
                }
            }
        }
        return centerPixels;
    }

    public static Map<Integer, List<Pixel>> fastAssign(
            List<Center> centers, int width, int height, double maxShellRadius, RandomSource rnd) {

        Result edt = computeEDTWithLabels(centers, width, height);
        return collectCenterPixels(edt, width, height, rnd, maxShellRadius);
    }

    public static class Result {
        public final double[][] distSq;
        public final int[][] nearestIndex;

        public Result(double[][] distSq, int[][] nearestIndex) {
            this.distSq = distSq;
            this.nearestIndex = nearestIndex;
        }
    }
}