package com.denfop.items.space.teleport;

public enum SpaceTeleportReason {
    NONE,
    MANUAL,
    LOW_CHARGE,
    ITEM_LOST,
    LOGIN_RECOVERY,
    INVALID_TARGET;

    public static SpaceTeleportReason byOrdinal(final int id) {
        SpaceTeleportReason[] values = values();
        if (id < 0 || id >= values.length) {
            return NONE;
        }
        return values[id];
    }
}