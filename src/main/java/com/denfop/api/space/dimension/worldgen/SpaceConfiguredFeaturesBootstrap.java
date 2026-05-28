package com.denfop.api.space.dimension.worldgen;

import com.denfop.api.space.dimension.*;
import com.denfop.api.space.dimension.worldgen.feature.SpaceAsteroidFieldFeatureConfig;
import com.denfop.api.space.dimension.worldgen.feature.SpaceBodyFeatureConfig;
import com.denfop.blocks.FluidName;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static com.denfop.api.space.dimension.worldgen.SpaceWorldgenContent.SPACE_GENERIC_FLUID;
import static com.denfop.api.space.dimension.worldgen.SpaceWorldgenContent.SPACE_GENERIC_ORE;

public final class SpaceConfiguredFeaturesBootstrap {

    private SpaceConfiguredFeaturesBootstrap() {
    }

    public static void bootstrap(final BootstrapContext<ConfiguredFeature<?, ?>> context) {
        for (final SpaceBodyRef body : SpaceBodyCatalog.allBodies()) {
            final SpaceDimensionProfile profile = SpaceBodyProfiles.byBody(body);
            final SpaceGenerationMode mode = profile.generationMode();
            final SpaceBodyFeatureConfig legacyCfg = new SpaceBodyFeatureConfig(body.name());

            if (profile.generateAsteroidField()) {
                context.register(
                        SpaceDimensionKeys.asteroidFieldConfiguredKey(body.name()),
                        new ConfiguredFeature<>(
                                SpaceWorldgenContent.SPACE_ASTEROID_FIELD.get(),
                                new SpaceAsteroidFieldFeatureConfig(body.name(), 64, 40, 36, -32, 288)
                        )
                );
                continue;
            }

            if (profile.generateCraters()) {
                context.register(
                        SpaceDimensionKeys.craterConfiguredKey(body.name()),
                        new ConfiguredFeature<>(SpaceWorldgenContent.SPACE_CRATER.get(), legacyCfg)
                );
            }
            context.register(
                    SpaceDimensionKeys.craterAdditionConfiguredKey(body.name()),
                    new ConfiguredFeature<>(SpaceWorldgenContent.SPACE_ADDITION_CRATER.get(), SpaceCraterFeatureConfig.defaultConfig(profile.topBlock(), profile.rimBlock(), profile.topBlock()))
            );
            if (profile.generateVolcanoes()) {
                context.register(
                        SpaceDimensionKeys.volcanoConfiguredKey(body.name()),
                        new ConfiguredFeature<>(SpaceWorldgenContent.SPACE_VOLCANO.get(), legacyCfg)
                );
            }

            if (profile.generateCavities()) {
                context.register(
                        SpaceDimensionKeys.cavityConfiguredKey(body.name()),
                        new ConfiguredFeature<>(SpaceWorldgenContent.SPACE_CAVITY.get(), legacyCfg)
                );
            }

            if (profile.generateVerticalShafts()) {
                context.register(
                        SpaceDimensionKeys.shaftConfiguredKey(body.name()),
                        new ConfiguredFeature<>(SpaceWorldgenContent.SPACE_VERTICAL_SHAFT.get(), legacyCfg)
                );
            }

            if (profile.generateLavaTubes()) {
                context.register(
                        SpaceDimensionKeys.lavaTubeConfiguredKey(body.name()),
                        new ConfiguredFeature<>(SpaceWorldgenContent.SPACE_LAVA_TUBE.get(), legacyCfg)
                );
            }

            int fluidIndex = 0;
            for (final SpaceFluidPocket ignored : profile.fluids()) {
                context.register(
                        SpaceDimensionKeys.lakeConfiguredKey(body.name(), fluidIndex),
                        new ConfiguredFeature<>(SpaceWorldgenContent.SPACE_LAKE_BASIN.get(), legacyCfg)
                );
                context.register(
                        SpaceDimensionKeys.geyserConfiguredKey(body.name(), fluidIndex),
                        new ConfiguredFeature<>(SpaceWorldgenContent.SPACE_GEYSER_FEATURE.get(), legacyCfg)
                );
                fluidIndex++;
            }

            int oreIndex = 0;
            for (final SpaceOreEntry ore : profile.ores()) {
                context.register(
                        SpaceDimensionKeys.oreConfiguredKey(body.name(), oreIndex),
                        new ConfiguredFeature<>(
                                Feature.ORE,
                                new OreConfiguration(buildTargets(profile, ore.state()), ore.veinSize())
                        )
                );
                oreIndex++;
            }

            final String bodyName = body.name().toLowerCase();
            final BlockState stone = nonNull(profile.defaultBlock(), Blocks.STONE.defaultBlockState());
            final BlockState top = nonNull(profile.topBlock(), stone);
            final BlockState subsurface = nonNull(profile.subsurfaceBlock(), stone);
            final BlockState rim = nonNull(profile.rimBlock(), top);
            final BlockState cobble = nonNull(profile.cobbleBlock(), stone);
            final BlockState fluid = firstFluid(profile, Blocks.AIR.defaultBlockState());
            final BlockState accent = firstOre(profile, rim);
            final BlockState caveFluid = fluid.isAir() ? Blocks.WATER.defaultBlockState() : fluid;

            final boolean volcanic = isVolcanic(mode, stone, top, fluid);
            final boolean icy = isIcy(mode, stone, top);
            final boolean dusty = isDusty(mode, top);
            final boolean cratered = isCratered(body, mode);

            registerCave(context, bodyName, "stone_tunnels", SpaceWorldgenContent.TUNNEL_SYSTEM.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.STONE_TUNNELS, stone, rim, cobble, caveFluid, 2, 4, 10, 18, 18, false, false
            ));
            registerCave(context, bodyName, "hall_system", SpaceWorldgenContent.TUNNEL_SYSTEM.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.HALL_SYSTEM, stone, rim, cobble, caveFluid, 4, 7, 12, 22, 24, false, false
            ));
            registerCave(context, bodyName, "fractures", SpaceWorldgenContent.FRACTURE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.FRACTURE, stone, rim, cobble, caveFluid, 2, 5, 18, 34, 24, true, volcanic || icy
            ));
            registerCave(context, bodyName, "porous", SpaceWorldgenContent.POROUS_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.POROUS, stone, rim, cobble, caveFluid, 1, 3, 6, 12, 20, false, false
            ));
            registerCave(context, bodyName, "shafts", SpaceWorldgenContent.TUNNEL_SYSTEM.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.VERTICAL_SHAFT, stone, rim, cobble, caveFluid, 2, 4, 18, 36, 12, true, volcanic || icy
            ));
            registerCave(context, bodyName, "volcanic_chamber", SpaceWorldgenContent.CHAMBER.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.VOLCANIC_CHAMBER, stone, rim, cobble, caveFluid, 4, 7, 6, 12, 20, false, false
            ));
            registerCave(context, bodyName, "frozen_chamber", SpaceWorldgenContent.CHAMBER.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.FROZEN_CHAMBER, stone, top, accent, caveFluid, 4, 7, 6, 12, 20, false, false
            ));
            registerCave(context, bodyName, "thermal_chamber", SpaceWorldgenContent.CHAMBER.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.THERMAL_CHAMBER, stone, rim, accent, caveFluid, 3, 6, 6, 10, 18, false, true
            ));
            registerCave(context, bodyName, "liquid_chamber", SpaceWorldgenContent.CHAMBER.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.LIQUID_CHAMBER, stone, rim, accent, caveFluid, 4, 7, 6, 10, 20, false, true
            ));
            registerCave(context, bodyName, "crater_chamber", SpaceWorldgenContent.CHAMBER.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.CRATER_LINKED_CHAMBER, stone, rim, cobble, caveFluid, 4, 8, 8, 12, 28, true, false
            ));

            registerCave(context, bodyName, "tunnel_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.TUNNEL_CAVES, stone, rim, cobble, caveFluid, 2, 4, 18, 30, 16, false, false
            ));
            registerCave(context, bodyName, "labyrinth_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.LABYRINTH_CAVES, stone, rim, accent, caveFluid, 2, 4, 10, 18, 18, false, false
            ));
            registerCave(context, bodyName, "shaft_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.SHAFT_CAVES, stone, rim, cobble, caveFluid, 2, 4, 22, 42, 18, true, volcanic || icy
            ));
            registerCave(context, bodyName, "dome_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.DOME_CAVES, stone, rim, accent, caveFluid, 5, 8, 10, 16, 20, false, false
            ));
            registerCave(context, bodyName, "well_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.WELL_CAVES, stone, rim, cobble, caveFluid, 2, 4, 24, 48, 14, true, icy
            ));
            registerCave(context, bodyName, "multi_tier_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.MULTI_TIER_CAVES, stone, rim, accent, caveFluid, 4, 7, 10, 18, 22, false, false
            ));
            registerCave(context, bodyName, "branched_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.BRANCHED_CAVES, stone, rim, cobble, caveFluid, 2, 4, 16, 28, 18, false, false
            ));
            registerCave(context, bodyName, "spiral_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.SPIRAL_CAVES, stone, rim, accent, caveFluid, 2, 4, 16, 26, 26, false, false
            ));
            registerCave(context, bodyName, "arch_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.ARCH_CAVES, stone, rim, cobble, caveFluid, 3, 6, 14, 22, 18, false, false
            ));
            registerCave(context, bodyName, "chamber_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.CHAMBER_CAVES, stone, rim, accent, caveFluid, 4, 8, 10, 18, 18, false, false
            ));
            registerCave(context, bodyName, "canyon_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.CANYON_CAVES, stone, rim, cobble, caveFluid, 4, 8, 20, 34, 24, true, false
            ));
            registerCave(context, bodyName, "pocket_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.POCKET_CAVES, stone, rim, accent, caveFluid, 1, 3, 8, 14, 18, false, false
            ));
            registerCave(context, bodyName, "crevice_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.CREVICE_CAVES, stone, rim, cobble, caveFluid, 1, 2, 20, 40, 32, true, volcanic
            ));
            registerCave(context, bodyName, "amphitheater_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.AMPHITHEATER_CAVES, stone, rim, accent, caveFluid, 5, 9, 12, 18, 18, false, false
            ));
            registerCave(context, bodyName, "cathedral_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.CATHEDRAL_CAVES, stone, rim, accent, caveFluid, 5, 8, 14, 24, 30, false, false
            ));
            registerCave(context, bodyName, "gallery_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.GALLERY_CAVES, stone, rim, cobble, caveFluid, 2, 4, 20, 30, 16, false, false
            ));
            registerCave(context, bodyName, "winding_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.WINDING_CAVES, stone, rim, cobble, caveFluid, 2, 4, 20, 34, 24, false, false
            ));
            registerCave(context, bodyName, "cascade_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.CASCADE_CAVES, stone, rim, accent, caveFluid, 3, 6, 12, 22, 22, false, volcanic || icy
            ));
            registerCave(context, bodyName, "collapse_caves", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.COLLAPSE_CAVES, stone, rim, cobble, caveFluid, 5, 10, 18, 30, 40, true, false
            ));
            registerCave(context, bodyName, "vertical_collectors", SpaceWorldgenContent.ADVANCED_CAVE.get(), new SpaceCaveFeatureConfig(
                    SpaceCaveKind.VERTICAL_COLLECTORS, stone, rim, cobble, caveFluid, 2, 4, 18, 28, 28, true, volcanic || icy
            ));

            registerFormation(context, bodyName, "pillars", new SpaceFormationFeatureConfig(
                    SpaceFormationKind.PILLAR_FIELD, stone, rim, accent, caveFluid, 2, 5, 6, 18, 2
            ));
            registerFormation(context, bodyName, "arches", new SpaceFormationFeatureConfig(
                    SpaceFormationKind.ARCH, stone, rim, accent, caveFluid, 2, 4, 8, 16, 1
            ));
            registerFormation(context, bodyName, "plateaus", new SpaceFormationFeatureConfig(
                    SpaceFormationKind.PLATEAU, top, subsurface, accent, caveFluid, 4, 8, 4, 10, 1
            ));
            registerFormation(context, bodyName, "spires", new SpaceFormationFeatureConfig(
                    icy ? SpaceFormationKind.ICE_CLIFF : SpaceFormationKind.MOUNTAIN_SPIRE,
                    icy ? top : stone, rim, accent, caveFluid, 2, 5, 8, 22, 1
            ));
            registerFormation(context, bodyName, "crystals", new SpaceFormationFeatureConfig(
                    SpaceFormationKind.CRYSTAL_OUTCROP, accent, top, rim, caveFluid, 1, 2, 5, 14, 4
            ));
            registerFormation(context, bodyName, "debris", new SpaceFormationFeatureConfig(
                    SpaceFormationKind.DEBRIS_FIELD, cobble, rim, stone, caveFluid, 2, 5, 0, 0, 0
            ));
            registerFormation(context, bodyName, "volcanic_fields", new SpaceFormationFeatureConfig(
                    SpaceFormationKind.VOLCANIC_FIELD,
                    rim,
                    stone,
                    accent,
                    fluid.isAir() ? FluidName.fluidpahoehoe_lava.getInstance().get().getSource().defaultFluidState().createLegacyBlock() : fluid,
                    2,
                    5,
                    8,
                    18,
                    2
            ));
            registerFormation(context, bodyName, "craters", new SpaceFormationFeatureConfig(
                    SpaceFormationKind.CRATER, rim, stone, accent, caveFluid, 4, 9, 0, 0, 0
            ), SpaceWorldgenContent.CRATER.get());
            registerFormation(context, bodyName, "surface_rifts", new SpaceFormationFeatureConfig(
                    SpaceFormationKind.SURFACE_RIFT, stone, rim, cobble, caveFluid, 2, 5, 12, 24, 1
            ), SpaceWorldgenContent.SURFACE_RIFT.get());
            if (volcanic) {
                context.register(
                        SpaceDimensionKeys.configuredKey(bodyName, "lava_hydrology"),
                        new ConfiguredFeature<>(
                                SpaceWorldgenContent.SPACE_REGIONAL_HYDROLOGY.get(),
                                legacyCfg
                        )
                );
            }

            if (mode == SpaceGenerationMode.ICY_SHELL) {
                context.register(
                        SpaceDimensionKeys.configuredKey(bodyName, "ice_surface_cover"),
                        new ConfiguredFeature<>(
                                SpaceWorldgenContent.SPACE_ICE_SURFACE_COVER.get(),
                                NoneFeatureConfiguration.NONE
                        )
                );
            }

            if (mode == SpaceGenerationMode.CRYO_CHEMICAL) {
                context.register(
                        SpaceDimensionKeys.configuredKey(bodyName, "cryo_surface_cover"),
                        new ConfiguredFeature<>(
                                SpaceWorldgenContent.SPACE_CRYO_SURFACE_COVER.get(),
                                NoneFeatureConfiguration.NONE
                        )
                );
            }
            registerBodyOres(context, profile, oreIndex);
            registerBodyFluids(context, profile);
        }
    }

    private static void registerCave(
            final BootstrapContext<ConfiguredFeature<?, ?>> context,
            final String bodyName,
            final String suffix,
            final Feature<SpaceCaveFeatureConfig> feature,
            final SpaceCaveFeatureConfig config
    ) {
        context.register(
                SpaceDimensionKeys.configuredKey(bodyName, suffix),
                new ConfiguredFeature<>(feature, config)
        );
    }

    private static void registerFormation(
            final BootstrapContext<ConfiguredFeature<?, ?>> context,
            final String bodyName,
            final String suffix,
            final SpaceFormationFeatureConfig config
    ) {
        registerFormation(context, bodyName, suffix, config, SpaceWorldgenContent.ROCK_FORMATION.get());
    }

    private static void registerFormation(
            final BootstrapContext<ConfiguredFeature<?, ?>> context,
            final String bodyName,
            final String suffix,
            final SpaceFormationFeatureConfig config,
            final Feature<SpaceFormationFeatureConfig> feature
    ) {
        context.register(
                SpaceDimensionKeys.configuredKey(bodyName, suffix),
                new ConfiguredFeature<>(feature, config)
        );
    }

    private static void registerBodyOres(final BootstrapContext<ConfiguredFeature<?, ?>> context, final SpaceDimensionProfile body, int oreIndex) {
        final List<String> replaceableBlockIds = buildReplaceableBlockIds(body);

        for (final SpaceOreEntry entry : body.ores()) {
            final SpaceOreFeatureConfig config = new SpaceOreFeatureConfig(
                    body.body().name(),
                    entry.oreBlockId(),
                    replaceableBlockIds,
                    entry.distributionType(),
                    entry.veinSize(),
                    entry.pathLength(),
                    entry.horizontalRadius(),
                    entry.verticalRadius(),
                    entry.minY(),
                    entry.maxY(),
                    entry.baseChance(),
                    entry.depthBonus(),
                    entry.requireNearAir(),
                    entry.requireNearFluid(),
                    entry.requireNearLava(),
                    entry.caveOnly()
            );

            context.register(
                    SpaceDimensionKeys.oreConfiguredKey(body.body().name(), oreIndex),
                    new ConfiguredFeature<>(SPACE_GENERIC_ORE.get(), config)
            );
            oreIndex++;
        }
    }

    private static void registerBodyFluids(
            final BootstrapContext<ConfiguredFeature<?, ?>> context,
            final SpaceDimensionProfile body
    ) {
        int fluid = 0;
        for (final SpaceFluidPocket pocket : body.fluids()) {
            if (pocket.ventType() == SpaceVentType.LAVA) {
                continue;
            }
            final String fluidId = BuiltInRegistries.FLUID.getKey(pocket.fluid()).toString();

            final SpaceFluidFeatureConfig config = new SpaceFluidFeatureConfig(
                    body.body().name(),
                    fluidId,
                    pocket.distributionType(),
                    pocket.radiusMin(),
                    pocket.radiusMax(),
                    pocket.minY(),
                    pocket.maxY(),
                    pocket.baseChance(),
                    pocket.caveOnly(),
                    pocket.surfaceLake(),
                    pocket.requireHotContext(),
                    pocket.requireColdContext()
            );

            context.register(
                    SpaceDimensionKeys.fluidConfiguredKey(body.body().name(), fluid),
                    new ConfiguredFeature<>(SPACE_GENERIC_FLUID.get(), config)
            );
            fluid++;
        }
    }

    private static void addReplaceable(final LinkedHashSet<String> ids, final BlockState state) {
        if (state == null) {
            return;
        }
        final Block block = state.getBlock();
        if (block == Blocks.AIR) {
            return;
        }
        ids.add(BuiltInRegistries.BLOCK.getKey(block).toString());
    }

    private static List<String> buildReplaceableBlockIds(final SpaceDimensionProfile profile) {
        final LinkedHashSet<String> ids = new LinkedHashSet<>();
        addReplaceable(ids, profile.defaultBlock());
        addReplaceable(ids, profile.topBlock());
        addReplaceable(ids, profile.subsurfaceBlock());
        addReplaceable(ids, profile.cobbleBlock());
        addReplaceable(ids, profile.rimBlock());

        if (ids.isEmpty()) {
            ids.add(BuiltInRegistries.BLOCK.getKey(Blocks.STONE).toString());
        }

        return new ArrayList<>(ids);
    }

    private static BlockState nonNull(final BlockState state, final BlockState fallback) {
        return state != null ? state : fallback;
    }

    private static BlockState firstOre(final SpaceDimensionProfile profile, final BlockState fallback) {
        if (profile.ores().isEmpty()) {
            return fallback;
        }
        final SpaceOreEntry entry = profile.ores().get(0);
        return entry != null && entry.state() != null ? entry.state() : fallback;
    }

    private static BlockState firstFluid(final SpaceDimensionProfile profile, final BlockState fallback) {
        if (profile.fluids().isEmpty()) {
            return fallback;
        }
        final SpaceFluidPocket entry = profile.fluids().get(0);
        return entry != null && entry.state() != null ? entry.state() : fallback;
    }

    private static List<OreConfiguration.TargetBlockState> buildTargets(final SpaceDimensionProfile profile, final BlockState oreState) {
        final LinkedHashSet<Block> baseBlocks = new LinkedHashSet<>();
        addIfValid(baseBlocks, profile.defaultBlock().getBlock());
        addIfValid(baseBlocks, profile.topBlock().getBlock());
        addIfValid(baseBlocks, profile.subsurfaceBlock().getBlock());
        addIfValid(baseBlocks, profile.cobbleBlock().getBlock());
        addIfValid(baseBlocks, profile.rimBlock().getBlock());

        final List<OreConfiguration.TargetBlockState> targets = new ArrayList<>();
        for (final Block block : baseBlocks) {
            targets.add(OreConfiguration.target(new BlockMatchTest(block), oreState));
        }

        if (targets.isEmpty()) {
            targets.add(OreConfiguration.target(new BlockMatchTest(profile.defaultBlock().getBlock()), oreState));
        }

        return targets;
    }

    private static void addIfValid(final LinkedHashSet<Block> blocks, final Block block) {
        if (block != null && block != Blocks.AIR) {
            blocks.add(block);
        }
    }

    private static boolean isVolcanic(
            final SpaceGenerationMode mode,
            final BlockState stone,
            final BlockState top,
            final BlockState fluid
    ) {
        return mode == SpaceGenerationMode.VOLCANIC
                || stone.is(Blocks.BLACKSTONE)
                || stone.is(Blocks.BASALT)
                || top.is(Blocks.MAGMA_BLOCK)
                || fluid.is(Blocks.LAVA);
    }

    private static boolean isIcy(
            final SpaceGenerationMode mode,
            final BlockState stone,
            final BlockState top
    ) {
        return mode == SpaceGenerationMode.ICY_SHELL
                || mode == SpaceGenerationMode.CRYO_CHEMICAL
                || stone.is(Blocks.PACKED_ICE)
                || stone.is(Blocks.BLUE_ICE)
                || top.is(Blocks.PACKED_ICE)
                || top.is(Blocks.SNOW_BLOCK);
    }

    private static boolean isDusty(
            final SpaceGenerationMode mode,
            final BlockState top
    ) {
        return mode == SpaceGenerationMode.DUSTY_DESERT
                || top.is(Blocks.RED_SAND)
                || top.is(Blocks.RED_SANDSTONE)
                || top.is(Blocks.SANDSTONE)
                || top.is(Blocks.TERRACOTTA);
    }

    private static boolean isCratered(final SpaceBodyRef body, final SpaceGenerationMode mode) {
        return body.isSatellite()
                || mode == SpaceGenerationMode.AIRLESS_BARREN
                || mode == SpaceGenerationMode.AIRLESS_CRATERED
                || mode == SpaceGenerationMode.ASTEROID_FIELD;
    }
}
