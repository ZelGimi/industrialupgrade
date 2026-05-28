package com.denfop.api.pollution.client;

import com.denfop.potion.IUPotion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public final class PollutionVisualUpdateCache {

    private boolean initialized;
    private ResourceKey<Level> dimension;
    private ChunkPos chunkPos;
    private BlockPos blockPos = BlockPos.ZERO;
    private long nextRecomputeTick;
    private float rainLevel;
    private float thunderLevel;
    private boolean snowAtPlayer;
    private int effectFingerprint;

    private static long computeUpdateInterval(PollutionVisualData data, float rainLevel) {
        if (data == null) {
            return 40L;
        }

        float overall = data.getOverallInfluence();
        float screen = data.getScreenInfluence();
        float fog = data.getFogInfluence();

        if (overall > 0.60F || screen > 0.45F) {
            return 40L;
        }

        if (overall > 0.28F || fog > 0.22F) {
            return 40L;
        }

        if (data.isActive()) {
            return 40L;
        }

        return 40L;
    }

    private static int buildEffectFingerprint(LocalPlayer player) {
        int hash = 1;

        hash = 31 * hash + (player.hasEffect(MobEffects.BLINDNESS) ? 1 : 0);
        hash = 31 * hash + (player.hasEffect(MobEffects.CONFUSION) ? 1 : 0);
        hash = 31 * hash + (player.hasEffect(MobEffects.WITHER) ? 1 : 0);
        hash = 31 * hash + (player.hasEffect(MobEffects.POISON) ? 1 : 0);
        hash = 31 * hash + (player.hasEffect(IUPotion.poison) ? 1 : 0);
        hash = 31 * hash + (player.hasEffect(IUPotion.rad) ? 1 : 0);

        double storedRadiation = player.getPersistentData().getDouble("radiation");
        int radiationBucket = (int) Math.floor(storedRadiation * 4.0D);
        hash = 31 * hash + radiationBucket;

        return hash;
    }

    public boolean shouldRecompute(Minecraft minecraft, LocalPlayer player, long gameTime) {
        if (!initialized || minecraft == null || minecraft.level == null || player == null) {
            return true;
        }

        if (dimension == null || dimension != minecraft.level.dimension()) {
            return true;
        }

        if (gameTime >= nextRecomputeTick) {
            return true;
        }

        BlockPos currentBlock = player.blockPosition();
        ChunkPos currentChunk = new ChunkPos(currentBlock);

        if (!currentChunk.equals(this.chunkPos)) {
            return true;
        }

        int dx = Math.abs(currentBlock.getX() - this.blockPos.getX());
        int dy = Math.abs(currentBlock.getY() - this.blockPos.getY());
        int dz = Math.abs(currentBlock.getZ() - this.blockPos.getZ());

        if ((dx + dz) >= 4 || dy >= 4) {
            return true;
        }

        float currentRain = minecraft.level.getRainLevel(1.0F);
        if (Math.abs(currentRain - this.rainLevel) > 0.03F) {
            return true;
        }

        float currentThunder = minecraft.level.getThunderLevel(1.0F);
        if (Math.abs(currentThunder - this.thunderLevel) > 0.03F) {
            return true;
        }

        boolean currentSnowAtPlayer = PollutionWeatherUtil.isSnowAtPlayer(minecraft);
        if (currentSnowAtPlayer != this.snowAtPlayer) {
            return true;
        }

        int currentFingerprint = buildEffectFingerprint(player);
        return currentFingerprint != this.effectFingerprint;
    }

    public void updateSnapshot(Minecraft minecraft, LocalPlayer player, long gameTime, PollutionVisualData newTarget) {
        this.initialized = true;
        this.dimension = minecraft.level.dimension();
        this.blockPos = player.blockPosition();
        this.chunkPos = new ChunkPos(this.blockPos);
        this.rainLevel = minecraft.level.getRainLevel(1.0F);
        this.thunderLevel = minecraft.level.getThunderLevel(1.0F);
        this.snowAtPlayer = PollutionWeatherUtil.isSnowAtPlayer(minecraft);
        this.effectFingerprint = buildEffectFingerprint(player);
        this.nextRecomputeTick = gameTime + computeUpdateInterval(newTarget, this.rainLevel);
    }

    public void reset() {
        this.initialized = false;
        this.dimension = null;
        this.chunkPos = null;
        this.blockPos = BlockPos.ZERO;
        this.nextRecomputeTick = 0L;
        this.rainLevel = 0.0F;
        this.thunderLevel = 0.0F;
        this.snowAtPlayer = false;
        this.effectFingerprint = 0;
    }
}