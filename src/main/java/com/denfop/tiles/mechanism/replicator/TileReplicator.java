package com.denfop.tiles.mechanism.replicator;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.mechanism.TileBaseReplicator;

public class TileReplicator extends TileBaseReplicator {

    public TileReplicator() {
        super(1);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.replicator_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
