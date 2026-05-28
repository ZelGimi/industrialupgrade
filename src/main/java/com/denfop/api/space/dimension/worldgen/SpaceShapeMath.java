package com.denfop.api.space.dimension.worldgen;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class SpaceShapeMath {
    private SpaceShapeMath() {
    }

    public static long mixSeed(long seed, long value) {
        long x = seed ^ (value + 0x9E3779B97F4A7C15L + (seed << 6) + (seed >> 2));
        x ^= x >>> 30;
        x *= 0xBF58476D1CE4E5B9L;
        x ^= x >>> 27;
        x *= 0x94D049BB133111EBL;
        x ^= x >>> 31;
        return x;
    }

    public static double unit(long seed) {
        long value = mixSeed(seed, 0x632BE5ABL);
        return ((value >>> 11) & ((1L << 53) - 1)) / (double) (1L << 53);
    }

    public static double signedUnit(long seed) {
        return unit(seed) * 2.0D - 1.0D;
    }

    public static double latticeNoise(long seed, int x, int y, int z) {
        long value = mixSeed(seed, x * 341873128712L ^ y * 132897987541L ^ z * 42317861L);
        return signedUnit(value);
    }

    public static double smoothNoise(long seed, double x, double y, double z) {
        int x0 = Mth.floor(x);
        int y0 = Mth.floor(y);
        int z0 = Mth.floor(z);
        double xf = x - x0;
        double yf = y - y0;
        double zf = z - z0;

        double u = smoothStep(xf);
        double v = smoothStep(yf);
        double w = smoothStep(zf);

        double c000 = latticeNoise(seed, x0, y0, z0);
        double c100 = latticeNoise(seed, x0 + 1, y0, z0);
        double c010 = latticeNoise(seed, x0, y0 + 1, z0);
        double c110 = latticeNoise(seed, x0 + 1, y0 + 1, z0);
        double c001 = latticeNoise(seed, x0, y0, z0 + 1);
        double c101 = latticeNoise(seed, x0 + 1, y0, z0 + 1);
        double c011 = latticeNoise(seed, x0, y0 + 1, z0 + 1);
        double c111 = latticeNoise(seed, x0 + 1, y0 + 1, z0 + 1);

        double x00 = Mth.lerp(u, c000, c100);
        double x10 = Mth.lerp(u, c010, c110);
        double x01 = Mth.lerp(u, c001, c101);
        double x11 = Mth.lerp(u, c011, c111);
        double y0v = Mth.lerp(v, x00, x10);
        double y1v = Mth.lerp(v, x01, x11);
        return Mth.lerp(w, y0v, y1v);
    }

    public static double ridgeNoise(long seed, double x, double y, double z) {
        return 1.0D - Math.abs(smoothNoise(seed, x, y, z));
    }

    public static double smoothStep(double value) {
        return value * value * (3.0D - 2.0D * value);
    }

    public static Vec3 rotateY(Vec3 input, double radians) {
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);
        return new Vec3(input.x * cos - input.z * sin, input.y, input.x * sin + input.z * cos);
    }

    public static Vec3 rotateAroundAxis(Vec3 vec, Vec3 axis, double radians) {
        Vec3 unitAxis = axis.normalize();
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return vec.scale(cos)
                .add(unitAxis.cross(vec).scale(sin))
                .add(unitAxis.scale(unitAxis.dot(vec) * (1.0D - cos)));
    }

    public static Vec3 randomUnit(long seed) {
        double u = unit(seed);
        double v = unit(seed ^ 0x51AF2D37L);
        double theta = u * (Math.PI * 2.0D);
        double phi = Math.acos(v * 2.0D - 1.0D);
        double sinPhi = Math.sin(phi);
        return new Vec3(Math.cos(theta) * sinPhi, Math.cos(phi), Math.sin(theta) * sinPhi);
    }

    public static double remap(double value, double oldMin, double oldMax, double newMin, double newMax) {
        if (oldMax - oldMin == 0.0D) {
            return newMin;
        }
        double normalized = (value - oldMin) / (oldMax - oldMin);
        return newMin + normalized * (newMax - newMin);
    }

    public static int hashInt(long seed, int bound) {
        if (bound <= 0) {
            return 0;
        }
        return (int) Math.floor(unit(seed) * bound) % bound;
    }
}
