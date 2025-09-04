package com.denfop.blockentity.mechanism.water;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpWaterGenerator extends BlockEntityBaseWaterGenerator {


    public BlockEntityImpWaterGenerator(BlockPos pos, BlockState state) {
        super(EnumLevelGenerators.THREE, BlockBaseMachine3Entity.imp_water_generator, pos, state);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.imp_water_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
