package com.denfop.api.pollution;

import net.minecraft.util.math.ChunkPos;

public interface IPollutionMechanism {

    ChunkPos getChunkPos();

    double getPollution();

}
