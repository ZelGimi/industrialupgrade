package com.denfop.api.space.dimension.worldgen;

import com.denfop.api.space.dimension.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class SpaceBiomesBootstrap {

    private SpaceBiomesBootstrap() {
    }

    public static void bootstrap(final BootstrapContext<Biome> context) {
        final HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        final HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        for (final SpaceBodyRef body : SpaceBodyCatalog.allBodies()) {
            final SpaceDimensionProfile profile = SpaceBodyProfiles.byBody(body);
            final SpaceGenerationMode mode = profile.generationMode();
            final String bodyName = body.name().toLowerCase();
            final BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placed, carvers);

            if (profile.generateAsteroidField()) {
                generation.addFeature(GenerationStep.Decoration.RAW_GENERATION, placed.getOrThrow(SpaceDimensionKeys.asteroidFieldPlacedKey(body.name())));

                final BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder()
                        .skyColor(rgb(profile.skyColor()))
                        .fogColor(rgb(profile.fogColor()))
                        .waterColor(profile.waterColor())
                        .waterFogColor(profile.waterFogColor())
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS);

                context.register(SpaceDimensionKeys.biomeKey(body.name()), new Biome.BiomeBuilder()
                        .temperature(profile.temperatureForBiome())
                        .downfall(0.0F)
                        .hasPrecipitation(false)
                        .specialEffects(effects.build())
                        .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                        .generationSettings(generation.build())
                        .build());
                continue;
            }

            if (profile.generateCraters()) {
                generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placed.getOrThrow(SpaceDimensionKeys.craterPlacedKey(body.name())));
            }
            generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placed.getOrThrow(SpaceDimensionKeys.craterAdditionPlacedKey(body.name())));

            if (profile.generateVolcanoes()) {
                generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placed.getOrThrow(SpaceDimensionKeys.volcanoPlacedKey(body.name())));
            }
            if (profile.generateCavities()) {
                generation.addFeature(GenerationStep.Decoration.RAW_GENERATION, placed.getOrThrow(SpaceDimensionKeys.cavityPlacedKey(body.name())));
            }
            if (profile.generateVerticalShafts()) {
                generation.addFeature(GenerationStep.Decoration.RAW_GENERATION, placed.getOrThrow(SpaceDimensionKeys.shaftPlacedKey(body.name())));
            }
            if (profile.generateLavaTubes()) {
                generation.addFeature(GenerationStep.Decoration.RAW_GENERATION, placed.getOrThrow(SpaceDimensionKeys.lavaTubePlacedKey(body.name())));
            }

            for (int i = 0; i < profile.ores().size(); i++) {
                generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(SpaceDimensionKeys.orePlacedKey(body.name(), i)));
            }
            for (int i = 0; i < profile.ores().size(); i++) {
                generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(SpaceDimensionKeys.orePlacedKey(body.name(), profile.ores().size() + i)));
            }

            for (int i = 0; i < profile.fluids().size(); i++) {
                generation.addFeature(GenerationStep.Decoration.LAKES, placed.getOrThrow(SpaceDimensionKeys.lakePlacedKey(body.name(), i)));
                if (profile.generateGeysers()) {
                    generation.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, placed.getOrThrow(SpaceDimensionKeys.geyserPlacedKey(body.name(), i)));
                }
                if (profile.fluids().get(i).ventType() != SpaceVentType.LAVA) {
                    generation.addFeature(GenerationStep.Decoration.LAKES, placed.getOrThrow(SpaceDimensionKeys.fluidPlacedKey(body.name(), i)));
                }
            }

            final boolean volcanic = isVolcanic(mode, profile);
            final boolean icy = isIcy(mode, profile);
            final boolean dusty = isDusty(mode, profile);
            final boolean cratered = isCratered(body, mode);
            final boolean atmosphereSoft = hasAtmosphereSoft(body, mode);

            final int tunnelCount = stableCount(bodyName, 1, 2, 5);
            final int hallCount = stableCount(bodyName, 2, 1, 3);
            final int fractureCount = stableCount(bodyName, 3, 1, 3);
            final int porousCount = stableCount(bodyName, 4, 0, 2);
            final int shaftCount = stableCount(bodyName, 5, 0, 2);
            final int chamberCount = stableCount(bodyName, 6, 1, 3);
            final int craterLinkedCount = cratered ? stableCount(bodyName, 7, 1, 2) : 0;
            final int liquidCount = (volcanic || icy) ? 1 : stableCount(bodyName, 8, 0, 1);

            final int tunnelCavesCount = stableCount(bodyName, 18, 1, 3);
            final int labyrinthCount = atmosphereSoft ? stableCount(bodyName, 19, 1, 2) : stableCount(bodyName, 19, 0, 1);
            final int shaftCavesCount = (cratered || volcanic) ? stableCount(bodyName, 20, 1, 3) : stableCount(bodyName, 20, 0, 2);
            final int domeCount = atmosphereSoft ? stableCount(bodyName, 21, 1, 3) : stableCount(bodyName, 21, 1, 2);
            final int wellCount = (cratered || icy) ? stableCount(bodyName, 22, 1, 2) : stableCount(bodyName, 22, 0, 1);
            final int multiTierCount = stableCount(bodyName, 23, 1, 2);
            final int branchedCount = stableCount(bodyName, 24, 1, 3);
            final int spiralCount = (volcanic || cratered) ? stableCount(bodyName, 25, 1, 2) : stableCount(bodyName, 25, 0, 1);
            final int archCavesCount = atmosphereSoft ? stableCount(bodyName, 26, 1, 2) : stableCount(bodyName, 26, 0, 1);
            final int chamberCavesCount = stableCount(bodyName, 27, 1, 3);
            final int canyonCount = (volcanic || dusty) ? stableCount(bodyName, 28, 1, 2) : stableCount(bodyName, 28, 0, 1);
            final int pocketCount = stableCount(bodyName, 29, 1, 3);
            final int creviceCount = (volcanic || cratered) ? stableCount(bodyName, 30, 1, 3) : stableCount(bodyName, 30, 1, 2);
            final int amphitheaterCount = atmosphereSoft ? stableCount(bodyName, 31, 1, 2) : stableCount(bodyName, 31, 0, 1);
            final int cathedralCount = (icy || atmosphereSoft) ? stableCount(bodyName, 32, 1, 2) : stableCount(bodyName, 32, 0, 1);
            final int galleryCount = stableCount(bodyName, 33, 1, 3);
            final int windingCount = stableCount(bodyName, 34, 1, 3);
            final int cascadeCount = (volcanic || icy) ? stableCount(bodyName, 35, 1, 2) : stableCount(bodyName, 35, 0, 1);
            final int collapseCount = (cratered || dusty) ? stableCount(bodyName, 36, 1, 2) : stableCount(bodyName, 36, 0, 1);
            final int collectorCount = (volcanic || cratered) ? stableCount(bodyName, 37, 1, 2) : stableCount(bodyName, 37, 0, 1);
            if (mode == SpaceGenerationMode.ICY_SHELL) {
                generation.addFeature(
                        GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                        placed.getOrThrow(SpaceDimensionKeys.placedKey(bodyName, "ice_surface_cover"))
                );
            }

            if (mode == SpaceGenerationMode.CRYO_CHEMICAL) {
                generation.addFeature(
                        GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                        placed.getOrThrow(SpaceDimensionKeys.placedKey(bodyName, "cryo_surface_cover"))
                );
            }
            addRawFeature(generation, placed, bodyName, "stone_tunnels", tunnelCount);
            addRawFeature(generation, placed, bodyName, "hall_system", hallCount);
            addRawFeature(generation, placed, bodyName, "fractures", fractureCount);
            addRawFeature(generation, placed, bodyName, "porous", porousCount);
            addRawFeature(generation, placed, bodyName, "shafts", shaftCount);
            addRawFeature(generation, placed, bodyName, "liquid_chamber", liquidCount);
            if (isVolcanic(mode, profile)) {
                generation.addFeature(
                        GenerationStep.Decoration.LAKES,
                        placed.getOrThrow(SpaceDimensionKeys.placedKey(bodyName, "lava_hydrology"))
                );
            }

            if (volcanic) {
                addRawFeature(generation, placed, bodyName, "volcanic_chamber", chamberCount);
            }
            if (icy) {
                addRawFeature(generation, placed, bodyName, "frozen_chamber", chamberCount);
            }
            if (volcanic || dusty) {
                addRawFeature(generation, placed, bodyName, "thermal_chamber", Math.max(1, chamberCount - 1));
            }
            if (cratered) {
                addRawFeature(generation, placed, bodyName, "crater_chamber", craterLinkedCount);
            }

            addRawFeature(generation, placed, bodyName, "tunnel_caves", tunnelCavesCount);
            addRawFeature(generation, placed, bodyName, "labyrinth_caves", labyrinthCount);
            addRawFeature(generation, placed, bodyName, "shaft_caves", shaftCavesCount);
            addRawFeature(generation, placed, bodyName, "dome_caves", domeCount);
            addRawFeature(generation, placed, bodyName, "well_caves", wellCount);
            addRawFeature(generation, placed, bodyName, "multi_tier_caves", multiTierCount);
            addRawFeature(generation, placed, bodyName, "branched_caves", branchedCount);
            addRawFeature(generation, placed, bodyName, "spiral_caves", spiralCount);
            addRawFeature(generation, placed, bodyName, "arch_caves", archCavesCount);
            addRawFeature(generation, placed, bodyName, "chamber_caves", chamberCavesCount);
            addRawFeature(generation, placed, bodyName, "canyon_caves", canyonCount);
            addRawFeature(generation, placed, bodyName, "pocket_caves", pocketCount);
            addRawFeature(generation, placed, bodyName, "crevice_caves", creviceCount);
            addRawFeature(generation, placed, bodyName, "amphitheater_caves", amphitheaterCount);
            addRawFeature(generation, placed, bodyName, "cathedral_caves", cathedralCount);
            addRawFeature(generation, placed, bodyName, "gallery_caves", galleryCount);
            addRawFeature(generation, placed, bodyName, "winding_caves", windingCount);
            addRawFeature(generation, placed, bodyName, "cascade_caves", cascadeCount);
            addRawFeature(generation, placed, bodyName, "collapse_caves", collapseCount);
            addRawFeature(generation, placed, bodyName, "vertical_collectors", collectorCount);

            final BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder()
                    .skyColor(rgb(profile.skyColor()))
                    .fogColor(rgb(profile.fogColor()))
                    .waterColor(profile.waterColor())
                    .waterFogColor(profile.waterFogColor())
                    .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS);

            if (profile.ambientParticle() != null) {
                effects.ambientParticle(profile.ambientParticle());
            }
            if (profile.music() != null) {
                effects.backgroundMusic(profile.music());
            }

            context.register(SpaceDimensionKeys.biomeKey(body.name()), new Biome.BiomeBuilder()
                    .temperature(profile.temperatureForBiome())
                    .downfall(profile.downfall())
                    .hasPrecipitation(profile.hasAtmosphere())
                    .specialEffects(effects.build())
                    .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                    .generationSettings(generation.build())
                    .build());
        }
    }

    private static void addRawFeature(
            final BiomeGenerationSettings.Builder generation,
            final HolderGetter<PlacedFeature> placed,
            final String bodyName,
            final String suffix,
            final int count
    ) {
        if (count <= 0) {
            return;
        }

        generation.addFeature(GenerationStep.Decoration.RAW_GENERATION, placed.getOrThrow(SpaceDimensionKeys.placedKey(bodyName, suffix)));
    }

    private static int stableCount(final String bodyName, final int salt, final int min, final int max) {
        final int hash = Math.abs((bodyName + "#" + salt).hashCode());
        return min + Math.floorMod(hash, (max - min) + 1);
    }

    private static boolean isVolcanic(final SpaceGenerationMode mode, final SpaceDimensionProfile profile) {
        return mode == SpaceGenerationMode.VOLCANIC
                || profile.defaultBlock().is(net.minecraft.world.level.block.Blocks.BLACKSTONE)
                || profile.defaultBlock().is(net.minecraft.world.level.block.Blocks.BASALT)
                || profile.topBlock().is(net.minecraft.world.level.block.Blocks.MAGMA_BLOCK);
    }

    private static boolean isIcy(final SpaceGenerationMode mode, final SpaceDimensionProfile profile) {
        return mode == SpaceGenerationMode.ICY_SHELL
                || mode == SpaceGenerationMode.CRYO_CHEMICAL
                || profile.defaultBlock().is(net.minecraft.world.level.block.Blocks.PACKED_ICE)
                || profile.defaultBlock().is(net.minecraft.world.level.block.Blocks.BLUE_ICE)
                || profile.topBlock().is(net.minecraft.world.level.block.Blocks.PACKED_ICE)
                || profile.topBlock().is(net.minecraft.world.level.block.Blocks.SNOW_BLOCK);
    }

    private static boolean isDusty(final SpaceGenerationMode mode, final SpaceDimensionProfile profile) {
        return mode == SpaceGenerationMode.DUSTY_DESERT
                || profile.topBlock().is(net.minecraft.world.level.block.Blocks.RED_SAND)
                || profile.topBlock().is(net.minecraft.world.level.block.Blocks.RED_SANDSTONE)
                || profile.topBlock().is(net.minecraft.world.level.block.Blocks.SANDSTONE)
                || profile.topBlock().is(net.minecraft.world.level.block.Blocks.TERRACOTTA);
    }

    private static boolean isCratered(final SpaceBodyRef body, final SpaceGenerationMode mode) {
        return body.isSatellite()
                || mode == SpaceGenerationMode.AIRLESS_BARREN
                || mode == SpaceGenerationMode.AIRLESS_CRATERED
                || mode == SpaceGenerationMode.ASTEROID_FIELD;
    }

    private static boolean hasAtmosphereSoft(final SpaceBodyRef body, final SpaceGenerationMode mode) {
        return !body.isSatellite()
                && mode != SpaceGenerationMode.AIRLESS_BARREN
                && mode != SpaceGenerationMode.AIRLESS_CRATERED
                && mode != SpaceGenerationMode.ASTEROID_FIELD;
    }

    private static int rgb(final net.minecraft.world.phys.Vec3 color) {
        final int r = (int) Math.round(color.x * 255.0D);
        final int g = (int) Math.round(color.y * 255.0D);
        final int b = (int) Math.round(color.z * 255.0D);
        return (r << 16) | (g << 8) | b;
    }
}
