package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.SpaceBodyProfiles;
import com.denfop.api.space.dimension.SpaceDimensionProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public final class WorldgenSupport {

    private WorldgenSupport() {
    }

    public static SpaceDimensionProfile profile(final SpaceBodyFeatureConfig config) {
        return SpaceBodyProfiles.byName(config.bodyName());
    }

    public static BlockPos surfaceCenter(final WorldGenLevel level, final BlockPos origin) {
        return new BlockPos(
                origin.getX(),
                level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, origin.getX(), origin.getZ()),
                origin.getZ()
        );
    }

    public static BlockState toSourceFluidBlock(final BlockState state) {
        if (state == null || state.isAir()) {
            return Blocks.AIR.defaultBlockState();
        }

        final Fluid fluid = state.getFluidState().getType();
        if (fluid == Fluids.EMPTY) {
            return state;
        }

        if (fluid instanceof FlowingFluid flowingFluid) {
            BlockState fluidState = flowingFluid.getSource().defaultFluidState().createLegacyBlock();
            fluidState = fluidState.setValue(BlockStateProperties.LEVEL, 15);
            return fluidState;
        }
        BlockState fluidState = fluid.defaultFluidState().createLegacyBlock();
        fluidState = fluidState.setValue(BlockStateProperties.LEVEL, 15);
        return fluidState;
    }

    public static void scheduleFluid(final LevelAccessor level, final BlockPos pos, final BlockState state) {
        if (state == null || state.isAir()) {
            return;
        }

        final Fluid fluid = state.getFluidState().getType();
        if (fluid == Fluids.EMPTY) {
            return;
        }

        level.scheduleTick(pos, fluid, 1);
    }

    public static boolean placeSourceAndUpdate(final WorldGenLevel level, final BlockPos pos, final BlockState fluid) {
        final BlockState source = toSourceFluidBlock(fluid);

        if (source.isAir()) {
            return false;
        }
        if (source.getFluidState().isEmpty()) {
            return false;
        }

        if (!canFluidReplace(level, pos)) {
            return false;
        }

        if (level.getBlockState(pos.below()).isAir()) {
            return false;
        }

        level.setBlock(pos, source, 2);

        scheduleFluid(level, pos, source);
        scheduleFluid(level, pos.above(), source);
        scheduleFluid(level, pos.below(), source);

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            scheduleFluid(level, pos.relative(direction), source);
        }

        return true;
    }

    public static boolean canFluidReplace(final WorldGenLevel level, final BlockPos pos) {
        final BlockState state = level.getBlockState(pos);

        if (state.is(Blocks.BEDROCK) || state.is(Blocks.BARRIER)) {
            return false;
        }

        return state.isAir()
                || state.canBeReplaced()
                || !state.getFluidState().isEmpty();
    }

    public static boolean canReplace(final WorldGenLevel level, final BlockPos pos) {
        final BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.BEDROCK) || state.is(Blocks.BARRIER)) {
            return false;
        }
        return state.canBeReplaced();
    }
}