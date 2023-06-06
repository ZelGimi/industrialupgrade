package com.denfop.api.vein;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

public interface IVeinSystem {

    List<Vein> getVeinsList();

    void addVein(Chunk chunk);

    void addVein(NBTTagCompound tag);

    Vein getVein(ChunkPos pos);

    void unload();

    List<ChunkPos> getChunkPos();

    Vein getEMPTY();

}
