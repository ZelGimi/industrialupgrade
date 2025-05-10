package com.denfop.api.windsystem;

public enum EnumLevelGenerators {
    ONE(1, 4),
    TWO(5, 8),
    THREE(9, 11),
    FOUR(12, 14);
    private final int min;
    private final int max;

    EnumLevelGenerators(int min, int max) {
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
