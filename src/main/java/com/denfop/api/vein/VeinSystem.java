package com.denfop.api.vein;

import com.denfop.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeHills;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VeinSystem implements IVeinSystem {

    public static IVeinSystem system;
    public static Vein EMPTY = new Vein(Type.EMPTY, 0, new ChunkPos(999999999, 999999999));
    private final Random rand;
    List<Vein> list;
    LinkedList<ChunkPos> chunkPos;
    Map<ChunkPos, Vein> chunkPosVeinMap;

    public VeinSystem() {
        system = this;
        this.list = new LinkedList<>();
        this.chunkPos = new LinkedList<>();
        this.chunkPosVeinMap = new HashMap<>();
        this.rand = new Random();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public Vein getEMPTY() {
        return EMPTY;
    }

    public LinkedList<ChunkPos> getChunkPos() {
        return chunkPos;
    }

    @Override
    public List<Vein> getVeinsList() {
        return this.list;
    }

    @Override
    public void addVein(final Chunk chunk) {

        int chance = rand.nextInt(100);
        rand.setSeed(rand.nextLong());
        Vein vein = new Vein(Type.EMPTY, 0, chunk.getPos());
        final Biome biome = chunk.getWorld().getBiome(new BlockPos(chunk.x * 16, 0, chunk.z * 16));
        int col = biome instanceof BiomeHills ? 25 : 0;
        if (chance >= 15 + col) {
            if (rand.nextInt(100) < 85) {
                getnumber(vein, biome);
            } else {
                vein.setType(Type.GAS);
                vein.setOldMineral(false);
                vein.setMeta(0);
                vein.setMaxCol(350000);
                vein.setCol(350000);
            }

        } else {
            int meta = rand.nextInt(30);
            vein.setType(Type.VEIN);
            vein.setOldMineral(meta <= 15);
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
        this.list.add(vein);
        this.chunkPos.add(vein.getChunk());
        this.chunkPosVeinMap.put(vein.getChunk(), vein);
    }

    @Override
    public Vein getVein(final ChunkPos pos) {
        return this.chunkPosVeinMap.getOrDefault(pos, EMPTY);
    }

    @Override
    public void unload() {
        this.list.clear();
        this.chunkPos.clear();
        this.chunkPosVeinMap.clear();
    }

    private void getnumber(Vein vein, final Biome biome) {
        int number;
        int meta = rand.nextInt(6);
        if (Biome.getIdForBiome(biome) == 2) {
            int random = rand.nextInt(100);
            if (random >= 35) {
                number = rand.nextInt(500000) + 40000;
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
                vein.setOldMineral(true);
                vein.setMeta(meta);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else if (Biome.getIdForBiome(biome) == 0) {
            int random;
            random = rand.nextInt(100);
            if (random >= 40) {
                number = rand.nextInt(500000) + 40000;
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
                vein.setOldMineral(true);
                vein.setMeta(meta);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else if (Biome.getIdForBiome(biome) == 24) {
            int random;
            random = rand.nextInt(100);
            if (random > 35) {
                number = rand.nextInt(300000);
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
                vein.setOldMineral(true);
                vein.setMeta(meta);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else if (Biome.getIdForBiome(biome) == 10) {
            int random;
            random = rand.nextInt(100);
            if (random > 60) {
                number = rand.nextInt(300000);
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
                vein.setOldMineral(true);
                vein.setMeta(meta);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else if (Biome.getIdForBiome(biome) == 17) {
            int random;
            random = rand.nextInt(100);
            if (random > 35) {
                number = rand.nextInt(300000) + 20000;
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
                vein.setOldMineral(true);
                vein.setMeta(meta);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else if (Biome.getIdForBiome(biome) == 7) {
            int random;
            random = rand.nextInt(100);
            if (random > 50) {
                number = rand.nextInt(200000);
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
                vein.setOldMineral(true);
                vein.setMeta(meta);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else if (Biome.getIdForBiome(biome) == 35) {
            int random;
            random = rand.nextInt(100);
            if (random > 50) {
                number = rand.nextInt(300000);
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
                vein.setOldMineral(true);
                vein.setMeta(meta);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        } else {
            int random;
            random = rand.nextInt(100);
            if (random > 89) {
                number = rand.nextInt(300000);
                vein.setCol(number);
                vein.setMaxCol(number);
                vein.setType(Type.OIL);
                vein.setOldMineral(true);
                vein.setMeta(meta);
            } else {
                vein.setType(Type.EMPTY);
                vein.setCol(0);
                vein.setMaxCol(0);
            }
        }
    }

}
