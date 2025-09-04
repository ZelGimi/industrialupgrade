package com.denfop.blockentity.mechanism.combpump;

public enum EnumTypePump {
    S(16, 4),
    A(32, 8),
    I(48, 12),
    P(64, 16),
    PH(128, 20);
    private final int xz;
    private final int y;

    EnumTypePump(int xz, int y) {
        this.xz = xz;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getXz() {
        return xz;
    }
}
