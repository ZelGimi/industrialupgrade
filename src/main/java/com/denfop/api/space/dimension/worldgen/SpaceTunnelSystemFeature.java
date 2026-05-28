package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.function.Predicate;

public class SpaceTunnelSystemFeature extends Feature<SpaceCaveFeatureConfig> {

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    public SpaceTunnelSystemFeature(final Codec<SpaceCaveFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceCaveFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final BlockPos origin = context.origin();
        final SpaceCaveFeatureConfig cfg = context.config();
        if (random.nextFloat() < 0.95)
            return false;
        final Predicate<BlockState> replaceable = state ->
                SpaceFeatureUtils.isReplaceableForCarving(state, cfg.primary(), cfg.secondary(), cfg.tertiary());

        final SafeChunkBox safeBox = SafeChunkBox.forOrigin(origin, level, Math.max(2, cfg.maxRadius() + 2));

        return switch (cfg.kind()) {
            case STONE_TUNNELS -> placeStoneTunnels(level, origin, random, cfg, replaceable, safeBox);
            case HALL_SYSTEM -> placeHallSystem(level, origin, random, cfg, replaceable, safeBox);
            case VERTICAL_SHAFT -> placeVerticalShaft(level, origin, random, cfg, replaceable, safeBox);
            default -> false;
        };
    }

    private boolean placeStoneTunnels(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final SafeChunkBox safeBox
    ) {
        final int systems = Mth.nextInt(random, 2, 4);

        for (int i = 0; i < systems; i++) {
            BlockPos current = safeBox.randomInside(
                    random,
                    safeBox.safeRadiusX(Math.max(2, cfg.maxRadius())),
                    Math.max(2, Math.min(cfg.maxVertical(), 10)),
                    safeBox.safeRadiusZ(Math.max(2, cfg.maxRadius()))
            ).offset(
                    random.nextInt(7) - 3,
                    random.nextInt(Math.max(3, cfg.maxVertical() / 2 + 1)) - Math.max(1, cfg.maxVertical() / 4),
                    random.nextInt(7) - 3
            );
            current = safeBox.clamp(current, Math.max(2, cfg.maxRadius()));

            float yaw = random.nextFloat() * ((float) Math.PI * 2.0F);
            float pitch = (random.nextFloat() - 0.5F) * 0.16F;
            final int steps = Mth.nextInt(
                    random,
                    Math.max(cfg.minLength(), 12),
                    Math.max(cfg.minLength() + 2, cfg.maxLength())
            );

            for (int step = 0; step < steps; step++) {
                final int radius = Mth.clamp(
                        Mth.floor(Mth.lerp(random.nextFloat(), cfg.minRadius(), cfg.maxRadius())),
                        1,
                        Math.max(1, cfg.maxRadius())
                );

                final BlockPos carvedCenter = safeBox.clamp(current, radius);
                carveLocalSphere(level, carvedCenter, radius, Math.max(1, radius - 1), radius, AIR, replaceable, safeBox);

                if (step % 6 == 0 && random.nextBoolean()) {
                    final float branchYaw = yaw + (random.nextBoolean() ? 1.0F : -1.0F) * (0.7F + random.nextFloat() * 0.6F);
                    carveShortBranch(level, carvedCenter, branchYaw, pitch * 0.5F, random, cfg, replaceable, safeBox);
                }

                yaw += (random.nextFloat() - 0.5F) * 0.28F;
                pitch = Mth.clamp(pitch + (random.nextFloat() - 0.5F) * 0.08F, -0.45F, 0.45F);

                final double dx = Mth.cos(yaw) * 1.8D;
                final double dy = pitch * 1.4D;
                final double dz = Mth.sin(yaw) * 1.8D;

                current = safeBox.clamp(
                        carvedCenter.offset(
                                Mth.floor(dx),
                                Mth.floor(dy),
                                Mth.floor(dz)
                        ),
                        radius
                );
            }
        }

        return true;
    }

