package com.denfop.api.space.dimension;

import net.minecraft.util.StringRepresentable;

public enum SpaceVentType implements StringRepresentable {
    STEAM("steam"),
    LAVA("lava"),
    ACID("acid"),
    CRYO("cryo"),
    GAS("gas");

    private final String name;

    SpaceVentType(final String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
