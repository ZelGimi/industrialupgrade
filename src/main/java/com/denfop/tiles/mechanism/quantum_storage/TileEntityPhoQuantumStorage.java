package com.denfop.tiles.mechanism.quantum_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityPhoQuantumStorage extends TileEntityQuantumStorage {

    public TileEntityPhoQuantumStorage() {
        super(5120000000D, EnumTypeStyle.PHOTONIC);
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_quantum_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine;
    }

}