    private boolean placeHallSystem(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final SafeChunkBox safeBox
    ) {
        final int halls = Mth.nextInt(random, 3, 5);
        BlockPos previous = null;

        for (int i = 0; i < halls; i++) {
            final int radius = Mth.clamp(
                    Mth.nextInt(random, Math.max(cfg.minRadius() + 1, cfg.maxRadius() - 1), cfg.maxRadius() + 1),
                    Math.max(2, cfg.minRadius()),
                    Math.max(2, cfg.maxRadius() + 1)
            );

            BlockPos center = safeBox.randomInside(
                    random,
                    safeBox.safeRadiusX(radius),
                    Math.max(2, Math.min(cfg.maxVertical(), 10)),
                    safeBox.safeRadiusZ(radius)
            ).offset(
                    random.nextInt(9) - 4,
                    random.nextInt(Math.max(3, cfg.maxVertical() / 2 + 1)) - Math.max(1, cfg.maxVertical() / 4),
                    random.nextInt(9) - 4
            );
            center = safeBox.clamp(center, radius);

            carveLocalSphere(level, center, radius + 1, radius, radius + 1, AIR, replaceable, safeBox);
            paintLocalShell(level, center, radius + 1, radius, radius + 1, cfg.secondary(), replaceable, safeBox);

            if (previous != null) {
                carveSafeLine(level, previous, center, Math.max(2, cfg.minRadius()), AIR, replaceable, safeBox);
            }

            previous = center;
        }

        return true;
    }

    private boolean placeVerticalShaft(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final SafeChunkBox safeBox
    ) {
        final int radius = Mth.clamp(
                Mth.nextInt(random, cfg.minRadius(), Math.max(cfg.minRadius(), cfg.maxRadius())),
                1,
                Math.max(1, cfg.maxRadius())
        );

        final BlockPos shaftOrigin = safeBox.clamp(origin, radius + 1);

        final int topY = cfg.openToSurface()
                ? Mth.clamp(
                SpaceFeatureUtils.surfaceY(level, shaftOrigin.getX(), shaftOrigin.getZ()) - 1,
                level.getMinBuildHeight() + radius + 2,
                level.getMaxBuildHeight() - radius - 3
        )
                : Mth.clamp(
                shaftOrigin.getY(),
                level.getMinBuildHeight() + radius + 2,
                level.getMaxBuildHeight() - radius - 3
        );

        final int maxDepthByWorld = Math.max(1, topY - (level.getMinBuildHeight() + radius + 1));
        final int depth = Math.min(
                Mth.nextInt(random, Math.max(cfg.minLength(), 8), Math.max(cfg.minLength() + 1, cfg.maxLength())),
                maxDepthByWorld
        );

        for (int y = 0; y < depth; y++) {
            final BlockPos center = new BlockPos(shaftOrigin.getX(), topY - y, shaftOrigin.getZ());
            final BlockState fill = (cfg.flooded() && y > depth - 5) ? cfg.fluid() : AIR;

            carveLocalSphere(level, center, radius, 1, radius, fill, replaceable, safeBox);

            if (y % 8 == 0) {
                paintLocalShell(level, center, radius + 1, 2, radius + 1, cfg.secondary(), replaceable, safeBox);
            }
        }

        return true;
    }

    private void carveShortBranch(
            final WorldGenLevel level,
            final BlockPos start,
            final float yaw,
            final float pitch,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final SafeChunkBox safeBox
    ) {
        BlockPos current = start;
        final int steps = Mth.nextInt(random, 4, 8);
        final int radius = Math.max(1, cfg.minRadius());

        for (int i = 0; i < steps; i++) {
            final BlockPos center = safeBox.clamp(current, radius);
            carveLocalSphere(level, center, radius, Math.max(1, radius - 1), radius, AIR, replaceable, safeBox);

            final double dx = Mth.cos(yaw) * 1.5D;
            final double dy = pitch * 1.0D;
            final double dz = Mth.sin(yaw) * 1.5D;

            current = safeBox.clamp(
                    center.offset(Mth.floor(dx), Mth.floor(dy), Mth.floor(dz)),
                    radius
            );
        }
    }

