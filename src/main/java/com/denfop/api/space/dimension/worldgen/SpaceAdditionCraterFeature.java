package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.function.Predicate;

import static net.minecraft.world.level.block.Blocks.AIR;

public class SpaceAdditionCraterFeature extends Feature<SpaceCraterFeatureConfig> {

    public SpaceAdditionCraterFeature(final Codec<SpaceCraterFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceCraterFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final BlockPos origin = context.origin();
        if (context.random().nextDouble() < 0.9)
            return false;
        final SpaceCraterFeatureConfig cfg = context.config();

        final Predicate<BlockState> replaceable = state ->
                SpaceFeatureUtils.isReplaceableForCarving(state, cfg.primary(), cfg.primary(), cfg.primary());

        final ChunkBounds bounds = ChunkBounds.from(level, origin);
        final long worldSeed = level.getSeed();

        final int searchChunks = Math.max(1, Mth.ceil(cfg.giantMaxRadius() * 1.85F / 16.0F) + 1);

        boolean changed = false;

        for (int candidateChunkX = bounds.chunkX - searchChunks; candidateChunkX <= bounds.chunkX + searchChunks; candidateChunkX++) {
            for (int candidateChunkZ = bounds.chunkZ - searchChunks; candidateChunkZ <= bounds.chunkZ + searchChunks; candidateChunkZ++) {
                final RandomSource craterRandom = RandomSource.create(mixSeed(worldSeed, candidateChunkX, candidateChunkZ, 0x9E3779B97F4A7C15L));

                for (int attempt = 0; attempt < cfg.attemptsPerCandidateChunk(); attempt++) {
                    if (craterRandom.nextFloat() > cfg.spawnChance()) {
                        continue;
                    }

                    final CraterSize size = rollSize(craterRandom, cfg);
                    if (size == null) {
                        continue;
                    }

                    final CraterDescriptor crater = createCraterDescriptor(craterRandom, cfg, candidateChunkX, candidateChunkZ, size);
                    if (!bounds.intersects(crater.centerX - crater.outerRadius - 1, crater.centerZ - crater.outerRadius - 1,
                            crater.centerX + crater.outerRadius + 1, crater.centerZ + crater.outerRadius + 1)) {
                        continue;
                    }

                    if (applyCrater(level, bounds, cfg, crater, worldSeed, replaceable)) {
                        changed = true;
                    }
                }
            }
        }

        return changed;
    }

