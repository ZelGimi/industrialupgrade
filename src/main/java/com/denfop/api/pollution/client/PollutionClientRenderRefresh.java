package com.denfop.api.pollution.client;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Collection;

public final class PollutionClientRenderRefresh {

    private static final int SINGLE_UPDATE_RERENDER_RADIUS = PollutionLocalTintCache.MAX_DEPENDENCY_RADIUS_CHUNKS;
    private static final int FALLBACK_PLAYER_AREA_RADIUS = PollutionLocalTintCache.MAX_DEPENDENCY_RADIUS_CHUNKS + 1;
    private static final int MAX_QUEUED_CHUNKS = 8;
    private static final int FLUSH_INTERVAL_TICKS = 4;

    private static final LongOpenHashSet PENDING_CHUNKS = new LongOpenHashSet();
    private static final LongOpenHashSet SNAPSHOT_CHUNKS = new LongOpenHashSet();
    private static final LongOpenHashSet EXPANDED_CHUNKS = new LongOpenHashSet();

    private static boolean fullRefreshRequested = false;
    private static int clientTicks = 0;

    private PollutionClientRenderRefresh() {
    }

    public static void queueSingleChunkPollutionUpdated(ChunkPos chunkPos) {
        if (chunkPos == null) {
            return;
        }
        queueChunkKey(packChunkKey(chunkPos.x, chunkPos.z));
    }

    public static void queueSingleChunkRadiationUpdated(ChunkPos chunkPos) {
        if (chunkPos == null) {
            return;
        }
        queueChunkKey(packChunkKey(chunkPos.x, chunkPos.z));
    }

    public static void queueFullPollutionSnapshotApplied(Collection<ChunkPos> changedChunks) {
        if (changedChunks == null || changedChunks.isEmpty()) {
            return;
        }

        synchronized (PENDING_CHUNKS) {
            for (ChunkPos chunkPos : changedChunks) {
                if (chunkPos != null) {
                    PENDING_CHUNKS.add(packChunkKey(chunkPos.x, chunkPos.z));
                }
            }
            if (PENDING_CHUNKS.size() > MAX_QUEUED_CHUNKS) {
                fullRefreshRequested = true;
            }
        }
    }

    public static void queueFullRadiationSnapshotApplied(Collection<ChunkPos> changedChunks) {
        queueFullPollutionSnapshotApplied(changedChunks);
    }

    public static void onSingleChunkPollutionUpdated(ChunkPos chunkPos) {
        queueSingleChunkPollutionUpdated(chunkPos);
    }


    public static void onFullPollutionSnapshotApplied(Collection<ChunkPos> changedChunks) {
        queueFullPollutionSnapshotApplied(changedChunks);
    }


    public static void flushIfNeeded(Minecraft minecraft) {
        clientTicks++;

        if (minecraft == null || minecraft.level == null || minecraft.levelRenderer == null || minecraft.player == null) {
            reset();
            return;
        }

        if (minecraft.level.dimension() != Level.OVERWORLD) {
            reset();
            return;
        }

        if ((clientTicks % FLUSH_INTERVAL_TICKS) != 0) {
            return;
        }

        boolean forceAreaRefresh;

        synchronized (PENDING_CHUNKS) {
            if (PENDING_CHUNKS.isEmpty() && !fullRefreshRequested) {
                return;
            }

            SNAPSHOT_CHUNKS.clear();
            LongIterator iterator = PENDING_CHUNKS.iterator();
            while (iterator.hasNext()) {
                SNAPSHOT_CHUNKS.add(iterator.nextLong());
            }

            forceAreaRefresh = fullRefreshRequested || SNAPSHOT_CHUNKS.size() > MAX_QUEUED_CHUNKS;

            PENDING_CHUNKS.clear();
            fullRefreshRequested = false;
        }

        if (forceAreaRefresh) {
            refreshAreaAroundPlayer(minecraft, FALLBACK_PLAYER_AREA_RADIUS);
            return;
        }

        refreshChunks(minecraft, SNAPSHOT_CHUNKS, SINGLE_UPDATE_RERENDER_RADIUS);
    }

    public static void reset() {
        synchronized (PENDING_CHUNKS) {
            PENDING_CHUNKS.clear();
            fullRefreshRequested = false;
        }
        SNAPSHOT_CHUNKS.clear();
        EXPANDED_CHUNKS.clear();
    }

    private static void queueChunkKey(long chunkKey) {
        synchronized (PENDING_CHUNKS) {
            PENDING_CHUNKS.add(chunkKey);
            if (PENDING_CHUNKS.size() > MAX_QUEUED_CHUNKS) {
                fullRefreshRequested = true;
            }
        }
    }

    private static void refreshAreaAroundPlayer(Minecraft minecraft, int radius) {
        int centerChunkX = minecraft.player.blockPosition().getX() >> 4;
        int centerChunkZ = minecraft.player.blockPosition().getZ() >> 4;

        SNAPSHOT_CHUNKS.clear();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                SNAPSHOT_CHUNKS.add(packChunkKey(centerChunkX + dx, centerChunkZ + dz));
            }
        }

        refreshChunks(minecraft, SNAPSHOT_CHUNKS, 0);
    }

    private static void refreshChunks(Minecraft minecraft, LongOpenHashSet changedChunks, int neighborRadius) {
        if (changedChunks == null || changedChunks.isEmpty()) {
            return;
        }

        EXPANDED_CHUNKS.clear();

        LongIterator changedIterator = changedChunks.iterator();
        while (changedIterator.hasNext()) {
            long changedChunkKey = changedIterator.nextLong();
            int changedChunkX = unpackChunkX(changedChunkKey);
            int changedChunkZ = unpackChunkZ(changedChunkKey);

            for (int dx = -neighborRadius; dx <= neighborRadius; dx++) {
                for (int dz = -neighborRadius; dz <= neighborRadius; dz++) {
                    EXPANDED_CHUNKS.add(packChunkKey(changedChunkX + dx, changedChunkZ + dz));
                }
            }
        }

        PollutionLocalTintCache.invalidateChunkKeys(EXPANDED_CHUNKS);
        PollutionBlockColorCache.invalidateChunkKeys(EXPANDED_CHUNKS);

        int minBuildY = minecraft.level.getMinBuildHeight();
        int maxBuildY = minecraft.level.getMaxBuildHeight() - 1;

        LongIterator iterator = EXPANDED_CHUNKS.iterator();
        while (iterator.hasNext()) {
            long chunkKey = iterator.nextLong();
            int chunkX = unpackChunkX(chunkKey);
            int chunkZ = unpackChunkZ(chunkKey);

            int minX = chunkX << 4;
            int minZ = chunkZ << 4;
            int maxX = minX + 15;
            int maxZ = minZ + 15;

            minecraft.levelRenderer.setBlocksDirty(
                    minX,
                    minBuildY,
                    minZ,
                    maxX,
                    maxBuildY,
                    maxZ
            );
        }
    }

    private static long packChunkKey(int chunkX, int chunkZ) {
        return ((long) chunkZ << 32) | (chunkX & 0xFFFFFFFFL);
    }

    private static int unpackChunkX(long chunkKey) {
        return (int) chunkKey;
    }

    private static int unpackChunkZ(long chunkKey) {
        return (int) (chunkKey >>> 32);
    }
}