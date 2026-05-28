package com.denfop.api.space.dimension.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record SpaceBodyFeatureConfig(String bodyName) implements FeatureConfiguration {
    public static final Codec<SpaceBodyFeatureConfig> CODEC = Codec.STRING.xmap(SpaceBodyFeatureConfig::new, SpaceBodyFeatureConfig::bodyName);
}
