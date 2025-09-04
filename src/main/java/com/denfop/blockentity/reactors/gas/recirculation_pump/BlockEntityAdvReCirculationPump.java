package com.denfop.blockentity.reactors.gas.recirculation_pump;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvReCirculationPump extends BlockEntityBaseReCirculationPump {

    public BlockEntityAdvReCirculationPump(BlockPos pos, BlockState state) {
        super(1, BlockGasReactorEntity.adv_gas_recirculation_pump, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.adv_gas_recirculation_pump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }


}