    private boolean applyCrater(
            final WorldGenLevel level,
            final ChunkBounds bounds,
            final SpaceCraterFeatureConfig cfg,
            final CraterDescriptor crater,
            final long worldSeed,
            final Predicate<BlockState> replaceable
    ) {
        final int minX = Math.max(bounds.minX, crater.centerX - crater.outerRadius - 1);
        final int maxX = Math.min(bounds.maxX, crater.centerX + crater.outerRadius + 1);
        final int minZ = Math.max(bounds.minZ, crater.centerZ - crater.outerRadius - 1);
        final int maxZ = Math.min(bounds.maxZ, crater.centerZ + crater.outerRadius + 1);

        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        boolean changed = false;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                final double dx = x + 0.5D - crater.centerX;
                final double dz = z + 0.5D - crater.centerZ;
                final double dist = Math.sqrt(dx * dx + dz * dz);

                if (dist > crater.outerRadius) {
                    continue;
                }

                final int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
                if (surfaceY <= bounds.minY + 2) {
                    continue;
                }

                final double bowl = craterBowl(dist, crater);
                final double rim = craterRim(dist, crater);
                final double ejecta = cfg.ejectaBlanket() ? craterEjecta(dist, crater) : 0.0D;
                final double peak = crater.centralPeak ? craterCentralPeak(dist, crater) : 0.0D;

                final double roughnessNoise = sampleNoise(worldSeed, crater.noiseSalt, x, z) * crater.roughness;
                final double roughnessFactor = dist <= crater.radius ? 1.0D : 0.35D;

                final double targetSurfaceRaw = surfaceY + rim + ejecta + peak - bowl + roughnessNoise * roughnessFactor;
                final int targetSurface = Mth.clamp(Mth.floor(targetSurfaceRaw), bounds.minY + 2, bounds.maxY - 2);

                final BlockState topMaterial;
                final BlockState fillMaterial;

                if (dist <= crater.radius * 0.85D || (crater.centralPeak && dist <= crater.peakRadius)) {
                    topMaterial = cfg.rim();
                    fillMaterial = cfg.primary();
                } else if (dist <= crater.rimOuterRadius) {
                    topMaterial = cfg.rim();
                    fillMaterial = cfg.rim();
                } else {
                    topMaterial = cfg.ejecta();
                    fillMaterial = cfg.ejecta();
                }

                changed |= reshapeColumn(
                        level,
                        bounds,
                        cursor,
                        x,
                        z,
                        surfaceY,
                        targetSurface,
                        topMaterial,
                        fillMaterial,
                        replaceable
                );
            }
        }

        return changed;
    }

    private boolean reshapeColumn(
            final WorldGenLevel level,
            final ChunkBounds bounds,
            final BlockPos.MutableBlockPos cursor,
            final int x,
            final int z,
            final int surfaceY,
            final int targetSurface,
            final BlockState topMaterial,
            final BlockState fillMaterial,
            final Predicate<BlockState> replaceable
    ) {
        boolean changed = false;

        if (targetSurface < surfaceY) {
            for (int y = surfaceY; y > targetSurface; y--) {
                if (y < bounds.minY || y > bounds.maxY) {
                    continue;
                }

                cursor.set(x, y, z);
                final BlockState current = level.getBlockState(cursor);
                if (!current.isAir() && replaceable.test(current) && SpaceFeatureUtils.canAccess(level, cursor)) {
                    level.setBlock(cursor, AIR.defaultBlockState(), 2);
                    changed = true;
                }
            }

            changed |= placePackedSurface(level, bounds, cursor, x, z, targetSurface, topMaterial, fillMaterial);
        } else if (targetSurface > surfaceY) {
            for (int y = surfaceY + 1; y <= targetSurface; y++) {
                if (y < bounds.minY || y > bounds.maxY) {
                    continue;
                }

                cursor.set(x, y, z);
                final BlockState current = level.getBlockState(cursor);
                if ((current.isAir() || replaceable.test(current)) && SpaceFeatureUtils.canAccess(level, cursor)) {
                    level.setBlock(cursor, y == targetSurface ? topMaterial : fillMaterial, 2);
                    changed = true;
                }
            }

            cursor.set(x, targetSurface, z);
            if (SpaceFeatureUtils.canAccess(level, cursor)) {
                level.setBlock(cursor, topMaterial, 2);
                changed = true;
            }
        } else {
            changed |= placePackedSurface(level, bounds, cursor, x, z, targetSurface, topMaterial, fillMaterial);
        }

        return changed;
    }

    private boolean placePackedSurface(
            final WorldGenLevel level,
            final ChunkBounds bounds,
            final BlockPos.MutableBlockPos cursor,
            final int x,
            final int z,
            final int surfaceY,
            final BlockState topMaterial,
            final BlockState fillMaterial
    ) {
        boolean changed = false;

        if (surfaceY < bounds.minY || surfaceY > bounds.maxY) {
            return false;
        }

        cursor.set(x, surfaceY, z);
        if (SpaceFeatureUtils.canAccess(level, cursor)) {
            level.setBlock(cursor, topMaterial, 2);
            changed = true;
        }

        for (int d = 1; d <= 3; d++) {
            final int y = surfaceY - d;
            if (y < bounds.minY) {
                break;
            }

            cursor.set(x, y, z);
            if (!SpaceFeatureUtils.canAccess(level, cursor)) {
                continue;
            }

            final BlockState current = level.getBlockState(cursor);
            if (current.isAir() || current.canBeReplaced()) {
                level.setBlock(cursor, fillMaterial, 2);
                changed = true;
            }
        }

        return changed;
    }

    private CraterDescriptor createCraterDescriptor(
            final RandomSource random,
            final SpaceCraterFeatureConfig cfg,
            final int candidateChunkX,
            final int candidateChunkZ,
            final CraterSize size
    ) {
        final int centerX = (candidateChunkX << 4) + random.nextInt(16);
        final int centerZ = (candidateChunkZ << 4) + random.nextInt(16);

        final int radius = switch (size) {
            case SMALL -> Mth.nextInt(random, cfg.smallMinRadius(), cfg.smallMaxRadius());
            case MEDIUM -> Mth.nextInt(random, cfg.mediumMinRadius(), cfg.mediumMaxRadius());
            case LARGE -> Mth.nextInt(random, cfg.largeMinRadius(), cfg.largeMaxRadius());
            case GIANT -> Mth.nextInt(random, cfg.giantMinRadius(), cfg.giantMaxRadius());
        };

        final double depthFactor = switch (size) {
            case SMALL -> 0.24D + random.nextDouble() * 0.08D;
            case MEDIUM -> 0.28D + random.nextDouble() * 0.10D;
            case LARGE -> 0.32D + random.nextDouble() * 0.12D;
            case GIANT -> 0.36D + random.nextDouble() * 0.14D;
        };

        final int depth = Math.max(3, Mth.floor(radius * depthFactor));
        final int rimHeight = Math.max(1, Mth.floor(radius * (0.10D + random.nextDouble() * 0.08D)));
        final int rimOuterRadius = Math.max(radius + 1, Mth.floor(radius * (1.18D + random.nextDouble() * 0.10D)));
        final int outerRadius = Math.max(rimOuterRadius + 1, Mth.floor(radius * (1.55D + random.nextDouble() * 0.22D)));
        final int ejectaHeight = Math.max(1, Mth.floor(rimHeight * (0.35D + random.nextDouble() * 0.35D)));

        final boolean centralPeak = cfg.centralPeaks() && (size == CraterSize.LARGE || size == CraterSize.GIANT)
                && random.nextFloat() < (size == CraterSize.GIANT ? 0.85F : 0.55F);

        final int peakRadius = centralPeak ? Math.max(2, Mth.floor(radius * (size == CraterSize.GIANT ? 0.20D : 0.16D))) : 0;
        final int peakHeight = centralPeak ? Math.max(2, Mth.floor(depth * (0.45D + random.nextDouble() * 0.35D))) : 0;

        final double roughness = switch (size) {
            case SMALL -> 0.35D + random.nextDouble() * 0.25D;
            case MEDIUM -> 0.45D + random.nextDouble() * 0.35D;
            case LARGE -> 0.60D + random.nextDouble() * 0.45D;
            case GIANT -> 0.80D + random.nextDouble() * 0.60D;
        };

        return new CraterDescriptor(
                size,
                centerX,
                centerZ,
                radius,
                depth,
                rimHeight,
                rimOuterRadius,
                outerRadius,
                ejectaHeight,
                centralPeak,
                peakRadius,
                peakHeight,
                roughness,
                random.nextLong()
        );
    }

    private CraterSize rollSize(final RandomSource random, final SpaceCraterFeatureConfig cfg) {
        final float total = cfg.smallWeight() + cfg.mediumWeight() + cfg.largeWeight() + cfg.giantWeight();
        if (total <= 0.0F) {
            return null;
        }

        float roll = random.nextFloat() * total;

        if (roll < cfg.smallWeight()) {
            return CraterSize.SMALL;
        }
        roll -= cfg.smallWeight();

        if (roll < cfg.mediumWeight()) {
            return CraterSize.MEDIUM;
        }
        roll -= cfg.mediumWeight();

        if (roll < cfg.largeWeight()) {
            return CraterSize.LARGE;
        }

        return CraterSize.GIANT;
    }

    private double craterBowl(final double distance, final CraterDescriptor crater) {
        if (distance >= crater.radius) {
            return 0.0D;
        }

        final double n = distance / crater.radius;
        final double base = 1.0D - n * n;
        return crater.depth * Math.pow(base, 1.38D) * (0.85D + (1.0D - n) * 0.15D);
    }

    private double craterRim(final double distance, final CraterDescriptor crater) {
        if (distance <= crater.radius * 0.72D || distance >= crater.rimOuterRadius) {
            return 0.0D;
        }

        final double peakRadius = crater.radius * 1.03D;
        final double sigma = Math.max(1.0D, (crater.rimOuterRadius - crater.radius) * 0.48D);
        final double d = (distance - peakRadius) / sigma;
        return crater.rimHeight * Math.exp(-(d * d) * 1.75D);
    }

    private double craterEjecta(final double distance, final CraterDescriptor crater) {
        if (distance <= crater.rimOuterRadius || distance >= crater.outerRadius) {
            return 0.0D;
        }

        final double t = (distance - crater.rimOuterRadius) / Math.max(1.0D, crater.outerRadius - crater.rimOuterRadius);
        final double falloff = (1.0D - t);
        return crater.ejectaHeight * falloff * falloff;
    }

    private double craterCentralPeak(final double distance, final CraterDescriptor crater) {
        if (!crater.centralPeak || distance >= crater.peakRadius) {
            return 0.0D;
        }

        final double t = distance / crater.peakRadius;
        return crater.peakHeight * (1.0D - t * t);
    }

    private double sampleNoise(final long worldSeed, final long salt, final int x, final int z) {
        long h = worldSeed;
        h ^= salt + 0x9E3779B97F4A7C15L;
        h ^= (long) x * 0x632BE59BD9B4E019L;
        h ^= (long) z * 0x9E3779B97F4A7C15L;
        h ^= (h >>> 33);
        h *= 0xFF51AFD7ED558CCDL;
        h ^= (h >>> 33);
        h *= 0xC4CEB9FE1A85EC53L;
        h ^= (h >>> 33);
        return ((h & 0xFFFFL) / 32767.5D) - 1.0D;
    }

    private long mixSeed(final long seed, final int chunkX, final int chunkZ, final long salt) {
        long h = seed ^ salt;
        h ^= (long) chunkX * 341873128712L;
        h ^= (long) chunkZ * 132897987541L;
        h ^= (h >>> 29);
        h *= 0x9E3779B97F4A7C15L;
        h ^= (h >>> 32);
        return h;
    }

    private enum CraterSize {
        SMALL,
        MEDIUM,
        LARGE,
        GIANT
    }

    private record CraterDescriptor(
            CraterSize size,
            int centerX,
            int centerZ,
            int radius,
            int depth,
            int rimHeight,
            int rimOuterRadius,
            int outerRadius,
            int ejectaHeight,
            boolean centralPeak,
            int peakRadius,
            int peakHeight,
            double roughness,
            long noiseSalt
    ) {
    }

    private static final class ChunkBounds {
        private final int chunkX;
        private final int chunkZ;
        private final int minX;
        private final int maxX;
        private final int minZ;
        private final int maxZ;
        private final int minY;
        private final int maxY;

        private ChunkBounds(
                final int chunkX,
                final int chunkZ,
                final int minX,
                final int maxX,
                final int minZ,
                final int maxZ,
                final int minY,
                final int maxY
        ) {
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.minX = minX;
            this.maxX = maxX;
            this.minZ = minZ;
            this.maxZ = maxZ;
            this.minY = minY;
            this.maxY = maxY;
        }

        private static ChunkBounds from(final WorldGenLevel level, final BlockPos origin) {
            final ChunkPos chunkPos = new ChunkPos(origin);
            return new ChunkBounds(
                    chunkPos.x,
                    chunkPos.z,
                    chunkPos.getMinBlockX(),
                    chunkPos.getMaxBlockX(),
                    chunkPos.getMinBlockZ(),
                    chunkPos.getMaxBlockZ(),
                    level.getMinBuildHeight() + 1,
                    level.getMaxBuildHeight() - 2
            );
        }

        private boolean intersects(final int otherMinX, final int otherMinZ, final int otherMaxX, final int otherMaxZ) {
            return this.maxX >= otherMinX
                    && this.minX <= otherMaxX
                    && this.maxZ >= otherMinZ
                    && this.minZ <= otherMaxZ;
        }
    }
}