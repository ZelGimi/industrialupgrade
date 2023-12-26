package com.denfop.tiles.reactors.heat.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityAdvController  extends TileEntityMainController {
    public TileEntityAdvController() {
        super(InitMultiBlockSystem.advHeatReactorMultiBlock, EnumHeatReactors.A);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_adv_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
