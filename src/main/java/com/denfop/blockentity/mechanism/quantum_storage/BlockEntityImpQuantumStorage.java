package com.denfop.blockentity.mechanism.quantum_storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpQuantumStorage extends BlockEntityQuantumStorage {

    public BlockEntityImpQuantumStorage(BlockPos pos, BlockState state) {
        super(16000000, EnumTypeStyle.IMPROVED, BlockBaseMachine3Entity.imp_quantum_storage, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.imp_quantum_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
