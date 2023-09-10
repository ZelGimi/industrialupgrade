package com.denfop.tiles.mechanism.replicator;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.TileBaseReplicator;

public class TileAdvReplicator extends TileBaseReplicator {

    public TileAdvReplicator() {
        super(0.95);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.adv_replicator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
