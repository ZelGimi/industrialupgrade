package com.denfop.tiles.wiring.storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEnergyStorage;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.wiring.EnumElectricBlock;

public class TileElectricMFE extends TileElectricBlock {

    public TileElectricMFE() {
        super(EnumElectricBlock.MFE);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockEnergyStorage.mfe_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.electricblock;
    }

}
