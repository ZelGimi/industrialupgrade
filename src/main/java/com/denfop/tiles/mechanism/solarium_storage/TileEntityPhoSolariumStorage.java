package com.denfop.tiles.mechanism.solarium_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityPhoSolariumStorage extends TileEntitySolariumStorage {

    public TileEntityPhoSolariumStorage() {
        super(128000000, EnumTypeStyle.PHOTONIC);
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_solarium_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine;
    }

}
