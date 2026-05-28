package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.worldgen.SpaceFluidFeatureConfig;
import com.denfop.api.space.dimension.worldgen.SpaceWorldgenFluidHelper;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;

public class SpaceGenericFluidFeature extends Feature<SpaceFluidFeatureConfig> {

    public SpaceGenericFluidFeature(final Codec<SpaceFluidFeatureConfig> codec) {
        super(codec);
    }

    private static int scaledRadiusMin(final SpaceFluidFeatureConfig config) {
        return Math.max(1, Mth.ceil(config.radiusMin() * 0.1F));
    }

    private static int scaledRadiusMax(final SpaceFluidFeatureConfig config) {
        return Math.max(scaledRadiusMin(config), Mth.ceil(config.radiusMax() * 0.1F));
    }

    private static int scaledVerticalRadius(final int horizontalRadius) {
        return Math.max(1, horizontalRadius / 2);
    }

    private static int scaledVeinLength(final SpaceFluidFeatureConfig config) {
        return Math.max(4, scaledRadiusMax(config) * 2);
    }

    private static int scaledBranchCount(final RandomSource random) {
        return random.nextIntBetweenInclusive(1, 2);
    }

    private static int allowedMaxY(final SpaceFluidFeatureConfig config) {
        return config.surfaceAllowed() ? config.maxY() + 40 : config.maxY();
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceFluidFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final BlockPos origin = context.origin();
        final SpaceFluidFeatureConfig config = context.config();
        final RandomSource random = context.random();

        if (origin.getY() < config.minY() || origin.getY() > allowedMaxY(config)) {
            return false;
        }

        final ResourceLocation fluidKey = ResourceLocation.tryParse(config.fluidId());
        if (fluidKey == null) {
            return false;
        }

        final Fluid fluid = BuiltInRegistries.FLUID.get(fluidKey);
        if (fluid == Fluids.EMPTY) {
            return false;
        }

        final BlockState fluidState = fluid.defaultFluidState().createLegacyBlock();
        if (!(fluidState.getBlock() instanceof LiquidBlock)) {
            return false;
        }

        if (config.requireColdContext() && !SpaceWorldgenFluidHelper.isColdContext(level, origin)) {
            return false;
        }

        if (config.requireHotContext()
                && !SpaceWorldgenFluidHelper.isAdjacentToLava(level, origin)
                && origin.getY() > config.maxY() - 10) {
            return false;
        }

        final List<BlockPos> changed = new ArrayList<>(32);
        boolean result = placeSurfaceLake(level, origin, random, fluidState, config, changed);
        if (false) {
            result = switch (config.distributionType()) {
                case RESERVOIR -> placeReservoir(level, origin, random, fluidState, config, changed);
                case POCKET -> placePocket(level, origin, random, fluidState, config, changed);
                case VEIN -> placeVein(level, origin, random, fluidState, config, changed);
                case UNDERGROUND_LAKE -> placeUndergroundLake(level, origin, random, fluidState, config, changed);
                case SURFACE_LAKE -> placeSurfaceLake(level, origin, random, fluidState, config, changed);
                case GEOTHERMAL -> placeGeothermal(level, origin, random, fluidState, config, changed);
                case LAVA_SYSTEM -> placeLavaSystem(level, origin, random, fluidState, config, changed);
                case CRYO -> placePocket(level, origin, random, fluidState, config, changed);
                case ACID -> placeUndergroundLake(level, origin, random, fluidState, config, changed);
                case GAS -> placeGasPocket(level, origin, random, fluidState, config, changed);
            };
        }
        if (result) {
            finalizeFluidBody(level, changed, fluid);
        }

        return result;
    }

