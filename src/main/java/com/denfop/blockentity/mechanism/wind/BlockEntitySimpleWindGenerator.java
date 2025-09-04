package com.denfop.blockentity.mechanism.wind;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleWindGenerator extends BlockEntityWindGenerator {


    public BlockEntitySimpleWindGenerator(BlockPos pos, BlockState state) {
        super(EnumLevelGenerators.ONE, BlockBaseMachine3Entity.simple_wind_generator, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.simple_wind_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
