package com.denfop.componets;

import com.denfop.api.pollution.IPollutionMechanism;
import net.minecraft.world.level.ChunkPos;

public class PollutionMechanism implements IPollutionMechanism {
    public final ChunkPos chunkPos;
    public double pollution = 0;
    public PollutionMechanism(ChunkPos chunkPos){
        this.chunkPos=chunkPos;
    }
    public PollutionMechanism(ChunkPos chunkPos, double pollution){
        this.chunkPos=chunkPos;
        this.pollution = pollution;
    }
    @Override
    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    @Override
    public double getPollution() {
        return pollution;
    }
}
