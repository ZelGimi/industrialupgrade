package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public record SpaceOreFeatureConfig(
        String bodyId,
        String oreBlockId,
        List<String> replaceableBlockIds,
        SpaceOreDistributionType distributionType,
        int size,
        int pathLength,
        int horizontalRadius,
        int verticalRadius,
        int minY,
        int maxY,
        float baseChance,
        float depthBonus,
        boolean requireNearAir,
        boolean requireNearFluid,
        boolean requireNearLava,
        boolean caveOnly
) implements FeatureConfiguration {

    public static final Codec<SpaceOreFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("body_id").forGetter(SpaceOreFeatureConfig::bodyId),
            Codec.STRING.fieldOf("ore_block_id").forGetter(SpaceOreFeatureConfig::oreBlockId),
            Codec.STRING.listOf().fieldOf("replaceable_block_ids").forGetter(SpaceOreFeatureConfig::replaceableBlockIds),
            SpaceOreDistributionType.CODEC.fieldOf("distribution_type").forGetter(SpaceOreFeatureConfig::distributionType),
            Codec.INT.fieldOf("size").forGetter(SpaceOreFeatureConfig::size),
            Codec.INT.fieldOf("path_length").forGetter(SpaceOreFeatureConfig::pathLength),
            Codec.INT.fieldOf("horizontal_radius").forGetter(SpaceOreFeatureConfig::horizontalRadius),
            Codec.INT.fieldOf("vertical_radius").forGetter(SpaceOreFeatureConfig::verticalRadius),
            Codec.INT.fieldOf("min_y").forGetter(SpaceOreFeatureConfig::minY),
            Codec.INT.fieldOf("max_y").forGetter(SpaceOreFeatureConfig::maxY),
            Codec.FLOAT.optionalFieldOf("base_chance", 1.0F).forGetter(SpaceOreFeatureConfig::baseChance),
            Codec.FLOAT.optionalFieldOf("depth_bonus", 0.0F).forGetter(SpaceOreFeatureConfig::depthBonus),
            Codec.BOOL.optionalFieldOf("require_near_air", false).forGetter(SpaceOreFeatureConfig::requireNearAir),
            Codec.BOOL.optionalFieldOf("require_near_fluid", false).forGetter(SpaceOreFeatureConfig::requireNearFluid),
            Codec.BOOL.optionalFieldOf("require_near_lava", false).forGetter(SpaceOreFeatureConfig::requireNearLava),
            Codec.BOOL.optionalFieldOf("cave_only", false).forGetter(SpaceOreFeatureConfig::caveOnly)
    ).apply(instance, SpaceOreFeatureConfig::new));
}
