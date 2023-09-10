package com.denfop.tiles.mechanism.generator.energy.redstone;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;

public class TileRedstoneGenerator extends TileBaseRedstoneGenerator {

    public TileRedstoneGenerator() {
        super(1, 1);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.redstone_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
