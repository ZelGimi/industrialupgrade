package com.denfop.tiles.mechanism.quarry;


import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;

public class TileQuantumQuarry extends TileBaseQuantumQuarry {

    public TileQuantumQuarry() {
        super(4);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.quantum_quarry;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

}
