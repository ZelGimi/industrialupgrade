package com.denfop.api.space.dimension.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public final class SpaceCarvingSupport {
    private SpaceCarvingSupport() {
    }

    public static boolean canReplace(BlockState state) {
        return !state.isAir() && state.getFluidState().isEmpty() && !state.is(Blocks.BEDROCK);
    }

    public static void carveEllipsoid(WorldGenLevel level, Vec3 center, double radiusX, double radiusY, double radiusZ, long seed, double roughness, BlockState fill) {
        int minX = Mth.floor(center.x - radiusX - 2.0D);
        int maxX = Mth.ceil(center.x + radiusX + 2.0D);
        int minY = Mth.floor(center.y - radiusY - 2.0D);
        int maxY = Mth.ceil(center.y + radiusY + 2.0D);
        int minZ = Mth.floor(center.z - radiusZ - 2.0D);
        int maxZ = Mth.ceil(center.z + radiusZ + 2.0D);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (y <= level.getMinBuildHeight() + 1 || y >= level.getMaxBuildHeight() - 1) {
                    continue;
                }
                for (int z = minZ; z <= maxZ; z++) {
                    double nx = (x + 0.5D - center.x) / radiusX;
                    double ny = (y + 0.5D - center.y) / radiusY;
                    double nz = (z + 0.5D - center.z) / radiusZ;
                    double base = nx * nx + ny * ny + nz * nz;
                    double noise = SpaceShapeMath.smoothNoise(seed, x * 0.12D, y * 0.12D, z * 0.12D) * roughness;
                    if (base + noise <= 1.0D) {
                        cursor.set(x, y, z);
                        BlockState state = level.getBlockState(cursor);
                        if (canReplace(state)) {
                            level.setBlock(cursor, fill, 2);
                        }
                    }
                }
            }
        }
    }

    public static void fillEllipsoid(WorldGenLevel level, Vec3 center, double radiusX, double radiusY, double radiusZ, long seed, double roughness, BlockState fill) {
        int minX = Mth.floor(center.x - radiusX - 1.0D);
        int maxX = Mth.ceil(center.x + radiusX + 1.0D);
        int minY = Mth.floor(center.y - radiusY - 1.0D);
        int maxY = Mth.ceil(center.y + radiusY + 1.0D);
        int minZ = Mth.floor(center.z - radiusZ - 1.0D);
        int maxZ = Mth.ceil(center.z + radiusZ + 1.0D);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (y <= level.getMinBuildHeight() + 1 || y >= level.getMaxBuildHeight() - 1) {
                    continue;
                }
                for (int z = minZ; z <= maxZ; z++) {
                    double nx = (x + 0.5D - center.x) / radiusX;
                    double ny = (y + 0.5D - center.y) / radiusY;
                    double nz = (z + 0.5D - center.z) / radiusZ;
                    double base = nx * nx + ny * ny + nz * nz;
                    double noise = SpaceShapeMath.smoothNoise(seed, x * 0.11D, y * 0.11D, z * 0.11D) * roughness;
                    if (base + noise <= 1.0D) {
                        cursor.set(x, y, z);
                        level.setBlock(cursor, fill, 2);
                    }
                }
            }
        }
    }

    public static void traceCarve(WorldGenLevel level, Vec3 start, Vec3 end, double radius, long seed, double roughness, BlockState fill) {
        double distance = start.distanceTo(end);
        int steps = Math.max(2, Mth.ceil(distance * 1.5D));
        for (int i = 0; i <= steps; i++) {
            double progress = i / (double) steps;
            Vec3 pos = start.lerp(end, progress);
            double localRadius = radius * (0.7D + 0.3D * Math.sin(progress * Math.PI));
            carveEllipsoid(level, pos, localRadius, localRadius * 0.85D, localRadius, SpaceShapeMath.mixSeed(seed, i), roughness, fill);
        }
    }

    public static void traceFill(WorldGenLevel level, Vec3 start, Vec3 end, double radius, long seed, double roughness, BlockState fill) {
        double distance = start.distanceTo(end);
        int steps = Math.max(2, Mth.ceil(distance * 1.5D));
        for (int i = 0; i <= steps; i++) {
            double progress = i / (double) steps;
            Vec3 pos = start.lerp(end, progress);
            double localRadius = radius * (0.8D + 0.2D * Math.sin(progress * Math.PI));
            fillEllipsoid(level, pos, localRadius, localRadius * 0.9D, localRadius, SpaceShapeMath.mixSeed(seed, i), roughness, fill);
        }
    }

    public static void fillPrism(WorldGenLevel level, BlockPos origin, int radiusX, int height, int radiusZ, BlockState state) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = -radiusX; x <= radiusX; x++) {
            for (int z = -radiusZ; z <= radiusZ; z++) {
                for (int y = 0; y < height; y++) {
                    cursor.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    level.setBlock(cursor, state, 2);
                }
            }
        }
    }

    public static void fillHexPrism(WorldGenLevel level, BlockPos origin, int radius, int height, BlockState state) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                int ax = Math.abs(x);
                int az = Math.abs(z);
                int ay = Math.abs(x + z);
                if (Math.max(ax, Math.max(az, ay)) > radius) {
                    continue;
                }
                for (int y = 0; y < height; y++) {
                    cursor.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    level.setBlock(cursor, state, 2);
                }
            }
        }
    }

    public static int surfaceY(WorldGenLevel level, int x, int z) {
        return level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG, x, z);
    }

    public static List<BlockPos> ring(BlockPos center, int radius, int samples) {
        List<BlockPos> positions = new ArrayList<>(samples);
        for (int i = 0; i < samples; i++) {
            double angle = (Math.PI * 2.0D) * (i / (double) samples);
            int x = center.getX() + Mth.floor(Math.cos(angle) * radius);
            int z = center.getZ() + Mth.floor(Math.sin(angle) * radius);
            positions.add(new BlockPos(x, center.getY(), z));
        }
        return positions;
    }

    public static void scatterDebris(WorldGenLevel level, BlockPos center, int radius, int count, BlockState state, long seed) {
        for (int i = 0; i < count; i++) {
            long localSeed = SpaceShapeMath.mixSeed(seed, i);
            int x = center.getX() + SpaceShapeMath.hashInt(localSeed, radius * 2 + 1) - radius;
            int z = center.getZ() + SpaceShapeMath.hashInt(localSeed ^ 0x55AAL, radius * 2 + 1) - radius;
            int y = center.getY() + SpaceShapeMath.hashInt(localSeed ^ 0xAA55L, Math.max(1, radius / 2 + 1)) - Math.max(1, radius / 4);
            fillEllipsoid(level, new Vec3(x + 0.5D, y + 0.5D, z + 0.5D), 0.8D + SpaceShapeMath.unit(localSeed) * 1.4D, 0.7D, 0.8D + SpaceShapeMath.unit(localSeed ^ 99L) * 1.4D, localSeed, 0.15D, state);
        }
    }
}
