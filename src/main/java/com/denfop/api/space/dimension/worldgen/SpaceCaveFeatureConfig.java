package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record SpaceCaveFeatureConfig(
        SpaceCaveKind kind,
        BlockState primary,
        BlockState secondary,
        BlockState tertiary,
        BlockState fluid,
        int minRadius,
        int maxRadius,
        int minLength,
        int maxLength,
        int maxVertical,
        boolean openToSurface,
        boolean flooded
) implements FeatureConfiguration {

    public static final Codec<SpaceCaveFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            StringRepresentableCodec.of(SpaceCaveKind.values()).fieldOf("kind").forGetter(SpaceCaveFeatureConfig::kind),
            BlockState.CODEC.fieldOf("primary").forGetter(SpaceCaveFeatureConfig::primary),
            BlockState.CODEC.fieldOf("secondary").forGetter(SpaceCaveFeatureConfig::secondary),
            BlockState.CODEC.fieldOf("tertiary").forGetter(SpaceCaveFeatureConfig::tertiary),
            BlockState.CODEC.fieldOf("fluid").forGetter(SpaceCaveFeatureConfig::fluid),
            Codec.INT.fieldOf("min_radius").forGetter(SpaceCaveFeatureConfig::minRadius),
            Codec.INT.fieldOf("max_radius").forGetter(SpaceCaveFeatureConfig::maxRadius),
            Codec.INT.fieldOf("min_length").forGetter(SpaceCaveFeatureConfig::minLength),
            Codec.INT.fieldOf("max_length").forGetter(SpaceCaveFeatureConfig::maxLength),
            Codec.INT.fieldOf("max_vertical").forGetter(SpaceCaveFeatureConfig::maxVertical),
            Codec.BOOL.fieldOf("open_to_surface").forGetter(SpaceCaveFeatureConfig::openToSurface),
            Codec.BOOL.fieldOf("flooded").forGetter(SpaceCaveFeatureConfig::flooded)
    ).apply(instance, SpaceCaveFeatureConfig::new));
}