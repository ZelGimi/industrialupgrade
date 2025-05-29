package com.denfop.api.energy;


import net.minecraft.core.BlockPos;

public class Cable {

    private final double limit;
    private final BlockPos pos;

    public Cable(BlockPos pos, double limit) {
        this.limit = limit;
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public double getLimit() {
        return limit;
    }

}
