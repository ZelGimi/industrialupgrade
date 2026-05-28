package com.denfop.api.pollution.client;

public final class PollutionLocalTintState {

    public static final PollutionLocalTintState NONE = new PollutionLocalTintState(
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0xFFFFFF,
            0xFFFFFF,
            0xFFFFFF
    );

    private final float airInfluence;
    private final float soilInfluence;
    private final float radiationInfluence;
    private final float biomeInfluence;
    private final float saturationLoss;
    private final int grassTintColor;
    private final int foliageTintColor;
    private final int waterTintColor;

    public PollutionLocalTintState(
            float airInfluence,
            float soilInfluence,
            float radiationInfluence,
            float biomeInfluence,
            float saturationLoss,
            int grassTintColor,
            int foliageTintColor,
            int waterTintColor
    ) {
        this.airInfluence = airInfluence;
        this.soilInfluence = soilInfluence;
        this.radiationInfluence = radiationInfluence;
        this.biomeInfluence = biomeInfluence;
        this.saturationLoss = saturationLoss;
        this.grassTintColor = grassTintColor;
        this.foliageTintColor = foliageTintColor;
        this.waterTintColor = waterTintColor;
    }

    public boolean isActive() {
        return this.biomeInfluence > 0.01F
                || this.airInfluence > 0.01F
                || this.soilInfluence > 0.01F
                || this.radiationInfluence > 0.01F;
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

    public float getBiomeInfluence() {
        return biomeInfluence;
    }

    public float getSaturationLoss() {
        return saturationLoss;
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