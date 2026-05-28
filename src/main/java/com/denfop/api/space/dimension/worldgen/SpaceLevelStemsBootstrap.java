package com.denfop.api.space.dimension.worldgen;

import com.denfop.api.space.dimension.SpaceBodyCatalog;
import com.denfop.api.space.dimension.SpaceDimensionKeys;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SpaceLevelStemsBootstrap {

    private SpaceLevelStemsBootstrap() {
    }

    public static Map<ResourceLocation, LevelStem> build(final RegistryAccess registryAccess) {
        final Registry<DimensionType> dimensionTypes = registryAccess.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        final Registry<Biome> biomes = registryAccess.registryOrThrow(Registry.BIOME_REGISTRY);
        final Registry<NoiseGeneratorSettings> noiseSettings = registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
        final Registry<StructureSet> structureSets = registryAccess.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
        final Registry<NormalNoise.NoiseParameters> noises = registryAccess.registryOrThrow(Registry.NOISE_REGISTRY);

        final Map<ResourceLocation, LevelStem> entries = new LinkedHashMap<>();

        for (var body : SpaceBodyCatalog.allBodies()) {
            final LevelStem stem = new LevelStem(
                    dimensionTypes.getOrCreateHolderOrThrow(SpaceDimensionKeys.dimensionTypeKey(body.name())),
                    new NoiseBasedChunkGenerator(
                            structureSets,
                            noises,
                            new FixedBiomeSource(biomes.getOrCreateHolderOrThrow(SpaceDimensionKeys.biomeKey(body.name()))),
                            noiseSettings.getOrCreateHolderOrThrow(SpaceDimensionKeys.noiseSettingsKey(body.name()))
                    )
            );
            register(entries, SpaceDimensionKeys.levelStemKey(body.name()), stem);
        }

        return entries;
    }

    private static void register(
            final Map<ResourceLocation, LevelStem> entries,
            final ResourceKey<LevelStem> key,
            final LevelStem value
    ) {
        entries.put(key.location(), value);
    }
}
