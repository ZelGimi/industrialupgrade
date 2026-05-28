package com.denfop.api.pollution.client;

import net.minecraft.util.Mth;

public final class PollutionVisualColors {

    public static final int AIR_SKY = rgb(126, 118, 97);
    public static final int SOIL_SKY = rgb(118, 112, 101);
    public static final int RADIATION_SKY = rgb(126, 214, 92);

    public static final int AIR_FOG = rgb(97, 89, 71);
    public static final int SOIL_FOG = rgb(92, 86, 79);
    public static final int RADIATION_FOG = rgb(112, 232, 84);

    public static final int AIR_OVERLAY = rgb(132, 119, 84);
    public static final int SOIL_OVERLAY = rgb(116, 108, 76);
    public static final int RADIATION_OVERLAY = rgb(132, 255, 96);

    public static final int AIR_GRASS = rgb(109, 113, 77);
    public static final int SOIL_GRASS = rgb(123, 116, 67);
    public static final int RADIATION_GRASS = rgb(108, 255, 82);

    public static final int AIR_FOLIAGE = rgb(104, 110, 83);
    public static final int SOIL_FOLIAGE = rgb(122, 118, 84);
    public static final int RADIATION_FOLIAGE = rgb(118, 255, 94);

    public static final int AIR_WATER = rgb(69, 83, 73);
    public static final int SOIL_WATER = rgb(37, 41, 26);
    public static final int RADIATION_WATER = rgb(92, 92, 77);

    private PollutionVisualColors() {
    }

    public static float clamp01(float value) {
        return Mth.clamp(value, 0.0F, 1.0F);
    }

    public static float smoothstep01(float value) {
        float t = clamp01(value);
        return t * t * (3.0F - 2.0F * t);
    }

    public static int rgb(int r, int g, int b) {
        return ((r & 255) << 16) | ((g & 255) << 8) | (b & 255);
    }

    public static int argb(int alpha, int rgb) {
        return ((alpha & 255) << 24) | (rgb & 0xFFFFFF);
    }

    public static int red(int color) {
        return (color >> 16) & 255;
    }

    public static int green(int color) {
        return (color >> 8) & 255;
    }

    public static int blue(int color) {
        return color & 255;
    }

    public static float red01(int color) {
        return red(color) / 255.0F;
    }

    public static float green01(int color) {
        return green(color) / 255.0F;
    }

    public static float blue01(int color) {
        return blue(color) / 255.0F;
    }

    public static int lerpColor(int from, int to, float alpha) {
        float t = clamp01(alpha);
        int r = Math.round(Mth.lerp(t, red(from), red(to)));
        int g = Math.round(Mth.lerp(t, green(from), green(to)));
        int b = Math.round(Mth.lerp(t, blue(from), blue(to)));
        return rgb(r, g, b);
    }

    public static int weightedColor(int first, int second, int third, float w1, float w2, float w3) {
        float total = w1 + w2 + w3;
        if (total <= 0.0001F) {
            return first;
        }

        float n1 = w1 / total;
        float n2 = w2 / total;
        float n3 = w3 / total;

        int r = Math.round(red(first) * n1 + red(second) * n2 + red(third) * n3);
        int g = Math.round(green(first) * n1 + green(second) * n2 + green(third) * n3);
        int b = Math.round(blue(first) * n1 + blue(second) * n2 + blue(third) * n3);

        return rgb(r, g, b);
    }

    public static int desaturate(int color, float amount) {
        float t = clamp01(amount);
        int r = red(color);
        int g = green(color);
        int b = blue(color);
        int gray = Math.round(r * 0.299F + g * 0.587F + b * 0.114F);

        int nr = Math.round(Mth.lerp(t, r, gray));
        int ng = Math.round(Mth.lerp(t, g, gray));
        int nb = Math.round(Mth.lerp(t, b, gray));
        return rgb(nr, ng, nb);
    }

    public static int multiply(int color, float brightness) {
        float f = clamp01(brightness);
        int r = Math.round(red(color) * f);
        int g = Math.round(green(color) * f);
        int b = Math.round(blue(color) * f);
        return rgb(r, g, b);
    }

    public static int brighten(int color, float amount) {
        float t = clamp01(amount);
        int r = Math.round(Mth.lerp(t, red(color), 255.0F));
        int g = Math.round(Mth.lerp(t, green(color), 255.0F));
        int b = Math.round(Mth.lerp(t, blue(color), 255.0F));
        return rgb(r, g, b);
    }

    public static int applyBiomeDegradation(int baseColor, int targetColor, float intensity, float saturationLoss) {
        float t = clamp01(intensity);
        int desaturated = desaturate(baseColor, clamp01(saturationLoss * (0.72F + t * 0.25F)));
        int blended = lerpColor(desaturated, targetColor, t);
        return multiply(blended, 1.0F - t * 0.10F);
    }

    public static int applyRadiationGreenBoost(int color, float radiationInfluence) {
        float t = smoothstep01(clamp01(radiationInfluence));
        if (t <= 0.001F) {
            return color;
        }

        int glowTarget = weightedColor(
                RADIATION_GRASS,
                RADIATION_FOLIAGE,
                RADIATION_OVERLAY,
                0.55F,
                0.25F,
                0.20F
        );

        int boosted = lerpColor(color, glowTarget, t * 0.82F);
        boosted = brighten(boosted, t * 0.18F);

        int r = red(boosted);
        int g = Math.min(255, Math.round(green(boosted) + 22.0F * t));
        int b = Math.max(0, Math.round(blue(boosted) - 8.0F * t));

        return rgb(r, g, b);
    }

    public static int applyRadiationWaterBoost(int color, float radiationInfluence) {
        float t = smoothstep01(clamp01(radiationInfluence));
        if (t <= 0.001F) {
            return color;
        }

        int boosted = lerpColor(color, RADIATION_WATER, t * 0.68F);
        boosted = brighten(boosted, t * 0.08F);
        return boosted;
    }


}