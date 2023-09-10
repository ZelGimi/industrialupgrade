package com.denfop.tiles.mechanism.solarium_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityImpSolariumStorage extends TileEntitySolariumStorage {

    public TileEntityImpSolariumStorage() {
        super(1600000, EnumTypeStyle.IMPROVED);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.imp_solarium_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
