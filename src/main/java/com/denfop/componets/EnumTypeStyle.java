package com.denfop.componets;

public enum EnumTypeStyle {
    DEFAULT(238, 0),
    ADVANCED(221, 0),
    IMPROVED(202, 0),
    PERFECT(184, 0),
    PHOTONIC(0, 0),
    STEAM(0, 0),
    BIO(0, 0),
    SPACE(0, 0),
    ;

    private final int x;
    private final int y;

    EnumTypeStyle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
