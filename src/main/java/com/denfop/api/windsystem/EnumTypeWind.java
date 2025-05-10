package com.denfop.api.windsystem;

public enum EnumTypeWind {
    ONE(0, 0.2),
    TWO(0.3, 1.5),
    THREE(1.6, 3.3),
    FOUR(3.4, 5.4),
    FIVE(5.5, 7.9),
    SIX(8.0, 10.7),
    SEVEN(10.8, 13.8),
    EIGHT(13.9, 17.1),
    NINE(17.2, 20.7),
    TEN(20.8, 24.4);

    private final double min;
    private final double max;

    EnumTypeWind(double min, double max) {
        this.min = min;
        this.max = max;

    }

    public static EnumTypeWind getValueFromWind(double wind_speed) {
        for (EnumTypeWind wind : values()) {
            if (wind.getMin() >= wind_speed && wind_speed <= wind.max) {
                return wind;
            }
        }
        return EnumTypeWind.ONE;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }
}
