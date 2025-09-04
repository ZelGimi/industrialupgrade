package com.denfop.api.vein.common;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.List;

public interface VeinBaseSystem {

    List<VeinBase> getVeinsList();

    void addVein(LevelChunk chunk);

    void addVein(CompoundTag tag);

    VeinBase getVein(ChunkPos pos);

    void unload();

    List<ChunkPos> getChunkPos();

    VeinBase getEMPTY();

}
