package com.denfop.blockentity.mechanism.water;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleWaterGenerator extends BlockEntityBaseWaterGenerator {


    public BlockEntitySimpleWaterGenerator(BlockPos pos, BlockState state) {
        super(EnumLevelGenerators.ONE, BlockBaseMachine3Entity.simple_water_generator, pos, state);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.simple_water_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
