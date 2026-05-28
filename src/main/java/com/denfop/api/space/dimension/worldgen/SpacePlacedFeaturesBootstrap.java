package com.denfop.api.space.dimension.worldgen;

import com.denfop.api.space.dimension.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class SpacePlacedFeaturesBootstrap {

    private SpacePlacedFeaturesBootstrap() {
    }

    public static void bootstrap(final BootstrapContext<PlacedFeature> context) {
        final HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);

        for (final SpaceBodyRef body : SpaceBodyCatalog.allBodies()) {
            final SpaceDimensionProfile profile = SpaceBodyProfiles.byBody(body);
            final SpaceGenerationMode mode = profile.generationMode();
            final String bodyName = body.name().toLowerCase(Locale.ROOT);

            if (profile.generateAsteroidField()) {
                context.register(
                        SpaceDimensionKeys.asteroidFieldPlacedKey(body.name()),
                        new PlacedFeature(
                                configured.getOrThrow(SpaceDimensionKeys.asteroidFieldConfiguredKey(body.name())),
                                List.of(CountPlacement.of(1), InSquarePlacement.spread(), BiomeFilter.biome())
                        )
                );
                continue;
            }

            if (profile.generateCraters()) {
                context.register(
                        SpaceDimensionKeys.craterPlacedKey(body.name()),
                        new PlacedFeature(
                                configured.getOrThrow(SpaceDimensionKeys.craterConfiguredKey(body.name())),
                                List.of(
                                        RarityFilter.onAverageOnceEvery(Math.max(profile.craterRarity(), 42)),
                                        InSquarePlacement.spread(),
                                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                        BiomeFilter.biome()
                                )
                        )
                );
            }

            if (profile.generateVolcanoes()) {
                context.register(
                        SpaceDimensionKeys.volcanoPlacedKey(body.name()),
                        new PlacedFeature(
                                configured.getOrThrow(SpaceDimensionKeys.volcanoConfiguredKey(body.name())),
                                List.of(
                                        RarityFilter.onAverageOnceEvery(Math.max(profile.volcanoRarity(), 72)),
                                        InSquarePlacement.spread(),
                                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                        BiomeFilter.biome()
                                )
                        )
                );

            }

            if (profile.generateCavities()) {
                context.register(
                        SpaceDimensionKeys.cavityPlacedKey(body.name()),
                        new PlacedFeature(
                                configured.getOrThrow(SpaceDimensionKeys.cavityConfiguredKey(body.name())),
                                List.of(
                                        RarityFilter.onAverageOnceEvery(profile.cavityRarity()),
                                        InSquarePlacement.spread(),
                                        HeightRangePlacement.uniform(
                                                VerticalAnchor.absolute(-56),
                                                VerticalAnchor.absolute(72)
                                        ),
                                        BiomeFilter.biome()
                                )
                        )
                );
            }

            if (profile.generateVerticalShafts()) {
                context.register(
                        SpaceDimensionKeys.shaftPlacedKey(body.name()),
                        new PlacedFeature(
                                configured.getOrThrow(SpaceDimensionKeys.shaftConfiguredKey(body.name())),
                                List.of(
                                        RarityFilter.onAverageOnceEvery(profile.shaftRarity()),
                                        InSquarePlacement.spread(),
                                        BiomeFilter.biome()
                                )
                        )
                );
            }

            if (profile.generateLavaTubes()) {
                context.register(
                        SpaceDimensionKeys.lavaTubePlacedKey(body.name()),
                        new PlacedFeature(
                                configured.getOrThrow(SpaceDimensionKeys.lavaTubeConfiguredKey(body.name())),
                                List.of(
                                        RarityFilter.onAverageOnceEvery(profile.lavaTubeRarity()),
                                        InSquarePlacement.spread(),
                                        HeightRangePlacement.uniform(
                                                VerticalAnchor.absolute(-40),
                                                VerticalAnchor.absolute(24)
                                        ),
                                        BiomeFilter.biome()
                                )
                        )
                );
            }

            int fluidIndex = 0;
            for (final SpaceFluidPocket fluid : profile.fluids()) {
                context.register(
                        SpaceDimensionKeys.lakePlacedKey(body.name(), fluidIndex),
                        new PlacedFeature(
                                configured.getOrThrow(SpaceDimensionKeys.lakeConfiguredKey(body.name(), fluidIndex)),
                                List.of(
                                        RarityFilter.onAverageOnceEvery(lakeRarity(fluid)),
                                        InSquarePlacement.spread(),
                                        HeightRangePlacement.uniform(
                                                VerticalAnchor.absolute(fluid.minY()),
                                                VerticalAnchor.absolute(fluid.maxY())
                                        ),
                                        BiomeFilter.biome()
                                )
                        )
                );

                context.register(
                        SpaceDimensionKeys.geyserPlacedKey(body.name(), fluidIndex),
                        new PlacedFeature(
                                configured.getOrThrow(SpaceDimensionKeys.geyserConfiguredKey(body.name(), fluidIndex)),
                                List.of(
                                        RarityFilter.onAverageOnceEvery(geyserRarity(fluid)),
                                        InSquarePlacement.spread(),
                                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                        BiomeFilter.biome()
                                )
                        )
                );

                fluidIndex++;
            }
            context.register(
                    SpaceDimensionKeys.craterAdditionPlacedKey(body.name()),
                    new PlacedFeature(
                            configured.getOrThrow(SpaceDimensionKeys.craterAdditionConfiguredKey(body.name())),
                            List.of(
                                    CountPlacement.of(1),
                                    InSquarePlacement.spread(),
                                    PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                                    BiomeFilter.biome()
                            )
                    )
            );
            int oreIndex = 0;
            for (final SpaceOreEntry ore : profile.ores()) {
                context.register(
                        SpaceDimensionKeys.orePlacedKey(body.name(), oreIndex),
                        new PlacedFeature(
                                configured.getOrThrow(SpaceDimensionKeys.oreConfiguredKey(body.name(), oreIndex)),
                                List.of(
                                        CountPlacement.of(Math.max(1, 1 + ore.weight() / 45)),
                                        InSquarePlacement.spread(),
                                        HeightRangePlacement.uniform(
                                                VerticalAnchor.absolute(ore.minY()),
                                                VerticalAnchor.absolute(ore.maxY())
                                        ),
                                        BiomeFilter.biome()
                                )
                        )
                );
                oreIndex++;
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

            final int pillarCount = stableCount(bodyName, 9, 1, 3);
            final int archCount = atmosphereSoft ? stableCount(bodyName, 10, 1, 2) : stableCount(bodyName, 10, 0, 1);
            final int plateauCount = atmosphereSoft ? stableCount(bodyName, 11, 1, 2) : 0;
            final int spireCount = (volcanic || cratered) ? stableCount(bodyName, 12, 1, 3) : stableCount(bodyName, 12, 0, 2);
            final int debrisCount = (cratered || dusty) ? stableCount(bodyName, 13, 2, 5) : stableCount(bodyName, 13, 1, 3);
            final int volcanicFieldCount = volcanic ? stableCount(bodyName, 14, 1, 3) : 0;
            final int craterCount = cratered ? stableCount(bodyName, 15, 2, 5) : 0;
            final int riftCount = (volcanic || dusty) ? stableCount(bodyName, 16, 1, 3) : stableCount(bodyName, 16, 0, 1);
            final int crystalCount = icy
                    ? stableCount(bodyName, 17, 2, 5)
                    : (mode == SpaceGenerationMode.CRYO_CHEMICAL ? stableCount(bodyName, 17, 1, 3) : 0);

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

            registerUnderground(context, configured, bodyName, "stone_tunnels", tunnelCount, -48, 96);
            registerUnderground(context, configured, bodyName, "hall_system", hallCount, -56, 72);
            registerUnderground(context, configured, bodyName, "fractures", fractureCount, -64, 90);
            registerUnderground(context, configured, bodyName, "porous", porousCount, -60, 48);
            registerUnderground(context, configured, bodyName, "shafts", shaftCount, -40, 120);
            registerUnderground(context, configured, bodyName, "liquid_chamber", liquidCount, -50, 28);

            registerUnderground(context, configured, bodyName, "tunnel_caves", tunnelCavesCount, -56, 80);
            registerUnderground(context, configured, bodyName, "labyrinth_caves", labyrinthCount, -48, 56);
            registerUnderground(context, configured, bodyName, "shaft_caves", shaftCavesCount, -24, 120);
            registerUnderground(context, configured, bodyName, "dome_caves", domeCount, -60, 44);
            registerUnderground(context, configured, bodyName, "well_caves", wellCount, -16, 96);
            registerUnderground(context, configured, bodyName, "multi_tier_caves", multiTierCount, -56, 52);
            registerUnderground(context, configured, bodyName, "branched_caves", branchedCount, -52, 68);
            registerUnderground(context, configured, bodyName, "spiral_caves", spiralCount, -60, 48);
            registerUnderground(context, configured, bodyName, "arch_caves", archCavesCount, -48, 54);
            registerUnderground(context, configured, bodyName, "chamber_caves", chamberCavesCount, -60, 40);
            registerUnderground(context, configured, bodyName, "canyon_caves", canyonCount, -64, 72);
            registerUnderground(context, configured, bodyName, "pocket_caves", pocketCount, -62, 48);
            registerUnderground(context, configured, bodyName, "crevice_caves", creviceCount, -48, 96);
            registerUnderground(context, configured, bodyName, "amphitheater_caves", amphitheaterCount, -32, 72);
            registerUnderground(context, configured, bodyName, "cathedral_caves", cathedralCount, -56, 36);
            registerUnderground(context, configured, bodyName, "gallery_caves", galleryCount, -50, 60);
            registerUnderground(context, configured, bodyName, "winding_caves", windingCount, -60, 72);
            registerUnderground(context, configured, bodyName, "cascade_caves", cascadeCount, -64, 28);
            registerUnderground(context, configured, bodyName, "collapse_caves", collapseCount, -8, 110);
            registerUnderground(context, configured, bodyName, "vertical_collectors", collectorCount, -40, 96);

            if (volcanic) {
                registerUnderground(context, configured, bodyName, "volcanic_chamber", chamberCount, -48, 36);
                registerSurface(context, configured, bodyName, "volcanic_fields", volcanicFieldCount);
                registerRegionalSurface(context, configured, bodyName, "lava_hydrology");

            }

            if (icy) {
                registerUnderground(context, configured, bodyName, "frozen_chamber", chamberCount, -56, 40);
                registerSurface(context, configured, bodyName, "crystals", crystalCount);
            }

            if (volcanic || dusty) {
                registerUnderground(context, configured, bodyName, "thermal_chamber", Math.max(1, chamberCount - 1), -48, 32);

            }

            if (cratered) {
                registerUnderground(context, configured, bodyName, "crater_chamber", craterLinkedCount, -32, 40);
                registerSurface(context, configured, bodyName, "craters", craterCount);
            }

            registerSurface(context, configured, bodyName, "pillars", pillarCount);
            registerSurface(context, configured, bodyName, "arches", archCount);
            registerSurface(context, configured, bodyName, "plateaus", plateauCount);
            registerSurface(context, configured, bodyName, "spires", spireCount);
            registerSurface(context, configured, bodyName, "debris", debrisCount);
            registerSurface(context, configured, bodyName, "surface_rifts", riftCount);
            registerBodyOres(context, configured, profile, oreIndex);
            registerBodyFluids(context, configured, profile);

            if (mode == SpaceGenerationMode.ICY_SHELL) {
                registerTopLayer(context, configured, bodyName, "ice_surface_cover");
            }

            if (mode == SpaceGenerationMode.CRYO_CHEMICAL) {
                registerTopLayer(context, configured, bodyName, "cryo_surface_cover");
            }

        }
    }

    private static void registerTopLayer(
            final BootstrapContext<PlacedFeature> context,
            final HolderGetter<ConfiguredFeature<?, ?>> configured,
            final String bodyName,
            final String suffix
    ) {
        context.register(
                SpaceDimensionKeys.placedKey(bodyName, suffix),
                new PlacedFeature(
                        configured.getOrThrow(SpaceDimensionKeys.configuredKey(bodyName, suffix)),
                        List.of(
                                CountPlacement.of(1),
                                InSquarePlacement.spread(),
                                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                BiomeFilter.biome()
                        )
                )
        );
    }

    private static void registerBodyOres(
            final BootstrapContext<PlacedFeature> context,
            final HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures,
            final SpaceDimensionProfile body,
            int oreIndex
    ) {
        for (final SpaceOreEntry entry : body.ores()) {
            final List<net.minecraft.world.level.levelgen.placement.PlacementModifier> modifiers = new ArrayList<>();
            modifiers.add(CountPlacement.of(Math.max(1, entry.weight())));
            modifiers.add(InSquarePlacement.spread());
            modifiers.add(HeightRangePlacement.uniform(
                    VerticalAnchor.absolute(entry.minY()),
                    VerticalAnchor.absolute(entry.maxY())
            ));
            modifiers.add(BiomeFilter.biome());

            context.register(
                    SpaceDimensionKeys.orePlacedKey(body.body().name(), oreIndex),
                    new PlacedFeature(
                            configuredFeatures.getOrThrow(SpaceDimensionKeys.oreConfiguredKey(body.body().name(), oreIndex)),
                            List.copyOf(modifiers)
                    )
            );
            oreIndex++;
        }
    }

    private static void registerBodyFluids(
            final BootstrapContext<PlacedFeature> context,
            final HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures,
            final SpaceDimensionProfile body
    ) {
        int fluidIndex = 0;
        for (final SpaceFluidPocket pocket : body.fluids()) {
            if (pocket.ventType() == SpaceVentType.LAVA) {
                continue;
            }
            final List<net.minecraft.world.level.levelgen.placement.PlacementModifier> modifiers = new ArrayList<>();
            modifiers.add(CountPlacement.of(Math.max(1, pocket.weight())));
            modifiers.add(InSquarePlacement.spread());

            if (pocket.surfaceLake()) {
                modifiers.add(HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG));
            } else {
                modifiers.add(HeightRangePlacement.uniform(
                        VerticalAnchor.absolute(pocket.minY()),
                        VerticalAnchor.absolute(pocket.maxY())
                ));
            }

            modifiers.add(BiomeFilter.biome());

            context.register(
                    SpaceDimensionKeys.fluidPlacedKey(body.body().name(), fluidIndex),
                    new PlacedFeature(
                            configuredFeatures.getOrThrow(SpaceDimensionKeys.fluidConfiguredKey(body.body().name(), fluidIndex)),
                            List.copyOf(modifiers)
                    )
            );
            fluidIndex++;
        }
    }

    private static void registerUnderground(
            final BootstrapContext<PlacedFeature> context,
            final HolderGetter<ConfiguredFeature<?, ?>> configured,
            final String bodyName,
            final String suffix,
            final int count,
            final int minY,
            final int maxY
    ) {
        if (count <= 0) {
            return;
        }

        context.register(
                SpaceDimensionKeys.placedKey(bodyName, suffix),
                new PlacedFeature(
                        configured.getOrThrow(SpaceDimensionKeys.configuredKey(bodyName, suffix)),
                        List.of(
                                CountPlacement.of(count),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(minY),
                                        VerticalAnchor.absolute(maxY)
                                ),
                                BiomeFilter.biome()
                        )
                )
        );
    }

    private static void registerRegionalSurface(
            final BootstrapContext<PlacedFeature> context,
            final HolderGetter<ConfiguredFeature<?, ?>> configured,
            final String bodyName,
            final String suffix
    ) {
        context.register(
                SpaceDimensionKeys.placedKey(bodyName, suffix),
                new PlacedFeature(
                        configured.getOrThrow(SpaceDimensionKeys.configuredKey(bodyName, suffix)),
                        List.of(
                                CountPlacement.of(1),
                                InSquarePlacement.spread(),
                                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                BiomeFilter.biome()
                        )
                )
        );
    }

    private static void registerSurface(
            final BootstrapContext<PlacedFeature> context,
            final HolderGetter<ConfiguredFeature<?, ?>> configured,
            final String bodyName,
            final String suffix,
            final int count
    ) {
        if (count <= 0) {
            return;
        }

        context.register(
                SpaceDimensionKeys.placedKey(bodyName, suffix),
                new PlacedFeature(
                        configured.getOrThrow(SpaceDimensionKeys.configuredKey(bodyName, suffix)),
                        List.of(
                                CountPlacement.of(count),
                                InSquarePlacement.spread(),
                                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                BiomeFilter.biome()
                        )
                )
        );
    }

    private static int lakeRarity(final SpaceFluidPocket fluid) {
        return Math.max(72, 220 - fluid.weight());
    }

    private static int geyserRarity(final SpaceFluidPocket fluid) {
        return Math.max(96, 260 - fluid.weight());
    }

    private static int stableCount(final String bodyName, final int salt, final int min, final int max) {
        final int hash = Math.abs((bodyName + "#" + salt).hashCode());
        return min + Math.floorMod(hash, (max - min) + 1);
    }

    private static boolean isVolcanic(final SpaceGenerationMode mode, final SpaceDimensionProfile profile) {
        return mode == SpaceGenerationMode.VOLCANIC
                || profile.defaultBlock().is(Blocks.BLACKSTONE)
                || profile.defaultBlock().is(Blocks.BASALT)
                || profile.topBlock().is(Blocks.MAGMA_BLOCK);
    }

    private static boolean isIcy(final SpaceGenerationMode mode, final SpaceDimensionProfile profile) {
        return mode == SpaceGenerationMode.ICY_SHELL
                || mode == SpaceGenerationMode.CRYO_CHEMICAL
                || profile.defaultBlock().is(Blocks.PACKED_ICE)
                || profile.defaultBlock().is(Blocks.BLUE_ICE)
                || profile.topBlock().is(Blocks.PACKED_ICE)
                || profile.topBlock().is(Blocks.SNOW_BLOCK);
    }

    private static boolean isDusty(final SpaceGenerationMode mode, final SpaceDimensionProfile profile) {
        return mode == SpaceGenerationMode.DUSTY_DESERT
                || profile.topBlock().is(Blocks.RED_SAND)
                || profile.topBlock().is(Blocks.RED_SANDSTONE)
                || profile.topBlock().is(Blocks.SANDSTONE)
                || profile.topBlock().is(Blocks.TERRACOTTA);
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
}
