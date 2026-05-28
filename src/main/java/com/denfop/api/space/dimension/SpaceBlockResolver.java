package com.denfop.api.space.dimension;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class SpaceBlockResolver {

    private SpaceBlockResolver() {
    }

    public static SpaceTerrainPalette resolvePalette(final SpaceBodyRef body, final SpaceGenerationMode mode) {
        final SpaceBodyDefinition definition = SpaceBodyDefinitionRegistry.getOrBuild(body, mode);

        final BlockState stone = valueOr(definition.getLayer(SpaceBodyDefinitionRegistry.STONE), Blocks.STONE.defaultBlockState());
        final BlockState top = valueOr(definition.getLayer(SpaceBodyDefinitionRegistry.TOP), stone);
        final BlockState subsurface = valueOr(definition.getLayer(SpaceBodyDefinitionRegistry.SUBSURFACE), stone);
        final BlockState rim = valueOr(definition.getLayer(SpaceBodyDefinitionRegistry.RIM), subsurface);
        final BlockState cobble = valueOr(definition.getLayer(SpaceBodyDefinitionRegistry.COBBLE), stone);
        final BlockState fluid = valueOr(definition.getLayer(SpaceBodyDefinitionRegistry.FLUID), Blocks.AIR.defaultBlockState());

        final List<SpaceOreEntry> ores = definition.getOres();
        final List<SpaceFluidPocket> fluids = definition.getFluids();

        return new SpaceTerrainPalette(
                stone,
                top,
                subsurface,
                rim,
                cobble,
                fluid,
                ores,
                fluids
        );
    }

    private static BlockState valueOr(final BlockState state, final BlockState fallback) {
        return state != null ? state : fallback;
    }
}