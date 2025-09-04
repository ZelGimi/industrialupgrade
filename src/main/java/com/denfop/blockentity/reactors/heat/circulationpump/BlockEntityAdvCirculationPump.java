package com.denfop.blockentity.reactors.heat.circulationpump;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvCirculationPump extends BlockEntityBaseCirculationPump {

    public BlockEntityAdvCirculationPump(BlockPos pos, BlockState state) {
        super(1, BlockHeatReactorEntity.heat_adv_circulationpump, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_adv_circulationpump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
