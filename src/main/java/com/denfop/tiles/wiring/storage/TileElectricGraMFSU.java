package com.denfop.tiles.wiring.storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEnergyStorage;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.wiring.EnumElectricBlock;

public class TileElectricGraMFSU extends TileElectricBlock {

    public TileElectricGraMFSU() {
        super(EnumElectricBlock.GRA_MFSU);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockEnergyStorage.gra_mfsu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.electricblock;
    }

}
