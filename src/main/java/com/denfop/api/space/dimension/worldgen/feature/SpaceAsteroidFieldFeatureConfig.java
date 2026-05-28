package com.denfop.api.space.dimension.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record SpaceAsteroidFieldFeatureConfig(
        String bodyName,
        int horizontalCell,
        int verticalCell,
        int maxRadius,
        int minY,
        int maxY
) implements FeatureConfiguration {

    public static final Codec<SpaceAsteroidFieldFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("body_name").forGetter(SpaceAsteroidFieldFeatureConfig::bodyName),
            Codec.INT.optionalFieldOf("horizontal_cell", 64).forGetter(SpaceAsteroidFieldFeatureConfig::horizontalCell),
            Codec.INT.optionalFieldOf("vertical_cell", 40).forGetter(SpaceAsteroidFieldFeatureConfig::verticalCell),
            Codec.INT.optionalFieldOf("max_radius", 36).forGetter(SpaceAsteroidFieldFeatureConfig::maxRadius),
            Codec.INT.optionalFieldOf("min_y", -32).forGetter(SpaceAsteroidFieldFeatureConfig::minY),
            Codec.INT.optionalFieldOf("max_y", 288).forGetter(SpaceAsteroidFieldFeatureConfig::maxY)
    ).apply(instance, SpaceAsteroidFieldFeatureConfig::new));
}
