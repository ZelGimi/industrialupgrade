package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.SpaceBodyProfiles;
import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.denfop.api.space.dimension.SpaceGenerationMode;
import com.denfop.blocks.FluidName;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.FluidState;

public final class SpaceRegionalHydrologyFeature extends Feature<SpaceBodyFeatureConfig> {

    private static final int REGION_SIZE_CHUNKS = 18;

    private static final int TYPE_NONE = 0;
    private static final int TYPE_OCEAN = 1;
    private static final int TYPE_RIVER = 2;

    private static final double OCEAN_WATER_RADIUS = 1.00D;
    private static final double OCEAN_SHELF_RADIUS = 1.08D;
    private static final double OCEAN_BANK_RADIUS = 1.22D;
    private static final double OCEAN_SMOOTH_RADIUS = 1.42D;


    private static final int OCEAN_MAX_TERRAIN_DELTA = 18;

    private static final int OCEAN_MAX_CENTER_CUT = 6;
    private static final int OCEAN_MAX_EDGE_CUT = 3;

    public SpaceRegionalHydrologyFeature(final Codec<SpaceBodyFeatureConfig> codec) {
        super(codec);
    }

    private static int pickHydrologyType(final RandomSource random) {
        final int roll = random.nextInt(100);

        if (roll < 76) {
            return TYPE_NONE;
        }
        if (roll < 86) {
            return TYPE_OCEAN;
        }
        return TYPE_RIVER;
    }

    private static OceanTerrainStats sampleOceanTerrainStats(
            final WorldGenLevel level,
            final int centerX,
            final int centerZ,
            final int radiusX,
            final int radiusZ,
            final double radiusFactor,
            final int fallback
    ) {
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int sumY = 0;
        int count = 0;

        final int sampleRadiusX = Mth.ceil(radiusX * radiusFactor);
        final int sampleRadiusZ = Mth.ceil(radiusZ * radiusFactor);

        for (int dx = -sampleRadiusX; dx <= sampleRadiusX; dx += 6) {
            for (int dz = -sampleRadiusZ; dz <= sampleRadiusZ; dz += 6) {
                final double nx = dx / (double) radiusX;
                final double nz = dz / (double) radiusZ;
                final double radial = Math.sqrt(nx * nx + nz * nz);

                if (radial > radiusFactor) {
                    continue;
                }

                final int x = centerX + dx;
                final int z = centerZ + dz;

                if (!hasChunkAt(level, x, z)) {
                    continue;
                }

                final int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z);

                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
                sumY += y;
                count++;
            }
        }

        if (count <= 0) {
            return new OceanTerrainStats(false, fallback, fallback, fallback);
        }

