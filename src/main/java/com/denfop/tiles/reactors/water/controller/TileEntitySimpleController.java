package com.denfop.tiles.reactors.water.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.register.InitMultiBlockSystem;

public class TileEntitySimpleController extends TileEntityMainController {

    public TileEntitySimpleController() {
        super(InitMultiBlockSystem.WaterReactorMultiBlock, EnumFluidReactors.S);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }

}
