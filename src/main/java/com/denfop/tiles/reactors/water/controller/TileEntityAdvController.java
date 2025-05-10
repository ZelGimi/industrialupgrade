package com.denfop.tiles.reactors.water.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAdvController extends TileEntityMainController {

    public TileEntityAdvController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.AdvWaterReactorMultiBlock, EnumFluidReactors.A,BlockWaterReactors.water_adv_controller,pos,state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_adv_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component.getBlock(getTeBlock());
    }

}
