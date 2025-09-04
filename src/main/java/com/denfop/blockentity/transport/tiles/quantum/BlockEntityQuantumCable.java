package com.denfop.blockentity.transport.tiles.quantum;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityQCable;
import com.denfop.blockentity.transport.types.QEType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockQuantumCableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityQuantumCable extends BlockEntityQCable {
    public BlockEntityQuantumCable(BlockPos pos, BlockState state) {
        super(QEType.qcable, BlockQuantumCableEntity.quantum, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockQuantumCableEntity.quantum;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.qcable.getBlock(getTeBlock().getId());
    }
}
