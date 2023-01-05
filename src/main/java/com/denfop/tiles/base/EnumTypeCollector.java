package com.denfop.tiles.base;

public enum EnumTypeCollector {
    END(6),
    EARTH(5),
    AER(7),
    AQUA(2),
    DEFAULT(0),
    NETHER(3);

    private final int meta;

    EnumTypeCollector(int meta) {
        this.meta = meta;
    }

    public int getMeta() {
        return meta;
    }
}
