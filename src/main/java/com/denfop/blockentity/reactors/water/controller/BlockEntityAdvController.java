package com.denfop.blockentity.reactors.water.controller;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactorsEntity;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvController extends BlockEntityMainController {

    public BlockEntityAdvController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.AdvWaterReactorMultiBlock, EnumFluidReactors.A, BlockWaterReactorsEntity.water_adv_controller, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockWaterReactorsEntity.water_adv_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component.getBlock(getTeBlock());
    }

}
