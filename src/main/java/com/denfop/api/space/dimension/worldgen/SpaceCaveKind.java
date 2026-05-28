package com.denfop.api.space.dimension.worldgen;

import net.minecraft.util.StringRepresentable;

public enum SpaceCaveKind implements StringRepresentable {
    STONE_TUNNELS("stone_tunnels"),
    HALL_SYSTEM("hall_system"),
    FRACTURE("fracture"),
    POROUS("porous"),
    VERTICAL_SHAFT("vertical_shaft"),
    VOLCANIC_CHAMBER("volcanic_chamber"),
    FROZEN_CHAMBER("frozen_chamber"),
    THERMAL_CHAMBER("thermal_chamber"),
    LIQUID_CHAMBER("liquid_chamber"),
    CRATER_LINKED_CHAMBER("crater_linked_chamber"),
    TUNNEL_CAVES("tunnel_caves"),
    LABYRINTH_CAVES("labyrinth_caves"),
    SHAFT_CAVES("shaft_caves"),
    DOME_CAVES("dome_caves"),
    WELL_CAVES("well_caves"),
    MULTI_TIER_CAVES("multi_tier_caves"),
    BRANCHED_CAVES("branched_caves"),
    SPIRAL_CAVES("spiral_caves"),
    ARCH_CAVES("arch_caves"),
    CHAMBER_CAVES("chamber_caves"),
    CANYON_CAVES("canyon_caves"),
    POCKET_CAVES("pocket_caves"),
    CREVICE_CAVES("crevice_caves"),
    AMPHITHEATER_CAVES("amphitheater_caves"),
    CATHEDRAL_CAVES("cathedral_caves"),
    GALLERY_CAVES("gallery_caves"),
    WINDING_CAVES("winding_caves"),
    CASCADE_CAVES("cascade_caves"),
    COLLAPSE_CAVES("collapse_caves"),
    VERTICAL_COLLECTORS("vertical_collectors");

    private final String name;

    SpaceCaveKind(final String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
