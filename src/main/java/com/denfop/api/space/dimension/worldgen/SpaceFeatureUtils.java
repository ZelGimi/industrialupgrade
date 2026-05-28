package com.denfop.api.space.dimension.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Predicate;

public final class SpaceFeatureUtils {

    private SpaceFeatureUtils() {
    }

    public static boolean isSolidTerrain(final BlockState state) {
        return !state.isAir()
                && !state.liquid()
                && state.getDestroySpeed(null, BlockPos.ZERO) >= 0
                && !(state.getBlock() instanceof LiquidBlock)
                && !state.is(Blocks.BEDROCK);
    }

    public static boolean isReplaceableForCarving(
            final BlockState state,
            final BlockState primary,
            final BlockState secondary,
            final BlockState tertiary
    ) {
        if (state.isAir()) {
            return false;
        }

        return state.getBlock() == primary.getBlock()
                || state.getBlock() == secondary.getBlock()
                || state.getBlock() == tertiary.getBlock()
                || isSolidTerrain(state);
    }

    public static boolean canAccess(final WorldGenLevel level, final BlockPos pos) {
        return canAccess(level, pos.getX(), pos.getY(), pos.getZ());
    }

    private static boolean canAccess(final WorldGenLevel level, final int x, final int y, final int z) {
        if (y < level.getMinBuildHeight() || y >= level.getMaxBuildHeight()) {
            return false;
        }

        if (level instanceof WorldGenRegion region) {
            final ChunkPos center = region.getCenter();
            if ((x >> 4) != center.x || (z >> 4) != center.z) {
                return false;
            }
        }

        return true;
    }

    public static BlockPos clampToCurrentChunk(final WorldGenLevel level, final BlockPos pos) {
        if (level instanceof WorldGenRegion region) {
            final ChunkPos center = region.getCenter();
            final int x = Mth.clamp(pos.getX(), center.getMinBlockX(), center.getMaxBlockX());
            final int z = Mth.clamp(pos.getZ(), center.getMinBlockZ(), center.getMaxBlockZ());
            final int y = Mth.clamp(pos.getY(), level.getMinBuildHeight(), level.getMaxBuildHeight() - 1);
            return new BlockPos(x, y, z);
        }

        final int y = Mth.clamp(pos.getY(), level.getMinBuildHeight(), level.getMaxBuildHeight() - 1);
        return new BlockPos(pos.getX(), y, pos.getZ());
    }