    private boolean placeReservoir(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState fluid,
            final SpaceFluidFeatureConfig config,
            final List<BlockPos> changed
    ) {
        final int radius = random.nextIntBetweenInclusive(scaledRadiusMin(config), scaledRadiusMax(config));
        final int vertical = Math.max(1, radius / 3);
        int placed = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -vertical; y <= vertical; y++) {
                for (int z = -radius; z <= radius; z++) {
                    final double noise = 1.0D + random.nextDouble() * 0.20D;
                    final double eq =
                            (x * x + z * z) / (double) (radius * radius)
                                    + (y * y) / Math.max(1.0D, (double) (vertical * vertical));

                    if (eq > noise) {
                        continue;
                    }

                    final BlockPos pos = origin.offset(x, y, z);
                    if (tryPlaceFluid(level, pos, fluid, config, false, changed)) {
                        placed++;
                    }
                }
            }
        }

        return placed > 0;
    }

    private boolean placePocket(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState fluid,
            final SpaceFluidFeatureConfig config,
            final List<BlockPos> changed
    ) {
        final int radius = random.nextIntBetweenInclusive(scaledRadiusMin(config), scaledRadiusMax(config));
        return placeSimpleEllipsoid(
                level,
                origin,
                radius,
                scaledVerticalRadius(radius),
                fluid,
                config,
                false,
                changed
        ) > 0;
    }

    private boolean placeVein(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState fluid,
            final SpaceFluidFeatureConfig config,
            final List<BlockPos> changed
    ) {
        int placed = 0;
        final double angle = random.nextDouble() * Math.PI * 2.0D;
        final double dx = Math.cos(angle);
        final double dz = Math.sin(angle);

        final int length = scaledVeinLength(config);
        final int radius = Math.max(1, scaledRadiusMin(config));

        for (int step = 0; step < length; step++) {
            final double progress = (double) step / Math.max(1, length - 1);

            final BlockPos center = new BlockPos(
                    origin.getX() + Mth.floor(dx * progress * length),
                    origin.getY() + random.nextIntBetweenInclusive(-1, 1),
                    origin.getZ() + Mth.floor(dz * progress * length)
            );

            placed += placeSimpleEllipsoid(
                    level,
                    center,
                    radius,
                    1,
                    fluid,
                    config,
                    false,
                    changed
            );
        }

        return placed > 0;
    }

    private boolean placeUndergroundLake(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState fluid,
            final SpaceFluidFeatureConfig config,
            final List<BlockPos> changed
    ) {
        final int radius = random.nextIntBetweenInclusive(
                scaledRadiusMin(config) + 1,
                scaledRadiusMax(config) + 1
        );

        return placeSimpleEllipsoid(
                level,
                origin,
                radius,
                Math.max(1, radius / 3),
                fluid,
                config,
                true,
                changed
        ) > 0;
    }

    private boolean placeSurfaceLake(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState fluid,
            final SpaceFluidFeatureConfig config,
            final List<BlockPos> changed
    ) {
        float rand = random.nextFloat();
        if (rand < 0.99)
            return false;
        rand = random.nextFloat();
        if (rand < 0.95)
            return false;
        final BlockPos surface = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, origin);
        final int radius = random.nextIntBetweenInclusive(
                scaledRadiusMin(config) + 1,
                scaledRadiusMax(config) + 1
        );

        return placeSimpleEllipsoid(
                level,
                surface.below(),
                radius,
                1,
                fluid,
                config,
                true,
                changed
        ) > 0;
    }

    private boolean placeGeothermal(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState fluid,
            final SpaceFluidFeatureConfig config,
            final List<BlockPos> changed
    ) {
        final int radius = random.nextIntBetweenInclusive(scaledRadiusMin(config), scaledRadiusMax(config));
        int placed = placeSimpleEllipsoid(level, origin, radius, 1, fluid, config, true, changed);

        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(origin.getX(), origin.getY(), origin.getZ());
        final int height = random.nextIntBetweenInclusive(2, 5);

        for (int i = 0; i < height; i++) {
            if (tryPlaceFluid(level, cursor, fluid, config, false, changed)) {
                placed++;
            }
            cursor.move(0, 1, 0);
        }

        return placed > 0;
    }

    private boolean placeLavaSystem(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState fluid,
            final SpaceFluidFeatureConfig config,
            final List<BlockPos> changed
    ) {
        final int branches = scaledBranchCount(random);
        final int spread = Math.max(2, scaledRadiusMax(config));
        int placed = 0;

        for (int i = 0; i < branches; i++) {
            final BlockPos branchOrigin = origin.offset(
                    random.nextIntBetweenInclusive(-spread, spread),
                    random.nextIntBetweenInclusive(-1, 1),
                    random.nextIntBetweenInclusive(-spread, spread)
            );

            final int radius = random.nextIntBetweenInclusive(scaledRadiusMin(config), scaledRadiusMax(config));
            placed += placeSimpleEllipsoid(level, branchOrigin, radius, 1, fluid, config, true, changed);
            placed += carveConnector(level, origin, branchOrigin, fluid, config, changed);
        }

        return placed > 0;
    }

    private boolean placeGasPocket(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState fluid,
            final SpaceFluidFeatureConfig config,
            final List<BlockPos> changed
    ) {
        final int radius = random.nextIntBetweenInclusive(scaledRadiusMin(config), scaledRadiusMax(config));
        int placed = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    final double eq =
                            (x * x + z * z) / (double) (radius * radius)
                                    + (y * y) / Math.max(1.0D, (double) (radius * radius));

                    if (eq > 1.0D) {
                        continue;
                    }

                    final BlockPos pos = origin.offset(x, y, z);

                    if (y > 0) {
                        if (level.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                            this.markAboveForPostProcessing(level, pos);
                        }
                    } else if (tryPlaceFluid(level, pos, fluid, config, false, changed)) {
                        placed++;
                    }
                }
            }
        }

        return placed > 0;
    }

    private int carveConnector(
            final WorldGenLevel level,
            final BlockPos from,
            final BlockPos to,
            final BlockState fluid,
            final SpaceFluidFeatureConfig config,
            final List<BlockPos> changed
    ) {
        final int steps = Math.max(1, (int) Math.sqrt(from.distSqr(to)));
        int placed = 0;

        for (int i = 0; i <= steps; i++) {
            final float progress = i / (float) steps;

            final BlockPos pos = new BlockPos(
                    Mth.floor(Mth.lerp(progress, from.getX(), to.getX())),
                    Mth.floor(Mth.lerp(progress, from.getY(), to.getY())),
                    Mth.floor(Mth.lerp(progress, from.getZ(), to.getZ()))
            );

            placed += placeSimpleEllipsoid(level, pos, 1, 1, fluid, config, false, changed);
        }

        return placed;
    }

    private int placeSimpleEllipsoid(
            final WorldGenLevel level,
            final BlockPos center,
            final int horizontalRadius,
            final int verticalRadius,
            final BlockState fluid,
            final SpaceFluidFeatureConfig config,
            final boolean keepAirRoof,
            final List<BlockPos> changed
    ) {
        int placed = 0;

        for (int x = -horizontalRadius; x <= horizontalRadius; x++) {
            for (int y = -verticalRadius; y <= verticalRadius; y++) {
                for (int z = -horizontalRadius; z <= horizontalRadius; z++) {
                    final double eq =
                            (x * x + z * z) / (double) (horizontalRadius * horizontalRadius)
                                    + (y * y) / Math.max(1.0D, (double) (verticalRadius * verticalRadius));

                    if (eq > 1.0D) {
                        continue;
                    }

                    final BlockPos pos = center.offset(x, y, z);

                    if (keepAirRoof && y == verticalRadius && level.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                        this.markAboveForPostProcessing(level, pos);
                        continue;
                    }

                    if (tryPlaceFluid(level, pos, fluid, config, keepAirRoof, changed)) {
                        placed++;
                    }
                }
            }
        }

        return placed;
    }

    private boolean tryPlaceFluid(
            final WorldGenLevel level,
            final BlockPos pos,
            final BlockState fluid,
            final SpaceFluidFeatureConfig config,
            final boolean keepAirRoof,
            final List<BlockPos> changed
    ) {
        if (pos.getY() < config.minY() || pos.getY() > allowedMaxY(config)) {
            return false;
        }

        if (level.isOutsideBuildHeight(pos)) {
            return false;
        }

        if (config.caveOnly() && !SpaceWorldgenFluidHelper.isAdjacentToAir(level, pos)) {
            return false;
        }

        if (!SpaceWorldgenFluidHelper.canReplaceWithFluid(level, pos)) {
            return false;
        }

        if (keepAirRoof && SpaceWorldgenFluidHelper.isAdjacentToAir(level, pos.above())) {
            if (!level.isOutsideBuildHeight(pos.above())) {
                level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 2);
                this.markAboveForPostProcessing(level, pos.above());
            }
        }

        level.setBlock(pos, fluid, 2);
        changed.add(pos.immutable());
        this.markAboveForPostProcessing(level, pos);

        return true;
    }

    private void finalizeFluidBody(
            final WorldGenLevel level,
            final List<BlockPos> changed,
            final Fluid fluid
    ) {
        if (changed.isEmpty()) {
            return;
        }

        final int step = Math.max(1, changed.size() / 24);

        for (int i = 0; i < changed.size(); i += step) {
            final BlockPos pos = changed.get(i);

            this.markAboveForPostProcessing(level, pos);
            this.markAboveForPostProcessing(level, pos.above());

            for (Direction dir : Direction.values()) {
                this.markAboveForPostProcessing(level, pos.relative(dir));
            }

            level.scheduleTick(pos, fluid, 1);

            for (Direction dir : Direction.Plane.HORIZONTAL) {
                final BlockPos neighbor = pos.relative(dir);
                final BlockState neighborState = level.getBlockState(neighbor);

                if (neighborState.isAir() || neighborState.getFluidState().getType() == fluid) {
                    level.scheduleTick(neighbor, fluid, 1);
                }
            }
        }
    }
}