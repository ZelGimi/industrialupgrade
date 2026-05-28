package com.denfop.blockentity.reactors.gas.regenerator;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpRegenerator extends BlockEntityRegenerator {

    public BlockEntityImpRegenerator(BlockPos pos, BlockState state) {
        super(2, ModConfig.mechanismInt("advanced_gas_cooling_reactor_regenerator_capacity", 1000), BlockGasReactorEntity.imp_gas_regenerator, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.imp_gas_regenerator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }


}
