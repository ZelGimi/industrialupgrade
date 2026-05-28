package com.denfop.blockentity.mechanism.combpump;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpCombPump extends BlockEntityCombinedPump {

    public BlockEntityImpCombPump(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismInt("advanced_combined_pump_radius_or_size", 160), ModConfig.mechanismInt("advanced_combined_pump_operation_length", 20), EnumTypePump.I, BlockBaseMachine3Entity.imp_comb_pump, pos, state);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.imp_comb_pump;
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

}
