package com.denfop.tiles.reactors.heat.controller;

import com.denfop.IUItem;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntitySimpleController  extends TileEntityMainController {

    public TileEntitySimpleController() {
        super(InitMultiBlockSystem.HeatReactorMultiBlock, EnumHeatReactors.S);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
