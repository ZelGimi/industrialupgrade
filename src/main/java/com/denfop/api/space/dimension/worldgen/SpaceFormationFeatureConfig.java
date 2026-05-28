package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record SpaceFormationFeatureConfig(
        SpaceFormationKind kind,
        BlockState primary,
        BlockState secondary,
        BlockState tertiary,
        BlockState fluid,
        int minRadius,
        int maxRadius,
        int minHeight,
        int maxHeight,
        int branches
) implements FeatureConfiguration {

    public static final Codec<SpaceFormationFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            StringRepresentableCodec.of(SpaceFormationKind.values()).fieldOf("kind").forGetter(SpaceFormationFeatureConfig::kind),
            BlockState.CODEC.fieldOf("primary").forGetter(SpaceFormationFeatureConfig::primary),
            BlockState.CODEC.fieldOf("secondary").forGetter(SpaceFormationFeatureConfig::secondary),
            BlockState.CODEC.fieldOf("tertiary").forGetter(SpaceFormationFeatureConfig::tertiary),
            BlockState.CODEC.fieldOf("fluid").forGetter(SpaceFormationFeatureConfig::fluid),
            Codec.INT.fieldOf("min_radius").forGetter(SpaceFormationFeatureConfig::minRadius),
            Codec.INT.fieldOf("max_radius").forGetter(SpaceFormationFeatureConfig::maxRadius),
            Codec.INT.fieldOf("min_height").forGetter(SpaceFormationFeatureConfig::minHeight),
            Codec.INT.fieldOf("max_height").forGetter(SpaceFormationFeatureConfig::maxHeight),
            Codec.INT.fieldOf("branches").forGetter(SpaceFormationFeatureConfig::branches)
    ).apply(instance, SpaceFormationFeatureConfig::new));
}