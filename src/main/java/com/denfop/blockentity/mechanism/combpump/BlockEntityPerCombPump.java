package com.denfop.blockentity.mechanism.combpump;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerCombPump extends BlockEntityCombinedPump {

    public BlockEntityPerCombPump(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismInt("perfect_combined_pump_radius_or_size", 320), ModConfig.mechanismInt("perfect_combined_pump_operation_length", 10), EnumTypePump.P, BlockBaseMachine3Entity.per_comb_pump, pos, state);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.per_comb_pump;
    }

}
