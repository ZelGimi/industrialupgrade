package com.denfop.tiles.reactors.heat.coolant;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.heat.ICoolant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityBaseCoolant extends TileEntityMultiBlockElement implements ICoolant {

    private final int levelBlock;
    private final int max;
    private int helium;

    public TileEntityBaseCoolant(int levelBlock, int max, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block,pos,state);
        this.levelBlock = levelBlock;
        this.max = max;
    }

    @Override
    public int getBlockLevel() {
        return levelBlock;
    }


    @Override
    public void addHeliumToRegenerate(final double col) {
        this.helium += col;
    }

    @Override
    public double getMaxRegenerate() {
        return max;
    }

    @Override
    public double getHeliumToRegenerate() {
        return helium;
    }

}
