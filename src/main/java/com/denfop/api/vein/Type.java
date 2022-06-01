package com.denfop.api.vein;


public enum Type {
    OIL,
    VEIN,
    EMPTY;

    Type() {
    }

    public static Type getID(int id) {
        return values()[id % values().length];
    }

}
