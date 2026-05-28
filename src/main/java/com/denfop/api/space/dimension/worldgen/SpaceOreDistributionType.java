package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum SpaceOreDistributionType implements StringRepresentable {
    VEIN("vein"),
    CLUSTER("cluster"),
    LAYERED("layered"),
    SCATTERED("scattered"),
    DEEP("deep"),
    CAVE_WALL("cave_wall"),
    GEOLOGICAL("geological"),
    FRACTURE("fracture"),
    CORE("core"),
    GRADIENT("gradient");

    public static final Codec<SpaceOreDistributionType> CODEC =
            StringRepresentable.fromEnum(SpaceOreDistributionType::values);

    private final String serializedName;

    SpaceOreDistributionType(String serializedName) {
        this.serializedName = serializedName;
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }
}
