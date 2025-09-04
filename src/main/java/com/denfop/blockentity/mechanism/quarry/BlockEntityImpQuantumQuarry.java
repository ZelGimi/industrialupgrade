package com.denfop.blockentity.mechanism.quarry;


import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachineEntity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpQuantumQuarry extends BlockEntityBaseQuantumQuarry {

    public BlockEntityImpQuantumQuarry(BlockPos pos, BlockState blockState) {
        super(2, BlockBaseMachineEntity.imp_quantum_quarry, pos, blockState);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachineEntity.imp_quantum_quarry;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock().getId());
    }
}
