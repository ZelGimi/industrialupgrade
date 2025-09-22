package com.denfop.api.gasvein;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

public interface IGasVeinSystem {

    List<GasVein> getVeinsList();

    void addVein(Chunk chunk);

    void addVein(NBTTagCompound tag);

    GasVein getVein(ChunkPos pos);

    void unload();

    List<ChunkPos> getChunkPos();

    GasVein getEMPTY();

}
