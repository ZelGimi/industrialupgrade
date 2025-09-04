package com.denfop.componets;

import net.minecraft.world.level.ChunkPos;

public class PollutionMechanism implements com.denfop.api.pollution.PollutionMechanism {
    public final ChunkPos chunkPos;
    public double pollution = 0;

    public PollutionMechanism(ChunkPos chunkPos) {
        this.chunkPos = chunkPos;
    }

    public PollutionMechanism(ChunkPos chunkPos, double pollution) {
        this.chunkPos = chunkPos;
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
