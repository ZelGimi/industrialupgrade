package com.denfop.api.brewage;

public enum EnumTimeVariety {

    BREW(0),
    YOUNGSTER(0.5),
    BEER(2),
    ALE(12),
    DRAGONBLOOD(24),
    BLACK_STUFF(25);
    static EnumTimeVariety[] varieties = values();
    private final double time;

    EnumTimeVariety(double time) {
        this.time = time;
    }

    public static EnumTimeVariety getVarietyFromTime(double time) {
        for (int i = varieties.length - 1; i >= 0; i--) {
            if (time >= varieties[i].time) {
                return varieties[i];
            }
        }
        return EnumTimeVariety.BREW;
    }

    public double getTime() {
        return time;
    }
}
