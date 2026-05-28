package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum SpaceFluidDistributionType implements StringRepresentable {
    RESERVOIR("reservoir"),
    POCKET("pocket"),
    VEIN("vein"),
    UNDERGROUND_LAKE("underground_lake"),
    SURFACE_LAKE("surface_lake"),
    GEOTHERMAL("geothermal"),
    LAVA_SYSTEM("lava_system"),
    CRYO("cryo"),
    ACID("acid"),
    GAS("gas");

    public static final Codec<SpaceFluidDistributionType> CODEC =
            StringRepresentable.fromEnum(SpaceFluidDistributionType::values);

    private final String serializedName;

    SpaceFluidDistributionType(String serializedName) {
        this.serializedName = serializedName;
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }
}
