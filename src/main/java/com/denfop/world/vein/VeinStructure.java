package com.denfop.world.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;

public class VeinStructure {
    private final WorldGenLevel level;
    private final VeinType veinType;
    private final BlockPos blockPos;
    private final int depositsMeta;
    private final ChunkAccess chunk;

    public VeinStructure(WorldGenLevel level, VeinType veinType, BlockPos blockPos, ChunkAccess chunk, int depositsMeta) {
        this.level = level;
        this.veinType = veinType;
        this.blockPos = blockPos;
        this.chunk = chunk;
        this.depositsMeta = depositsMeta;
    }

    public VeinType getVeinType() {
        return veinType;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public ChunkAccess getChunk() {
        return chunk;
    }

    public int getDepositsMeta() {
        return depositsMeta;
    }

    public WorldGenLevel getLevel() {
        return level;
    }

}
