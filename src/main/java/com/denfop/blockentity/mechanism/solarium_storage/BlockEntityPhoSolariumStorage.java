package com.denfop.blockentity.mechanism.solarium_storage;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhoSolariumStorage extends BlockEntitySolariumStorage {

    public BlockEntityPhoSolariumStorage(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismDouble("photonic_quantum_storage_energy_storage", 128000000.0D), EnumTypeStyle.PHOTONIC, BlocksPhotonicMachine.photonic_solarium_storage, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_solarium_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

}
