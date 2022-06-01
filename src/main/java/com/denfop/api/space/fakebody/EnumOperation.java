package com.denfop.api.space.fakebody;

public enum EnumOperation {
    SUCCESS,
    FAIL,
    WAIT;

    EnumOperation() {

    }

    public static EnumOperation getID(int id) {
        return EnumOperation.values()[id % EnumOperation.values().length];
    }
}
