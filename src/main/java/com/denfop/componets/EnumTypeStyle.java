package com.denfop.componets;

public enum EnumTypeStyle {
    DEFAULT(238, 0),
    ADVANCED(221, 0),
    IMPROVED(202, 0),
    PERFECT(184, 0),
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
