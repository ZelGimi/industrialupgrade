package com.denfop.blockentity.reactors.water.controller;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactorsEntity;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpController extends BlockEntityMainController {

    public BlockEntityImpController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.ImpWaterReactorMultiBlock, EnumFluidReactors.I, BlockWaterReactorsEntity.water_imp_controller, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockWaterReactorsEntity.water_imp_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component.getBlock(getTeBlock());
    }

}
