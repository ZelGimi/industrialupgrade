package com.denfop.blockentity.mechanism.solardestiller;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpSolarDestiller extends BlockEntityBaseSolarDestiller {

    public BlockEntityImpSolarDestiller(BlockPos pos, BlockState state) {
        super(EnumTypeStyle.IMPROVED, BlockBaseMachine3Entity.imp_solar_destiller, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.imp_solar_destiller;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
