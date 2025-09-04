package com.denfop.blockentity.reactors.gas.chamber;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.gas.IChamber;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleChamber extends BlockEntityMultiBlockElement implements IChamber {

    public BlockEntitySimpleChamber(BlockPos pos, BlockState state) {
        super(BlockGasReactorEntity.gas_chamber, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.gas_chamber;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 0;
    }

}
