package com.denfop.api.pollution.radiation;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class EventHandler {

    private static final String BONUS_SPAWN_TAG = "iu_rad_bonus_spawn";

    @SubscribeEvent
    public void tick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().dimension() != Level.OVERWORLD || event.getEntity().level().isClientSide) {
            return;
        }
        RadiationSystem.rad_system.work(event.getEntity());
    }

    @SubscribeEvent
    public void tick(LevelTickEvent.Post event) {
        if (event.getLevel().dimension() != Level.OVERWORLD || event.getLevel().isClientSide) {
            return;
        }
        RadiationSystem.rad_system.workDecay(event.getLevel());
    }

    @SubscribeEvent
    public void onFinalizeSpawn(FinalizeSpawnEvent event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (serverLevel.dimension() != Level.OVERWORLD) {
            return;
        }

        if (!(event.getEntity() instanceof Monster originalMob)) {
            return;
        }

        if (originalMob.getPersistentData().getBoolean(BONUS_SPAWN_TAG)) {
            return;
        }

        if (event.getSpawnType() != MobSpawnType.NATURAL && event.getSpawnType() != MobSpawnType.REINFORCEMENT) {
            return;
        }

        RadiationSystem system = RadiationSystem.rad_system;
        if (system == null) {
            return;
        }

        Radiation radiation = system.getMap().get(new ChunkPos(originalMob.blockPosition()));
        if (radiation == null) {
            return;
        }

        int radiationOrdinal = radiation.getLevel().ordinal();
        int extraAttempts = getExtraSpawnAttempts(radiationOrdinal);
        if (extraAttempts <= 0) {
            return;
        }

        float chance = getExtraSpawnChance(radiationOrdinal);
        DifficultyInstance difficulty = event.getDifficulty();

        for (int i = 0; i < extraAttempts; i++) {
            if (serverLevel.random.nextFloat() <= chance) {
                spawnBonusMob(serverLevel, originalMob, event.getSpawnType(), difficulty, radiationOrdinal);
            }
        }
    }

    private int getExtraSpawnAttempts(int radiationOrdinal) {
        switch (radiationOrdinal) {
            case 1: // DEFAULT
                return 1;
            case 2: // MEDIUM
                return 1;
            case 3: // HIGH
                return 2;
            case 4: // VERY_HIGH
                return 3;
            default: // LOW
                return 0;
        }
    }

    private float getExtraSpawnChance(int radiationOrdinal) {
        switch (radiationOrdinal) {
            case 1:
                return 0.12F;
            case 2:
                return 0.28F;
            case 3:
                return 0.55F;
            case 4:
                return 0.85F;
            default:
                return 0.0F;
        }
    }

    private void spawnBonusMob(
            ServerLevel serverLevel,
            Monster originalMob,
            MobSpawnType spawnType,
            DifficultyInstance difficulty,
            int radiationOrdinal
    ) {
        Entity entity = originalMob.getType().create(serverLevel);
        if (!(entity instanceof Mob extraMob)) {
            return;
        }

        extraMob.getPersistentData().putBoolean(BONUS_SPAWN_TAG, true);

        int spread = 2 + radiationOrdinal * 2;

        for (int attempt = 0; attempt < 6; attempt++) {
            int x = Mth.floor(originalMob.getX()) + serverLevel.random.nextInt(spread * 2 + 1) - spread;
            int z = Mth.floor(originalMob.getZ()) + serverLevel.random.nextInt(spread * 2 + 1) - spread;

            BlockPos spawnPos = serverLevel.getHeightmapPos(
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    new BlockPos(x, 0, z)
            );

            if (!serverLevel.getFluidState(spawnPos.below()).isEmpty()) {
                continue;
            }

            extraMob.moveTo(
                    x + 0.5D,
                    spawnPos.getY(),
                    z + 0.5D,
                    serverLevel.random.nextFloat() * 360.0F,
                    0.0F
            );

            if (!serverLevel.noCollision(extraMob)) {
                continue;
            }

            extraMob.finalizeSpawn(serverLevel, difficulty, spawnType, null);
            serverLevel.addFreshEntity(extraMob);
            return;
        }
    }
}