package com.denfop.tiles.mechanism.quantum_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityPerQuantumStorage extends TileEntityQuantumStorage {

    public TileEntityPerQuantumStorage() {
        super(160000000, EnumTypeStyle.PERFECT);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.per_quantum_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
