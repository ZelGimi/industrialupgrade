package com.denfop.tiles.reactors.heat.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.register.InitMultiBlockSystem;

public class TileEntityImpController extends TileEntityMainController {

    public TileEntityImpController() {
        super(InitMultiBlockSystem.impHeatReactorMultiBlock, EnumHeatReactors.I);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_imp_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
