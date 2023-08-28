package com.denfop.tiles.mechanism.quantum_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileEntitySimpleQuantumStorage extends TileEntityQuantumStorage {

    public TileEntitySimpleQuantumStorage() {
        super(400000, EnumTypeStyle.DEFAULT);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.quantum_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
