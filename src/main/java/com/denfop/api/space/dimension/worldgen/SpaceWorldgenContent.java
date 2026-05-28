package com.denfop.api.space.dimension.worldgen;

import com.denfop.api.space.dimension.worldgen.block.SpaceGeyserBlock;
import com.denfop.api.space.dimension.worldgen.block.SpaceGeyserBlockEntity;
import com.denfop.api.space.dimension.worldgen.feature.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.RegistryObject;

import static com.denfop.register.Register.*;

public final class SpaceWorldgenContent {

    public static final RegistryObject<Block> SPACE_GEYSER = BLOCKS.register("space_geyser", SpaceGeyserBlock::new);
    public static final RegistryObject<Feature<SpaceBodyFeatureConfig>> SPACE_CRATER = FEATURES.register("space_crater", () -> new com.denfop.api.space.dimension.worldgen.feature.SpaceCraterFeature(SpaceBodyFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceBodyFeatureConfig>> SPACE_VOLCANO = FEATURES.register("space_volcano", () -> new SpaceVolcanoFeature(SpaceBodyFeatureConfig.CODEC));    public static final RegistryObject<BlockEntityType<SpaceGeyserBlockEntity>> SPACE_GEYSER_BE = BLOCK_ENTITIES.register(
            "space_geyser",
            () -> BlockEntityType.Builder.of(SpaceGeyserBlockEntity::new, SPACE_GEYSER.get()).build(null)
    );
    public static final RegistryObject<Feature<SpaceBodyFeatureConfig>> SPACE_LAKE_BASIN = FEATURES.register("space_lake_basin", () -> new SpaceLakeBasinFeature(SpaceBodyFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceBodyFeatureConfig>> SPACE_GEYSER_FEATURE = FEATURES.register("space_geyser_feature", () -> new SpaceGeyserFeature(SpaceBodyFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceBodyFeatureConfig>> SPACE_CAVITY = FEATURES.register("space_cavity", () -> new SpaceCavityFeature(SpaceBodyFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceBodyFeatureConfig>> SPACE_VERTICAL_SHAFT = FEATURES.register("space_vertical_shaft", () -> new SpaceVerticalShaftFeature(SpaceBodyFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceBodyFeatureConfig>> SPACE_LAVA_TUBE = FEATURES.register("space_lava_tube", () -> new SpaceLavaTubeFeature(SpaceBodyFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceAsteroidFieldFeatureConfig>> SPACE_ASTEROID_FIELD = FEATURES.register("space_asteroid_field", () -> new SpaceAsteroidFieldFeature(SpaceAsteroidFieldFeatureConfig.CODEC));
    public static final RegistryObject<Feature<com.denfop.api.space.dimension.worldgen.SpaceCaveFeatureConfig>> TUNNEL_SYSTEM =
            FEATURES.register("space_tunnel_system", () -> new SpaceTunnelSystemFeature(com.denfop.api.space.dimension.worldgen.SpaceCaveFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceCraterFeatureConfig>> SPACE_ADDITION_CRATER =
            FEATURES.register("space_addition_crater", () -> new SpaceAdditionCraterFeature(SpaceCraterFeatureConfig.CODEC));
    public static final RegistryObject<Feature<com.denfop.api.space.dimension.worldgen.SpaceCaveFeatureConfig>> FRACTURE =
            FEATURES.register("space_fracture", () -> new SpaceFractureFeature(com.denfop.api.space.dimension.worldgen.SpaceCaveFeatureConfig.CODEC));
    public static final RegistryObject<Feature<com.denfop.api.space.dimension.worldgen.SpaceCaveFeatureConfig>> POROUS_CAVE =
            FEATURES.register("space_porous_cave", () -> new SpacePorousCaveFeature(com.denfop.api.space.dimension.worldgen.SpaceCaveFeatureConfig.CODEC));
    public static final RegistryObject<Feature<com.denfop.api.space.dimension.worldgen.SpaceCaveFeatureConfig>> CHAMBER =
            FEATURES.register("space_chamber", () -> new SpaceChamberFeature(com.denfop.api.space.dimension.worldgen.SpaceCaveFeatureConfig.CODEC));
    public static final RegistryObject<Feature<com.denfop.api.space.dimension.worldgen.SpaceCaveFeatureConfig>> ADVANCED_CAVE =
            FEATURES.register("space_advanced_cave", () -> new SpaceAdvancedCaveFeature(com.denfop.api.space.dimension.worldgen.SpaceCaveFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceFormationFeatureConfig>> ROCK_FORMATION =
            FEATURES.register("space_rock_formation", () -> new SpaceRockFormationFeature(SpaceFormationFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceFormationFeatureConfig>> DEBRIS_FIELD =
            FEATURES.register("space_debris_field", () -> new SpaceDebrisFieldFeature(SpaceFormationFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceFormationFeatureConfig>> VOLCANIC_FIELD =
            FEATURES.register("space_volcanic_field", () -> new SpaceVolcanicFieldFeature(SpaceFormationFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceFormationFeatureConfig>> CRATER =
            FEATURES.register("surface_space_crater", () -> new com.denfop.api.space.dimension.worldgen.SpaceCraterFeature(SpaceFormationFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceFormationFeatureConfig>> SURFACE_RIFT =
            FEATURES.register("space_surface_rift", () -> new SpaceSurfaceRiftFeature(SpaceFormationFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceOreFeatureConfig>> SPACE_GENERIC_ORE =
            FEATURES.register("space_generic_ore", () -> new SpaceGenericOreFeature(SpaceOreFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceFluidFeatureConfig>> SPACE_GENERIC_FLUID =
            FEATURES.register("space_generic_fluid", () -> new SpaceGenericFluidFeature(SpaceFluidFeatureConfig.CODEC));
    public static final RegistryObject<Feature<SpaceBodyFeatureConfig>> SPACE_REGIONAL_HYDROLOGY =
            FEATURES.register("space_regional_hydrology", () ->
                    new SpaceRegionalHydrologyFeature(SpaceBodyFeatureConfig.CODEC)
            );
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPACE_ICE_SURFACE_COVER =
            FEATURES.register("space_ice_surface_cover",
                    () -> new SpaceSurfaceCoverFeature(2, 3, false));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPACE_CRYO_SURFACE_COVER =
            FEATURES.register("space_cryo_surface_cover",
                    () -> new SpaceSurfaceCoverFeature(5, 10, true));

    private SpaceWorldgenContent() {
    }

    public static void init() {
    }


}
