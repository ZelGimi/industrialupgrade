package com.denfop.blockentity.mechanism.wind;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvWindGenerator extends BlockEntityWindGenerator {


    public BlockEntityAdvWindGenerator(BlockPos pos, BlockState state) {
        super(EnumLevelGenerators.TWO, BlockBaseMachine3Entity.adv_wind_generator, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.adv_wind_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
