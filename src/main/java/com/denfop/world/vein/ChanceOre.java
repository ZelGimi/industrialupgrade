package com.denfop.world.vein;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class ChanceOre {

    private final IBlockState block;
    private final int chance;
    private final int meta;
    private final IBlockState withoutRadiation;

    public ChanceOre(IBlockState block, int chance, int meta) {
        this.block = block;
        this.chance = chance;
        this.meta = meta;
        this.withoutRadiation = block;
    }

    public ChanceOre(IBlockState block, int chance, int meta, IBlockState withoutRadiation) {
        this.block = block;
        this.chance = chance;
        this.meta = meta;
        this.withoutRadiation = withoutRadiation;
    }

    public IBlockState getWithoutRadiation() {
        return withoutRadiation;
    }

    public IBlockState getBlock() {
        return block;
    }

    public boolean needGenerate(World world) {
        return world.rand.nextInt(100) <= this.chance;
    }

    public int getMeta() {
        return meta;
    }

    public int getChance() {
        return chance;
    }

}
