package com.denfop.tiles.reactors.gas.recirculation_pump;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IInterCooler;
import com.denfop.tiles.reactors.gas.IRecirculationPump;

public class TileEntityAdvReCirculationPump   extends TileEntityBaseReCirculationPump   {
    public TileEntityAdvReCirculationPump() {
        super(1);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.adv_gas_recirculation_pump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
