package com.denfop.api.vein;

import com.denfop.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VeinSystem implements IVeinSystem {

    public static IVeinSystem system;
    private final Random rand;
    List<Vein> list;
    List<ChunkPos> chunkPos;
    Map<ChunkPos, Vein> chunkPosVeinMap;

    public VeinSystem() {
        system = this;
        this.list = new ArrayList<>();
        this.chunkPos = new ArrayList<>();
        this.chunkPosVeinMap = new HashMap<>();
        this.rand = new Random();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void load(ChunkEvent.Load event) {
        if (event.getChunk().getWorld().provider.getDimension() == 0) {
            if (event.getWorld().isRemote) {
                return;
            }
            ChunkPos pos1 = event.getChunk().getPos();
            if (!this.chunkPos.contains(pos1)) {
                this.addVein(event.getChunk());
            }

        }

    }

    @Override
    public List<Vein> getVeinsList() {
        return this.list;
    }

    @Override
    public void addVein(final Chunk chunk) {
        Random rand = new Random();
        int chance = rand.nextInt(100);
        Vein vein = new Vein(Type.EMPTY, 0, chunk.getPos());
        if (chance >= 15) {
            getnumber(chunk, vein);

        } else {
            int meta = rand.nextInt(16);
            vein.setType(Type.VEIN);
            vein.setMeta(meta);
            vein.setMaxCol(Config.maxVein);
            vein.setCol(Config.maxVein);
        }
        this.list.add(vein);
        this.chunkPos.add(vein.getChunk());
        this.chunkPosVeinMap.put(vein.getChunk(), vein);
    }

    @Override
    public void addVein(final NBTTagCompound tag) {
        Vein vein = new Vein(tag);
        if (!this.list.contains(vein)) {
            this.list.add(vein);
            this.chunkPos.add(vein.getChunk());
            this.chunkPosVeinMap.put(vein.getChunk(), vein);
        }
    }

    @Override
    public Vein getVein(final ChunkPos pos) {
        return this.chunkPosVeinMap.get(pos);
    }

    @Override
    public void unload() {
        this.list.clear();
        this.chunkPos.clear();
        this.chunkPosVeinMap.clear();
    }

    private void getnumber(Chunk chunk, IVein vein) {
        final Biome biome = chunk.getWorld().getBiome(new BlockPos(chunk.x * 16, 0, chunk.z * 16));
        int number;
        if (Biome.getIdForBiome(biome) == 2) {
            int random = rand.nextInt(100);
            if (random > 45) {
                number = rand.nextInt(150000) + 40000;
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else if (Biome.getIdForBiome(biome) == 0) {
            int random;
            random = rand.nextInt(100);
            if (random > 70) {
                number = rand.nextInt(150000) + 40000;
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else if (Biome.getIdForBiome(biome) == 24) {
            int random;
            random = rand.nextInt(100);
            if (random > 65) {
                number = rand.nextInt(120000);
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else if (Biome.getIdForBiome(biome) == 10) {
            int random;
            random = rand.nextInt(100);
            if (random > 70) {
                number = rand.nextInt(120000);
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else if (Biome.getIdForBiome(biome) == 17) {
            int random;
            random = rand.nextInt(100);
            if (random > 65) {
                number = rand.nextInt(100000) + 20000;
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else if (Biome.getIdForBiome(biome) == 7) {
            int random;
            random = rand.nextInt(100);
            if (random > 60) {
                number = rand.nextInt(60000);
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else if (Biome.getIdForBiome(biome) == 35) {
            int random;
            random = rand.nextInt(100);
            if (random > 60) {
                number = rand.nextInt(80000);
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else {
            int random;
            random = rand.nextInt(100);
            if (random > 30) {
                number = rand.nextInt(60000);
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        }
    }

}
