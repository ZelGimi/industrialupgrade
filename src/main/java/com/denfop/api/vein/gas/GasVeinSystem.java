package com.denfop.api.vein.gas;

import com.denfop.config.ModConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.*;

public class GasVeinSystem implements GasVeinBaseSystem {

    public static GasVeinBaseSystem system;
    public static GasVeinBase EMPTY = new GasVeinBase(TypeGas.NONE, new ChunkPos(999999999, 999999999));
    private final Random rand;
    List<GasVeinBase> list;
    List<ChunkPos> chunkPos;
    Map<ChunkPos, GasVeinBase> chunkPosVeinMap;

    public GasVeinSystem() {
        system = this;
        this.list = new LinkedList<>();
        this.chunkPos = new LinkedList<>();
        this.chunkPosVeinMap = new HashMap<>();
        this.rand = new Random();
    }

    public GasVeinBase getEMPTY() {
        return EMPTY;
    }

    public List<ChunkPos> getChunkPos() {
        return chunkPos;
    }

    @Override
    public List<GasVeinBase> getVeinsList() {
        return this.list;
    }

    @Override
    public void addVein(final ChunkAccess chunk) {

        int chance = rand.nextInt(100);
        GasVeinBase vein = new GasVeinBase(TypeGas.NONE, chunk.getPos());
        if (chance < ModConfig.COMMON.gasChance.get()) {
            vein.setType(TypeGas.values()[rand.nextInt(4) + 1]);
            vein.setMaxCol(ModConfig.COMMON.gasMaxVein.get());
            vein.setCol(ModConfig.COMMON.gasMaxVein.get());
        }
        this.list.add(vein);
        this.chunkPos.add(vein.getChunk());
        this.chunkPosVeinMap.put(vein.getChunk(), vein);
    }

    @Override
    public void addVein(final CompoundTag tag) {
        GasVeinBase vein = new GasVeinBase(tag);
        this.list.add(vein);
        this.chunkPos.add(vein.getChunk());
        this.chunkPosVeinMap.put(vein.getChunk(), vein);
    }

    @Override
    public GasVeinBase getVein(final ChunkPos pos) {
        return this.chunkPosVeinMap.getOrDefault(pos, EMPTY);
    }

    @Override
    public void unload() {
        this.list.clear();
        this.chunkPos.clear();
        this.chunkPosVeinMap.clear();
    }


}
