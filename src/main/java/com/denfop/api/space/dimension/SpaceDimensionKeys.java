package com.denfop.api.space.dimension;

import com.denfop.IUCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Locale;

public final class SpaceDimensionKeys {

    private SpaceDimensionKeys() {
    }

    public static ResourceLocation bodyId(final String bodyName) {
        return ResourceLocation.tryBuild(IUCore.MODID, bodyName.toLowerCase(Locale.ROOT));
    }

    public static ResourceLocation effectsLocation(final String bodyName) {
        return bodyId(bodyName);
    }

    public static ResourceKey<Level> levelKey(final String bodyName) {
        return ResourceKey.create(Registries.DIMENSION, bodyId(bodyName));
    }

    public static ResourceKey<LevelStem> levelStemKey(final String bodyName) {
        return ResourceKey.create(Registries.LEVEL_STEM, bodyId(bodyName));
    }

    public static ResourceKey<DimensionType> dimensionTypeKey(final String bodyName) {
        return ResourceKey.create(
                Registries.DIMENSION_TYPE,
                ResourceLocation.tryBuild(IUCore.MODID, bodyName.toLowerCase(Locale.ROOT) + "_type")
        );
    }

    public static ResourceKey<Biome> biomeKey(final String bodyName) {
        return ResourceKey.create(
                Registries.BIOME,
                ResourceLocation.tryBuild(IUCore.MODID, bodyName.toLowerCase(Locale.ROOT) + "_biome")
        );
    }

    public static ResourceKey<NoiseGeneratorSettings> noiseSettingsKey(final String bodyName) {
        return ResourceKey.create(
                Registries.NOISE_SETTINGS,
                ResourceLocation.tryBuild(IUCore.MODID, bodyName.toLowerCase(Locale.ROOT) + "_noise")
        );
    }

    /*
     * LEGACY KEYS
     */
    public static ResourceKey<ConfiguredFeature<?, ?>> craterConfiguredKey(final String bodyName) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                ResourceLocation.tryBuild(IUCore.MODID, bodyName.toLowerCase(Locale.ROOT) + "_craters")
        );
    }

    public static ResourceKey<PlacedFeature> craterPlacedKey(final String bodyName) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                ResourceLocation.tryBuild(IUCore.MODID, bodyName.toLowerCase(Locale.ROOT) + "_craters")
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> craterAdditionConfiguredKey(final String bodyName) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                ResourceLocation.tryBuild(IUCore.MODID, "space/" + bodyName + "/addition_craters")
        );
    }

    public static ResourceKey<PlacedFeature> craterAdditionPlacedKey(final String bodyName) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                ResourceLocation.tryBuild(IUCore.MODID, "space/" + bodyName + "/addition_craters")
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> volcanoConfiguredKey(final String body) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                bodyId(body + "_volcano")
        );
    }

    public static ResourceKey<PlacedFeature> volcanoPlacedKey(final String body) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                bodyId(body + "_volcano")
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> lakeConfiguredKey(final String body, final int index) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                bodyId(body + "_lake_" + index)
        );
    }

    public static ResourceKey<PlacedFeature> lakePlacedKey(final String body, final int index) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                bodyId(body + "_lake_" + index)
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> geyserConfiguredKey(final String body, final int index) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                bodyId(body + "_geyser_" + index)
        );
    }

    public static ResourceKey<PlacedFeature> geyserPlacedKey(final String body, final int index) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                bodyId(body + "_geyser_" + index)
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> cavityConfiguredKey(final String body) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                bodyId(body + "_cavity")
        );
    }

    public static ResourceKey<PlacedFeature> cavityPlacedKey(final String body) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                bodyId(body + "_cavity")
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> shaftConfiguredKey(final String body) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                bodyId(body + "_shaft")
        );
    }

    public static ResourceKey<PlacedFeature> shaftPlacedKey(final String body) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                bodyId(body + "_shaft")
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> lavaTubeConfiguredKey(final String body) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                bodyId(body + "_lava_tube")
        );
    }

    public static ResourceKey<PlacedFeature> lavaTubePlacedKey(final String body) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                bodyId(body + "_lava_tube")
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> oreConfiguredKey(final String bodyName, final int index) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                ResourceLocation.tryBuild(IUCore.MODID, bodyName.toLowerCase(Locale.ROOT) + "_ore_" + index)
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> fluidConfiguredKey(final String bodyName, final int index) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                ResourceLocation.tryBuild(IUCore.MODID, bodyName.toLowerCase(Locale.ROOT) + "_fluid_" + index)
        );
    }

    public static ResourceKey<PlacedFeature> orePlacedKey(final String bodyName, final int index) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                ResourceLocation.tryBuild(IUCore.MODID, bodyName.toLowerCase(Locale.ROOT) + "_ore_" + index)
        );
    }

    public static ResourceKey<PlacedFeature> fluidPlacedKey(final String bodyName, final int index) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                ResourceLocation.tryBuild(IUCore.MODID, bodyName.toLowerCase(Locale.ROOT) + "_fluid_" + index)
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> configuredKey(final String bodyName, final String suffix) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                ResourceLocation.tryBuild(
                        IUCore.MODID,
                        "space/" + bodyName.toLowerCase(Locale.ROOT) + "/" + suffix.toLowerCase(Locale.ROOT)
                )
        );
    }

    public static ResourceKey<PlacedFeature> placedKey(final String bodyName, final String suffix) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                ResourceLocation.tryBuild(
                        IUCore.MODID,
                        "space/" + bodyName.toLowerCase(Locale.ROOT) + "/" + suffix.toLowerCase(Locale.ROOT)
                )
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> asteroidFieldConfiguredKey(final String bodyName) {
        return configuredKey(bodyName, "asteroid_field");
    }

    public static ResourceKey<PlacedFeature> asteroidFieldPlacedKey(final String bodyName) {
        return placedKey(bodyName, "asteroid_field");
    }
}
