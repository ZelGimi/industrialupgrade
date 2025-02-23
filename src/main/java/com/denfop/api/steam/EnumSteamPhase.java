package com.denfop.api.steam;

public enum EnumSteamPhase {
    ONE(0, 15),
    TWO(16, 30),
    THREE(31, 55),
    FOUR(56, 80),
    FIVE(81, 110),
    SIX(111, 140),
    SEVEN(141, 165),
    EIGHT(166, 185),
    NINE(186, 210),
    TEN(211, 240);
    private final int min;
    private final int max;

    EnumSteamPhase(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }
}
