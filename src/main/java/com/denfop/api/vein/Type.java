package com.denfop.api.vein;


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
