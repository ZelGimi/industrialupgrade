package com.denfop.blockentity.mechanism.quarry;


import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityQuantumQuarry extends BlockEntityBaseQuantumQuarry {

    public BlockEntityQuantumQuarry(BlockPos pos, BlockState state) {
        super(4, BlockBaseMachineEntity.quantum_quarry, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachineEntity.quantum_quarry;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock().getId());
    }
}
