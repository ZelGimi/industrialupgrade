package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;

public class TilePhotonicReplicator extends TileBaseReplicator {


    public TilePhotonicReplicator() {
        super(0.7);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PHOTONIC;
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_replicator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine;
    }

}
