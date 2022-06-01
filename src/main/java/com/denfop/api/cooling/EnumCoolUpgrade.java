package com.denfop.api.cooling;

public enum EnumCoolUpgrade {
    AZOTE(2),
    HYDROGEN(3),
    HELIUM(4);
    private final int level;

    EnumCoolUpgrade(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }
}
