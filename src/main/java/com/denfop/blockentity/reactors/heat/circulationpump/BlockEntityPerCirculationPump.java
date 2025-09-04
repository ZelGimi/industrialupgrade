package com.denfop.blockentity.reactors.heat.circulationpump;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerCirculationPump extends BlockEntityBaseCirculationPump {

    public BlockEntityPerCirculationPump(BlockPos pos, BlockState state) {
        super(3, BlockHeatReactorEntity.heat_per_circulationpump, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_per_circulationpump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
