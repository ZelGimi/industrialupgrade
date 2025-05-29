package com.denfop.world.vein;


import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class ChanceOre {

    private final BlockState block;
    private final int chance;
    private final int meta;

    public ChanceOre(BlockState block, int chance, int meta) {
        this.block = block;
        this.chance = chance;
        this.meta = meta;
    }

    public BlockState getBlock() {
        return block;
    }

    public boolean needGenerate(WorldGenLevel world) {
        return world.getRandom().nextInt(100) <= this.chance;
    }

    public int getMeta() {
        return meta;
    }

    public int getChance() {
        return chance;
    }

}
