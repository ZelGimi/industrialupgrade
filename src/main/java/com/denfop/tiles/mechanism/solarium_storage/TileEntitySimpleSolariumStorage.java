package com.denfop.tiles.mechanism.solarium_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileEntitySimpleSolariumStorage extends TileEntitySolariumStorage {

    public TileEntitySimpleSolariumStorage() {
        super(100000, EnumTypeStyle.DEFAULT);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.solarium_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
