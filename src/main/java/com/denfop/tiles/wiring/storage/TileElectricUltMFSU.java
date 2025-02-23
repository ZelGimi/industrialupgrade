package com.denfop.tiles.wiring.storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEnergyStorage;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.wiring.EnumElectricBlock;

public class TileElectricUltMFSU extends TileElectricBlock {

    public TileElectricUltMFSU() {
        super(EnumElectricBlock.ULT_MFSU);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockEnergyStorage.ult_mfsu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.electricblock;
    }

}
