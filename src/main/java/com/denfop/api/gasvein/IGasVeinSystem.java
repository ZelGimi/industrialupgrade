package com.denfop.api.gasvein;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.List;

public interface IGasVeinSystem {

    List<GasVein> getVeinsList();

    void addVein(ChunkAccess chunk);

    void addVein(CompoundTag tag);

    GasVein getVein(ChunkPos pos);

    void unload();

    List<ChunkPos> getChunkPos();

    GasVein getEMPTY();

}
