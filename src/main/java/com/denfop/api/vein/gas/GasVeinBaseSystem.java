package com.denfop.api.vein.gas;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.List;

public interface GasVeinBaseSystem {

    List<GasVeinBase> getVeinsList();

    void addVein(ChunkAccess chunk);

    void addVein(CompoundTag tag);

    GasVeinBase getVein(ChunkPos pos);

    void unload();

    List<ChunkPos> getChunkPos();

    GasVeinBase getEMPTY();

}
