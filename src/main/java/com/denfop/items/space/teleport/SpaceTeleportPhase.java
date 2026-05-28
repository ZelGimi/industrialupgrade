package com.denfop.items.space.teleport;

public enum SpaceTeleportPhase {
    INACTIVE,
    OUTBOUND_PREP,
    ACTIVE,
    RETURN_PREP;

    public static SpaceTeleportPhase byOrdinal(final int id) {
        SpaceTeleportPhase[] values = values();
        if (id < 0 || id >= values.length) {
            return INACTIVE;
        }
        return values[id];
    }
}