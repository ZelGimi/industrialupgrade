package com.denfop.network.packet;

public enum EnumPollutionSyncType {
    AIR((byte) 0),
    SOIL((byte) 1);

    private final byte id;

    EnumPollutionSyncType(byte id) {
        this.id = id;
    }

    public static EnumPollutionSyncType byId(byte id) {
        return id == 1 ? SOIL : AIR;
    }

    public byte getId() {
        return this.id;
    }
}