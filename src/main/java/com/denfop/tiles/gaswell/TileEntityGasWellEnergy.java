package com.denfop.tiles.gaswell;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasWell;
import com.denfop.componets.Energy;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityGasWellEnergy extends TileEntityMultiBlockElement implements ISocket {

    Energy energy;

    public TileEntityGasWellEnergy() {
        energy = this.addComponent(Energy.asBasicSink(this, 4000, 14));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasWell.gas_well_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_well;
    }

    @Override
    public Energy getEnergy() {
        return energy;
    }

}
