package com.denfop.api.pollution.analyzer;

import com.denfop.api.pollution.component.ChunkLevel;
import com.denfop.api.pollution.component.LevelPollution;
import net.minecraft.util.Mth;

public final class PollutionAnalyzerMath {

    public static final double BAND_SIZE = 125.0D;

    private PollutionAnalyzerMath() {
    }

    public static double severity(LevelPollution level, double bandPollution) {
        if (level == null) {
            return 0.0D;
        }
        return level.ordinal() * BAND_SIZE + Math.max(0.0D, bandPollution);
    }

    public static double severity(ChunkLevel level) {
        if (level == null) {
            return 0.0D;
        }
        return severity(level.getLevelPollution(), level.getPollution());
    }

    public static LevelPollution levelFromSeverity(double severity) {
        int ordinal = Mth.clamp((int) Math.floor(Math.max(0.0D, severity) / BAND_SIZE), 0, LevelPollution.values().length - 1);
        return LevelPollution.values()[ordinal];
    }

    public static double bandPollutionFromSeverity(double severity) {
        if (severity <= 0.0D) {
            return 0.0D;
        }
        return severity % BAND_SIZE;
    }

    public static PollutionAnalyzerThresholds airThresholds() {
        return new PollutionAnalyzerThresholds("air", 124.0D, 125.0D, 250.0D, 375.0D);
    }

    public static PollutionAnalyzerThresholds soilThresholds() {
        return new PollutionAnalyzerThresholds("soil", 124.0D, 125.0D, 250.0D, 375.0D);
    }

    public static int riskBand(double severity, PollutionAnalyzerThresholds thresholds) {
        if (severity <= thresholds.getRecommendedMax()) {
            return 0;
        }
        if (severity < thresholds.getDangerFrom()) {
            return 1;
        }
        if (severity < thresholds.getCriticalFrom()) {
            return 2;
        }
        return 3;
    }

    public static float normalizedRisk(double severity, PollutionAnalyzerThresholds thresholds) {
        if (severity <= thresholds.getRecommendedMax()) {
            return Mth.clamp((float) (severity / Math.max(1.0D, thresholds.getRecommendedMax())), 0.0F, 0.33F);
        }
        if (severity < thresholds.getDangerFrom()) {
            double range = Math.max(1.0D, thresholds.getDangerFrom() - thresholds.getWarningFrom());
            return 0.33F + Mth.clamp((float) ((severity - thresholds.getWarningFrom()) / range), 0.0F, 0.33F);
        }
        if (severity < thresholds.getCriticalFrom()) {
            double range = Math.max(1.0D, thresholds.getCriticalFrom() - thresholds.getDangerFrom());
            return 0.66F + Mth.clamp((float) ((severity - thresholds.getDangerFrom()) / range), 0.0F, 0.34F);
        }
        return 1.0F;
    }

    public static double averageTerrain(int nw, int ne, int sw, int se, int center) {
        return (nw + ne + sw + se + center) / 5.0D;
    }
}