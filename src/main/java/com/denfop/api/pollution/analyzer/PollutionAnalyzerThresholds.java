package com.denfop.api.pollution.analyzer;

import net.minecraft.nbt.CompoundTag;

public class PollutionAnalyzerThresholds {

    private final String medium;
    private final double recommendedMax;
    private final double warningFrom;
    private final double dangerFrom;
    private final double criticalFrom;

    public PollutionAnalyzerThresholds(
            String medium,
            double recommendedMax,
            double warningFrom,
            double dangerFrom,
            double criticalFrom
    ) {
        this.medium = medium;
        this.recommendedMax = recommendedMax;
        this.warningFrom = warningFrom;
        this.dangerFrom = dangerFrom;
        this.criticalFrom = criticalFrom;
    }

    public static PollutionAnalyzerThresholds fromTag(CompoundTag tag) {
        return new PollutionAnalyzerThresholds(
                tag.getString("medium"),
                tag.getDouble("recommendedMax"),
                tag.getDouble("warningFrom"),
                tag.getDouble("dangerFrom"),
                tag.getDouble("criticalFrom")
        );
    }

    public String getMedium() {
        return medium;
    }

    public double getRecommendedMax() {
        return recommendedMax;
    }

    public double getWarningFrom() {
        return warningFrom;
    }

    public double getDangerFrom() {
        return dangerFrom;
    }

    public double getCriticalFrom() {
        return criticalFrom;
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("medium", medium);
        tag.putDouble("recommendedMax", recommendedMax);
        tag.putDouble("warningFrom", warningFrom);
        tag.putDouble("dangerFrom", dangerFrom);
        tag.putDouble("criticalFrom", criticalFrom);
        return tag;
    }
}