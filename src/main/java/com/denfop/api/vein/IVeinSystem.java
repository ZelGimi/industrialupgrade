package com.denfop.api.vein;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.List;

public interface IVeinSystem {

    List<Vein> getVeinsList();

    void addVein(LevelChunk chunk);

    void addVein(CompoundTag tag);

    Vein getVein(ChunkPos pos);

    void unload();

    List<ChunkPos> getChunkPos();

    Vein getEMPTY();

}
