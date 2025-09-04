package com.denfop.api.vein.common;


public enum Type {
    OIL,
    VEIN,
    GAS,
    EMPTY;

    Type() {
    }

    public static Type getID(int id) {
        return values()[id % values().length];
    }

}
