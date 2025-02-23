package com.denfop.tiles.lightning_rod;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasWell;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.blocks.mechanism.BlockLightningRod;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityLightningRodCasing extends TileEntityMultiBlockElement implements ICasing {
    public TileEntityLightningRodCasing(){}
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockLightningRod.lightning_rod_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.lightning_rod;
    }

}