    public static void carveSphere(
            final WorldGenLevel level,
            final BlockPos center,
            final int radiusX,
            final int radiusY,
            final int radiusZ,
            final BlockState fillState,
            final Predicate<BlockState> canReplace
    ) {
        final int rx = Math.max(1, radiusX);
        final int ry = Math.max(1, radiusY);
        final int rz = Math.max(1, radiusZ);

        final Bounds bounds = Bounds.forLevel(level);

        final int cx = center.getX();
        final int cy = center.getY();
        final int cz = center.getZ();

        final int minX = Math.max(cx - rx, bounds.minX);
        final int maxX = Math.min(cx + rx, bounds.maxX);
        final int minY = Math.max(cy - ry, bounds.minY);
        final int maxY = Math.min(cy + ry, bounds.maxY);
        final int minZ = Math.max(cz - rz, bounds.minZ);
        final int maxZ = Math.min(cz + rz, bounds.maxZ);

        final double invRx = 1.0D / rx;
        final double invRy = 1.0D / ry;
        final double invRz = 1.0D / rz;

        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            final double dx = (x - cx) * invRx;
            final double dx2 = dx * dx;

            for (int y = minY; y <= maxY; y++) {
                final double dy = (y - cy) * invRy;
                final double dy2 = dy * dy;

                for (int z = minZ; z <= maxZ; z++) {
                    final double dz = (z - cz) * invRz;
                    final double dist = dx2 + dy2 + dz * dz;

                    if (dist > 1.0D) {
                        continue;
                    }

                    mutable.set(x, y, z);

                    final BlockState current = level.getBlockState(mutable);
                    if (!canReplace.test(current)) {
                        continue;
                    }

                    level.setBlock(mutable, fillState, 2);
                }
            }
        }
    }

    public static void paintShell(
            final WorldGenLevel level,
            final BlockPos center,
            final int radiusX,
            final int radiusY,
            final int radiusZ,
            final BlockState shell,
            final Predicate<BlockState> canReplace
    ) {
        final int rx = Math.max(1, radiusX);
        final int ry = Math.max(1, radiusY);
        final int rz = Math.max(1, radiusZ);

        final Bounds bounds = Bounds.forLevel(level);

        final int cx = center.getX();
        final int cy = center.getY();
        final int cz = center.getZ();

        final int minX = Math.max(cx - rx - 1, bounds.minX);
        final int maxX = Math.min(cx + rx + 1, bounds.maxX);
        final int minY = Math.max(cy - ry - 1, bounds.minY);
        final int maxY = Math.min(cy + ry + 1, bounds.maxY);
        final int minZ = Math.max(cz - rz - 1, bounds.minZ);
        final int maxZ = Math.min(cz + rz + 1, bounds.maxZ);

        final double invRx = 1.0D / rx;
        final double invRy = 1.0D / ry;
        final double invRz = 1.0D / rz;

        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            final double dx = (x - cx) * invRx;
            final double dx2 = dx * dx;

            for (int y = minY; y <= maxY; y++) {
                final double dy = (y - cy) * invRy;
                final double dy2 = dy * dy;

                for (int z = minZ; z <= maxZ; z++) {
                    final double dz = (z - cz) * invRz;
                    final double dist = dx2 + dy2 + dz * dz;

                    if (dist < 0.90D || dist > 1.25D) {
                        continue;
                    }

                    mutable.set(x, y, z);

                    final BlockState current = level.getBlockState(mutable);
                    if (!canReplace.test(current)) {
                        continue;
                    }

                    level.setBlock(mutable, shell, 2);
                }
            }
        }
    }

    public static void carveTunnel(
            final WorldGenLevel level,
            final BlockPos start,
            final RandomSource random,
            final int segments,
            final int minRadius,
            final int maxRadius,
            final int verticalRange,
            final BlockState carveState,
            final Predicate<BlockState> canReplace
    ) {
        final Bounds bounds = Bounds.forLevel(level);

        double x = Mth.clamp(start.getX(), bounds.minX, bounds.maxX);
        double y = Mth.clamp(start.getY(), bounds.minY, bounds.maxY);
        double z = Mth.clamp(start.getZ(), bounds.minZ, bounds.maxZ);

        float yaw = random.nextFloat() * ((float) Math.PI * 2F);
        float pitch = (random.nextFloat() - 0.5F) * 0.35F;

        final int safeMinRadius = Math.max(1, minRadius);
        final int safeMaxRadius = Math.max(safeMinRadius, maxRadius);
        final int safeSegments = Math.max(1, Math.min(segments, 32));

        for (int i = 0; i < safeSegments; i++) {
            final int rx = Mth.nextInt(random, safeMinRadius, safeMaxRadius);
            final int ry = Math.max(2, rx - random.nextInt(2));
            final int rz = Mth.nextInt(random, safeMinRadius, safeMaxRadius);

            final int cx = Mth.clamp(Mth.floor(x), bounds.minX + rx, bounds.maxX - rx);
            final int cy = Mth.clamp(Mth.floor(y), bounds.minY + ry, bounds.maxY - ry);
            final int cz = Mth.clamp(Mth.floor(z), bounds.minZ + rz, bounds.maxZ - rz);

            carveSphere(level, new BlockPos(cx, cy, cz), rx, ry, rz, carveState, canReplace);

            x = Mth.clamp(x + Mth.cos(yaw) * 2.5D, bounds.minX + rx, bounds.maxX - rx);
            z = Mth.clamp(z + Mth.sin(yaw) * 2.5D, bounds.minZ + rz, bounds.maxZ - rz);
            y = Mth.clamp(
                    y + Mth.clamp(Mth.sin(pitch) * 2.0D, -verticalRange, verticalRange),
                    bounds.minY + ry,
                    bounds.maxY - ry
            );

            yaw += (random.nextFloat() - 0.5F) * 0.30F;
            pitch += (random.nextFloat() - 0.5F) * 0.16F;
            pitch = Mth.clamp(pitch, -0.55F, 0.55F);
        }
    }

    public static void lineOfSpheres(
            final WorldGenLevel level,
            final BlockPos start,
            final BlockPos end,
            final int radius,
            final BlockState fill,
            final Predicate<BlockState> canReplace
    ) {
        final int safeRadius = Math.max(1, radius);

        final double dx = end.getX() - start.getX();
        final double dy = end.getY() - start.getY();
        final double dz = end.getZ() - start.getZ();
        final double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        final double stepSize = Math.max(1.5D, safeRadius * 0.85D);
        final int steps = Math.max(1, Mth.ceil(distance / stepSize));

        for (int i = 0; i <= steps; i++) {
            final double t = i / (double) steps;
            final double x = Mth.lerp(t, start.getX(), end.getX());
            final double y = Mth.lerp(t, start.getY(), end.getY());
            final double z = Mth.lerp(t, start.getZ(), end.getZ());

            carveSphere(level, BlockPos.containing(x, y, z), safeRadius, safeRadius, safeRadius, fill, canReplace);
        }
    }

    public static void placeSpike(
            final WorldGenLevel level,
            final BlockPos base,
            final int height,
            final BlockState state
    ) {
        final Bounds bounds = Bounds.forLevel(level);
        final int safeHeight = Math.max(1, Math.min(height, bounds.maxY - base.getY()));

        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int y = 0; y < safeHeight; y++) {
            final int radius = Math.max(0, (safeHeight - y) / 4);

            final int yy = base.getY() + y;
            if (yy < bounds.minY || yy > bounds.maxY) {
                continue;
            }

            final int minX = Math.max(base.getX() - radius, bounds.minX);
            final int maxX = Math.min(base.getX() + radius, bounds.maxX);
            final int minZ = Math.max(base.getZ() - radius, bounds.minZ);
            final int maxZ = Math.min(base.getZ() + radius, bounds.maxZ);

            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    mutable.set(x, yy, z);

                    final BlockState current = level.getBlockState(mutable);
                    if (current.isAir() || current.canBeReplaced()) {
                        level.setBlock(mutable, state, 2);
                    }
                }
            }
        }
    }

    public static int surfaceY(final WorldGenLevel level, final int x, final int z) {
        int safeX = x;
        int safeZ = z;

        if (level instanceof WorldGenRegion region) {
            final ChunkPos center = region.getCenter();
            safeX = Mth.clamp(x, center.getMinBlockX(), center.getMaxBlockX());
            safeZ = Mth.clamp(z, center.getMinBlockZ(), center.getMaxBlockZ());
        }

        return level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, safeX, safeZ);
    }

    public static int signedVariance(final String seed, final int salt, final int min, final int max) {
        final int hash = Math.abs((seed + "#" + salt).hashCode());
        return min + Math.floorMod(hash, (max - min) + 1);
    }

    private static final class Bounds {
        private final int minX;
        private final int maxX;
        private final int minY;
        private final int maxY;
        private final int minZ;
        private final int maxZ;

        private Bounds(
                final int minX,
                final int maxX,
                final int minY,
                final int maxY,
                final int minZ,
                final int maxZ
        ) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
            this.minZ = minZ;
            this.maxZ = maxZ;
        }

        private static Bounds forLevel(final WorldGenLevel level) {
            final int minY = level.getMinBuildHeight();
            final int maxY = level.getMaxBuildHeight() - 1;

            if (level instanceof WorldGenRegion region) {
                final ChunkPos center = region.getCenter();
                return new Bounds(
                        center.getMinBlockX(),
                        center.getMaxBlockX(),
                        minY,
                        maxY,
                        center.getMinBlockZ(),
                        center.getMaxBlockZ()
                );
            }

            return new Bounds(
                    Integer.MIN_VALUE / 4,
                    Integer.MAX_VALUE / 4,
                    minY,
                    maxY,
                    Integer.MIN_VALUE / 4,
                    Integer.MAX_VALUE / 4
            );
        }
    }
}