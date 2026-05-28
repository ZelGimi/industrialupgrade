package com.denfop.blockentity.mechanism.quantum_storage;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleQuantumStorage extends BlockEntityQuantumStorage {

    public BlockEntitySimpleQuantumStorage(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismDouble("background_radiation_is_low_energy_storage", 400000.0D), EnumTypeStyle.DEFAULT, BlockBaseMachine3Entity.quantum_storage, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.quantum_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
