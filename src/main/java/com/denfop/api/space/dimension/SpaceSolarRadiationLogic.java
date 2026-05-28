package com.denfop.api.space.dimension;

import net.minecraft.util.Mth;

public final class SpaceSolarRadiationLogic {

    private static final double MIN_DISTANCE = 0.08D;
    private static final double MAX_DISTANCE = 64.0D;

    private static final float MIN_ACTIVE_EXPOSURE = 0.01F;
    private static final float NIGHT_BACKGROUND_FACTOR = 0.16F;

    private static final double ALTITUDE_START_ABOVE_SEA = 64.0D;
    private static final double ALTITUDE_FULL_ABOVE_SEA = 256.0D;
    private static final float MAX_ALTITUDE_BONUS = 0.35F;

    private SpaceSolarRadiationLogic() {
    }

    public static boolean hasSolarHazard(final SpaceDimensionProfile profile) {
        return profile != null
                && profile.hasSkyLight()
                && !profile.hasCeiling()
                && computeBaseSolarIntensity(profile) > MIN_ACTIVE_EXPOSURE;
    }

    public static float computeBaseSolarIntensity(final SpaceDimensionProfile profile) {
        if (profile == null || !profile.hasSkyLight() || profile.hasCeiling()) {
            return 0.0F;
        }

        final float distanceFactor = computeDistanceFactor(profile.body().distanceFromStar());
        final float atmosphereFactor = computeAtmosphereFactor(profile.atmosphereDensity());

        final float intensity = distanceFactor * atmosphereFactor;
        return Mth.clamp(intensity, 0.0F, 1.35F);
    }

    public static float computeDayNightFactor(final boolean isDay) {
        return isDay ? 1.0F : NIGHT_BACKGROUND_FACTOR;
    }

    public static float computeAltitudeMultiplier(final SpaceDimensionProfile profile, final double y) {
        if (profile == null) {
            return 1.0F;
        }

        final double start = profile.seaLevel() + ALTITUDE_START_ABOVE_SEA;
        final double relativeAltitude = y - start;

        final double normalized = Mth.clamp(
                relativeAltitude / Math.max(1.0D, ALTITUDE_FULL_ABOVE_SEA - ALTITUDE_START_ABOVE_SEA),
                0.0D,
                1.0D
        );

        final float smooth = smoothstep((float) normalized);
        return 1.0F + smooth * MAX_ALTITUDE_BONUS;
    }

    public static float computeExposure(
            final SpaceDimensionProfile profile,
            final double y,
            final boolean canSeeSky,
            final boolean isDay
    ) {
        if (!canSeeSky || profile == null) {
            return 0.0F;
        }

        final float base = computeBaseSolarIntensity(profile);
        if (base <= MIN_ACTIVE_EXPOSURE) {
            return 0.0F;
        }

        final float exposure = base
                * computeDayNightFactor(isDay)
                * computeAltitudeMultiplier(profile, y);

        return Mth.clamp(exposure, 0.0F, 1.50F);
    }

    private static float computeDistanceFactor(final double rawDistance) {
        final double distance = Mth.clamp(rawDistance, MIN_DISTANCE, MAX_DISTANCE);

        final double inverseSquare = 1.0D / (distance * distance);

        final double normalized = 1.0D - Math.exp(-inverseSquare * 0.65D);

        return Mth.clamp((float) (normalized * 1.65D), 0.0F, 1.60F);
    }

    private static float computeAtmosphereFactor(final float atmosphereDensity) {
        final float density = Mth.clamp(atmosphereDensity, 0.0F, 1.0F);

        final float shielding = Mth.clamp((float) Math.pow(density, 1.35D), 0.0F, 0.97F);
        final float factor = 1.0F - shielding;

        return Mth.clamp(factor, 0.02F, 1.0F);
    }

    private static float smoothstep(final float value) {
        final float t = Mth.clamp(value, 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }
}