package com.denfop.api.pollution.client;

import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.component.ChunkLevel;
import com.denfop.api.pollution.radiation.Radiation;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.config.ModConfig;
import it.unimi.dsi.fastutil.longs.Long2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public final class PollutionLocalTintCache {

    public static final int CELL_SIZE_BLOCKS = 3;

    private static final int MAX_CACHE_ENTRIES = 256;
    private static final long CACHE_TTL_TICKS = 4L;

    private static final int MAX_ACTIVITY_CACHE_ENTRIES = 256;
    private static final long ACTIVITY_CACHE_TTL_TICKS = 20L;

    private static final int MAX_CACHE_DISTANCE_CHUNKS = 10;

    private static final int AIR_BLEND_RADIUS_CHUNKS = 2;
    private static final int SOIL_BLEND_RADIUS_CHUNKS = 2;
    private static final int RADIATION_BLEND_RADIUS_CHUNKS = 2;

    public static final int MAX_DEPENDENCY_RADIUS_CHUNKS = Math.max(
            AIR_BLEND_RADIUS_CHUNKS,
            Math.max(SOIL_BLEND_RADIUS_CHUNKS, RADIATION_BLEND_RADIUS_CHUNKS)
    );

    private static final float AIR_SIGMA_BLOCKS = 14.0F;
    private static final float SOIL_SIGMA_BLOCKS = 14.0F;
    private static final float RADIATION_SIGMA_BLOCKS = 14.0F;

    private static final int CELL_CENTER_OFFSET = CELL_SIZE_BLOCKS / 2;

    private static final Long2ObjectLinkedOpenHashMap<PollutionLocalTintState> STATE_CACHE =
            new Long2ObjectLinkedOpenHashMap<>();
    private static final Long2LongLinkedOpenHashMap STATE_TIMES =
            new Long2LongLinkedOpenHashMap();

    /**
     * key = packed chunk key
     * value = (gameTime << 1) | hasInfluenceBit
     */
    private static final Long2LongLinkedOpenHashMap ACTIVITY_CACHE =
            new Long2LongLinkedOpenHashMap();

    private static long lastCellKey = Long.MIN_VALUE;
    private static long lastCellGameTime = Long.MIN_VALUE;
    private static PollutionLocalTintState lastCellState = PollutionLocalTintState.NONE;

    private PollutionLocalTintCache() {
    }

    public static PollutionLocalTintState get(BlockPos pos) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;

        if (level == null || pos == null || level.dimension() != Level.OVERWORLD) {
            return PollutionLocalTintState.NONE;
        }

        long cellKey = getCellKey(pos);
        int sampleX = getCellSampleX(cellKey);
        int sampleZ = getCellSampleZ(cellKey);
        long gameTime = level.getGameTime();

        if (cellKey == lastCellKey && gameTime == lastCellGameTime) {
            return lastCellState;
        }

        boolean allowCaching = shouldCacheForPlayer(minecraft, sampleX, sampleZ);

        if (allowCaching) {
            synchronized (STATE_CACHE) {
                if (STATE_TIMES.containsKey(cellKey)) {
                    long cachedTime = STATE_TIMES.get(cellKey);
                    if ((gameTime - cachedTime) <= CACHE_TTL_TICKS) {
                        PollutionLocalTintState cached = STATE_CACHE.get(cellKey);
                        if (cached != null) {
                            lastCellKey = cellKey;
                            lastCellGameTime = gameTime;
                            lastCellState = cached;
                            return cached;
                        }
                    } else {
                        STATE_TIMES.remove(cellKey);
                        STATE_CACHE.remove(cellKey);
                    }
                }
            }
        }

        int cellChunkX = sampleX >> 4;
        int cellChunkZ = sampleZ >> 4;

        boolean hasInfluence = allowCaching
                ? hasAnyInfluenceNearCached(cellChunkX, cellChunkZ, gameTime)
                : hasAnyInfluenceNearUncached(cellChunkX, cellChunkZ);

        if (!hasInfluence) {
            lastCellKey = cellKey;
            lastCellGameTime = gameTime;
            lastCellState = PollutionLocalTintState.NONE;
            return PollutionLocalTintState.NONE;
        }

        PollutionLocalTintState computed = computeForCell(sampleX, sampleZ);

        if (allowCaching && computed.isActive()) {
            synchronized (STATE_CACHE) {
                STATE_CACHE.put(cellKey, computed);
                STATE_TIMES.put(cellKey, gameTime);
                trimStateCache();
            }
        }

        lastCellKey = cellKey;
        lastCellGameTime = gameTime;
        lastCellState = computed;
        return computed;
    }

    public static BlockPos getCellSamplePos(BlockPos pos) {
        return new BlockPos(getCellSampleX(pos), pos.getY(), getCellSampleZ(pos));
    }

    public static long getCellKey(BlockPos pos) {
        return packXZ(getCellOriginX(pos.getX()), getCellOriginZ(pos.getZ()));
    }

    public static int getCellSampleX(BlockPos pos) {
        return getCellOriginX(pos.getX()) + CELL_CENTER_OFFSET;
    }

    public static int getCellSampleZ(BlockPos pos) {
        return getCellOriginZ(pos.getZ()) + CELL_CENTER_OFFSET;
    }

    public static int getCellSampleX(long cellKey) {
        return unpackX(cellKey) + CELL_CENTER_OFFSET;
    }

    public static int getCellSampleZ(long cellKey) {
        return unpackZ(cellKey) + CELL_CENTER_OFFSET;
    }

    public static void invalidateArea(ChunkPos center, int radius) {
        if (center == null) {
            return;
        }

        long minChunkX = center.x - radius;
        long maxChunkX = center.x + radius;
        long minChunkZ = center.z - radius;
        long maxChunkZ = center.z + radius;

        synchronized (STATE_CACHE) {
            LongIterator iterator = STATE_TIMES.keySet().iterator();
            while (iterator.hasNext()) {
                long cellKey = iterator.nextLong();
                int chunkX = unpackX(cellKey) >> 4;
                int chunkZ = unpackZ(cellKey) >> 4;

                if (chunkX >= minChunkX && chunkX <= maxChunkX && chunkZ >= minChunkZ && chunkZ <= maxChunkZ) {
                    iterator.remove();
                    STATE_CACHE.remove(cellKey);
                }
            }
        }

        synchronized (ACTIVITY_CACHE) {
            LongIterator iterator = ACTIVITY_CACHE.keySet().iterator();
            while (iterator.hasNext()) {
                long chunkKey = iterator.nextLong();
                int chunkX = unpackX(chunkKey);
                int chunkZ = unpackZ(chunkKey);

                if (chunkX >= minChunkX && chunkX <= maxChunkX && chunkZ >= minChunkZ && chunkZ <= maxChunkZ) {
                    iterator.remove();
                }
            }
        }

        resetLastCellCache();
    }

    public static void invalidateChunkKeys(LongSet chunkKeys) {
        if (chunkKeys == null || chunkKeys.isEmpty()) {
            return;
        }

        synchronized (STATE_CACHE) {
            LongIterator iterator = STATE_TIMES.keySet().iterator();
            while (iterator.hasNext()) {
                long cellKey = iterator.nextLong();
                long chunkKey = packXZ(unpackX(cellKey) >> 4, unpackZ(cellKey) >> 4);

                if (chunkKeys.contains(chunkKey)) {
                    iterator.remove();
                    STATE_CACHE.remove(cellKey);
                }
            }
        }

        synchronized (ACTIVITY_CACHE) {
            LongIterator iterator = ACTIVITY_CACHE.keySet().iterator();
            while (iterator.hasNext()) {
                long chunkKey = iterator.nextLong();
                if (chunkKeys.contains(chunkKey)) {
                    iterator.remove();
                }
            }
        }

        resetLastCellCache();
    }

    public static void clear() {
        synchronized (STATE_CACHE) {
            STATE_CACHE.clear();
            STATE_TIMES.clear();
        }
        synchronized (ACTIVITY_CACHE) {
            ACTIVITY_CACHE.clear();
        }
        resetLastCellCache();
    }

    private static PollutionLocalTintState computeForCell(int sampleX, int sampleZ) {
        float worldX = sampleX;
        float worldZ = sampleZ;

        float air = computeTypeInfluenceAt(
                worldX,
                worldZ,
                AIR_BLEND_RADIUS_CHUNKS,
                AIR_SIGMA_BLOCKS,
                PollutionLocalTintCache::getAirSeverity
        );

        float soil = computeTypeInfluenceAt(
                worldX,
                worldZ,
                SOIL_BLEND_RADIUS_CHUNKS,
                SOIL_SIGMA_BLOCKS,
                PollutionLocalTintCache::getSoilSeverity
        );

        float radiation = computeTypeInfluenceAt(
                worldX,
                worldZ,
                RADIATION_BLEND_RADIUS_CHUNKS,
                RADIATION_SIGMA_BLOCKS,
                PollutionLocalTintCache::getRadiationSeverity
        );

        float overall = Mth.clamp(
                Math.max(Math.max(air, soil), radiation)
                        + air * 0.08F
                        + soil * 0.10F
                        + radiation * 0.32F,
                0.0F,
                1.0F
        );

        if (overall < 0.01F) {
            return PollutionLocalTintState.NONE;
        }

        float biomeInfluence = Mth.clamp(
                soil * 0.76F
                        + air * 0.12F
                        + radiation * 0.78F,
                0.0F,
                1.0F
        );

        float saturationLoss = Mth.clamp(
                overall * 0.12F
                        + soil * 0.06F
                        + radiation * 0.10F,
                0.0F,
                1.0F
        );

        float wa = air * 0.65F;
        float ws = soil * 0.75F;
        float wr = radiation * 2.85F;

        float total = wa + ws + wr;
        if (total <= 0.0001F) {
            wa = 1.0F;
            ws = 0.0F;
            wr = 0.0F;
            total = 1.0F;
        }

        wa /= total;
        ws /= total;
        wr /= total;

        int grassTint = PollutionVisualColors.weightedColor(
                PollutionVisualColors.AIR_GRASS,
                PollutionVisualColors.SOIL_GRASS,
                PollutionVisualColors.RADIATION_GRASS,
                wa,
                ws,
                wr
        );

        int foliageTint = PollutionVisualColors.weightedColor(
                PollutionVisualColors.AIR_FOLIAGE,
                PollutionVisualColors.SOIL_FOLIAGE,
                PollutionVisualColors.RADIATION_FOLIAGE,
                wa,
                ws,
                wr
        );

        int waterTint = PollutionVisualColors.weightedColor(
                PollutionVisualColors.AIR_WATER,
                PollutionVisualColors.SOIL_WATER,
                PollutionVisualColors.RADIATION_WATER,
                wa,
                ws,
                wr
        );

        return new PollutionLocalTintState(
                air,
                soil,
                radiation,
                biomeInfluence,
                saturationLoss,
                grassTint,
                foliageTint,
                waterTint
        );
    }

    private static float computeTypeInfluenceAt(
            float worldX,
            float worldZ,
            int blendRadiusChunks,
            float sigma,
            ChunkSeverityProvider provider
    ) {
        int centerChunkX = Mth.floor(worldX) >> 4;
        int centerChunkZ = Mth.floor(worldZ) >> 4;

        float influence = 0.0F;

        for (int dx = -blendRadiusChunks; dx <= blendRadiusChunks; dx++) {
            for (int dz = -blendRadiusChunks; dz <= blendRadiusChunks; dz++) {
                int sampleChunkX = centerChunkX + dx;
                int sampleChunkZ = centerChunkZ + dz;

                float severity = provider.getSeverity(sampleChunkX, sampleChunkZ);
                if (severity <= 0.0F) {
                    continue;
                }

                float chunkCenterX = (sampleChunkX << 4) + 8.0F;
                float chunkCenterZ = (sampleChunkZ << 4) + 8.0F;

                float diffX = worldX - chunkCenterX;
                float diffZ = worldZ - chunkCenterZ;
                float weight = gaussianWeight(diffX, diffZ, sigma);

                if (dx == 0 && dz == 0) {
                    weight *= 1.28F;
                }

                influence += severity * weight;
            }
        }

        return Mth.clamp(influence, 0.0F, 1.0F);
    }

    private static float gaussianWeight(float dx, float dz, float sigma) {
        float distanceSq = dx * dx + dz * dz;
        float divisor = 2.0F * sigma * sigma;
        return (float) Math.exp(-distanceSq / divisor);
    }

    private static float getAirSeverity(int chunkX, int chunkZ) {
        if (!ModConfig.COMMON.airPollution.get()) {
            return 0.0F;
        }

        PollutionManager manager = PollutionManager.pollutionManager;
        if (manager == null) {
            return 0.0F;
        }

        return pollutionSeverity(manager.getChunkLevelAir(new ChunkPos(chunkX, chunkZ)));
    }

    private static float getSoilSeverity(int chunkX, int chunkZ) {
        if (!ModConfig.COMMON.soilPollution.get()) {
            return 0.0F;
        }

        PollutionManager manager = PollutionManager.pollutionManager;
        if (manager == null) {
            return 0.0F;
        }

        return pollutionSeverity(manager.getChunkLevelSoil(new ChunkPos(chunkX, chunkZ)));
    }

    private static float getRadiationSeverity(int chunkX, int chunkZ) {
        if (!ModConfig.COMMON.radiationChunksEnabled.get()) {
            return 0.0F;
        }

        RadiationSystem system = RadiationSystem.rad_system;
        if (system == null) {
            return 0.0F;
        }

        return radiationSeverity(system.getMap().get(new ChunkPos(chunkX, chunkZ)));
    }

    private static float pollutionSeverity(ChunkLevel chunkLevel) {
        if (chunkLevel == null) {
            return 0.0F;
        }

        float level = chunkLevel.getLevelPollution().ordinal();
        float withinLevel = Mth.clamp((float) (chunkLevel.getPollution() / 125.0D), 0.0F, 1.0F);

        if (level <= 0.0F && withinLevel < 0.05F) {
            return 0.0F;
        }

        return Mth.clamp(level * 0.21F + withinLevel * 0.11F, 0.0F, 1.0F);
    }

    private static float radiationSeverity(Radiation radiation) {
        if (radiation == null) {
            return 0.0F;
        }

        float level = radiation.getLevel().ordinal();
        float withinLevel = Mth.clamp((float) (radiation.getRadiation() / 1000.0D), 0.0F, 1.0F);

        if (level <= 0.0F && withinLevel < 0.03F) {
            return 0.0F;
        }

        return Mth.clamp(level * 0.34F + withinLevel * 0.26F, 0.0F, 1.0F);
    }

    private static boolean hasAnyInfluenceNearCached(int centerChunkX, int centerChunkZ, long gameTime) {
        long chunkKey = packXZ(centerChunkX, centerChunkZ);

        synchronized (ACTIVITY_CACHE) {
            if (ACTIVITY_CACHE.containsKey(chunkKey)) {
                long packed = ACTIVITY_CACHE.get(chunkKey);
                long cachedTime = packed >>> 1;
                if ((gameTime - cachedTime) <= ACTIVITY_CACHE_TTL_TICKS) {
                    return (packed & 1L) != 0L;
                }
                ACTIVITY_CACHE.remove(chunkKey);
            }
        }

        boolean hasInfluence = hasAnyInfluenceNearUncached(centerChunkX, centerChunkZ);

        synchronized (ACTIVITY_CACHE) {
            long packed = (gameTime << 1) | (hasInfluence ? 1L : 0L);
            ACTIVITY_CACHE.put(chunkKey, packed);
            trimActivityCache();
        }

        return hasInfluence;
    }

    private static boolean hasAnyInfluenceNearUncached(int centerChunkX, int centerChunkZ) {
        PollutionManager pollutionManager = PollutionManager.pollutionManager;
        RadiationSystem radiationSystem = RadiationSystem.rad_system;

        for (int dx = -MAX_DEPENDENCY_RADIUS_CHUNKS; dx <= MAX_DEPENDENCY_RADIUS_CHUNKS; dx++) {
            for (int dz = -MAX_DEPENDENCY_RADIUS_CHUNKS; dz <= MAX_DEPENDENCY_RADIUS_CHUNKS; dz++) {
                int sampleChunkX = centerChunkX + dx;
                int sampleChunkZ = centerChunkZ + dz;

                if (ModConfig.COMMON.airPollution.get() && pollutionManager != null) {
                    ChunkLevel air = pollutionManager.getChunkLevelAir(new ChunkPos(sampleChunkX, sampleChunkZ));
                    if (air != null && pollutionSeverity(air) > 0.0F) {
                        return true;
                    }
                }

                if (ModConfig.COMMON.soilPollution.get() && pollutionManager != null) {
                    ChunkLevel soil = pollutionManager.getChunkLevelSoil(new ChunkPos(sampleChunkX, sampleChunkZ));
                    if (soil != null && pollutionSeverity(soil) > 0.0F) {
                        return true;
                    }
                }

                if (ModConfig.COMMON.radiationChunksEnabled.get() && radiationSystem != null) {
                    Radiation radiation = radiationSystem.getMap().get(new ChunkPos(sampleChunkX, sampleChunkZ));
                    if (radiation != null && radiationSeverity(radiation) > 0.0F) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean shouldCacheForPlayer(Minecraft minecraft, int sampleX, int sampleZ) {
        if (minecraft.player == null) {
            return false;
        }

        int playerChunkX = minecraft.player.blockPosition().getX() >> 4;
        int playerChunkZ = minecraft.player.blockPosition().getZ() >> 4;
        int sampleChunkX = sampleX >> 4;
        int sampleChunkZ = sampleZ >> 4;

        return Math.abs(sampleChunkX - playerChunkX) <= MAX_CACHE_DISTANCE_CHUNKS
                && Math.abs(sampleChunkZ - playerChunkZ) <= MAX_CACHE_DISTANCE_CHUNKS;
    }

    private static void trimStateCache() {
        while (STATE_TIMES.size() > MAX_CACHE_ENTRIES) {
            LongIterator iterator = STATE_TIMES.keySet().iterator();
            if (!iterator.hasNext()) {
                return;
            }
            long oldestKey = iterator.nextLong();
            iterator.remove();
            STATE_CACHE.remove(oldestKey);
        }
    }

    private static void trimActivityCache() {
        while (ACTIVITY_CACHE.size() > MAX_ACTIVITY_CACHE_ENTRIES) {
            LongIterator iterator = ACTIVITY_CACHE.keySet().iterator();
            if (!iterator.hasNext()) {
                return;
            }
            iterator.nextLong();
            iterator.remove();
        }
    }

    private static void resetLastCellCache() {
        lastCellKey = Long.MIN_VALUE;
        lastCellGameTime = Long.MIN_VALUE;
        lastCellState = PollutionLocalTintState.NONE;
    }

    private static int getCellOriginX(int x) {
        return Math.floorDiv(x, CELL_SIZE_BLOCKS) * CELL_SIZE_BLOCKS;
    }

    private static int getCellOriginZ(int z) {
        return Math.floorDiv(z, CELL_SIZE_BLOCKS) * CELL_SIZE_BLOCKS;
    }

    private static long packXZ(int x, int z) {
        return ((long) z << 32) | (x & 0xFFFFFFFFL);
    }

    private static int unpackX(long packed) {
        return (int) packed;
    }

    private static int unpackZ(long packed) {
        return (int) (packed >>> 32);
    }

    @FunctionalInterface
    private interface ChunkSeverityProvider {
        float getSeverity(int chunkX, int chunkZ);
    }
}