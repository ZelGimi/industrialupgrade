package com.denfop.api.space.dimension;

import com.denfop.IUCore;
import com.denfop.api.space.dimension.worldgen.*;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Map;

public final class SpaceDatagenRegistryBuilder {

    private SpaceDatagenRegistryBuilder() {
    }

    public static void addDataProviders(final GatherDataEvent event) {
        SpaceBootstrap.ensureSpaceCatalogInitialized();
        SpaceWorldgenContent.init();

        final var generator = event.getGenerator();
        final var existingFileHelper = event.getExistingFileHelper();
        final RegistryAccess registryAccess = RegistryAccess.builtinCopy();

        final Map<ResourceLocation, ConfiguredFeature<?, ?>> configuredEntries = SpaceConfiguredFeaturesBootstrap.build();
        bindEntriesIfPresent(registryAccess, Registry.CONFIGURED_FEATURE_REGISTRY, configuredEntries);

        final Map<ResourceLocation, PlacedFeature> placedEntries = SpacePlacedFeaturesBootstrap.build(registryAccess);
        bindEntriesIfPresent(registryAccess, Registry.PLACED_FEATURE_REGISTRY, placedEntries);

        final Map<ResourceLocation, Biome> biomeEntries = SpaceBiomesBootstrap.build(registryAccess);
        bindEntriesIfPresent(registryAccess, Registry.BIOME_REGISTRY, biomeEntries);

        final Map<ResourceLocation, NoiseGeneratorSettings> noiseEntries = SpaceNoiseSettingsBootstrap.build(registryAccess);
        bindEntriesIfPresent(registryAccess, Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, noiseEntries);

        final Map<ResourceLocation, DimensionType> dimensionTypeEntries = SpaceDimensionTypesBootstrap.build();
        bindEntriesIfPresent(registryAccess, Registry.DIMENSION_TYPE_REGISTRY, dimensionTypeEntries);

        final Map<ResourceLocation, LevelStem> levelStemEntries = SpaceLevelStemsBootstrap.build(registryAccess);

        final RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, registryAccess);

        generator.addProvider(
                event.includeServer(),
                JsonCodecProvider.forDatapackRegistry(
                        generator,
                        existingFileHelper,
                        IUCore.MODID,
                        registryOps,
                        Registry.CONFIGURED_FEATURE_REGISTRY,
                        configuredEntries
                )
        );

        generator.addProvider(
                event.includeServer(),
                JsonCodecProvider.forDatapackRegistry(
                        generator,
                        existingFileHelper,
                        IUCore.MODID,
                        registryOps,
                        Registry.PLACED_FEATURE_REGISTRY,
                        placedEntries
                )
        );

        generator.addProvider(
                event.includeServer(),
                JsonCodecProvider.forDatapackRegistry(
                        generator,
                        existingFileHelper,
                        IUCore.MODID,
                        registryOps,
                        Registry.BIOME_REGISTRY,
                        biomeEntries
                )
        );

        generator.addProvider(
                event.includeServer(),
                JsonCodecProvider.forDatapackRegistry(
                        generator,
                        existingFileHelper,
                        IUCore.MODID,
                        registryOps,
                        Registry.NOISE_GENERATOR_SETTINGS_REGISTRY,
                        noiseEntries
                )
        );

        generator.addProvider(
                event.includeServer(),
                JsonCodecProvider.forDatapackRegistry(
                        generator,
                        existingFileHelper,
                        IUCore.MODID,
                        registryOps,
                        Registry.DIMENSION_TYPE_REGISTRY,
                        dimensionTypeEntries
                )
        );


        generator.addProvider(
                event.includeServer(),
                new SpaceLevelStemProvider(generator, IUCore.MODID, registryOps, levelStemEntries)
        );
    }

    private static <T> void bindEntriesIfPresent(
            final RegistryAccess registryAccess,
            final net.minecraft.resources.ResourceKey<? extends Registry<T>> registryKey,
            final Map<ResourceLocation, T> entries
    ) {
        final var registryOptional = registryAccess.registry(registryKey);
        if (registryOptional.isEmpty()) {
            return;
        }

        final Registry<T> registry = registryOptional.get();
        for (final Map.Entry<ResourceLocation, T> entry : entries.entrySet()) {
            if (!registry.containsKey(entry.getKey())) {
                Registry.register(registry, entry.getKey(), entry.getValue());
            }
        }
    }
}
