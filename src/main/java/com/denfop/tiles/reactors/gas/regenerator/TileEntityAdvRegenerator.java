package com.denfop.tiles.reactors.gas.regenerator;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IRecirculationPump;
import com.denfop.tiles.reactors.gas.IRegenerator;

public class TileEntityAdvRegenerator   extends TileEntityRegenerator   {
    public TileEntityAdvRegenerator() {
        super(1, 7500);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.adv_gas_regenerator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
