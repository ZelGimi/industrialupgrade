package com.denfop.api.pollution;

import com.denfop.api.pollution.component.ChunkLevel;
import com.denfop.api.pollution.component.LevelPollution;
import com.denfop.api.pollution.utils.Vec2f;
import com.denfop.api.windsystem.EnumTypeWind;
import com.denfop.api.windsystem.EnumWindSide;
import com.denfop.api.windsystem.IWindSystem;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.config.ModConfig;
import com.denfop.network.packet.EnumPollutionSyncType;
import com.denfop.network.packet.PacketPollution;
import com.denfop.network.packet.PacketUpdatePollution;
import com.denfop.potion.IUPotion;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.common.NeoForge;

import java.util.*;

public class PollutionManager {

    private static final int CLIENT_SYNC_RADIUS = 5;

    private static final int TERRAIN_SPREAD_RADIUS_CHUNKS = 1;
    private static final float TERRAIN_SPREAD_SIGMA_BLOCKS = 6.0F;
    private static final float TERRAIN_MIN_INFLUENCE = 0.08F;

    public static PollutionManager pollutionManager;

    private final IWindSystem wind;
    private final Random random;
    private final Set<ChunkPos> dirtyAirChunks = new HashSet<>();
    private final Set<ChunkPos> dirtySoilChunks = new HashSet<>();
    Map<ChunkPos, ChunkLevel> pollutionAir = new HashMap<>();
    Map<ChunkPos, List<PollutionMechanism>> pollutionAirChunks = new HashMap<>();
    Map<ChunkPos, List<PollutionMechanism>> pollutionSoilChunks = new HashMap<>();
    Map<ChunkPos, ChunkLevel> pollutionSoil = new HashMap<>();

    public PollutionManager() {
        pollutionManager = this;
        this.wind = WindSystem.windSystem;
        this.random = new Random();
        NeoForge.EVENT_BUS.register(new EventHandler());
    }

    public void work(Player player) {
        if (player.level().dimension() != Level.OVERWORLD || player.level().isClientSide) {
            return;
        }

        if (player.level().getGameTime() % 200 == 0) {
            ChunkPos pos = new ChunkPos(player.blockPosition());

            ChunkLevel soilPollution = this.pollutionSoil.get(pos);
            if (ModConfig.COMMON.soilPollution.get() && soilPollution != null) {
                int soilLevel = soilPollution.getLevelPollution().ordinal();

                if (soilLevel >= 1) {
                    int slownessAmplifier = Math.min(3, Math.max(0, soilLevel - 1));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 220, slownessAmplifier));

                    if (soilLevel >= 2) {
                        int digSlowAmplifier = Math.min(2, soilLevel - 2);
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 220, digSlowAmplifier));
                    }

