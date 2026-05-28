package com.denfop.api.pollution.client;

public final class PollutionVisualData {

    public static final PollutionVisualData NONE = new PollutionVisualData(
            PollutionVisualType.NONE,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            1.0F,
            1.0F,
            0xFFFFFF,
            0xFFFFFF,
            0xFFFFFF,
            0xFFFFFF,
            0xFFFFFF,
            0xFFFFFF
    );

    private final PollutionVisualType dominantType;

    private final float overallInfluence;
    private final float airInfluence;
    private final float soilInfluence;
    private final float radiationInfluence;

    private final float skyInfluence;
    private final float fogInfluence;
    private final float screenInfluence;
    private final float biomeInfluence;

    private final float saturationLoss;
    private final float horizonHaze;
    private final float cloudVeil;
    private final float shimmerStrength;

    private final float fogNearFactor;
    private final float fogFarFactor;

    private final int skyColor;
    private final int fogColor;
    private final int overlayColor;
    private final int grassTintColor;
    private final int foliageTintColor;
    private final int waterTintColor;

    public PollutionVisualData(
            PollutionVisualType dominantType,
            float overallInfluence,
            float airInfluence,
            float soilInfluence,
            float radiationInfluence,
            float skyInfluence,
            float fogInfluence,
            float screenInfluence,
            float biomeInfluence,
            float saturationLoss,
            float horizonHaze,
            float cloudVeil,
            float shimmerStrength,
            float fogNearFactor,
            float fogFarFactor,
            int skyColor,
            int fogColor,
            int overlayColor,
            int grassTintColor,
            int foliageTintColor,
            int waterTintColor
    ) {
        this.dominantType = dominantType;
        this.overallInfluence = overallInfluence;
        this.airInfluence = airInfluence;
        this.soilInfluence = soilInfluence;
        this.radiationInfluence = radiationInfluence;
        this.skyInfluence = skyInfluence;
        this.fogInfluence = fogInfluence;
        this.screenInfluence = screenInfluence;
        this.biomeInfluence = biomeInfluence;
        this.saturationLoss = saturationLoss;
        this.horizonHaze = horizonHaze;
        this.cloudVeil = cloudVeil;
        this.shimmerStrength = shimmerStrength;
        this.fogNearFactor = fogNearFactor;
        this.fogFarFactor = fogFarFactor;
        this.skyColor = skyColor;
        this.fogColor = fogColor;
        this.overlayColor = overlayColor;
        this.grassTintColor = grassTintColor;
        this.foliageTintColor = foliageTintColor;
        this.waterTintColor = waterTintColor;
    }

    public PollutionVisualData lerpTo(PollutionVisualData target, float alpha) {
        float t = PollutionVisualColors.clamp01(alpha);

        PollutionVisualType resultType = target.overallInfluence >= (this.overallInfluence * 0.85F)
                ? target.dominantType
                : this.dominantType;

        return new PollutionVisualData(
                resultType,
                net.minecraft.util.Mth.lerp(t, this.overallInfluence, target.overallInfluence),
                net.minecraft.util.Mth.lerp(t, this.airInfluence, target.airInfluence),
                net.minecraft.util.Mth.lerp(t, this.soilInfluence, target.soilInfluence),
                net.minecraft.util.Mth.lerp(t, this.radiationInfluence, target.radiationInfluence),
                net.minecraft.util.Mth.lerp(t, this.skyInfluence, target.skyInfluence),
                net.minecraft.util.Mth.lerp(t, this.fogInfluence, target.fogInfluence),
                net.minecraft.util.Mth.lerp(t, this.screenInfluence, target.screenInfluence),
                net.minecraft.util.Mth.lerp(t, this.biomeInfluence, target.biomeInfluence),
                net.minecraft.util.Mth.lerp(t, this.saturationLoss, target.saturationLoss),
                net.minecraft.util.Mth.lerp(t, this.horizonHaze, target.horizonHaze),
                net.minecraft.util.Mth.lerp(t, this.cloudVeil, target.cloudVeil),
                net.minecraft.util.Mth.lerp(t, this.shimmerStrength, target.shimmerStrength),
                net.minecraft.util.Mth.lerp(t, this.fogNearFactor, target.fogNearFactor),
                net.minecraft.util.Mth.lerp(t, this.fogFarFactor, target.fogFarFactor),
                PollutionVisualColors.lerpColor(this.skyColor, target.skyColor, t),
                PollutionVisualColors.lerpColor(this.fogColor, target.fogColor, t),
                PollutionVisualColors.lerpColor(this.overlayColor, target.overlayColor, t),
                PollutionVisualColors.lerpColor(this.grassTintColor, target.grassTintColor, t),
                PollutionVisualColors.lerpColor(this.foliageTintColor, target.foliageTintColor, t),
                PollutionVisualColors.lerpColor(this.waterTintColor, target.waterTintColor, t)
        );
    }

    public boolean isActive() {
        return this.overallInfluence > 0.01F
                || this.skyInfluence > 0.01F
                || this.fogInfluence > 0.01F
                || this.screenInfluence > 0.01F
                || this.biomeInfluence > 0.01F;
    }

    public PollutionVisualType getDominantType() {
        return dominantType;
    }

    public float getOverallInfluence() {
        return overallInfluence;
    }

    public float getAirInfluence() {
        return airInfluence;
    }

    public float getSoilInfluence() {
        return soilInfluence;
    }

    public float getRadiationInfluence() {
        return radiationInfluence;
    }

    public float getSkyInfluence() {
        return skyInfluence;
    }

    public float getFogInfluence() {
        return fogInfluence;
    }

    public float getScreenInfluence() {
        return screenInfluence;
    }

    public float getBiomeInfluence() {
        return biomeInfluence;
    }

    public float getSaturationLoss() {
        return saturationLoss;
    }

    public float getHorizonHaze() {
        return horizonHaze;
    }

    public float getCloudVeil() {
        return cloudVeil;
    }

    public float getShimmerStrength() {
        return shimmerStrength;
    }

    public float getFogNearFactor() {
        return fogNearFactor;
    }

    public float getFogFarFactor() {
        return fogFarFactor;
    }

    public int getSkyColor() {
        return skyColor;
    }

    public int getFogColor() {
        return fogColor;
    }

    public int getOverlayColor() {
        return overlayColor;
    }

    public int getGrassTintColor() {
        return grassTintColor;
    }

    public int getFoliageTintColor() {
        return foliageTintColor;
    }

    public int getWaterTintColor() {
        return waterTintColor;
    }
}