package com.denfop.api.space.dimension;

import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record SpaceTerrainPalette(
        BlockState defaultBlock,
        BlockState topBlock,
        BlockState subsurfaceBlock,
        BlockState rimBlock,
        BlockState cobbleBlock,
        BlockState defaultFluid,
        List<SpaceOreEntry> ores,
        List<SpaceFluidPocket> fluids
) {
}
