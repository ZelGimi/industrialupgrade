package com.denfop.api.pollution.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Arrays;

public final class PollutionWeatherColumnCache {

    private boolean initialized;
    private ResourceKey<Level> dimension;
    private int centerBlockX;
    private int centerBlockZ;
    private int centerChunkX;
    private int centerChunkZ;
    private int radius;
    private long lastBuildTick;

    private ColumnEntry[] entries = new ColumnEntry[0];
    private int entryCount;

    private static long columnSeed(int x, int z) {
        long seed = (long) x * 3129871L ^ (long) z * 116129781L;
        seed = seed * seed * 42317861L + seed * 11L;
        return seed;
    }

    private static float toUnit(long value) {
        return (float) ((value & 1023L) / 1023.0D);
    }

    public void ensureFresh(Minecraft minecraft, int radius, long gameTime) {
        if (needsRebuild(minecraft, radius, gameTime)) {
            rebuild(minecraft, radius, gameTime);
        }
    }

    public ColumnEntry[] getEntries() {
        return this.entries;
    }

    public int getEntryCount() {
        return this.entryCount;
    }

    public void reset() {
        this.initialized = false;
        this.dimension = null;
        this.centerBlockX = 0;
        this.centerBlockZ = 0;
        this.centerChunkX = 0;
        this.centerChunkZ = 0;
        this.radius = 0;
        this.lastBuildTick = 0L;
        this.entries = new ColumnEntry[0];
        this.entryCount = 0;
    }

    private boolean needsRebuild(Minecraft minecraft, int radius, long gameTime) {
        if (!this.initialized || minecraft == null || minecraft.level == null || minecraft.player == null) {
            return true;
        }

        if (this.dimension != minecraft.level.dimension()) {
            return true;
        }

        if (this.radius != radius) {
            return true;
        }

        BlockPos playerPos = minecraft.player.blockPosition();
        ChunkPos chunkPos = new ChunkPos(playerPos);

        if (chunkPos.x != this.centerChunkX || chunkPos.z != this.centerChunkZ) {
            return true;
        }

        int dx = Math.abs(playerPos.getX() - this.centerBlockX);
        int dz = Math.abs(playerPos.getZ() - this.centerBlockZ);
        if (dx >= 3 || dz >= 3) {
            return true;
        }

        return gameTime - this.lastBuildTick >= 20L;
    }

    private void rebuild(Minecraft minecraft, int radius, long gameTime) {
        if (minecraft == null || minecraft.level == null || minecraft.player == null) {
            reset();
            return;
        }

        Level level = minecraft.level;
        BlockPos playerPos = minecraft.player.blockPosition();
        ChunkPos playerChunk = new ChunkPos(playerPos);

        this.initialized = true;
        this.dimension = level.dimension();
        this.centerBlockX = playerPos.getX();
        this.centerBlockZ = playerPos.getZ();
        this.centerChunkX = playerChunk.x;
        this.centerChunkZ = playerChunk.z;
        this.radius = radius;
        this.lastBuildTick = gameTime;

        ColumnEntry[] temp = new ColumnEntry[(radius * 2 + 1) * (radius * 2 + 1)];
        int count = 0;

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int dz = -radius; dz <= radius; dz++) {
            for (int dx = -radius; dx <= radius; dx++) {
                float distance = Mth.sqrt((float) (dx * dx + dz * dz));
                if (distance > radius) {
                    continue;
                }

                int worldX = this.centerBlockX + dx;
                int worldZ = this.centerBlockZ + dz;
                int terrainY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, worldX, worldZ);

                mutablePos.set(worldX, terrainY + 1, worldZ);
                if (!level.canSeeSky(mutablePos)) {
                    continue;
                }

                float radial = 1.0F - (distance / (radius + 0.65F));
                float radialFactor = radial * radial;

                long seed = columnSeed(worldX, worldZ);
                float seed01 = toUnit(seed);
                float seed01b = toUnit(seed >> 24);
                float seed01c = toUnit(seed >> 41);
                int densitySeed = Math.floorMod((int) (seed ^ (seed >>> 32)), 4);

                temp[count++] = new ColumnEntry(
                        worldX,
                        worldZ,
                        terrainY,
                        radialFactor,
                        seed01,
                        seed01b,
                        seed01c,
                        densitySeed
                );
            }
        }

        this.entryCount = count;
        this.entries = count == temp.length ? temp : Arrays.copyOf(temp, count);
    }

    public static final class ColumnEntry {
        private final int worldX;
        private final int worldZ;
        private final int terrainY;
        private final float radialFactor;
        private final float seed01;
        private final float seed01b;
        private final float seed01c;
        private final int densitySeed;

        private ColumnEntry(
                int worldX,
                int worldZ,
                int terrainY,
                float radialFactor,
                float seed01,
                float seed01b,
                float seed01c,
                int densitySeed
        ) {
            this.worldX = worldX;
            this.worldZ = worldZ;
            this.terrainY = terrainY;
            this.radialFactor = radialFactor;
            this.seed01 = seed01;
            this.seed01b = seed01b;
            this.seed01c = seed01c;
            this.densitySeed = densitySeed;
        }

        public int getWorldX() {
            return worldX;
        }

        public int getWorldZ() {
            return worldZ;
        }

        public int getTerrainY() {
            return terrainY;
        }

        public float getRadialFactor() {
            return radialFactor;
        }

        public float getSeed01() {
            return seed01;
        }

        public float getSeed01b() {
            return seed01b;
        }

        public float getSeed01c() {
            return seed01c;
        }

        public int getDensitySeed() {
            return densitySeed;
        }
    }
}