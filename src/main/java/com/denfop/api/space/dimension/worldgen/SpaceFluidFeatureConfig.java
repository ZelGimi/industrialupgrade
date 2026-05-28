package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record SpaceFluidFeatureConfig(
        String bodyId,
        String fluidId,
        SpaceFluidDistributionType distributionType,
        int radiusMin,
        int radiusMax,
        int minY,
        int maxY,
        float baseChance,
        boolean caveOnly,
        boolean surfaceAllowed,
        boolean requireHotContext,
        boolean requireColdContext
) implements FeatureConfiguration {

    public static final Codec<SpaceFluidFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("body_id").forGetter(SpaceFluidFeatureConfig::bodyId),
            Codec.STRING.fieldOf("fluid_id").forGetter(SpaceFluidFeatureConfig::fluidId),
            SpaceFluidDistributionType.CODEC.fieldOf("distribution_type").forGetter(SpaceFluidFeatureConfig::distributionType),
            Codec.INT.fieldOf("radius_min").forGetter(SpaceFluidFeatureConfig::radiusMin),
            Codec.INT.fieldOf("radius_max").forGetter(SpaceFluidFeatureConfig::radiusMax),
            Codec.INT.fieldOf("min_y").forGetter(SpaceFluidFeatureConfig::minY),
            Codec.INT.fieldOf("max_y").forGetter(SpaceFluidFeatureConfig::maxY),
            Codec.FLOAT.optionalFieldOf("base_chance", 1.0F).forGetter(SpaceFluidFeatureConfig::baseChance),
            Codec.BOOL.optionalFieldOf("cave_only", false).forGetter(SpaceFluidFeatureConfig::caveOnly),
            Codec.BOOL.optionalFieldOf("surface_allowed", false).forGetter(SpaceFluidFeatureConfig::surfaceAllowed),
            Codec.BOOL.optionalFieldOf("require_hot_context", false).forGetter(SpaceFluidFeatureConfig::requireHotContext),
            Codec.BOOL.optionalFieldOf("require_cold_context", false).forGetter(SpaceFluidFeatureConfig::requireColdContext)
    ).apply(instance, SpaceFluidFeatureConfig::new));
}
