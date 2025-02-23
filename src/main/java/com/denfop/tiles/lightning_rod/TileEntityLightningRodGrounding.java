package com.denfop.tiles.lightning_rod;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasWell;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.blocks.mechanism.BlockLightningRod;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityLightningRodGrounding extends TileEntityMultiBlockElement implements IGrounding {
    public TileEntityLightningRodGrounding(){}
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockLightningRod.lightning_rod_grounding;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.lightning_rod;
    }

}
