package com.denfop.api.space.dimension.worldgen;

import com.denfop.api.space.dimension.*;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.*;

public final class SpacePlacedFeaturesBootstrap {

    private SpacePlacedFeaturesBootstrap() {
    }

    public static Map<ResourceLocation, PlacedFeature> build(final RegistryAccess registryAccess) {
        final Registry<ConfiguredFeature<?, ?>> configured = registryAccess.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
        final Map<ResourceLocation, PlacedFeature> entries = new LinkedHashMap<>();

        for (final SpaceBodyRef body : SpaceBodyCatalog.allBodies()) {
            final SpaceDimensionProfile profile = SpaceBodyProfiles.byBody(body);
            final SpaceGenerationMode mode = profile.generationMode();
            final String bodyName = body.name().toLowerCase(Locale.ROOT);

            if (profile.generateAsteroidField()) {
                register(entries,
                        SpaceDimensionKeys.asteroidFieldPlacedKey(body.name()),
                        new PlacedFeature(
                                configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.asteroidFieldConfiguredKey(body.name())),
                                List.of(CountPlacement.of(1), InSquarePlacement.spread(), BiomeFilter.biome())
                        )
                );
                continue;
            }

            if (profile.generateCraters()) {
                register(entries,
                        SpaceDimensionKeys.craterPlacedKey(body.name()),
                        new PlacedFeature(
                                configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.craterConfiguredKey(body.name())),
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
                register(entries,
                        SpaceDimensionKeys.volcanoPlacedKey(body.name()),
                        new PlacedFeature(
                                configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.volcanoConfiguredKey(body.name())),
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
                register(entries,
                        SpaceDimensionKeys.cavityPlacedKey(body.name()),
                        new PlacedFeature(
                                configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.cavityConfiguredKey(body.name())),
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
                register(entries,
                        SpaceDimensionKeys.shaftPlacedKey(body.name()),
                        new PlacedFeature(
                                configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.shaftConfiguredKey(body.name())),
                                List.of(
                                        RarityFilter.onAverageOnceEvery(profile.shaftRarity()),
                                        InSquarePlacement.spread(),
                                        BiomeFilter.biome()
                                )
                        )
                );
            }

            if (profile.generateLavaTubes()) {
                register(entries,
                        SpaceDimensionKeys.lavaTubePlacedKey(body.name()),
                        new PlacedFeature(
                                configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.lavaTubeConfiguredKey(body.name())),
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
                register(entries,
                        SpaceDimensionKeys.lakePlacedKey(body.name(), fluidIndex),
                        new PlacedFeature(
                                configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.lakeConfiguredKey(body.name(), fluidIndex)),
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

                register(entries,
                        SpaceDimensionKeys.geyserPlacedKey(body.name(), fluidIndex),
                        new PlacedFeature(
                                configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.geyserConfiguredKey(body.name(), fluidIndex)),
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
            register(entries,
                    SpaceDimensionKeys.craterAdditionPlacedKey(body.name()),
                    new PlacedFeature(
                            configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.craterAdditionConfiguredKey(body.name())),
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
                register(entries,
                        SpaceDimensionKeys.orePlacedKey(body.name(), oreIndex),
                        new PlacedFeature(
                                configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.oreConfiguredKey(body.name(), oreIndex)),
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

            registerUnderground(entries, configured, bodyName, "stone_tunnels", tunnelCount, -48, 96);
            registerUnderground(entries, configured, bodyName, "hall_system", hallCount, -56, 72);
            registerUnderground(entries, configured, bodyName, "fractures", fractureCount, -64, 90);
            registerUnderground(entries, configured, bodyName, "porous", porousCount, -60, 48);
            registerUnderground(entries, configured, bodyName, "shafts", shaftCount, -40, 120);
            registerUnderground(entries, configured, bodyName, "liquid_chamber", liquidCount, -50, 28);

            registerUnderground(entries, configured, bodyName, "tunnel_caves", tunnelCavesCount, -56, 80);
            registerUnderground(entries, configured, bodyName, "labyrinth_caves", labyrinthCount, -48, 56);
            registerUnderground(entries, configured, bodyName, "shaft_caves", shaftCavesCount, -24, 120);
            registerUnderground(entries, configured, bodyName, "dome_caves", domeCount, -60, 44);
            registerUnderground(entries, configured, bodyName, "well_caves", wellCount, -16, 96);
            registerUnderground(entries, configured, bodyName, "multi_tier_caves", multiTierCount, -56, 52);
            registerUnderground(entries, configured, bodyName, "branched_caves", branchedCount, -52, 68);
            registerUnderground(entries, configured, bodyName, "spiral_caves", spiralCount, -60, 48);
            registerUnderground(entries, configured, bodyName, "arch_caves", archCavesCount, -48, 54);
            registerUnderground(entries, configured, bodyName, "chamber_caves", chamberCavesCount, -60, 40);
            registerUnderground(entries, configured, bodyName, "canyon_caves", canyonCount, -64, 72);
            registerUnderground(entries, configured, bodyName, "pocket_caves", pocketCount, -62, 48);
            registerUnderground(entries, configured, bodyName, "crevice_caves", creviceCount, -48, 96);
            registerUnderground(entries, configured, bodyName, "amphitheater_caves", amphitheaterCount, -32, 72);
            registerUnderground(entries, configured, bodyName, "cathedral_caves", cathedralCount, -56, 36);
            registerUnderground(entries, configured, bodyName, "gallery_caves", galleryCount, -50, 60);
            registerUnderground(entries, configured, bodyName, "winding_caves", windingCount, -60, 72);
            registerUnderground(entries, configured, bodyName, "cascade_caves", cascadeCount, -64, 28);
            registerUnderground(entries, configured, bodyName, "collapse_caves", collapseCount, -8, 110);
            registerUnderground(entries, configured, bodyName, "vertical_collectors", collectorCount, -40, 96);

            if (volcanic) {
                registerUnderground(entries, configured, bodyName, "volcanic_chamber", chamberCount, -48, 36);
                registerSurface(entries, configured, bodyName, "volcanic_fields", volcanicFieldCount);
                registerRegionalSurface(entries, configured, bodyName, "lava_hydrology");

            }

            if (icy) {
                registerUnderground(entries, configured, bodyName, "frozen_chamber", chamberCount, -56, 40);
                registerSurface(entries, configured, bodyName, "crystals", crystalCount);
            }

            if (volcanic || dusty) {
                registerUnderground(entries, configured, bodyName, "thermal_chamber", Math.max(1, chamberCount - 1), -48, 32);

            }

            if (cratered) {
                registerUnderground(entries, configured, bodyName, "crater_chamber", craterLinkedCount, -32, 40);
                registerSurface(entries, configured, bodyName, "craters", craterCount);
            }

            registerSurface(entries, configured, bodyName, "pillars", pillarCount);
            registerSurface(entries, configured, bodyName, "arches", archCount);
            registerSurface(entries, configured, bodyName, "plateaus", plateauCount);
            registerSurface(entries, configured, bodyName, "spires", spireCount);
            registerSurface(entries, configured, bodyName, "debris", debrisCount);
            registerSurface(entries, configured, bodyName, "surface_rifts", riftCount);
            registerBodyOres(entries, configured, profile, oreIndex);
            registerBodyFluids(entries, configured, profile);

            if (mode == SpaceGenerationMode.ICY_SHELL) {
                registerTopLayer(entries, configured, bodyName, "ice_surface_cover");
            }

            if (mode == SpaceGenerationMode.CRYO_CHEMICAL) {
                registerTopLayer(entries, configured, bodyName, "cryo_surface_cover");
            }

        }

        return entries;
    }

    private static void register(
            final Map<ResourceLocation, PlacedFeature> entries,
            final ResourceKey<PlacedFeature> key,
            final PlacedFeature value
    ) {
        entries.put(key.location(), value);
    }

    private static void registerTopLayer(
            final Map<ResourceLocation, PlacedFeature> entries,
            final Registry<ConfiguredFeature<?, ?>> configured,
            final String bodyName,
            final String suffix
    ) {
        register(entries,
                SpaceDimensionKeys.placedKey(bodyName, suffix),
                new PlacedFeature(
                        configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.configuredKey(bodyName, suffix)),
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
            final Map<ResourceLocation, PlacedFeature> entries,
            final Registry<ConfiguredFeature<?, ?>> configuredFeatures,
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

            register(entries,
                    SpaceDimensionKeys.orePlacedKey(body.body().name(), oreIndex),
                    new PlacedFeature(
                            configuredFeatures.getOrCreateHolderOrThrow(SpaceDimensionKeys.oreConfiguredKey(body.body().name(), oreIndex)),
                            List.copyOf(modifiers)
                    )
            );
            oreIndex++;
        }
    }

    private static void registerBodyFluids(
            final Map<ResourceLocation, PlacedFeature> entries,
            final Registry<ConfiguredFeature<?, ?>> configuredFeatures,
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

            register(entries,
                    SpaceDimensionKeys.fluidPlacedKey(body.body().name(), fluidIndex),
                    new PlacedFeature(
                            configuredFeatures.getOrCreateHolderOrThrow(SpaceDimensionKeys.fluidConfiguredKey(body.body().name(), fluidIndex)),
                            List.copyOf(modifiers)
                    )
            );
            fluidIndex++;
        }
    }

    private static void registerUnderground(
            final Map<ResourceLocation, PlacedFeature> entries,
            final Registry<ConfiguredFeature<?, ?>> configured,
            final String bodyName,
            final String suffix,
            final int count,
            final int minY,
            final int maxY
    ) {
        if (count <= 0) {
            return;
        }

        register(entries,
                SpaceDimensionKeys.placedKey(bodyName, suffix),
                new PlacedFeature(
                        configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.configuredKey(bodyName, suffix)),
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
            final Map<ResourceLocation, PlacedFeature> entries,
            final Registry<ConfiguredFeature<?, ?>> configured,
            final String bodyName,
            final String suffix
    ) {
        register(entries,
                SpaceDimensionKeys.placedKey(bodyName, suffix),
                new PlacedFeature(
                        configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.configuredKey(bodyName, suffix)),
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
            final Map<ResourceLocation, PlacedFeature> entries,
            final Registry<ConfiguredFeature<?, ?>> configured,
            final String bodyName,
            final String suffix,
            final int count
    ) {
        if (count <= 0) {
            return;
        }

        register(entries,
                SpaceDimensionKeys.placedKey(bodyName, suffix),
                new PlacedFeature(
                        configured.getOrCreateHolderOrThrow(SpaceDimensionKeys.configuredKey(bodyName, suffix)),
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
