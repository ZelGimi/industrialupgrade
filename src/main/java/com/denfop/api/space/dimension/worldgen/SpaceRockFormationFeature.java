package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SpaceRockFormationFeature extends Feature<SpaceFormationFeatureConfig> {

    public SpaceRockFormationFeature(final Codec<SpaceFormationFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceFormationFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final BlockPos origin = context.origin();
        final SpaceFormationFeatureConfig cfg = context.config();

        return switch (cfg.kind()) {
            case PILLAR_FIELD -> placePillars(level, origin, random, cfg);
            case ARCH -> placeArch(level, origin, random, cfg);
            case PLATEAU -> placePlateau(level, origin, random, cfg);
            case MOUNTAIN_SPIRE -> placeMountainSpire(level, origin, random, cfg);
            case ICE_CLIFF -> placeIceCliff(level, origin, random, cfg);
            case CRYSTAL_OUTCROP -> placeCrystals(level, origin, random, cfg);
            default -> false;
        };
    }

    private boolean placePillars(final WorldGenLevel level, final BlockPos origin, final RandomSource random, final SpaceFormationFeatureConfig cfg) {
        final int count = Mth.nextInt(random, 4, 10);
        for (int i = 0; i < count; i++) {
            final int x = origin.getX() + random.nextInt(24) - 12;
            final int z = origin.getZ() + random.nextInt(24) - 12;
            final int y = SpaceFeatureUtils.surfaceY(level, x, z);
            final int h = Mth.nextInt(random, cfg.minHeight(), cfg.maxHeight());
            final int r = Mth.nextInt(random, cfg.minRadius(), cfg.maxRadius());

            for (int dy = 0; dy < h; dy++) {
                SpaceFeatureUtils.carveSphere(level, new BlockPos(x, y + dy, z), r, 1, r, cfg.primary(), state -> state.isAir() || state.canBeReplaced());
            }
        }
        return true;
    }

    private boolean placeArch(final WorldGenLevel level, final BlockPos origin, final RandomSource random, final SpaceFormationFeatureConfig cfg) {
        final int x1 = origin.getX() - 8 - random.nextInt(5);
        final int z1 = origin.getZ();
        final int x2 = origin.getX() + 8 + random.nextInt(5);
        final int z2 = origin.getZ() + random.nextInt(7) - 3;

        final int y1 = SpaceFeatureUtils.surfaceY(level, x1, z1);
        final int y2 = SpaceFeatureUtils.surfaceY(level, x2, z2);

        final int h1 = Mth.nextInt(random, cfg.minHeight(), cfg.maxHeight());
        final int h2 = Mth.nextInt(random, cfg.minHeight(), cfg.maxHeight());

        for (int i = 0; i < h1; i++) {
            SpaceFeatureUtils.carveSphere(level, new BlockPos(x1, y1 + i, z1), cfg.minRadius(), 1, cfg.minRadius(), cfg.primary(), s -> s.isAir() || s.canBeReplaced());
        }
        for (int i = 0; i < h2; i++) {
            SpaceFeatureUtils.carveSphere(level, new BlockPos(x2, y2 + i, z2), cfg.minRadius(), 1, cfg.minRadius(), cfg.primary(), s -> s.isAir() || s.canBeReplaced());
        }

        final BlockPos bridgeStart = new BlockPos(x1, y1 + h1 - 1, z1);
        final BlockPos bridgeEnd = new BlockPos(x2, y2 + h2 - 1, z2);
        SpaceFeatureUtils.lineOfSpheres(level, bridgeStart, bridgeEnd.above(4), cfg.minRadius(), cfg.primary(), s -> s.isAir() || s.canBeReplaced());

        final int carveRadius = Math.max(2, cfg.minRadius());
        SpaceFeatureUtils.lineOfSpheres(level, bridgeStart.above(1), bridgeEnd.above(3), carveRadius, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), s -> !s.isAir());

        return true;
    }

    private boolean placePlateau(final WorldGenLevel level, final BlockPos origin, final RandomSource random, final SpaceFormationFeatureConfig cfg) {
        final int radius = Mth.nextInt(random, cfg.maxRadius() + 4, cfg.maxRadius() + 8);
        final int thickness = Mth.nextInt(random, 4, 8);
        final int centerY = SpaceFeatureUtils.surfaceY(level, origin.getX(), origin.getZ()) + Mth.nextInt(random, 4, 10);

        for (int y = 0; y < thickness; y++) {
            final int currentRadius = Math.max(2, radius - y / 2);
            SpaceFeatureUtils.carveSphere(level, new BlockPos(origin.getX(), centerY + y, origin.getZ()),
                    currentRadius, 1, currentRadius, cfg.primary(), s -> s.isAir() || s.canBeReplaced());
        }

        return true;
    }

    private boolean placeMountainSpire(final WorldGenLevel level, final BlockPos origin, final RandomSource random, final SpaceFormationFeatureConfig cfg) {
        final int x = origin.getX();
        final int z = origin.getZ();
        final int y = SpaceFeatureUtils.surfaceY(level, x, z);
        final int h = Mth.nextInt(random, cfg.maxHeight() + 8, cfg.maxHeight() + 20);

        for (int dy = 0; dy < h; dy++) {
            final int radius = Math.max(1, cfg.maxRadius() - (dy / 4));
            SpaceFeatureUtils.carveSphere(level, new BlockPos(x, y + dy, z), radius, 1, radius, cfg.primary(), s -> s.isAir() || s.canBeReplaced());
        }

        return true;
    }

    private boolean placeIceCliff(final WorldGenLevel level, final BlockPos origin, final RandomSource random, final SpaceFormationFeatureConfig cfg) {
        final int width = Mth.nextInt(random, cfg.maxRadius() + 5, cfg.maxRadius() + 10);
        final int height = Mth.nextInt(random, cfg.maxHeight(), cfg.maxHeight() + 10);
        final int baseY = SpaceFeatureUtils.surfaceY(level, origin.getX(), origin.getZ());

        for (int x = -width; x <= width; x++) {
            final int localHeight = height - Math.abs(x / 2);
            for (int y = 0; y < localHeight; y++) {
                for (int z = -2; z <= 2; z++) {
                    final BlockPos pos = new BlockPos(origin.getX() + x, baseY + y, origin.getZ() + z);
                    if (level.getBlockState(pos).isAir() || level.getBlockState(pos).canBeReplaced()) {
                        level.setBlock(pos, (y % 3 == 0) ? cfg.secondary() : cfg.primary(), 2);
                    }
                }
            }
        }

        return true;
    }

    private boolean placeCrystals(final WorldGenLevel level, final BlockPos origin, final RandomSource random, final SpaceFormationFeatureConfig cfg) {
        final int count = Mth.nextInt(random, 6, 14);
        for (int i = 0; i < count; i++) {
            final int x = origin.getX() + random.nextInt(18) - 9;
            final int z = origin.getZ() + random.nextInt(18) - 9;
            final int y = SpaceFeatureUtils.surfaceY(level, x, z);
            final int h = Mth.nextInt(random, cfg.minHeight(), cfg.maxHeight());
            SpaceFeatureUtils.placeSpike(level, new BlockPos(x, y, z), h, (i % 3 == 0) ? cfg.tertiary() : cfg.primary());
        }
        return true;
    }
}