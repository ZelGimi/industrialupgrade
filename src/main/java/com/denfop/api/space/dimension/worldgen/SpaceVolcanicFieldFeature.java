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

public class SpaceVolcanicFieldFeature extends Feature<SpaceFormationFeatureConfig> {

    public SpaceVolcanicFieldFeature(final Codec<SpaceFormationFeatureConfig> codec) {
        super(codec);
    }

    private static void fillDownToGround(final WorldGenLevel level, final BlockPos start, final BlockState state) {
        BlockPos.MutableBlockPos cursor = start.mutable();
        int limit = 24;

        while (limit-- > 0 && cursor.getY() > level.getMinBuildHeight()) {
            if (!level.getBlockState(cursor).isAir()) {
                break;
            }
            level.setBlock(cursor, state, 2);
            cursor.move(0, -1, 0);
        }
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceFormationFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final BlockPos origin = context.origin();
        final SpaceFormationFeatureConfig cfg = context.config();

        if (cfg.kind() != SpaceFormationKind.VOLCANIC_FIELD) {
            return false;
        }

        final BlockState primary = cfg.primary();
        final BlockState secondary = cfg.secondary();
        final BlockState fluid = com.denfop.api.space.dimension.worldgen.feature.WorldgenSupport.toSourceFluidBlock(cfg.fluid());

        boolean placedAny = false;
        final int vents = Mth.nextInt(random, 2, 5);

        for (int i = 0; i < vents; i++) {
            final int x = origin.getX() + random.nextInt(24) - 12;
            final int z = origin.getZ() + random.nextInt(24) - 12;
            final int y = SpaceFeatureUtils.surfaceY(level, x, z);

            final int coneHeight = Mth.nextInt(random, cfg.minHeight(), cfg.maxHeight());
            final int coneRadius = Mth.nextInt(random, cfg.minRadius() + 1, cfg.maxRadius() + 2);


            for (int dx = -coneRadius; dx <= coneRadius; dx++) {
                for (int dz = -coneRadius; dz <= coneRadius; dz++) {
                    if ((dx * dx + dz * dz) > coneRadius * coneRadius) {
                        continue;
                    }

                    final BlockPos base = new BlockPos(x + dx, y, z + dz);
                    fillDownToGround(level, base, primary);
                }
            }


            for (int dy = 0; dy < coneHeight; dy++) {
                final int radius = Math.max(1, coneRadius - dy / 2);

                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        if ((dx * dx + dz * dz) > radius * radius) {
                            continue;
                        }

                        final BlockPos pos = new BlockPos(x + dx, y + dy, z + dz);

                        if (dy == coneHeight - 1 && Math.abs(dx) <= 1 && Math.abs(dz) <= 1) {
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                        } else {
                            level.setBlock(pos, primary, 2);
                        }
                    }
                }
            }


            final int craterRadius = Math.max(1, coneRadius / 2);
            for (int dx = -craterRadius; dx <= craterRadius; dx++) {
                for (int dz = -craterRadius; dz <= craterRadius; dz++) {
                    if ((dx * dx + dz * dz) > craterRadius * craterRadius) {
                        continue;
                    }

                    final BlockPos crater = new BlockPos(x + dx, y + coneHeight - 1, z + dz);
                    level.setBlock(crater, Blocks.AIR.defaultBlockState(), 2);

                    final BlockPos lavaPos = crater.below();
                    com.denfop.api.space.dimension.worldgen.feature.WorldgenSupport.placeSourceAndUpdate(level, lavaPos, fluid);
                }
            }


            final int flows = Mth.nextInt(random, 3, 7);
            for (int f = 0; f < flows; f++) {
                final int len = Mth.nextInt(random, 6, 14);
                final float yaw = random.nextFloat() * ((float) Math.PI * 2F);

                int lastX = x;
                int lastZ = z;
                int currentY = y + coneHeight - 2;

                for (int step = 0; step < len; step++) {
                    final int px = x + Mth.floor(Math.cos(yaw) * step);
                    final int pz = z + Mth.floor(Math.sin(yaw) * step);

                    if (px != lastX || pz != lastZ) {
                        currentY = SpaceFeatureUtils.surfaceY(level, px, pz);
                        lastX = px;
                        lastZ = pz;
                    }

                    final BlockPos pos = new BlockPos(px, currentY, pz);

                    if (step < (len / 2)) {
                        com.denfop.api.space.dimension.worldgen.feature.WorldgenSupport.placeSourceAndUpdate(level, pos, fluid);
                    } else {
                        level.setBlock(pos, secondary, 2);
                    }

                    if (level.getBlockState(pos.below()).isAir()) {
                        fillDownToGround(level, pos.below(), secondary);
                    }
                }
            }

            placedAny = true;
        }

        return placedAny;
    }
}