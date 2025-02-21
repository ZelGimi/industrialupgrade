package com.denfop.api.gasvein;

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

public class GasVeinSystem implements IGasVeinSystem {

    public static IGasVeinSystem system;
    public static GasVein EMPTY = new GasVein(TypeGas.NONE, new ChunkPos(999999999, 999999999));
    private final Random rand;
    List<GasVein> list;
    List<ChunkPos> chunkPos;
    Map<ChunkPos, GasVein> chunkPosVeinMap;

    public GasVeinSystem() {
        system = this;
        this.list = new LinkedList<>();
        this.chunkPos = new LinkedList<>();
        this.chunkPosVeinMap = new HashMap<>();
        this.rand = new Random();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public GasVein getEMPTY() {
        return EMPTY;
    }

    public List<ChunkPos> getChunkPos() {
        return chunkPos;
    }

    @Override
    public List<GasVein> getVeinsList() {
        return this.list;
    }

    @Override
    public void addVein(final Chunk chunk) {

        int chance = rand.nextInt(100);
        rand.setSeed(rand.nextLong());
        GasVein vein = new GasVein(TypeGas.NONE, chunk.getPos());
        if (chance < 16) {
            vein.setType(TypeGas.values()[rand.nextInt(4)+1]);
            vein.setMaxCol(200000);
            vein.setCol(200000);
        }
        this.list.add(vein);
        this.chunkPos.add(vein.getChunk());
        this.chunkPosVeinMap.put(vein.getChunk(), vein);
    }

    @Override
    public void addVein(final NBTTagCompound tag) {
        GasVein vein = new GasVein(tag);
        this.list.add(vein);
        this.chunkPos.add(vein.getChunk());
        this.chunkPosVeinMap.put(vein.getChunk(), vein);
    }

    @Override
    public GasVein getVein(final ChunkPos pos) {
        return this.chunkPosVeinMap.getOrDefault(pos, EMPTY);
    }

    @Override
    public void unload() {
        this.list.clear();
        this.chunkPos.clear();
        this.chunkPosVeinMap.clear();
    }


}
