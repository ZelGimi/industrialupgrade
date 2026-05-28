package com.denfop.items.space.teleport;

public enum SpaceTeleportFxType {
    NONE,
    OUTBOUND_CHARGE,
    OUTBOUND_TUNNEL,
    ARRIVAL,
    RETURN_CHARGE,
    RETURN_TUNNEL,
    RETURN_ARRIVAL;

    public static SpaceTeleportFxType byOrdinal(final int id) {
        SpaceTeleportFxType[] values = values();
        if (id < 0 || id >= values.length) {
            return NONE;
        }
        return values[id];
    }
}