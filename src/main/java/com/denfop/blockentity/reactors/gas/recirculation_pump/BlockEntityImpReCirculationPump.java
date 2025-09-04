package com.denfop.blockentity.reactors.gas.recirculation_pump;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpReCirculationPump extends BlockEntityBaseReCirculationPump {

    public BlockEntityImpReCirculationPump(BlockPos pos, BlockState state) {
        super(2, BlockGasReactorEntity.imp_gas_recirculation_pump, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.imp_gas_recirculation_pump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }


}
