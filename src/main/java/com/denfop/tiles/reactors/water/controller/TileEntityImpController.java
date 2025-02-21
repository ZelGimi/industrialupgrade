package com.denfop.tiles.reactors.water.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.register.InitMultiBlockSystem;

public class TileEntityImpController extends TileEntityMainController {

    public TileEntityImpController() {
        super(InitMultiBlockSystem.ImpWaterReactorMultiBlock, EnumFluidReactors.I);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_imp_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }

}
