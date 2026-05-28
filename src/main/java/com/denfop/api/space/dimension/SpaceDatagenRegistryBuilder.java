package com.denfop.api.space.dimension;


import com.denfop.api.space.dimension.worldgen.*;
import com.denfop.datagen.ConfiguredFeaturesGen;
import com.denfop.datagen.DamageTypes;
import com.denfop.datagen.ModPlacedFeatures;
import com.denfop.datagen.PaintingVariantProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class SpaceDatagenRegistryBuilder {

    private SpaceDatagenRegistryBuilder() {
    }

    public static RegistrySetBuilder createBuiltinBuilder() {
        SpaceBootstrap.ensureSpaceCatalogInitialized();
        return new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, SpaceDatagenRegistryBuilder::bootstrapConfigured)
                .add(Registries.PLACED_FEATURE, SpaceDatagenRegistryBuilder::bootstrapPlaced)
                .add(Registries.DAMAGE_TYPE, DamageTypes::bootstrap)
                .add(Registries.PAINTING_VARIANT, PaintingVariantProvider::bootstrap)
                .add(Registries.BIOME, SpaceBiomesBootstrap::bootstrap)
                .add(Registries.NOISE_SETTINGS, SpaceNoiseSettingsBootstrap::bootstrap)
                .add(Registries.DIMENSION_TYPE, SpaceDimensionTypesBootstrap::bootstrap)
                .add(Registries.LEVEL_STEM, SpaceLevelStemsBootstrap::bootstrap);
    }

    private static void bootstrapConfigured(final BootstrapContext<ConfiguredFeature<?, ?>> context) {
        ConfiguredFeaturesGen.bootstrap(context);
        SpaceConfiguredFeaturesBootstrap.bootstrap(context);
    }

    private static void bootstrapPlaced(final BootstrapContext<PlacedFeature> context) {
        ModPlacedFeatures.bootstrap(context);
        SpacePlacedFeaturesBootstrap.bootstrap(context);
    }
}