    private void carveSafeLine(
            final WorldGenLevel level,
            final BlockPos from,
            final BlockPos to,
            final int radius,
            final BlockState fill,
            final Predicate<BlockState> replaceable,
            final SafeChunkBox safeBox
    ) {
        final int dx = to.getX() - from.getX();
        final int dy = to.getY() - from.getY();
        final int dz = to.getZ() - from.getZ();
        final int steps = Math.max(1, Math.max(Math.abs(dx), Math.max(Math.abs(dy), Math.abs(dz))));

        for (int i = 0; i <= steps; i++) {
            final float t = i / (float) steps;
            final int x = Mth.floor(Mth.lerp(t, from.getX(), to.getX()));
            final int y = Mth.floor(Mth.lerp(t, from.getY(), to.getY()));
            final int z = Mth.floor(Mth.lerp(t, from.getZ(), to.getZ()));
            final BlockPos center = safeBox.clamp(new BlockPos(x, y, z), radius);
            carveLocalSphere(level, center, radius, Math.max(1, radius - 1), radius, fill, replaceable, safeBox);
        }
    }

    private void carveLocalSphere(
            final WorldGenLevel level,
            final BlockPos center,
            final int rx,
            final int ry,
            final int rz,
            final BlockState fill,
            final Predicate<BlockState> replaceable,
            final SafeChunkBox safeBox
    ) {
        final int minX = Math.max(center.getX() - rx, safeBox.minX);
        final int maxX = Math.min(center.getX() + rx, safeBox.maxX);
        final int minY = Math.max(center.getY() - ry, level.getMinBuildHeight());
        final int maxY = Math.min(center.getY() + ry, level.getMaxBuildHeight() - 1);
        final int minZ = Math.max(center.getZ() - rz, safeBox.minZ);
        final int maxZ = Math.min(center.getZ() + rz, safeBox.maxZ);

        for (int x = minX; x <= maxX; x++) {
            final double nx = (x - center.getX()) / (double) rx;
            final double nx2 = nx * nx;

            for (int y = minY; y <= maxY; y++) {
                final double ny = ry <= 0 ? 0.0D : (y - center.getY()) / (double) ry;
                final double ny2 = ny * ny;

                for (int z = minZ; z <= maxZ; z++) {
                    final double nz = (z - center.getZ()) / (double) rz;
                    final double nz2 = nz * nz;

                    if (nx2 + ny2 + nz2 > 1.0D) {
                        continue;
                    }

                    final BlockPos pos = new BlockPos(x, y, z);
                    if (!safeBox.contains(pos)) {
                        continue;
                    }

                    final BlockState current = level.getBlockState(pos);
                    if (!replaceable.test(current)) {
                        continue;
                    }

                    level.setBlock(pos, fill, 2);
                }
            }
        }
    }

    private void paintLocalShell(
            final WorldGenLevel level,
            final BlockPos center,
            final int rx,
            final int ry,
            final int rz,
            final BlockState shell,
            final Predicate<BlockState> replaceable,
            final SafeChunkBox safeBox
    ) {
        final int minX = Math.max(center.getX() - rx, safeBox.minX);
        final int maxX = Math.min(center.getX() + rx, safeBox.maxX);
        final int minY = Math.max(center.getY() - ry, level.getMinBuildHeight());
        final int maxY = Math.min(center.getY() + ry, level.getMaxBuildHeight() - 1);
        final int minZ = Math.max(center.getZ() - rz, safeBox.minZ);
        final int maxZ = Math.min(center.getZ() + rz, safeBox.maxZ);

        for (int x = minX; x <= maxX; x++) {
            final double nx = (x - center.getX()) / (double) rx;
            final double nx2 = nx * nx;

            for (int y = minY; y <= maxY; y++) {
                final double ny = ry <= 0 ? 0.0D : (y - center.getY()) / (double) ry;
                final double ny2 = ny * ny;

                for (int z = minZ; z <= maxZ; z++) {
                    final double nz = (z - center.getZ()) / (double) rz;
                    final double d = nx2 + ny2 + nz * nz;

                    if (d < 1.0D || d > 1.18D) {
                        continue;
                    }

                    final BlockPos pos = new BlockPos(x, y, z);
                    if (!safeBox.contains(pos)) {
                        continue;
                    }

                    final BlockState state = level.getBlockState(pos);
                    if (!replaceable.test(state)) {
                        continue;
                    }

                    level.setBlock(pos, shell, 2);
                }
            }
        }
    }

