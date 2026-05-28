package com.denfop.api.space.dimension.worldgen;

import com.denfop.api.space.dimension.SpaceBodyDefinition;
import com.denfop.api.space.dimension.SpaceBodyDefinitionRegistry;
import com.denfop.api.space.dimension.SpaceBodyRef;
import com.denfop.api.space.dimension.SpaceGenerationMode;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record SpaceWorldgenProfile(
        int tunnelCount,
        int hallCount,
        int fractureCount,
        int porousCount,
        int shaftCount,
        int chamberCount,
        int craterLinkedCount,
        int liquidCount,
        int pillarCount,
        int archCount,
        int plateauCount,
        int spireCount,
        int debrisCount,
        int volcanicFieldCount,
        int craterCount,
        int riftCount,
        int crystalCount,
        boolean volcanic,
        boolean icy,
        boolean dusty,
        boolean cratered,
        boolean atmosphereSoft
) {

    public static SpaceWorldgenProfile of(final SpaceBodyRef body, final SpaceGenerationMode mode) {
        final SpaceBodyDefinition definition = SpaceBodyDefinitionRegistry.getOrBuild(body, mode);
        final String name = body.name().toLowerCase();

        final BlockState stone = definition.getLayer(SpaceBodyDefinitionRegistry.STONE);
        final BlockState top = definition.getLayer(SpaceBodyDefinitionRegistry.TOP);
        final BlockState fluid = definition.getLayer(SpaceBodyDefinitionRegistry.FLUID);

        final boolean volcanic = fluid != null && fluid.getBlock() == net.minecraft.world.level.material.Fluids.FLOWING_LAVA.defaultFluidState().createLegacyBlock().getBlock()
                || stone != null && (stone.is(Blocks.BLACKSTONE) || stone.is(Blocks.BASALT) || top.is(Blocks.MAGMA_BLOCK));
        final boolean icy = stone != null && (stone.is(Blocks.PACKED_ICE) || stone.is(Blocks.BLUE_ICE) || top.is(Blocks.SNOW_BLOCK) || top.is(Blocks.PACKED_ICE));
        final boolean dusty = top != null && (top.is(Blocks.RED_SANDSTONE) || top.is(Blocks.RED_SAND) || top.is(Blocks.SANDSTONE) || top.is(Blocks.TERRACOTTA));
        final boolean cratered = body.isSatellite() || mode == SpaceGenerationMode.AIRLESS_BARREN || mode == SpaceGenerationMode.AIRLESS_CRATERED || mode == SpaceGenerationMode.ASTEROID_FIELD;
        final boolean atmosphereSoft = !body.isSatellite() && mode != SpaceGenerationMode.AIRLESS_BARREN && mode != SpaceGenerationMode.AIRLESS_CRATERED && mode != SpaceGenerationMode.ASTEROID_FIELD;

        int tunnel = base(name, 1, 2, 5);
        int halls = base(name, 2, 1, 3);
        int fractures = base(name, 3, 1, 3);
        int porous = base(name, 4, 0, 2);
        int shafts = base(name, 5, 0, 2);
        int chambers = base(name, 6, 1, 3);
        int craterLinked = cratered ? base(name, 7, 1, 2) : 0;
        int liquid = volcanic || icy ? 1 : base(name, 8, 0, 1);

        int pillars = base(name, 9, 1, 3);
        int arches = atmosphereSoft ? base(name, 10, 1, 2) : base(name, 10, 0, 1);
        int plateaus = atmosphereSoft ? base(name, 11, 1, 2) : 0;
        int spires = volcanic || cratered ? base(name, 12, 1, 3) : base(name, 12, 0, 2);
        int debris = cratered || dusty ? base(name, 13, 2, 5) : base(name, 13, 1, 3);
        int volcanicFields = volcanic ? base(name, 14, 1, 3) : 0;
        int craters = cratered ? base(name, 15, 2, 5) : 0;
        int rifts = volcanic || dusty ? base(name, 16, 1, 3) : base(name, 16, 0, 1);
        int crystals = icy ? base(name, 17, 2, 5) : (mode == SpaceGenerationMode.CRYO_CHEMICAL ? base(name, 17, 1, 3) : 0);

        if (name.equals("io")) {
            volcanicFields += 2;
            fractures += 1;
            chambers += 1;
            shafts += 1;
        }
        if (name.equals("europa") || name.equals("europe")) {
            crystals += 3;
            chambers += 1;
            liquid += 1;
        }
        if (name.equals("titan")) {
            porous += 1;
            liquid += 1;
            arches += 1;
            rifts += 1;
        }
        if (name.equals("mercury") || name.equals("moon") || name.equals("phobos") || name.equals("deimos")) {
            craters += 2;
            craterLinked += 1;
            debris += 2;
        }

        return new SpaceWorldgenProfile(
                tunnel,
                halls,
                fractures,
                porous,
                shafts,
                chambers,
                craterLinked,
                liquid,
                pillars,
                arches,
                plateaus,
                spires,
                debris,
                volcanicFields,
                craters,
                rifts,
                crystals,
                volcanic,
                icy,
                dusty,
                cratered,
                atmosphereSoft
        );
    }

    private static int base(final String name, final int salt, final int min, final int max) {
        return SpaceFeatureUtils.signedVariance(name, salt, min, max);
    }
}