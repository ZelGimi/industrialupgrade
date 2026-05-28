package com.denfop.api.space.dimension.worldgen;

import net.minecraft.util.StringRepresentable;

public enum SpaceFormationKind implements StringRepresentable {
    PILLAR_FIELD("pillar_field"),
    ARCH("arch"),
    PLATEAU("plateau"),
    MOUNTAIN_SPIRE("mountain_spire"),
    ICE_CLIFF("ice_cliff"),
    CRYSTAL_OUTCROP("crystal_outcrop"),
    DEBRIS_FIELD("debris_field"),
    VOLCANIC_FIELD("volcanic_field"),
    CRATER("crater"),
    SURFACE_RIFT("surface_rift");

    private final String name;

    SpaceFormationKind(final String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}