package com.denfop.blockentity.mechanism.quantum_storage;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvQuantumStorage extends BlockEntityQuantumStorage {

    public BlockEntityAdvQuantumStorage(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismDouble("advanced_energy_storage", 1600000.0D), EnumTypeStyle.ADVANCED, BlockBaseMachine3Entity.adv_quantum_storage, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.adv_quantum_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
