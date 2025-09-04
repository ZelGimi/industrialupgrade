package com.denfop.api.pollution;


import net.minecraft.world.level.ChunkPos;

public interface PollutionMechanism {

    ChunkPos getChunkPos();

    double getPollution();

}
