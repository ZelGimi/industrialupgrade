package com.denfop.api.pollution;

import com.denfop.IUPotion;
import com.denfop.api.windsystem.EnumTypeWind;
import com.denfop.api.windsystem.EnumWindSide;
import com.denfop.api.windsystem.IWindSystem;
import com.denfop.api.windsystem.WindSystem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
        FMLCommonHandler.instance().bus().register(new EventHandler());
    }

    public void work(EntityPlayer player) {
        if (player.getEntityWorld().provider.getDimension() != 0 || player.getEntityWorld().isRemote) {
            return;
        }

        if (player.getEntityWorld().getWorldTime() % 200 == 0) {
            ChunkPos pos = new ChunkPos(player.getPosition());
            ChunkLevel pollution = this.pollutionSoil.get(pos);
            if (pollution != null) {
                if (pollution.getLevelPollution().ordinal() >= 2) {
                    player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 0));
                    if (pollution.getLevelPollution().ordinal() >= 3) {
                        player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 0));
                        if (pollution.getLevelPollution().ordinal() >= 4) {
                            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));

                        }
                    }
                }
            }
            pollution = this.pollutionAir.get(pos);
            if (pollution != null) {
                if (pollution.getLevelPollution().ordinal() >= 2) {
                    player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
                    if (pollution.getLevelPollution().ordinal() >= 3) {
                        player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 200, 0));
                        if (pollution.getLevelPollution().ordinal() >= 4) {
                            player.addPotionEffect(new PotionEffect(IUPotion.poison_gas, 200, 0));

                        }
                    }
                }
            }
        }
    }


    public void addAirPollutionMechanism(IPollutionMechanism pollutionMechanism) {

        List<IPollutionMechanism> pollution = pollutionAirChunks.get(pollutionMechanism.getChunkPos());
        if (pollution == null) {
            pollution = new LinkedList<>();
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
                k -> new LinkedList<>()
        );

        pollution.remove(pollutionMechanism);

    }

    public void removeSoilPollutionMechanism(IPollutionMechanism pollutionMechanism) {
        if (pollutionMechanism == null) {
            return;
        }
        List<IPollutionMechanism> pollution = pollutionSoilChunks.computeIfAbsent(
                pollutionMechanism.getChunkPos(),
                k -> new LinkedList<>()
        );
        pollution.remove(pollutionMechanism);

    }

    public void loadData(NBTTagCompound tagCompound) {
        pollutionSoil.clear();
        pollutionAir.clear();
        pollutionAirChunks.clear();
        pollutionSoilChunks.clear();
        NBTTagCompound soil = tagCompound.getCompoundTag("soil");
        NBTTagCompound air = tagCompound.getCompoundTag("air");
        final int size = soil.getInteger("size");
        final int size1 = air.getInteger("size");
        for (int i = 0; i < size; i++) {
            ChunkLevel chunkLevel = new ChunkLevel(soil.getCompoundTag(String.valueOf(i)));
            pollutionSoil.put(chunkLevel.getPos(), chunkLevel);
        }
        for (int i = 0; i < size1; i++) {
            ChunkLevel chunkLevel = new ChunkLevel(air.getCompoundTag(String.valueOf(i)));
            pollutionAir.put(chunkLevel.getPos(), chunkLevel);
        }
    }

    public NBTTagCompound writeCompound() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        NBTTagCompound soil = new NBTTagCompound();
        NBTTagCompound air = new NBTTagCompound();
        List<ChunkLevel> pollutionSoilChunk = new LinkedList<>(pollutionSoil.values());
        List<ChunkLevel> pollutionAirChunk = new LinkedList<>(pollutionAir.values());
        soil.setInteger("size", pollutionSoilChunk.size());
        air.setInteger("size", pollutionAirChunk.size());
        for (int i = 0; i < pollutionSoilChunk.size(); i++) {
            ChunkLevel chunkLevel = pollutionSoilChunk.get(i);
            soil.setTag(String.valueOf(i), chunkLevel.writeCompound());
        }
        for (int i = 0; i < pollutionAirChunk.size(); i++) {
            ChunkLevel chunkLevel = pollutionAirChunk.get(i);
            air.setTag(String.valueOf(i), chunkLevel.writeCompound());
        }
        nbtTagCompound.setTag("air", air);
        nbtTagCompound.setTag("soil", soil);
        return nbtTagCompound;
    }


    public void addSoilPollutionMechanism(IPollutionMechanism pollutionMechanism) {
        if (pollutionMechanism == null) {
            return;
        }
        List<IPollutionMechanism> pollution = pollutionSoilChunks.get(pollutionMechanism.getChunkPos());
        if (pollution == null) {
            pollution = new LinkedList<>();
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
    public void addChunkLevelSoil(ChunkLevel level){
        pollutionSoil.put(level.getPos(),level);
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

    public void tick(World world) {

        if (world.provider.getWorldTime() % 20 != 0) {
            return;
        }
        final Set<Map.Entry<ChunkPos, ChunkLevel>> entries = new HashSet<>(pollutionAir.entrySet());

        final EnumWindSide windSide = this.wind.getWindSide();
        final Vec2f vector = getVector(windSide);
        final EnumTypeWind windLevel = this.wind.getEnumTypeWind();

        entries.parallelStream().forEach(entry -> {
            ChunkLevel chunkLevel = entry.getValue();

            double distance = calculateDistance(chunkLevel.getDefaultPos(), chunkLevel.getPos());

            if (canChange(windLevel) && distance <= 10) {
                pollutionAir.remove(entry.getKey());
                chunkLevel.addChunkPos((int) vector.x, (int) vector.y);
                pollutionAir.put(chunkLevel.getPos(), chunkLevel);
            }
        });
        if (world.provider.getWorldTime() % 6000 == 0) {
            for (Map.Entry<ChunkPos, ChunkLevel> entry : pollutionSoil.entrySet()) {
                ChunkLevel chunkLevel = entry.getValue();
                if (chunkLevel != null) {
                    chunkLevel.setPollution(chunkLevel.getPollution() / 2);
                    if (chunkLevel.getPollution() < 2 && chunkLevel.getLevelPollution() != LevelPollution.VERY_LOW){
                        chunkLevel.setPollution(10);
                        chunkLevel.setLevelPollution(LevelPollution.values()[Math.max(0,
                                chunkLevel.getLevelPollution().ordinal()-1)]);
                    }
                }
            }
        }
        if (world.provider.getWorldTime() % 6000 == 0) {
            for (Map.Entry<ChunkPos, ChunkLevel> entry : pollutionAir.entrySet()) {
                ChunkLevel chunkLevel = entry.getValue();
                if (chunkLevel != null) {
                    chunkLevel.setPollution(chunkLevel.getPollution() / 2);
                    if (chunkLevel.getPollution() < 2 && chunkLevel.getLevelPollution() != LevelPollution.VERY_LOW){
                        chunkLevel.setPollution(10);
                        chunkLevel.setLevelPollution(LevelPollution.values()[Math.max(0,
                                chunkLevel.getLevelPollution().ordinal()-1)]);
                    }
                }
            }
        }
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
