package com.denfop.api.pollution;

import com.denfop.IUPotion;
import com.denfop.ModConfig;
import com.denfop.api.windsystem.EnumTypeWind;
import com.denfop.api.windsystem.EnumWindSide;
import com.denfop.api.windsystem.IWindSystem;
import com.denfop.api.windsystem.WindSystem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;

import java.util.*;

public class PollutionManager {

    public static PollutionManager pollutionManager;
    private final IWindSystem wind;
    private final Random random;
    Map<ChunkPos, ChunkLevel> pollutionAir = new HashMap<>();
    Map<ChunkPos, List<IPollutionMechanism>> pollutionAirChunks = new HashMap<>();
    Map<ChunkPos, List<IPollutionMechanism>> pollutionSoilChunks = new HashMap<>();
    Map<ChunkPos, ChunkLevel> pollutionSoil = new HashMap<>();

    public PollutionManager() {
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
            ChunkLevel pollution = this.pollutionSoil.get(pos);
            if (ModConfig.COMMON.soilPollution.get())
                if (pollution != null) {
                    if (pollution.getLevelPollution().ordinal() >= 2) {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0));

                        if (pollution.getLevelPollution().ordinal() >= 3) {
                            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0));

                            if (pollution.getLevelPollution().ordinal() >= 4) {
                                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                            }
                        }
                    }
                }
            pollution = this.pollutionAir.get(pos);
            if (ModConfig.COMMON.airPollution.get())
                if (pollution != null) {
                    if (pollution.getLevelPollution().ordinal() >= 2) {
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                        if (pollution.getLevelPollution().ordinal() >= 3) {
                            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0));
                            if (pollution.getLevelPollution().ordinal() >= 4) {
                                player.addEffect(new MobEffectInstance(IUPotion.poison, 200, 0));

                            }
                        }
                    }
                }
        }
    }


    public void addAirPollutionMechanism(IPollutionMechanism pollutionMechanism) {

        List<IPollutionMechanism> pollution = pollutionAirChunks.get(pollutionMechanism.getChunkPos());
        if (pollution == null) {
            pollution = new ArrayList<>();
            pollution.add(pollutionMechanism);
            pollutionAirChunks.put(pollutionMechanism.getChunkPos(), pollution);
        } else {
            pollution.add(pollutionMechanism);
        }

    }

    public void removeAirPollutionMechanism(IPollutionMechanism pollutionMechanism) {
        if (pollutionMechanism == null) {
            return;
        }
        final List<IPollutionMechanism> pollution = pollutionAirChunks.computeIfAbsent(
                pollutionMechanism.getChunkPos(),
                k -> new ArrayList<>()
        );

        pollution.remove(pollutionMechanism);

    }

    public void removeSoilPollutionMechanism(IPollutionMechanism pollutionMechanism) {
        if (pollutionMechanism == null) {
            return;
        }
        List<IPollutionMechanism> pollution = pollutionSoilChunks.computeIfAbsent(
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


            List<ChunkLevel> pollutionSoilChunk = pollutionSoil.values().stream().toList();
            List<ChunkLevel> pollutionAirChunk = pollutionAir.values().stream().toList();
            soil.putInt("size", pollutionSoilChunk.size());
            air.putInt("size", pollutionAirChunk.size());
            for (int i = 0; i < pollutionSoilChunk.size(); i++) {
                ChunkLevel chunkLevel = pollutionSoilChunk.get(i);
                if (chunkLevel != null)
                    soil.put(String.valueOf(i), chunkLevel.writeCompound());
            }
            for (int i = 0; i < pollutionAirChunk.size(); i++) {
                ChunkLevel chunkLevel = pollutionAirChunk.get(i);
                if (chunkLevel != null)
                    air.put(String.valueOf(i), chunkLevel.writeCompound());
            }
            nbtTagCompound.put("air", air);
            nbtTagCompound.put("soil", soil);
        } catch (Exception e) {
        }
        ;
        return nbtTagCompound;
    }


    public void addSoilPollutionMechanism(IPollutionMechanism pollutionMechanism) {
        if (pollutionMechanism == null) {
            return;
        }
        List<IPollutionMechanism> pollution = pollutionSoilChunks.get(pollutionMechanism.getChunkPos());
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

    public void addChunkLevelSoil(ChunkLevel level) {
        pollutionSoil.put(level.getPos(), level);
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
        final Set<Map.Entry<ChunkPos, ChunkLevel>> entries = new HashSet<>(pollutionAir.entrySet());

        final EnumWindSide windSide = this.wind.getWindSide();
        final Vec2f vector = getVector(windSide);
        final EnumTypeWind windLevel = this.wind.getEnumTypeWind();
        if (ModConfig.COMMON.airPollution.get())
            entries.parallelStream().forEach(entry -> {
                ChunkLevel chunkLevel = entry.getValue();

                double distance = calculateDistance(chunkLevel.getDefaultPos(), chunkLevel.getPos());

                if (canChange(windLevel) && distance <= 10) {
                    pollutionAir.remove(entry.getKey());
                    chunkLevel.addChunkPos((int) vector.x, (int) vector.y);
                    pollutionAir.put(chunkLevel.getPos(), chunkLevel);
                }
            });
        if (ModConfig.COMMON.soilPollution.get())
            if (world.getGameTime() % 6000 == 0) {
                for (Map.Entry<ChunkPos, ChunkLevel> entry : pollutionSoil.entrySet()) {
                    ChunkLevel chunkLevel = entry.getValue();
                    if (chunkLevel != null) {
                        chunkLevel.setPollution(chunkLevel.getPollution() / 2);
                        if (chunkLevel.getPollution() < 2 && chunkLevel.getLevelPollution() != LevelPollution.VERY_LOW) {
                            chunkLevel.setPollution(10);
                            chunkLevel.setLevelPollution(LevelPollution.values()[Math.max(0,
                                    chunkLevel.getLevelPollution().ordinal() - 1)]);
                        }
                    }
                }
            }
        if (ModConfig.COMMON.airPollution.get())
            if (world.getGameTime() % 6000 == 0) {
                for (Map.Entry<ChunkPos, ChunkLevel> entry : pollutionAir.entrySet()) {
                    ChunkLevel chunkLevel = entry.getValue();
                    if (chunkLevel != null) {
                        chunkLevel.setPollution(chunkLevel.getPollution() / 2);
                        if (chunkLevel.getPollution() < 2 && chunkLevel.getLevelPollution() != LevelPollution.VERY_LOW) {
                            chunkLevel.setPollution(10);
                            chunkLevel.setLevelPollution(LevelPollution.values()[Math.max(0,
                                    chunkLevel.getLevelPollution().ordinal() - 1)]);
                        }
                    }
                }
            }
        if (ModConfig.COMMON.airPollution.get())
            for (Map.Entry<ChunkPos, List<IPollutionMechanism>> entry : pollutionAirChunks.entrySet()) {
                ChunkLevel chunkLevel = pollutionAir.get(entry.getKey());
                if (chunkLevel == null) {
                    chunkLevel = new ChunkLevel(entry.getKey(), LevelPollution.VERY_LOW, 0);
                    for (IPollutionMechanism pollutionMechanism : entry.getValue()) {
                        chunkLevel.addPollution(pollutionMechanism.getPollution());
                    }
                    pollutionAir.put(chunkLevel.getPos(), chunkLevel);
                } else {
                    for (IPollutionMechanism pollutionMechanism : entry.getValue()) {
                        chunkLevel.addPollution(pollutionMechanism.getPollution());
                    }
                }
            }
        if (ModConfig.COMMON.soilPollution.get())
            for (Map.Entry<ChunkPos, List<IPollutionMechanism>> entry : pollutionSoilChunks.entrySet()) {
                ChunkLevel chunkLevel = pollutionSoil.get(entry.getKey());
                if (chunkLevel == null) {
                    chunkLevel = new ChunkLevel(entry.getKey(), LevelPollution.VERY_LOW, 0);
                    for (IPollutionMechanism pollutionMechanism : entry.getValue()) {
                        chunkLevel.addPollution(pollutionMechanism.getPollution());
                    }
                    pollutionSoil.put(chunkLevel.getPos(), chunkLevel);
                } else {
                    for (IPollutionMechanism pollutionMechanism : entry.getValue()) {
                        chunkLevel.addPollution(pollutionMechanism.getPollution());
                    }
                }
            }
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
