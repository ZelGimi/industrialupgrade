package com.denfop.tiles.mechanism.quantum_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityImpQuantumStorage extends TileEntityQuantumStorage {

    public TileEntityImpQuantumStorage() {
        super(16000000, EnumTypeStyle.IMPROVED);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.imp_quantum_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