        return new OceanTerrainStats(true, minY, maxY, sumY / count);
    }

    private static double cubicBezier(
            final double p0,
            final double p1,
            final double p2,
            final double p3,
            final double t
    ) {
        final double it = 1.0D - t;
        return it * it * it * p0
                + 3.0D * it * it * t * p1
                + 3.0D * it * t * t * p2
                + t * t * t * p3;
    }

    private static boolean ellipseIntersectsChunkExpanded(
            final int centerX,
            final int centerZ,
            final int radiusX,
            final int radiusZ,
            final double expandFactor,
            final int chunkMinX,
            final int chunkMinZ,
            final int chunkMaxX,
            final int chunkMaxZ
    ) {
        final int nearestX = Mth.clamp(centerX, chunkMinX, chunkMaxX);
        final int nearestZ = Mth.clamp(centerZ, chunkMinZ, chunkMaxZ);

        final double nx = (nearestX - centerX) / (radiusX * expandFactor);
        final double nz = (nearestZ - centerZ) / (radiusZ * expandFactor);
        return (nx * nx + nz * nz) <= 1.0D;
    }

    private static void placeFluidBlock(
            final WorldGenLevel level,
            final BlockPos pos,
            final BlockState fluidBlock
    ) {
        if (!hasChunkAt(level, pos.getX(), pos.getZ())) {
            return;
        }

        level.setBlock(pos, fluidBlock, 2);
        level.getChunk(pos).markPosForPostprocessing(pos);

        final FluidState ownFluid = fluidBlock.getFluidState();
        if (!ownFluid.isEmpty()) {
            level.scheduleTick(pos, ownFluid.getType(), 0);
        }

        for (final Direction direction : Direction.values()) {
            final BlockPos side = pos.relative(direction);
            if (!hasChunkAt(level, side.getX(), side.getZ())) {
                continue;
            }

            level.getChunk(side).markPosForPostprocessing(side);

            final FluidState sideFluid = level.getFluidState(side);
            if (!sideFluid.isEmpty()) {
                level.scheduleTick(side, sideFluid.getType(), 1);
            }
        }
    }

    private static void safeSetBlock(
            final WorldGenLevel level,
            final BlockPos pos,
            final BlockState state
    ) {
        if (!hasChunkAt(level, pos.getX(), pos.getZ())) {
            return;
        }

        level.setBlock(pos, state, 2);
        level.getChunk(pos).markPosForPostprocessing(pos);
    }

    private static boolean hasChunkAt(
            final WorldGenLevel level,
            final int blockX,
            final int blockZ
    ) {
        return level.hasChunk(
                SectionPos.blockToSectionCoord(blockX),
                SectionPos.blockToSectionCoord(blockZ)
        );
    }

    private static BlockState resolveHydrologyFluid(final SpaceDimensionProfile profile) {
        if (profile.defaultFluid() != null && !profile.defaultFluid().isAir()) {
            return profile.defaultFluid();
        }

        try {
            return FluidName.fluidpahoehoe_lava.getInstance()
                    .get()
                    .getSource()
                    .defaultFluidState()
                    .createLegacyBlock();
        } catch (final Exception ignored) {
            return Blocks.LAVA.defaultBlockState();
        }
    }

    private static BlockState firstSolid(
            final BlockState first,
            final BlockState second,
            final BlockState third,
            final BlockState fallback
    ) {
        if (first != null && !first.isAir()) {
            return first;
        }
        if (second != null && !second.isAir()) {
            return second;
        }
        if (third != null && !third.isAir()) {
            return third;
        }
        return fallback;
    }

    private static boolean isVolcanic(final SpaceDimensionProfile profile) {
        final SpaceGenerationMode mode = profile.generationMode();

        return mode == SpaceGenerationMode.VOLCANIC
                || (profile.defaultBlock() != null && profile.defaultBlock().is(Blocks.BLACKSTONE))
                || (profile.defaultBlock() != null && profile.defaultBlock().is(Blocks.BASALT))
                || (profile.topBlock() != null && profile.topBlock().is(Blocks.MAGMA_BLOCK))
                || (profile.defaultFluid() != null && profile.defaultFluid().is(Blocks.LAVA));
    }

    private static long mixSeed(
            final long seed,
            final int a,
            final int b,
            final int c
    ) {
        long value = seed;
        value ^= ((long) a * 341873128712L);
        value ^= ((long) b * 132897987541L);
        value ^= ((long) c * 42317861L);
        value ^= (value >>> 33);
        value *= 0xff51afd7ed558ccdL;
        value ^= (value >>> 33);
        value *= 0xc4ceb9fe1a85ec53L;
        value ^= (value >>> 33);
        return value;
    }

    private static double smoothstep(final double edge0, final double edge1, final double x) {
        if (edge0 == edge1) {
            return x < edge0 ? 0.0D : 1.0D;
        }

        final double t = Mth.clamp((x - edge0) / (edge1 - edge0), 0.0D, 1.0D);
        return t * t * (3.0D - 2.0D * t);
    }

    private static boolean canReplaceForSupport(final BlockState state) {
        return state.isAir() || state.canBeReplaced() || !state.getFluidState().isEmpty();
    }

    private static void fillSupportDown(
            final WorldGenLevel level,
            final int x,
            final int z,
            final int topY,
            final int bottomY,
            final BlockState topBlock,
            final BlockState fillBlock
    ) {
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int y = topY; y >= bottomY; y--) {
            if (y <= level.getMinBuildHeight() + 1 || y >= level.getMaxBuildHeight() - 1) {
                continue;
            }
            if (!hasChunkAt(level, x, z)) {
                return;
            }

            pos.set(x, y, z);
            final BlockState existing = level.getBlockState(pos);

            if (canReplaceForSupport(existing)) {
                safeSetBlock(level, pos, y == topY ? topBlock : fillBlock);
            }
        }
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceBodyFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final BlockPos origin = context.origin();

        final SpaceDimensionProfile profile = SpaceBodyProfiles.byDimensionPath(
                level.getLevel().dimension().location().getPath()
        );
        if (profile == null) {
            return false;
        }

        if (!isVolcanic(profile)) {
            return false;
        }

        final BlockState fluidBlock = resolveHydrologyFluid(profile);
        if (fluidBlock == null || fluidBlock.isAir() || fluidBlock.getFluidState().isEmpty()) {
            return false;
        }

        final BlockState floorBlock = firstSolid(
                profile.subsurfaceBlock(),
                profile.defaultBlock(),
                profile.rimBlock(),
                Blocks.BLACKSTONE.defaultBlockState()
        );
        final BlockState bankBlock = firstSolid(
                profile.rimBlock(),
                profile.topBlock(),
                profile.defaultBlock(),
                floorBlock
        );

        final ChunkPos currentChunk = new ChunkPos(origin);
        final int chunkX = currentChunk.x;
        final int chunkZ = currentChunk.z;

        final int regionX = Math.floorDiv(chunkX, REGION_SIZE_CHUNKS);
        final int regionZ = Math.floorDiv(chunkZ, REGION_SIZE_CHUNKS);

        final long seed = mixSeed(
                level.getSeed(),
                regionX,
                regionZ,
                level.getLevel().dimension().location().hashCode()
        );
        final RandomSource random = RandomSource.create(seed);

        final int hydrologyType = pickHydrologyType(random);
        if (hydrologyType == TYPE_NONE) {
            return false;
        }

        if (hydrologyType == TYPE_OCEAN) {
            return carveOceanRegion(level, profile, chunkX, chunkZ, regionX, regionZ, random, fluidBlock, bankBlock, floorBlock);
        }

        if (hydrologyType == TYPE_RIVER) {
            return carveRiverSystems(level, profile, chunkX, chunkZ, regionX, regionZ, random, fluidBlock, bankBlock, profile.topBlock());
        }

        return false;
    }

    private boolean carveOceanRegion(
            final WorldGenLevel level,
            final SpaceDimensionProfile profile,
            final int currentChunkX,
            final int currentChunkZ,
            final int regionX,
            final int regionZ,
            final RandomSource random,
            final BlockState fluidBlock,
            final BlockState bankBlock,
            final BlockState floorBlock
    ) {
        final int regionStartChunkX = regionX * REGION_SIZE_CHUNKS;
        final int regionStartChunkZ = regionZ * REGION_SIZE_CHUNKS;

        final int radiusChunksX = 5 + random.nextInt(4);
        final int radiusChunksZ = 5 + random.nextInt(4);

        final int centerChunkX = regionStartChunkX + 3 + random.nextInt(Math.max(1, REGION_SIZE_CHUNKS - 6));
        final int centerChunkZ = regionStartChunkZ + 3 + random.nextInt(Math.max(1, REGION_SIZE_CHUNKS - 6));

        final int centerX = (centerChunkX << 4) + 8 + random.nextInt(9) - 4;
        final int centerZ = (centerChunkZ << 4) + 8 + random.nextInt(9) - 4;

        final int radiusX = radiusChunksX * 16 + 12 + random.nextInt(12);
        final int radiusZ = radiusChunksZ * 16 + 12 + random.nextInt(12);

        final OceanTerrainStats stats = sampleOceanTerrainStats(
                level,
                centerX,
                centerZ,
                radiusX,
                radiusZ,
                OCEAN_SMOOTH_RADIUS,
                profile.seaLevel()
        );

        if (!stats.valid) {
            return false;
        }


        if ((stats.maxY - stats.minY) > OCEAN_MAX_TERRAIN_DELTA) {
            return false;
        }

        final int oceanSurfaceY = Mth.clamp(
                stats.avgY - 1 - random.nextInt(2),
                profile.minY() + 8,
                profile.minY() + profile.height() - 12
        );

        final int chunkMinX = currentChunkX << 4;
        final int chunkMinZ = currentChunkZ << 4;
        final int chunkMaxX = chunkMinX + 15;
        final int chunkMaxZ = chunkMinZ + 15;

        if (!ellipseIntersectsChunkExpanded(
                centerX,
                centerZ,
                radiusX,
                radiusZ,
                OCEAN_SMOOTH_RADIUS,
                chunkMinX,
                chunkMinZ,
                chunkMaxX,
                chunkMaxZ
        )) {
            return false;
        }

        boolean placedAny = false;
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        final int maxOceanDepth = 6 + random.nextInt(3);
        final int shelfDepth = 2 + random.nextInt(2);
        final int bankRise = 5 + random.nextInt(3);

        for (int worldX = chunkMinX; worldX <= chunkMaxX; worldX++) {
            for (int worldZ = chunkMinZ; worldZ <= chunkMaxZ; worldZ++) {
                if (!hasChunkAt(level, worldX, worldZ)) {
                    continue;
                }

                final double nx = (worldX - centerX) / (double) radiusX;
                final double nz = (worldZ - centerZ) / (double) radiusZ;
                final double radial = Math.sqrt(nx * nx + nz * nz);

                if (radial > OCEAN_SMOOTH_RADIUS) {
                    continue;
                }

                final int terrainHeight = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, worldX, worldZ);
                final int terrainTopY = terrainHeight - 1;
                final int fluidTopY = oceanSurfaceY;


                if (radial <= OCEAN_WATER_RADIUS) {
                    final double deepAlpha = 1.0D - smoothstep(0.70D, OCEAN_WATER_RADIUS, radial);
                    final int depth = Math.max(
                            2,
                            shelfDepth + Mth.floor(deepAlpha * (maxOceanDepth - shelfDepth))
                    );
                    final int floorY = fluidTopY - depth;

                    final int maxAllowedCut = Mth.floor(Mth.lerp(
                            Math.min(1.0F, (float) radial),
                            OCEAN_MAX_CENTER_CUT,
                            OCEAN_MAX_EDGE_CUT
                    ));

                    if (terrainHeight > fluidTopY + maxAllowedCut) {
                        continue;
                    }


                    final int carveDownTo = fluidTopY + 1;
                    if (terrainHeight > carveDownTo) {
                        for (int y = terrainHeight; y >= carveDownTo; y--) {
                            pos.set(worldX, y, worldZ);
                            safeSetBlock(level, pos, Blocks.AIR.defaultBlockState());
                        }
                    }

                    pos.set(worldX, floorY, worldZ);
                    safeSetBlock(level, pos, floorBlock);

                    fillSupportDown(
                            level,
                            worldX,
                            worldZ,
                            floorY - 1,
                            Math.max(level.getMinBuildHeight() + 2, floorY - 4),
                            floorBlock,
                            floorBlock
                    );

                    for (int y = floorY + 1; y <= fluidTopY; y++) {
                        pos.set(worldX, y, worldZ);
                        placeFluidBlock(level, pos, fluidBlock);
                    }

                    if (radial >= 0.84D) {
                        final int bankTopY = fluidTopY + 1;
                        fillSupportDown(
                                level,
                                worldX,
                                worldZ,
                                bankTopY,
                                Math.max(level.getMinBuildHeight() + 2, bankTopY - 2),
                                bankBlock,
                                floorBlock
                        );
                    }

                    placedAny = true;
                    continue;
                }


                final double shoreT = smoothstep(OCEAN_WATER_RADIUS, OCEAN_SMOOTH_RADIUS, radial);
                final int desiredSlopeTopY = fluidTopY + 1 + Mth.floor(shoreT * bankRise);

                int targetTopY = desiredSlopeTopY;


                if (terrainTopY > targetTopY + 2) {
                    continue;
                }

                if (terrainHeight > targetTopY + 1) {
                    for (int y = terrainHeight; y >= targetTopY + 1; y--) {
                        pos.set(worldX, y, worldZ);
                        safeSetBlock(level, pos, Blocks.AIR.defaultBlockState());
                    }
                }

                if (radial <= OCEAN_BANK_RADIUS) {
                    fillSupportDown(
                            level,
                            worldX,
                            worldZ,
                            targetTopY,
                            Math.max(level.getMinBuildHeight() + 2, targetTopY - 3),
                            bankBlock,
                            floorBlock
                    );
                    placedAny = true;
                } else {
                    pos.set(worldX, targetTopY, worldZ);
                    final BlockState existing = level.getBlockState(pos);
                    if (canReplaceForSupport(existing)) {
                        safeSetBlock(level, pos, bankBlock);
                        placedAny = true;
                    }
                }
            }
        }

        return placedAny;
    }

    private boolean carveRiverSystems(
            final WorldGenLevel level,
            final SpaceDimensionProfile profile,
            final int currentChunkX,
            final int currentChunkZ,
            final int regionX,
            final int regionZ,
            final RandomSource random,
            final BlockState fluidBlock,
            final BlockState bankBlock,
            final BlockState floorBlock
    ) {
        boolean changed = false;

        final int riverCount = random.nextInt(100) < 22 ? 2 : 1;

        final int regionStartChunkX = regionX * REGION_SIZE_CHUNKS;
        final int regionStartChunkZ = regionZ * REGION_SIZE_CHUNKS;
        final int regionMinBlockX = regionStartChunkX << 4;
        final int regionMinBlockZ = regionStartChunkZ << 4;
        final int regionMaxBlockX = ((regionStartChunkX + REGION_SIZE_CHUNKS) << 4) - 1;
        final int regionMaxBlockZ = ((regionStartChunkZ + REGION_SIZE_CHUNKS) << 4) - 1;

        for (int riverIndex = 0; riverIndex < riverCount; riverIndex++) {
            final RandomSource riverRandom = RandomSource.create(mixSeed(random.nextLong(), riverIndex, 17, 31));

            double startX;
            double startZ;
            double endX;
            double endZ;

            final int side = riverRandom.nextInt(4);
            switch (side) {
                case 0 -> {
                    startX = regionMinBlockX - 24;
                    startZ = regionMinBlockZ + riverRandom.nextInt(REGION_SIZE_CHUNKS * 16);
                    endX = regionMaxBlockX + 24;
                    endZ = regionMinBlockZ + riverRandom.nextInt(REGION_SIZE_CHUNKS * 16);
                }
                case 1 -> {
                    startX = regionMaxBlockX + 24;
                    startZ = regionMinBlockZ + riverRandom.nextInt(REGION_SIZE_CHUNKS * 16);
                    endX = regionMinBlockX - 24;
                    endZ = regionMinBlockZ + riverRandom.nextInt(REGION_SIZE_CHUNKS * 16);
                }
                case 2 -> {
                    startX = regionMinBlockX + riverRandom.nextInt(REGION_SIZE_CHUNKS * 16);
                    startZ = regionMinBlockZ - 24;
                    endX = regionMinBlockX + riverRandom.nextInt(REGION_SIZE_CHUNKS * 16);
                    endZ = regionMaxBlockZ + 24;
                }
                default -> {
                    startX = regionMinBlockX + riverRandom.nextInt(REGION_SIZE_CHUNKS * 16);
                    startZ = regionMaxBlockZ + 24;
                    endX = regionMinBlockX + riverRandom.nextInt(REGION_SIZE_CHUNKS * 16);
                    endZ = regionMinBlockZ - 24;
                }
            }

            final double controlX1 = Mth.lerp(0.30D, startX, endX) + riverRandom.nextInt(97) - 48;
            final double controlZ1 = Mth.lerp(0.30D, startZ, endZ) + riverRandom.nextInt(97) - 48;
            final double controlX2 = Mth.lerp(0.70D, startX, endX) + riverRandom.nextInt(97) - 48;
            final double controlZ2 = Mth.lerp(0.70D, startZ, endZ) + riverRandom.nextInt(97) - 48;

            final int baseHalfWidth = 2 + riverRandom.nextInt(2);
            final int baseDepth = 2 + riverRandom.nextInt(2);
            final int steps = 96 + riverRandom.nextInt(64);

            double prevX = startX;
            double prevZ = startZ;

            for (int step = 1; step <= steps; step++) {
                final double t = step / (double) steps;
                final double x = cubicBezier(startX, controlX1, controlX2, endX, t);
                final double z = cubicBezier(startZ, controlZ1, controlZ2, endZ, t);

                final int halfWidth = baseHalfWidth + ((step % 17) == 0 ? 1 : 0);
                final int depth = baseDepth + ((step % 23) == 0 ? 1 : 0);

                if (carveRiverSegment(level, currentChunkX, currentChunkZ, prevX, prevZ, x, z, halfWidth, depth, fluidBlock, bankBlock, floorBlock)) {
                    changed = true;
                }

                prevX = x;
                prevZ = z;
            }
        }

        return changed;
    }

    private boolean carveRiverSegment(
            final WorldGenLevel level,
            final int currentChunkX,
            final int currentChunkZ,
            final double startX,
            final double startZ,
            final double endX,
            final double endZ,
            final int riverHalfWidth,
            final int depth,
            final BlockState fluidBlock,
            final BlockState bankBlock,
            final BlockState floorBlock
    ) {
        final double dx = endX - startX;
        final double dz = endZ - startZ;
        final double length = Math.max(1.0D, Math.sqrt(dx * dx + dz * dz));

        boolean changed = false;

        final int steps = Math.max(6, Mth.ceil(length));
        for (int i = 0; i <= steps; i++) {
            final double t = i / (double) steps;
            final double px = Mth.lerp(t, startX, endX);
            final double pz = Mth.lerp(t, startZ, endZ);

            if (carveRiverCrossSection(level, currentChunkX, currentChunkZ, px, pz, riverHalfWidth, depth, fluidBlock, bankBlock, floorBlock)) {
                changed = true;
            }
        }

        return changed;
    }

    private boolean carveRiverCrossSection(
            final WorldGenLevel level,
            final int currentChunkX,
            final int currentChunkZ,
            final double centerX,
            final double centerZ,
            final int riverHalfWidth,
            final int depth,
            final BlockState fluidBlock,
            final BlockState bankBlock,
            final BlockState floorBlock
    ) {
        boolean changed = false;
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        final int chunkMinX = currentChunkX << 4;
        final int chunkMinZ = currentChunkZ << 4;
        final int chunkMaxX = chunkMinX + 15;
        final int chunkMaxZ = chunkMinZ + 15;

        final int minX = Math.max(chunkMinX, Mth.floor(centerX) - riverHalfWidth - 1);
        final int maxX = Math.min(chunkMaxX, Mth.floor(centerX) + riverHalfWidth + 1);
        final int minZ = Math.max(chunkMinZ, Mth.floor(centerZ) - riverHalfWidth - 1);
        final int maxZ = Math.min(chunkMaxZ, Mth.floor(centerZ) + riverHalfWidth + 1);

        for (int worldX = minX; worldX <= maxX; worldX++) {
            for (int worldZ = minZ; worldZ <= maxZ; worldZ++) {
                if (!hasChunkAt(level, worldX, worldZ)) {
                    continue;
                }

                final double dist = Math.sqrt(
                        (worldX + 0.5D - centerX) * (worldX + 0.5D - centerX) +
                                (worldZ + 0.5D - centerZ) * (worldZ + 0.5D - centerZ)
                );

                if (dist > riverHalfWidth + 0.35D) {
                    continue;
                }

                final int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, worldX, worldZ);
                final int fluidTopY = surfaceY - 1;

                final float edge = (float) (dist / Math.max(1.0D, riverHalfWidth));
                final int localDepth = Math.max(1, depth - Mth.floor(edge * Math.max(1, depth - 1)));
                final int floorY = fluidTopY - localDepth;

                for (int y = surfaceY; y > fluidTopY; y--) {
                    pos.set(worldX, y, worldZ);
                    safeSetBlock(level, pos, Blocks.AIR.defaultBlockState());
                }

                pos.set(worldX, floorY, worldZ);
                safeSetBlock(level, pos, floorBlock);

                for (int y = floorY + 1; y <= fluidTopY; y++) {
                    pos.set(worldX, y, worldZ);
                    placeFluidBlock(level, pos, fluidBlock);
                }

                if (dist >= riverHalfWidth - 0.8D) {
                    pos.set(worldX, fluidTopY, worldZ);
                    final BlockState existing = level.getBlockState(pos);
                    if (existing.isAir()) {
                        safeSetBlock(level, pos, bankBlock);
                    }
                }

                changed = true;
            }
        }

        return changed;
    }

    private static final class OceanTerrainStats {
        private final boolean valid;
        private final int minY;
        private final int maxY;
        private final int avgY;

        private OceanTerrainStats(
                final boolean valid,
                final int minY,
                final int maxY,
                final int avgY
        ) {
            this.valid = valid;
            this.minY = minY;
            this.maxY = maxY;
            this.avgY = avgY;
        }
    }
}