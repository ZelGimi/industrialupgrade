package com.denfop.tiles.reactors.water.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.reactors.water.ISecurity;
import com.denfop.tiles.reactors.water.ISocket;

public class TileEntityAdvSocket  extends TileEntityMainSocket implements ISocket {
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_adv_socket;
    }
    public TileEntityAdvSocket() {
        super(15000);
    }
    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }
    @Override
    public int getLevel() {
        return 1;
    }
}
