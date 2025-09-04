package com.denfop.blockentity.creative;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.quantum_storage.BlockEntityQuantumStorage;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCreativeBlocksEntity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCreativeQuantumStorage extends BlockEntityQuantumStorage {
    public BlockEntityCreativeQuantumStorage(BlockPos pos, BlockState state) {
        super(Math.pow(10, 24), EnumTypeStyle.PERFECT, BlockCreativeBlocksEntity.creative_quantum_storage, pos, state);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.qe.buffer.storage = this.qe.buffer.capacity;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.creativeBlock.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCreativeBlocksEntity.creative_quantum_storage;
    }
}
