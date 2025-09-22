package com.denfop.tiles.reactors.water.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.register.InitMultiBlockSystem;

public class TileEntityAdvController extends TileEntityMainController {

    public TileEntityAdvController() {
        super(InitMultiBlockSystem.AdvWaterReactorMultiBlock, EnumFluidReactors.A);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_adv_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }

}
