package com.denfop.world.vein;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class ChanceOre {

    private final IBlockState block;
    private final int chance;
    private final int meta;

    public ChanceOre(IBlockState block, int chance, int meta){
        this.block = block;
        this.chance = chance;
        this.meta = meta;
    }

    public IBlockState getBlock() {
        return block;
    }
    public boolean needGenerate(World world){
        return world.rand.nextInt(100) <= this.chance;
    }

    public int getMeta() {
        return meta;
    }

    public int getChance() {
        return chance;
    }

}
