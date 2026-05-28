package com.denfop.api.space.dimension.worldgen;

import com.denfop.api.space.dimension.SpaceBodyCatalog;
import com.denfop.api.space.dimension.SpaceDimensionKeys;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public final class SpaceLevelStemsBootstrap {

    private SpaceLevelStemsBootstrap() {
    }

    public static void bootstrap(final BootstrapContext<LevelStem> context) {
        HolderGetter<DimensionType> types = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<NoiseGeneratorSettings> noise = context.lookup(Registries.NOISE_SETTINGS);

        for (var body : SpaceBodyCatalog.allBodies()) {
            context.register(SpaceDimensionKeys.levelStemKey(body.name()), new LevelStem(
                    types.getOrThrow(SpaceDimensionKeys.dimensionTypeKey(body.name())),
                    new NoiseBasedChunkGenerator(
                            new FixedBiomeSource(biomes.getOrThrow(SpaceDimensionKeys.biomeKey(body.name()))),
                            noise.getOrThrow(SpaceDimensionKeys.noiseSettingsKey(body.name()))
                    )
            ));
        }
    }
}
