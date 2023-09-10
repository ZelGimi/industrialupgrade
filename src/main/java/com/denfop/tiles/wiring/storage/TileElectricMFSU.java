package com.denfop.tiles.wiring.storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEnergyStorage;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.wiring.EnumElectricBlock;

public class TileElectricMFSU extends TileElectricBlock {

    public TileElectricMFSU() {
        super(EnumElectricBlock.MFSU);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockEnergyStorage.mfsu_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.electricblock;
    }

}
