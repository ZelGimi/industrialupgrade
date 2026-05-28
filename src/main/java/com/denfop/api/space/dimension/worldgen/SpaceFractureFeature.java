package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.function.Predicate;

public class SpaceFractureFeature extends Feature<SpaceCaveFeatureConfig> {

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    public SpaceFractureFeature(final Codec<SpaceCaveFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceCaveFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final BlockPos origin = context.origin();
        final SpaceCaveFeatureConfig cfg = context.config();

        if (cfg.kind() != SpaceCaveKind.FRACTURE) {
            return false;
        }
        if (random.nextFloat() < 0.98)
            return false;
        final Predicate<BlockState> replaceable = state ->
                SpaceFeatureUtils.isReplaceableForCarving(state, cfg.primary(), cfg.secondary(), cfg.tertiary());

        final LocalChunkBounds bounds = LocalChunkBounds.from(level, origin);

        final int width = Mth.clamp(
                Mth.nextInt(random, Math.max(1, cfg.minRadius()), Math.max(Math.max(1, cfg.minRadius()), cfg.maxRadius())),
                1,
                5
        );

        final int length = Mth.clamp(
                Mth.nextInt(random, Math.max(8, cfg.minLength()), Math.max(Math.max(8, cfg.minLength()), cfg.maxLength())),
                8,
                18
        );

        final int startY = cfg.openToSurface()
                ? Mth.clamp(
                SpaceFeatureUtils.surfaceY(level, origin.getX(), origin.getZ()) - 1,
                bounds.minY + 2,
                bounds.maxY - 2
        )
                : Mth.clamp(
                origin.getY(),
                bounds.minY + 2,
                bounds.maxY - 2
        );

        final int depthLimitByWorld = Math.max(6, startY - bounds.minY - 2);
        final int requestedDepth = Mth.nextInt(
                random,
                Math.max(10, Math.min(cfg.minLength(), 18)),
                Math.max(Math.max(12, cfg.minLength()), Math.min(cfg.maxLength() + 8, 32))
        );
        final int depth = Math.max(6, Math.min(requestedDepth, depthLimitByWorld));

        double currentX = Mth.clamp(origin.getX() + 0.5D, bounds.minX + width + 1.0D, bounds.maxX - width - 1.0D);
        double currentZ = Mth.clamp(origin.getZ() + 0.5D, bounds.minZ + width + 1.0D, bounds.maxZ - width - 1.0D);

        float yaw = random.nextFloat() * ((float) Math.PI * 2.0F);
        final double horizontalStep = 1.35D;

        for (int i = 0; i < length; i++) {
            final int centerX = Mth.clamp(Mth.floor(currentX), bounds.minX + width, bounds.maxX - width);
            final int centerZ = Mth.clamp(Mth.floor(currentZ), bounds.minZ + width, bounds.maxZ - width);

            for (int y = 0; y < depth; y++) {
                final int currentWidth = Math.max(1, width - (y / 12));
                final int cy = startY - y;

                if (cy < bounds.minY + 1) {
                    break;
                }

                final BlockPos center = new BlockPos(centerX, cy, centerZ);
                final BlockState carveState = (cfg.flooded() && y >= depth - 5) ? safeFluid(cfg) : AIR;

                SpaceFeatureUtils.carveSphere(level, center, currentWidth, 1, currentWidth, carveState, replaceable);

                if (y % 12 == 0) {
                    SpaceFeatureUtils.paintShell(level, center, currentWidth + 1, 2, currentWidth + 1, cfg.secondary(), replaceable);
                }
            }

            yaw += (random.nextFloat() - 0.5F) * 0.12F;

            currentX = Mth.clamp(
                    currentX + Mth.cos(yaw) * horizontalStep,
                    bounds.minX + width + 1.0D,
                    bounds.maxX - width - 1.0D
            );
            currentZ = Mth.clamp(
                    currentZ + Mth.sin(yaw) * horizontalStep,
                    bounds.minZ + width + 1.0D,
                    bounds.maxZ - width - 1.0D
            );
        }

        return true;
    }

    private BlockState safeFluid(final SpaceCaveFeatureConfig cfg) {
        return cfg.fluid().isAir() ? Blocks.WATER.defaultBlockState() : cfg.fluid();
    }

    private static final class LocalChunkBounds {
        private final int minX;
        private final int maxX;
        private final int minY;
        private final int maxY;
        private final int minZ;
        private final int maxZ;

        private LocalChunkBounds(
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

        private static LocalChunkBounds from(final WorldGenLevel level, final BlockPos origin) {
            final int minY = level.getMinBuildHeight();
            final int maxY = level.getMaxBuildHeight() - 1;

            if (level instanceof WorldGenRegion region) {
                final ChunkPos center = region.getCenter();
                return new LocalChunkBounds(
                        center.getMinBlockX(),
                        center.getMaxBlockX(),
                        minY,
                        maxY,
                        center.getMinBlockZ(),
                        center.getMaxBlockZ()
                );
            }

            final ChunkPos chunkPos = new ChunkPos(origin);
            return new LocalChunkBounds(
                    chunkPos.getMinBlockX(),
                    chunkPos.getMaxBlockX(),
                    minY,
                    maxY,
                    chunkPos.getMinBlockZ(),
                    chunkPos.getMaxBlockZ()
            );
        }
    }
}