    private static final class SafeChunkBox {
        private final int minX;
        private final int maxX;
        private final int minZ;
        private final int maxZ;
        private final int minY;
        private final int maxY;

        private SafeChunkBox(
                final int minX,
                final int maxX,
                final int minZ,
                final int maxZ,
                final int minY,
                final int maxY
        ) {
            this.minX = minX;
            this.maxX = maxX;
            this.minZ = minZ;
            this.maxZ = maxZ;
            this.minY = minY;
            this.maxY = maxY;
        }

        public static SafeChunkBox forOrigin(final BlockPos origin, final WorldGenLevel level, final int padding) {
            final ChunkPos chunkPos = new ChunkPos(origin);
            final int chunkMinX = chunkPos.getMinBlockX();
            final int chunkMaxX = chunkPos.getMaxBlockX();
            final int chunkMinZ = chunkPos.getMinBlockZ();
            final int chunkMaxZ = chunkPos.getMaxBlockZ();

            final int safeMinX = chunkMinX + padding;
            final int safeMaxX = chunkMaxX - padding;
            final int safeMinZ = chunkMinZ + padding;
            final int safeMaxZ = chunkMaxZ - padding;

            final int fixedMinX = Math.min(safeMinX, safeMaxX);
            final int fixedMaxX = Math.max(safeMinX, safeMaxX);
            final int fixedMinZ = Math.min(safeMinZ, safeMaxZ);
            final int fixedMaxZ = Math.max(safeMinZ, safeMaxZ);

            return new SafeChunkBox(
                    fixedMinX,
                    fixedMaxX,
                    fixedMinZ,
                    fixedMaxZ,
                    level.getMinBuildHeight() + 1,
                    level.getMaxBuildHeight() - 2
            );
        }

        public boolean contains(final BlockPos pos) {
            return pos.getX() >= minX && pos.getX() <= maxX
                    && pos.getZ() >= minZ && pos.getZ() <= maxZ
                    && pos.getY() >= minY && pos.getY() <= maxY;
        }

        public BlockPos clamp(final BlockPos pos, final int radius) {
            final int r = Math.max(1, radius);
            final int x = Mth.clamp(pos.getX(), minX + r, maxX - r);
            final int y = Mth.clamp(pos.getY(), minY + 1, maxY - 1);
            final int z = Mth.clamp(pos.getZ(), minZ + r, maxZ - r);
            return new BlockPos(x, y, z);
        }

        public BlockPos randomInside(final RandomSource random, final int radiusX, final int radiusY, final int radiusZ) {
            final int xMin = Math.min(minX + radiusX, maxX - radiusX);
            final int xMax = Math.max(minX + radiusX, maxX - radiusX);
            final int zMin = Math.min(minZ + radiusZ, maxZ - radiusZ);
            final int zMax = Math.max(minZ + radiusZ, maxZ - radiusZ);
            final int yMin = minY + Math.max(1, radiusY);
            final int yMax = maxY - Math.max(1, radiusY);

            final int x = xMin >= xMax ? xMin : Mth.nextInt(random, xMin, xMax);
            final int y = yMin >= yMax ? yMin : Mth.nextInt(random, yMin, yMax);
            final int z = zMin >= zMax ? zMin : Mth.nextInt(random, zMin, zMax);

            return new BlockPos(x, y, z);
        }

        public int safeRadiusX(final int preferred) {
            return Math.min(preferred, Math.max(1, (maxX - minX) / 2 - 1));
        }

        public int safeRadiusZ(final int preferred) {
            return Math.min(preferred, Math.max(1, (maxZ - minZ) / 2 - 1));
        }
    }
}