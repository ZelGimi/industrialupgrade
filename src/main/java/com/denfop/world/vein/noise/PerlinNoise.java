package com.denfop.world.vein.noise;

import java.util.Random;

public class PerlinNoise {
    private final int[] permutation;

    public PerlinNoise(long seed) {
        permutation = new int[512];
        Integer[] p = new Integer[256];
        for (int i = 0; i < 256; i++) p[i] = i;

        Random random = new Random(seed);

        for (int i = 255; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = p[i];
            p[i] = p[index];
            p[index] = temp;
        }

        for (int i = 0; i < 512; i++) {
            permutation[i] = p[i % 256];
        }
    }

    public PerlinNoise() {
        this(System.currentTimeMillis());
    }


    public double noise(double x, double y) {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;

        x -= Math.floor(x);
        y -= Math.floor(y);

        double u = fade(x);
        double v = fade(y);

        int A = permutation[X] + Y;
        int B = permutation[X + 1] + Y;

        return lerp(v,
                lerp(u, grad(permutation[A], x, y), grad(permutation[B], x - 1, y)),
                lerp(u, grad(permutation[A + 1], x, y - 1), grad(permutation[B + 1], x - 1, y - 1))
        );
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y) {
        int h = hash & 7;
        double u = h < 4 ? x : y;
        double v = h < 4 ? y : x;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}
