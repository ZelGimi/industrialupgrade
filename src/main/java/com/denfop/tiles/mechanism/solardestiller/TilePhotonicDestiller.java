package com.denfop.tiles.mechanism.solardestiller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;

public class TilePhotonicDestiller extends TileEntityBaseSolarDestiller {

    public TilePhotonicDestiller() {
        super(EnumTypeStyle.PHOTONIC);
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_destiller;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine;
    }

}
