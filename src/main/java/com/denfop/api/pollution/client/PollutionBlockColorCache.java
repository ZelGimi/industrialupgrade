package com.denfop.api.pollution.client;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

import java.util.function.IntSupplier;

public final class PollutionBlockColorCache {

    private static final int MAX_CACHE_ENTRIES = 1024;
    private static final long CACHE_TTL_TICKS = 4L;

    private static final Long2IntLinkedOpenHashMap COLOR_CACHE = new Long2IntLinkedOpenHashMap();
    private static final Long2LongLinkedOpenHashMap TIME_CACHE = new Long2LongLinkedOpenHashMap();

    private PollutionBlockColorCache() {
    }

    public static int getOrCompute(long cellKey, int baseColor, TintType tintType, IntSupplier computer) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.level == null || tintType == null) {
            return computer.getAsInt();
        }

        if (minecraft.level.dimension() != Level.OVERWORLD) {
            return computer.getAsInt();
        }

        long gameTime = minecraft.level.getGameTime();
        long packedKey = packKey(cellKey, tintType.id);

        synchronized (COLOR_CACHE) {
            if (TIME_CACHE.containsKey(packedKey)) {
                long cachedTime = TIME_CACHE.get(packedKey);
                if ((gameTime - cachedTime) <= CACHE_TTL_TICKS) {
                    if (COLOR_CACHE.containsKey(packedKey)) {
                        return COLOR_CACHE.get(packedKey);
                    }
                } else {
                    TIME_CACHE.remove(packedKey);
                    COLOR_CACHE.remove(packedKey);
                }
            }
        }

        int color = computer.getAsInt();

        if (color != baseColor) {
            synchronized (COLOR_CACHE) {
                COLOR_CACHE.put(packedKey, color);
                TIME_CACHE.put(packedKey, gameTime);
                trimCache();
            }
        }

        return color;
    }

    public static void invalidateChunkKeys(LongSet chunkKeys) {
        if (chunkKeys == null || chunkKeys.isEmpty()) {
            return;
        }

        synchronized (COLOR_CACHE) {
            LongIterator iterator = TIME_CACHE.keySet().iterator();
            while (iterator.hasNext()) {
                long packedKey = iterator.nextLong();
                long cellKey = unpackCellKey(packedKey);
                long chunkKey = packXZ(unpackX(cellKey) >> 4, unpackZ(cellKey) >> 4);

                if (chunkKeys.contains(chunkKey)) {
                    iterator.remove();
                    COLOR_CACHE.remove(packedKey);
                }
            }
        }
    }

    public static void clear() {
        synchronized (COLOR_CACHE) {
            COLOR_CACHE.clear();
            TIME_CACHE.clear();
        }
    }

    private static void trimCache() {
        while (TIME_CACHE.size() > MAX_CACHE_ENTRIES) {
            LongIterator iterator = TIME_CACHE.keySet().iterator();
            if (!iterator.hasNext()) {
                return;
            }
            long oldestKey = iterator.nextLong();
            iterator.remove();
            COLOR_CACHE.remove(oldestKey);
        }
    }


    private static long packKey(long cellKey, byte tintType) {
        return cellKey | (tintType & 0x3L);
    }

    private static long unpackCellKey(long packedKey) {
        return packedKey & ~0x3L;
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

    public enum TintType {
        GRASS((byte) 0),
        FOLIAGE((byte) 1),
        WATER_PLANT((byte) 2);

        private final byte id;

        TintType(byte id) {
            this.id = id;
        }
    }
}