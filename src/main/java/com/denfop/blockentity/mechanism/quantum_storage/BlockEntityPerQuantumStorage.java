package com.denfop.blockentity.mechanism.quantum_storage;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerQuantumStorage extends BlockEntityQuantumStorage {

    public BlockEntityPerQuantumStorage(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismDouble("perfect_energy_storage", 160000000.0D), EnumTypeStyle.PERFECT, BlockBaseMachine3Entity.per_quantum_storage, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.per_quantum_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