                    if (soilLevel >= 3) {
                        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 220, 0));
                    }

                    if (soilLevel >= 4) {
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 220, 0));
                    }
                }
            }

            ChunkLevel airPollution = this.pollutionAir.get(pos);
            if (ModConfig.COMMON.airPollution.get() && airPollution != null) {
                int airLevel = airPollution.getLevelPollution().ordinal();

                if (airLevel >= 2) {
                    player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 220, 0));

                    if (airLevel >= 3) {
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 220, 0));

                        if (airLevel >= 4) {
                            player.addEffect(new MobEffectInstance(IUPotion.poison, 220, 0));
                        }
                    }
                }
            }
        }
    }

    public void addAirPollutionMechanism(PollutionMechanism pollutionMechanism) {
        List<PollutionMechanism> pollution = pollutionAirChunks.get(pollutionMechanism.getChunkPos());
        if (pollution == null) {
            pollution = new ArrayList<>();
            pollution.add(pollutionMechanism);
            pollutionAirChunks.put(pollutionMechanism.getChunkPos(), pollution);
        } else {
            pollution.add(pollutionMechanism);
        }
    }

    public void removeAirPollutionMechanism(PollutionMechanism pollutionMechanism) {
        if (pollutionMechanism == null) {
            return;
        }
        final List<PollutionMechanism> pollution = pollutionAirChunks.computeIfAbsent(
                pollutionMechanism.getChunkPos(),
                k -> new ArrayList<>()
        );
        pollution.remove(pollutionMechanism);
    }

    public void removeSoilPollutionMechanism(PollutionMechanism pollutionMechanism) {
        if (pollutionMechanism == null) {
            return;
        }
        List<PollutionMechanism> pollution = pollutionSoilChunks.computeIfAbsent(
                pollutionMechanism.getChunkPos(),
                k -> new ArrayList<>()
        );
        pollution.remove(pollutionMechanism);
    }

    public void loadData(CompoundTag tagCompound) {
        pollutionSoil.clear();
        pollutionAir.clear();
        pollutionAirChunks.clear();
        pollutionSoilChunks.clear();
        dirtyAirChunks.clear();
        dirtySoilChunks.clear();

        CompoundTag soil = tagCompound.getCompound("soil");
        CompoundTag air = tagCompound.getCompound("air");
        final int size = soil.getInt("size");
        final int size1 = air.getInt("size");

        for (int i = 0; i < size; i++) {
            ChunkLevel chunkLevel = new ChunkLevel(soil.getCompound(String.valueOf(i)));
            pollutionSoil.put(chunkLevel.getPos(), chunkLevel);
        }
        for (int i = 0; i < size1; i++) {
            ChunkLevel chunkLevel = new ChunkLevel(air.getCompound(String.valueOf(i)));
            pollutionAir.put(chunkLevel.getPos(), chunkLevel);
        }
    }

    public CompoundTag writeCompound() {
        CompoundTag nbtTagCompound = new CompoundTag();
        CompoundTag soil = new CompoundTag();
        CompoundTag air = new CompoundTag();
        try {
            List<ChunkLevel> pollutionSoilChunk = new ArrayList<>(pollutionSoil.values());
            List<ChunkLevel> pollutionAirChunk = new ArrayList<>(pollutionAir.values());

            soil.putInt("size", pollutionSoilChunk.size());
            air.putInt("size", pollutionAirChunk.size());

            for (int i = 0; i < pollutionSoilChunk.size(); i++) {
                ChunkLevel chunkLevel = pollutionSoilChunk.get(i);
                if (chunkLevel != null) {
                    soil.put(String.valueOf(i), chunkLevel.writeCompound());
                }
            }

            for (int i = 0; i < pollutionAirChunk.size(); i++) {
                ChunkLevel chunkLevel = pollutionAirChunk.get(i);
                if (chunkLevel != null) {
                    air.put(String.valueOf(i), chunkLevel.writeCompound());
                }
            }

            nbtTagCompound.put("air", air);
            nbtTagCompound.put("soil", soil);
        } catch (Exception e) {
            com.denfop.IUCore.LOGGER.warn("[IU pollution] Failed to write pollution saved data; writing empty pollution tag instead.", e);
        }
        return nbtTagCompound;
    }

    public void addSoilPollutionMechanism(PollutionMechanism pollutionMechanism) {
        if (pollutionMechanism == null) {
            return;
        }
        List<PollutionMechanism> pollution = pollutionSoilChunks.get(pollutionMechanism.getChunkPos());
        if (pollution == null) {
            pollution = new ArrayList<>();
            pollution.add(pollutionMechanism);
            pollutionSoilChunks.put(pollutionMechanism.getChunkPos(), pollution);
        } else {
            pollution.add(pollutionMechanism);
        }
    }

    public ChunkLevel getChunkLevelAir(ChunkPos chunkPos) {
        return pollutionAir.get(chunkPos);
    }

    public ChunkLevel getChunkLevelSoil(ChunkPos chunkPos) {
        return pollutionSoil.get(chunkPos);
    }

    public Map<ChunkPos, ChunkLevel> getPollutionAirMap() {
        return pollutionAir;
    }

    public Map<ChunkPos, ChunkLevel> getPollutionSoilMap() {
        return pollutionSoil;
    }

    public void addChunkLevelAir(ChunkLevel level) {
        if (level == null) {
            return;
        }
        pollutionAir.put(level.getPos(), level);
    }

    public void addChunkLevelSoil(ChunkLevel level) {
        if (level == null) {
            return;
        }
        pollutionSoil.put(level.getPos(), level);
    }

    public void clearSyncedPollution() {
        pollutionAir.clear();
        pollutionSoil.clear();
    }

    public void update(Player player) {
        update(player, CLIENT_SYNC_RADIUS);
    }

    public void update(Player player, int radius) {
        if (player == null || player.level().isClientSide || player.level().dimension() != Level.OVERWORLD) {
            return;
        }

        ChunkPos center = new ChunkPos(player.blockPosition());
        List<ChunkLevel> air = collectNearby(pollutionAir, center, radius);
        List<ChunkLevel> soil = collectNearby(pollutionSoil, center, radius);
        new PacketPollution(air, soil, player);
    }

    private List<ChunkLevel> collectNearby(Map<ChunkPos, ChunkLevel> source, ChunkPos center, int radius) {
        List<ChunkLevel> result = new ArrayList<>();
        for (ChunkLevel chunkLevel : source.values()) {
            if (chunkLevel == null) {
                continue;
            }

            ChunkPos pos = chunkLevel.getPos();
            if (Math.abs(pos.x - center.x) <= radius && Math.abs(pos.z - center.z) <= radius) {
                result.add(chunkLevel);
            }
        }
        return result;
    }

    public Vec2f getVector(EnumWindSide windSide) {
        switch (windSide) {
            case E:
                return new Vec2f(1, 0);
            case W:
                return new Vec2f(-1, 0);
            case N:
                return new Vec2f(0, 1);
            case S:
                return new Vec2f(0, -1);
            case NE:
                return new Vec2f(1, 1);
            case NW:
                return new Vec2f(-1, 1);
            case SE:
                return new Vec2f(1, -1);
            case SW:
                return new Vec2f(-1, -1);
        }
        return new Vec2f(0, 0);
    }

    public void tick(Level world) {
        if (world.getGameTime() % 20 != 0) {
            return;
        }

        if (ModConfig.COMMON.airPollution.get()) {
            final Set<Map.Entry<ChunkPos, ChunkLevel>> entries = new HashSet<>(pollutionAir.entrySet());
            final EnumWindSide windSide = this.wind.getWindSide();
            final Vec2f vector = getVector(windSide);
            final EnumTypeWind windLevel = this.wind.getEnumTypeWind();

            for (Map.Entry<ChunkPos, ChunkLevel> entry : entries) {
                ChunkLevel chunkLevel = entry.getValue();
                if (chunkLevel == null) {
                    continue;
                }

                double distance = calculateDistance(chunkLevel.getDefaultPos(), chunkLevel.getPos());

                if (canChange(windLevel) && distance <= 10) {
                    ChunkPos oldPos = entry.getKey();
                    pollutionAir.remove(oldPos);
                    chunkLevel.addChunkPos((int) vector.x, (int) vector.y);
                    pollutionAir.put(chunkLevel.getPos(), chunkLevel);

                    markAirDirty(oldPos);
                    markAirDirty(chunkLevel.getPos());
                }
            }
        }

        if (ModConfig.COMMON.soilPollution.get() && world.getGameTime() % 6000 == 0) {
            for (Map.Entry<ChunkPos, ChunkLevel> entry : pollutionSoil.entrySet()) {
                ChunkLevel chunkLevel = entry.getValue();
                if (chunkLevel != null) {
                    double oldPollution = chunkLevel.getPollution();
                    LevelPollution oldLevel = chunkLevel.getLevelPollution();

                    chunkLevel.setPollution(chunkLevel.getPollution() / 2);
                    if (chunkLevel.getPollution() < 2 && chunkLevel.getLevelPollution() != LevelPollution.VERY_LOW) {
                        chunkLevel.setPollution(10);
                        chunkLevel.setLevelPollution(LevelPollution.values()[Math.max(
                                0,
                                chunkLevel.getLevelPollution().ordinal() - 1
                        )]);
                    }

                    if (oldPollution != chunkLevel.getPollution() || oldLevel != chunkLevel.getLevelPollution()) {
                        markSoilDirty(entry.getKey());
                    }
                }
            }
        }

        if (ModConfig.COMMON.airPollution.get() && world.getGameTime() % 6000 == 0) {
            for (Map.Entry<ChunkPos, ChunkLevel> entry : pollutionAir.entrySet()) {
                ChunkLevel chunkLevel = entry.getValue();
                if (chunkLevel != null) {
                    double oldPollution = chunkLevel.getPollution();
                    LevelPollution oldLevel = chunkLevel.getLevelPollution();

                    chunkLevel.setPollution(chunkLevel.getPollution() / 2);
                    if (chunkLevel.getPollution() < 2 && chunkLevel.getLevelPollution() != LevelPollution.VERY_LOW) {
                        chunkLevel.setPollution(10);
                        chunkLevel.setLevelPollution(LevelPollution.values()[Math.max(
                                0,
                                chunkLevel.getLevelPollution().ordinal() - 1
                        )]);
                    }

                    if (oldPollution != chunkLevel.getPollution() || oldLevel != chunkLevel.getLevelPollution()) {
                        markAirDirty(entry.getKey());
                    }
                }
            }
        }

        if (ModConfig.COMMON.airPollution.get()) {
            for (Map.Entry<ChunkPos, List<PollutionMechanism>> entry : pollutionAirChunks.entrySet()) {
                ChunkLevel chunkLevel = pollutionAir.get(entry.getKey());
                if (chunkLevel == null) {
                    chunkLevel = new ChunkLevel(entry.getKey(), LevelPollution.VERY_LOW, 0);
                    for (PollutionMechanism pollutionMechanism : entry.getValue()) {
                        chunkLevel.addPollution(pollutionMechanism.getPollution());
                    }
                    pollutionAir.put(chunkLevel.getPos(), chunkLevel);
                    markAirDirty(chunkLevel.getPos());
                } else {
                    for (PollutionMechanism pollutionMechanism : entry.getValue()) {
                        chunkLevel.addPollution(pollutionMechanism.getPollution());
                    }
                    markAirDirty(chunkLevel.getPos());
                }
            }
        }

        if (ModConfig.COMMON.soilPollution.get()) {
            for (Map.Entry<ChunkPos, List<PollutionMechanism>> entry : pollutionSoilChunks.entrySet()) {
                ChunkLevel chunkLevel = pollutionSoil.get(entry.getKey());
                if (chunkLevel == null) {
                    chunkLevel = new ChunkLevel(entry.getKey(), LevelPollution.VERY_LOW, 0);
                    for (PollutionMechanism pollutionMechanism : entry.getValue()) {
                        chunkLevel.addPollution(pollutionMechanism.getPollution());
                    }
                    pollutionSoil.put(chunkLevel.getPos(), chunkLevel);
                    markSoilDirty(chunkLevel.getPos());
                } else {
                    for (PollutionMechanism pollutionMechanism : entry.getValue()) {
                        chunkLevel.addPollution(pollutionMechanism.getPollution());
                    }
                    markSoilDirty(chunkLevel.getPos());
                }
            }
        }

        flushDirtyChunks(world);
    }

    public void processSoilTerrain(ServerLevel world) {
        if (!ModConfig.COMMON.soilPollution.get()) {
            return;
        }

        if (world.getGameTime() % 20 != 0) {
            return;
        }

        Set<ChunkPos> candidateChunks = new HashSet<>();

        for (Map.Entry<ChunkPos, ChunkLevel> entry : this.pollutionSoil.entrySet()) {
            ChunkLevel chunkLevel = entry.getValue();
            if (chunkLevel == null || chunkLevel.getLevelPollution().ordinal() <= 0) {
                continue;
            }

            ChunkPos origin = entry.getKey();
            for (int dx = -TERRAIN_SPREAD_RADIUS_CHUNKS; dx <= TERRAIN_SPREAD_RADIUS_CHUNKS; dx++) {
                for (int dz = -TERRAIN_SPREAD_RADIUS_CHUNKS; dz <= TERRAIN_SPREAD_RADIUS_CHUNKS; dz++) {
                    candidateChunks.add(new ChunkPos(origin.x + dx, origin.z + dz));
                }
            }
        }

        long stepTime = world.getGameTime() / 20L;

        for (ChunkPos candidateChunk : candidateChunks) {
            float chunkInfluence = computeSoilChunkInfluence(candidateChunk);
            if (chunkInfluence < TERRAIN_MIN_INFLUENCE) {
                continue;
            }

            int intervalSteps = getSoilDegradationIntervalStepsByInfluence(chunkInfluence);
            int chunkHash = Math.floorMod(candidateChunk.x * 31 + candidateChunk.z * 17, Math.max(1, intervalSteps));
            if ((stepTime + chunkHash) % intervalSteps != 0) {
                continue;
            }

            int attempts = getSoilDegradationAttemptsByInfluence(chunkInfluence);
            float chance = getSoilDegradationChanceByInfluence(chunkInfluence);

            for (int i = 0; i < attempts; i++) {
                if (world.random.nextFloat() > chance) {
                    continue;
                }
                tryDegradeRandomSurface(world, candidateChunk, chunkInfluence);
            }
        }
    }

    public void processVegetationDecay(ServerLevel world) {
        if (!ModConfig.COMMON.soilPollution.get()) {
            return;
        }

        if (world.getGameTime() % 20 != 0) {
            return;
        }

        for (Map.Entry<ChunkPos, ChunkLevel> entry : this.pollutionSoil.entrySet()) {
            ChunkLevel chunkLevel = entry.getValue();
            if (chunkLevel == null) {
                continue;
            }

            int levelOrdinal = chunkLevel.getLevelPollution().ordinal();
            if (levelOrdinal <= 0) {
                continue;
            }

            int intervalSteps = getVegetationDecayIntervalSteps(levelOrdinal);
            long stepTime = world.getGameTime() / 20L;
            int chunkHash = Math.floorMod(chunkLevel.getPos().x * 43 + chunkLevel.getPos().z * 19, Math.max(1, intervalSteps));
            if ((stepTime + chunkHash) % intervalSteps != 0) {
                continue;
            }

            int attempts = getVegetationDecayAttempts(levelOrdinal);
            float chance = getVegetationDecayChance(levelOrdinal);

            for (int i = 0; i < attempts; i++) {
                if (world.random.nextFloat() > chance) {
                    continue;
                }
                tryDecayRandomVegetation(world, chunkLevel.getPos());
            }
        }
    }

    public void processLeafDecay(ServerLevel world) {
        if (!ModConfig.COMMON.soilPollution.get()) {
            return;
        }

        if (world.getGameTime() % 20 != 0) {
            return;
        }

        for (Map.Entry<ChunkPos, ChunkLevel> entry : this.pollutionSoil.entrySet()) {
            ChunkLevel chunkLevel = entry.getValue();
            if (chunkLevel == null) {
                continue;
            }

            int levelOrdinal = chunkLevel.getLevelPollution().ordinal();
            if (levelOrdinal <= 0) {
                continue;
            }

            int intervalSteps = getLeafDecayIntervalSteps(levelOrdinal);
            long stepTime = world.getGameTime() / 20L;
            int chunkHash = Math.floorMod(chunkLevel.getPos().x * 59 + chunkLevel.getPos().z * 23, Math.max(1, intervalSteps));
            if ((stepTime + chunkHash) % intervalSteps != 0) {
                continue;
            }

            int attempts = getLeafDecayAttempts(levelOrdinal);
            float chance = getLeafDecayChance(levelOrdinal);

            for (int i = 0; i < attempts; i++) {
                if (world.random.nextFloat() > chance) {
                    continue;
                }
                tryDecayRandomLeaves(world, chunkLevel.getPos());
            }
        }
    }

    private void markAirDirty(ChunkPos pos) {
        if (pos != null) {
            this.dirtyAirChunks.add(pos);
        }
    }

    private void markSoilDirty(ChunkPos pos) {
        if (pos != null) {
            this.dirtySoilChunks.add(pos);
        }
    }

    private void flushDirtyChunks(Level world) {
        if (world.isClientSide || world.dimension() != Level.OVERWORLD) {
            this.dirtyAirChunks.clear();
            this.dirtySoilChunks.clear();
            return;
        }

        for (ChunkPos pos : this.dirtyAirChunks) {
            ChunkLevel level = this.pollutionAir.get(pos);
            if (level != null) {
                new PacketUpdatePollution(world, level, EnumPollutionSyncType.AIR);
            }
        }

        for (ChunkPos pos : this.dirtySoilChunks) {
            ChunkLevel level = this.pollutionSoil.get(pos);
            if (level != null) {
                new PacketUpdatePollution(world, level, EnumPollutionSyncType.SOIL);
            }
        }

        this.dirtyAirChunks.clear();
        this.dirtySoilChunks.clear();
    }

    private int getVegetationDecayIntervalSteps(int levelOrdinal) {
        switch (levelOrdinal) {
            case 1:
                return 6;
            case 2:
                return 4;
            case 3:
                return 2;
            case 4:
                return 1;
            default:
                return Integer.MAX_VALUE;
        }
    }

    private int getVegetationDecayAttempts(int levelOrdinal) {
        switch (levelOrdinal) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 5;
            default:
                return 0;
        }
    }

    private float getVegetationDecayChance(int levelOrdinal) {
        switch (levelOrdinal) {
            case 1:
                return 0.30F;
            case 2:
                return 0.55F;
            case 3:
                return 0.80F;
            case 4:
                return 1.00F;
            default:
                return 0.0F;
        }
    }

    private int getLeafDecayIntervalSteps(int levelOrdinal) {
        switch (levelOrdinal) {
            case 1:
                return 8;
            case 2:
                return 5;
            case 3:
                return 3;
            case 4:
                return 1;
            default:
                return Integer.MAX_VALUE;
        }
    }

    private int getLeafDecayAttempts(int levelOrdinal) {
        switch (levelOrdinal) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 6;
            default:
                return 0;
        }
    }

    private float getLeafDecayChance(int levelOrdinal) {
        switch (levelOrdinal) {
            case 1:
                return 0.25F;
            case 2:
                return 0.50F;
            case 3:
                return 0.80F;
            case 4:
                return 1.00F;
            default:
                return 0.0F;
        }
    }

    private void tryDegradeRandomSurface(ServerLevel world, ChunkPos chunkPos, float chunkInfluence) {
        int worldX = (chunkPos.x << 4) + world.random.nextInt(16);
        int worldZ = (chunkPos.z << 4) + world.random.nextInt(16);

        float localInfluence = computeSoilBlockInfluence(worldX, worldZ);
        if (localInfluence < TERRAIN_MIN_INFLUENCE) {
            return;
        }

        if (world.random.nextFloat() > localInfluence) {
            return;
        }

        int motionY = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, worldX, worldZ) - 1;
        BlockState state = world.getBlockState(new BlockPos(worldX, motionY, worldZ));

        int targetY = motionY;
        if (!state.getFluidState().isEmpty()) {
            targetY = world.getHeight(Heightmap.Types.OCEAN_FLOOR, worldX, worldZ) - 1;
        }

        if (targetY < world.getMinBuildHeight()) {
            return;
        }

        BlockPos targetPos = new BlockPos(worldX, targetY, worldZ);
        BlockState targetState = world.getBlockState(targetPos);

        if (world.getBlockState(targetPos.above()).is(Blocks.DEAD_BUSH)) {
            return;
        }

        if (!isMudConvertible(targetState)) {
            return;
        }

        world.setBlock(targetPos, Blocks.MUD.defaultBlockState(), 3);
    }

    private float computeSoilChunkInfluence(ChunkPos candidateChunk) {
        float centerX = (candidateChunk.x << 4) + 8.0F;
        float centerZ = (candidateChunk.z << 4) + 8.0F;
        return computeSoilInfluenceAt(centerX, centerZ);
    }

    private float computeSoilBlockInfluence(int worldX, int worldZ) {
        return computeSoilInfluenceAt(worldX + 0.5F, worldZ + 0.5F);
    }

    private float computeSoilInfluenceAt(float worldX, float worldZ) {
        float influence = 0.0F;

        int chunkX = Mth.floor(worldX) >> 4;
        int chunkZ = Mth.floor(worldZ) >> 4;

        for (int dx = -TERRAIN_SPREAD_RADIUS_CHUNKS; dx <= TERRAIN_SPREAD_RADIUS_CHUNKS; dx++) {
            for (int dz = -TERRAIN_SPREAD_RADIUS_CHUNKS; dz <= TERRAIN_SPREAD_RADIUS_CHUNKS; dz++) {
                ChunkPos samplePos = new ChunkPos(chunkX + dx, chunkZ + dz);
                ChunkLevel chunkLevel = this.pollutionSoil.get(samplePos);
                if (chunkLevel == null) {
                    continue;
                }

                float severity = getSoilTerrainSeverity(chunkLevel);
                if (severity <= 0.0F) {
                    continue;
                }

                float centerX = (samplePos.x << 4) + 8.0F;
                float centerZ = (samplePos.z << 4) + 8.0F;

                float diffX = worldX - centerX;
                float diffZ = worldZ - centerZ;
                float weight = gaussianWeight(diffX, diffZ, TERRAIN_SPREAD_SIGMA_BLOCKS);

                influence += severity * weight;
            }
        }

        return Mth.clamp(influence, 0.0F, 1.0F);
    }

    private float getSoilTerrainSeverity(ChunkLevel chunkLevel) {
        if (chunkLevel == null) {
            return 0.0F;
        }

        float level = chunkLevel.getLevelPollution().ordinal();
        float withinLevel = Mth.clamp((float) (chunkLevel.getPollution() / 125.0D), 0.0F, 1.0F);

        if (level <= 0.0F && withinLevel < 0.05F) {
            return 0.0F;
        }

        return Mth.clamp(level * 0.22F + withinLevel * 0.12F, 0.0F, 1.0F);
    }

    private float gaussianWeight(float dx, float dz, float sigma) {
        float distanceSq = dx * dx + dz * dz;
        float divisor = 2.0F * sigma * sigma;
        return (float) Math.exp(-distanceSq / divisor);
    }

    private int getSoilDegradationIntervalStepsByInfluence(float influence) {
        float t = smoothstep01(normalizeInfluence(influence));

        if (t >= 0.85F) {
            return 1;
        }
        if (t >= 0.60F) {
            return 2;
        }
        if (t >= 0.35F) {
            return 3;
        }
        if (t >= 0.18F) {
            return 5;
        }
        return 8;
    }

    private int getSoilDegradationAttemptsByInfluence(float influence) {
        float t = smoothstep01(normalizeInfluence(influence));

        if (t >= 0.85F) {
            return 6;
        }
        if (t >= 0.60F) {
            return 4;
        }
        if (t >= 0.35F) {
            return 3;
        }
        if (t >= 0.18F) {
            return 2;
        }
        return 1;
    }

    private float getSoilDegradationChanceByInfluence(float influence) {
        float t = smoothstep01(normalizeInfluence(influence));
        return Mth.lerp(t, 0.18F, 1.00F);
    }

    private float normalizeInfluence(float influence) {
        return Mth.clamp((influence - TERRAIN_MIN_INFLUENCE) / (1.0F - TERRAIN_MIN_INFLUENCE), 0.0F, 1.0F);
    }

    private float smoothstep01(float value) {
        float t = Mth.clamp(value, 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }

    private void tryDecayRandomVegetation(ServerLevel world, ChunkPos chunkPos) {
        int worldX = (chunkPos.x << 4) + world.random.nextInt(16);
        int worldZ = (chunkPos.z << 4) + world.random.nextInt(16);

        int topY = world.getHeight(Heightmap.Types.WORLD_SURFACE, worldX, worldZ) - 1;
        if (topY < world.getMinBuildHeight()) {
            return;
        }

        int minY = Math.max(world.getMinBuildHeight(), topY - 4);
        for (int y = topY; y >= minY; y--) {
            BlockPos pos = new BlockPos(worldX, y, worldZ);
            BlockState state = world.getBlockState(pos);

            if (state.isAir() || !state.getFluidState().isEmpty()) {
                continue;
            }

            if (!isVegetationToDryBush(state)) {
                continue;
            }

            replaceVegetationWithDeadBush(world, pos, state);
            return;
        }
    }

    private void tryDecayRandomLeaves(ServerLevel world, ChunkPos chunkPos) {
        int worldX = (chunkPos.x << 4) + world.random.nextInt(16);
        int worldZ = (chunkPos.z << 4) + world.random.nextInt(16);

        int topY = world.getHeight(Heightmap.Types.WORLD_SURFACE, worldX, worldZ) - 1;
        if (topY < world.getMinBuildHeight()) {
            return;
        }

        int scanTop = Math.min(world.getMaxBuildHeight() - 1, topY + 6);
        int scanBottom = Math.max(world.getMinBuildHeight(), topY - 8);

        for (int y = scanTop; y >= scanBottom; y--) {
            BlockPos pos = new BlockPos(worldX, y, worldZ);
            BlockState state = world.getBlockState(pos);

            if (!state.is(BlockTags.LEAVES)) {
                continue;
            }

            world.levelEvent(2001, pos, Block.getId(state));
            world.removeBlock(pos, false);
            return;
        }
    }

    private void replaceVegetationWithDeadBush(ServerLevel world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof DoublePlantBlock && state.hasProperty(DoublePlantBlock.HALF)) {
            BlockPos lowerPos = state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
            BlockPos upperPos = lowerPos.above();

            world.removeBlock(upperPos, false);

            prepareDryBushBase(world, lowerPos.below());
            if (Blocks.DEAD_BUSH.defaultBlockState().canSurvive(world, lowerPos)) {
                world.setBlock(lowerPos, Blocks.DEAD_BUSH.defaultBlockState(), 3);
            } else {
                world.removeBlock(lowerPos, false);
            }
            return;
        }

        prepareDryBushBase(world, pos.below());
        if (Blocks.DEAD_BUSH.defaultBlockState().canSurvive(world, pos)) {
            world.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(), 3);
        } else {
            world.removeBlock(pos, false);
        }
    }

    private void prepareDryBushBase(ServerLevel world, BlockPos groundPos) {
        BlockState groundState = world.getBlockState(groundPos);

        if (groundState.isAir() || !groundState.getFluidState().isEmpty()) {
            return;
        }

        if (groundState.is(Blocks.MUD)) {
            return;
        }

        if (groundState.is(Blocks.GRASS_BLOCK)
                || groundState.is(Blocks.DIRT)
                || groundState.is(Blocks.COARSE_DIRT)
                || groundState.is(Blocks.ROOTED_DIRT)
                || groundState.is(Blocks.PODZOL)
                || groundState.is(Blocks.MYCELIUM)
                || groundState.is(Blocks.DIRT_PATH)
                || groundState.is(Blocks.FARMLAND)
                || groundState.is(Blocks.CLAY)
                || groundState.is(Blocks.GRAVEL)
                || groundState.is(Blocks.SAND)
                || groundState.is(Blocks.RED_SAND)) {
            world.setBlock(groundPos, Blocks.MUD.defaultBlockState(), 3);
        }
    }

    private boolean isVegetationToDryBush(BlockState state) {
        if (state.is(Blocks.DEAD_BUSH)) {
            return false;
        }

        return state.is(Blocks.SHORT_GRASS)
                || state.is(Blocks.TALL_GRASS)
                || state.is(Blocks.FERN)
                || state.is(Blocks.LARGE_FERN)
                || state.is(Blocks.SWEET_BERRY_BUSH)
                || state.is(BlockTags.SMALL_FLOWERS)
                || state.is(BlockTags.TALL_FLOWERS)
                || state.getBlock() instanceof CropBlock
                || state.getBlock() instanceof SaplingBlock
                || state.getBlock() instanceof SweetBerryBushBlock;
    }

    private boolean isMudConvertible(BlockState state) {
        if (state.isAir()) {
            return false;
        }

        if (state.is(Blocks.MUD) || state.is(Blocks.MUDDY_MANGROVE_ROOTS)) {
            return false;
        }

        return state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.DIRT)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.ROOTED_DIRT)
                || state.is(Blocks.PODZOL)
                || state.is(Blocks.MYCELIUM)
                || state.is(Blocks.DIRT_PATH)
                || state.is(Blocks.FARMLAND)
                || state.is(Blocks.CLAY)
                || state.is(Blocks.SAND)
                || state.is(Blocks.RED_SAND)
                || state.is(Blocks.GRAVEL);
    }

    public double calculateDistance(ChunkPos pos1, ChunkPos pos2) {
        int xDiff = pos1.x - pos2.x;
        int zDiff = pos1.z - pos2.z;
        return Math.sqrt(xDiff * xDiff + zDiff * zDiff);
    }

    private boolean canChange(EnumTypeWind windLevel) {
        return random.nextInt(100 - windLevel.ordinal() * 5) == 0;
    }
}