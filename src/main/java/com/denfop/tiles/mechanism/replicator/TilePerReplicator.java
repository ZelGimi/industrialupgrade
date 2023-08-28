package com.denfop.tiles.mechanism.replicator;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.TileBaseReplicator;

public class TilePerReplicator extends TileBaseReplicator {

    public TilePerReplicator() {
        super(0.8);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.per_replicator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
