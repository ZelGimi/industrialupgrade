package com.denfop.api.gasvein;

import com.denfop.ModConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

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
    public void addVein(final ChunkAccess chunk) {

        int chance = rand.nextInt(100);
        GasVein vein = new GasVein(TypeGas.NONE, chunk.getPos());
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
