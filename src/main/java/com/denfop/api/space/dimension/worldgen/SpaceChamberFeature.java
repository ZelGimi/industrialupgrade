package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.function.Predicate;

public class SpaceChamberFeature extends Feature<SpaceCaveFeatureConfig> {

    public SpaceChamberFeature(final Codec<SpaceCaveFeatureConfig> codec) {
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

        return switch (cfg.kind()) {
            case VOLCANIC_CHAMBER -> placeVolcanic(level, origin, random, cfg, replaceable);
            case FROZEN_CHAMBER -> placeFrozen(level, origin, random, cfg, replaceable);
            case THERMAL_CHAMBER -> placeThermal(level, origin, random, cfg, replaceable);
            case LIQUID_CHAMBER -> placeLiquid(level, origin, random, cfg, replaceable);
            case CRATER_LINKED_CHAMBER -> placeCraterLinked(level, origin, random, cfg, replaceable);
            default -> false;
        };
    }

    private boolean placeVolcanic(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable
    ) {
        final int rx = Mth.nextInt(random, cfg.maxRadius(), cfg.maxRadius() + 4);
        final int ry = Math.max(4, rx - 2);
        final int rz = Mth.nextInt(random, cfg.maxRadius(), cfg.maxRadius() + 4);

        SpaceFeatureUtils.carveSphere(level, origin, rx, ry, rz, Blocks.AIR.defaultBlockState(), replaceable);
        SpaceFeatureUtils.paintShell(level, origin, rx + 1, ry + 1, rz + 1, cfg.secondary(), replaceable);
        SpaceFeatureUtils.carveSphere(level, origin.below(ry / 2), rx / 2, 2, rz / 2, cfg.fluid(), replaceable);

        final int vents = Mth.nextInt(random, 3, 7);
        for (int i = 0; i < vents; i++) {
            final BlockPos vent = origin.offset(random.nextInt(rx * 2) - rx, -ry + 1, random.nextInt(rz * 2) - rz);
            final int height = Mth.nextInt(random, 4, 10);
            SpaceFeatureUtils.placeSpike(level, vent, height, cfg.secondary());
        }

        return true;
    }

    private boolean placeFrozen(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable
    ) {
        final int rx = Mth.nextInt(random, cfg.maxRadius(), cfg.maxRadius() + 3);
        final int ry = rx;
        final int rz = Mth.nextInt(random, cfg.maxRadius(), cfg.maxRadius() + 3);

        SpaceFeatureUtils.carveSphere(level, origin, rx, ry, rz, Blocks.AIR.defaultBlockState(), replaceable);
        SpaceFeatureUtils.paintShell(level, origin, rx + 1, ry + 1, rz + 1, cfg.secondary(), replaceable);

        final int spikes = Mth.nextInt(random, 8, 20);
        for (int i = 0; i < spikes; i++) {
            final int dx = random.nextInt(rx * 2 + 1) - rx;
            final int dz = random.nextInt(rz * 2 + 1) - rz;
            final BlockPos floor = origin.offset(dx, -ry + 1, dz);
            final BlockPos ceil = origin.offset(dx, ry - 2, dz);
            SpaceFeatureUtils.placeSpike(level, floor, Mth.nextInt(random, 3, 8), cfg.secondary());
            downwardSpike(level, ceil, Mth.nextInt(random, 3, 8), cfg.secondary());
        }

        return true;
    }

    private void downwardSpike(final WorldGenLevel level, final BlockPos top, final int height, final BlockState state) {
        for (int y = 0; y < height; y++) {
            final BlockPos pos = top.below(y);
            if (level.getBlockState(pos).isAir() || level.getBlockState(pos).canBeReplaced()) {
                level.setBlock(pos, state, 2);
            }
        }
    }

    private boolean placeThermal(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable
    ) {
        final int rx = Mth.nextInt(random, cfg.minRadius() + 2, cfg.maxRadius() + 2);
        final int ry = Mth.nextInt(random, 4, Math.max(5, cfg.maxRadius()));
        final int rz = Mth.nextInt(random, cfg.minRadius() + 2, cfg.maxRadius() + 2);

        SpaceFeatureUtils.carveSphere(level, origin, rx, ry, rz, Blocks.AIR.defaultBlockState(), replaceable);
        SpaceFeatureUtils.paintShell(level, origin, rx + 1, ry + 1, rz + 1, cfg.secondary(), replaceable);

        final int pools = Mth.nextInt(random, 2, 4);
        for (int i = 0; i < pools; i++) {
            final BlockPos pool = origin.offset(random.nextInt(rx * 2) - rx, -ry + 1, random.nextInt(rz * 2) - rz);
            SpaceFeatureUtils.carveSphere(level, pool, 2, 1, 2, cfg.fluid(), replaceable);
        }

        return true;
    }

    private boolean placeLiquid(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable
    ) {
        final int rx = Mth.nextInt(random, cfg.minRadius() + 1, cfg.maxRadius() + 2);
        final int ry = Mth.nextInt(random, 4, cfg.maxRadius() + 1);
        final int rz = Mth.nextInt(random, cfg.minRadius() + 1, cfg.maxRadius() + 2);

        SpaceFeatureUtils.carveSphere(level, origin, rx, ry, rz, Blocks.AIR.defaultBlockState(), replaceable);
        SpaceFeatureUtils.paintShell(level, origin, rx + 1, ry + 1, rz + 1, cfg.secondary(), replaceable);

        final int waterline = origin.getY() - (ry / 2);
        for (int y = -ry; y <= 0; y++) {
            if (origin.getY() + y > waterline) {
                continue;
            }
            SpaceFeatureUtils.carveSphere(level, origin.offset(0, y, 0), rx - 1, 1, rz - 1, cfg.fluid(), replaceable);
        }

        return true;
    }

    private boolean placeCraterLinked(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable
    ) {
        final int surface = SpaceFeatureUtils.surfaceY(level, origin.getX(), origin.getZ());
        final BlockPos craterCenter = new BlockPos(origin.getX(), surface - 1, origin.getZ());
        final int craterRadius = Mth.nextInt(random, cfg.maxRadius(), cfg.maxRadius() + 4);

        SpaceFeatureUtils.carveSphere(level, craterCenter, craterRadius + 2, Math.max(3, craterRadius / 2), craterRadius + 2, Blocks.AIR.defaultBlockState(), replaceable);
        SpaceFeatureUtils.paintShell(level, craterCenter, craterRadius + 3, Math.max(4, craterRadius / 2 + 1), craterRadius + 3, cfg.secondary(), replaceable);

        final BlockPos chamberCenter = craterCenter.below(Mth.nextInt(random, 16, 28));
        SpaceFeatureUtils.carveSphere(level, chamberCenter, craterRadius, Math.max(4, craterRadius / 2), craterRadius, Blocks.AIR.defaultBlockState(), replaceable);
        SpaceFeatureUtils.lineOfSpheres(level, craterCenter.below(1), chamberCenter, Math.max(2, craterRadius / 4), Blocks.AIR.defaultBlockState(), replaceable);

        return true;
    }
}