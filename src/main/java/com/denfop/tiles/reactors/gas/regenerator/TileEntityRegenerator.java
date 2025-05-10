package com.denfop.tiles.reactors.gas.regenerator;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IRegenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityRegenerator extends TileEntityMultiBlockElement implements IRegenerator {

    private final int level;
    private final int max;
    private int helium;

    public TileEntityRegenerator(int level, int max, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block,pos,state);
        this.level = level;
        this.max = max;
    }

    @Override
    public int getBlockLevel() {
        return level;
    }

    @Override
    public int getMaxHelium() {
        return this.max;
    }

    @Override
    public int getHelium() {
        return this.helium;
    }

    @Override
    public void addHelium(final int helium) {
        this.helium += helium;
    }

}
