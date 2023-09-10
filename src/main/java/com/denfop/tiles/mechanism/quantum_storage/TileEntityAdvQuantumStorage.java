package com.denfop.tiles.mechanism.quantum_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityAdvQuantumStorage extends TileEntityQuantumStorage {

    public TileEntityAdvQuantumStorage() {
        super(1600000, EnumTypeStyle.ADVANCED);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.adv_quantum_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
