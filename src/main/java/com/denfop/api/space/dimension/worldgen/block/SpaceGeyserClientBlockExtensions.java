package com.denfop.api.space.dimension.worldgen.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

public class SpaceGeyserClientBlockExtensions implements IClientBlockExtensions {

    private static BlockState resolveSourceState(final Level level, final BlockPos pos) {
        final var be = level.getBlockEntity(pos);
        if (be instanceof SpaceGeyserBlockEntity geyser) {
            return geyser.getSourceState();
        }
        return null;
    }

    private static boolean isValidSourceState(final BlockState sourceState) {
        return sourceState != null
                && !sourceState.isAir()
                && !(sourceState.getBlock() instanceof SpaceGeyserBlock);
    }

    @Override
    public boolean addDestroyEffects(
            final BlockState state,
            final Level level,
            final BlockPos pos,
            final ParticleEngine manager
    ) {
        final BlockState sourceState = resolveSourceState(level, pos);
        if (!isValidSourceState(sourceState)) {
            return false;
        }

        manager.destroy(pos, sourceState);
        return true;
    }

    @Override
    public boolean addHitEffects(
            final BlockState state,
            final Level level,
            final net.minecraft.world.phys.HitResult target,
            final ParticleEngine manager
    ) {
        if (!(level instanceof ClientLevel clientLevel)) {
            return false;
        }

        if (!(target instanceof BlockHitResult hitResult)) {
            return false;
        }

        final BlockPos pos = hitResult.getBlockPos();
        final BlockState sourceState = resolveSourceState(level, pos);
        if (!isValidSourceState(sourceState)) {
            return false;
        }

        VoxelShape shape = sourceState.getShape(level, pos);
        if (shape.isEmpty()) {
            shape = Shapes.block();
        }

        final double minX = shape.min(Direction.Axis.X);
        final double minY = shape.min(Direction.Axis.Y);
        final double minZ = shape.min(Direction.Axis.Z);
        final double maxX = shape.max(Direction.Axis.X);
        final double maxY = shape.max(Direction.Axis.Y);
        final double maxZ = shape.max(Direction.Axis.Z);

        double x = pos.getX() + minX + level.random.nextDouble() * Math.max(0.001D, (maxX - minX) - 0.2D) + 0.1D;
        double y = pos.getY() + minY + level.random.nextDouble() * Math.max(0.001D, (maxY - minY) - 0.2D) + 0.1D;
        double z = pos.getZ() + minZ + level.random.nextDouble() * Math.max(0.001D, (maxZ - minZ) - 0.2D) + 0.1D;

        final Direction side = hitResult.getDirection();
        switch (side) {
            case DOWN -> y = pos.getY() + minY - 0.1D;
            case UP -> y = pos.getY() + maxY + 0.1D;
            case NORTH -> z = pos.getZ() + minZ - 0.1D;
            case SOUTH -> z = pos.getZ() + maxZ + 0.1D;
            case WEST -> x = pos.getX() + minX - 0.1D;
            case EAST -> x = pos.getX() + maxX + 0.1D;
        }

        manager.add(
                new TerrainParticle(
                        clientLevel,
                        x,
                        y,
                        z,
                        0.0D,
                        0.0D,
                        0.0D,
                        sourceState,
                        pos
                ).setPower(0.2F).scale(0.6F)
        );

        return true;
    }
}