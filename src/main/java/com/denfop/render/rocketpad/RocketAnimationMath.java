package com.denfop.render.rocketpad;

import net.minecraft.util.Mth;

public final class RocketAnimationMath {

    private RocketAnimationMath() {
    }

    public static float easeInQuad(float t) {
        return t * t;
    }

    public static float easeInCubic(float t) {
        return t * t * t;
    }

    public static double getShakeOffset(long seed, float age, int axis) {
        // shake тільки до старту і трохи під час відриву
        if (age > 40.0F) {
            return 0.0D;
        }

        float intensity;
        if (age < 30.0F) {
            intensity = 0.015F + (age / 30.0F) * 0.03F;
        } else {
            intensity = 0.02F;
        }

        float freq = 0.9F + axis * 0.17F;
        double wave = Math.sin((age + seed * 0.13 + axis * 7.0) * freq) * intensity;
        return wave;
    }

    public static float getPitch(float age) {
        if (age < 30.0F) {
            return 0.0F;
        }

        float flightAge = age - 30.0F;
        float t = Mth.clamp(flightAge / 80.0F, 0.0F, 1.0F);

        // невеликий нахил назад/вперед під час набору швидкості
        return -8.0F * easeInQuad(t);
    }

    public static float getRoll(long seed, float age) {
        if (age < 15.0F) {
            return 0.0F;
        }

        float intensity = age < 40.0F ? 2.5F : 1.0F;
        return (float) Math.sin(age * 0.35F + seed * 0.21F) * intensity;
    }
}