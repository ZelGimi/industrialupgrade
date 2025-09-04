package com.denfop.api.vein.gas;

import com.denfop.config.ModConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

public class GasVeinSystem implements GasVeinBaseSystem {

    public static GasVeinBaseSystem system;
    public static GasBaseVein EMPTY = new GasBaseVein(TypeGas.NONE, new ChunkPos(999999999, 999999999));
    private final Random rand;
    List<GasBaseVein> list;
    List<ChunkPos> chunkPos;
    Map<ChunkPos, GasBaseVein> chunkPosVeinMap;

    public GasVeinSystem() {
        system = this;
        this.list = new LinkedList<>();
        this.chunkPos = new LinkedList<>();
        this.chunkPosVeinMap = new HashMap<>();
        this.rand = new Random();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public GasBaseVein getEMPTY() {
        return EMPTY;
    }

    public List<ChunkPos> getChunkPos() {
        return chunkPos;
    }

    @Override
    public List<GasBaseVein> getVeinsList() {
        return this.list;
    }

    @Override
    public void addVein(final ChunkAccess chunk) {

        int chance = rand.nextInt(100);
        GasBaseVein vein = new GasBaseVein(TypeGas.NONE, chunk.getPos());
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
        GasBaseVein vein = new GasBaseVein(tag);
        this.list.add(vein);
        this.chunkPos.add(vein.getChunk());
        this.chunkPosVeinMap.put(vein.getChunk(), vein);
    }

    @Override
    public GasBaseVein getVein(final ChunkPos pos) {
        return this.chunkPosVeinMap.getOrDefault(pos, EMPTY);
    }

    @Override
    public void unload() {
        this.list.clear();
        this.chunkPos.clear();
        this.chunkPosVeinMap.clear();
    }


}
