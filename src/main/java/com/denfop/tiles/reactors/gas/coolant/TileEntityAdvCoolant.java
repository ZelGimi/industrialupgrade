package com.denfop.tiles.reactors.gas.coolant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.ICompressor;
import com.denfop.tiles.reactors.gas.ICoolant;

public class TileEntityAdvCoolant   extends TileEntityCoolant   {
    public TileEntityAdvCoolant() {
        super(1);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.adv_gas_coolant;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
