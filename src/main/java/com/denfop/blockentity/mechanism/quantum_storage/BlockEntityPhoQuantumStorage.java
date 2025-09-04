package com.denfop.blockentity.mechanism.quantum_storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhoQuantumStorage extends BlockEntityQuantumStorage {

    public BlockEntityPhoQuantumStorage(BlockPos pos, BlockState state) {
        super(5120000000D, EnumTypeStyle.PHOTONIC, BlocksPhotonicMachine.photonic_quantum_storage, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_quantum_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock());
    }

}
