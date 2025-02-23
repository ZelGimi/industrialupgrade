package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileBaseHandlerHeavyOre;

public class TilePhotonicHandlerHO extends TileBaseHandlerHeavyOre {


    public TilePhotonicHandlerHO() {
        super(EnumTypeStyle.PHOTONIC);
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_handlerho;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine;
    }

}
