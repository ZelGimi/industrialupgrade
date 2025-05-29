package com.denfop.api.pollution;


import net.minecraft.world.level.ChunkPos;

public interface IPollutionMechanism {

    ChunkPos getChunkPos();

    double getPollution();

}
