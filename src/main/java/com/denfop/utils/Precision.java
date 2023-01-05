package com.denfop.utils;

import java.math.BigDecimal;

public class Precision {

    private Precision() {
    }

    public static double round(double x, int scale) {
        return round(x, scale, 4);
    }

    public static double round(double x, int scale, int roundingMethod) {
        try {
            double rounded = (new BigDecimal(Double.toString(x))).setScale(scale, roundingMethod).doubleValue();
            return rounded == 0.0D ? 0.0D * x : rounded;
        } catch (NumberFormatException var7) {
            return Double.isInfinite(x) ? x : 0.0D / 0.0;
        }
    }

}
