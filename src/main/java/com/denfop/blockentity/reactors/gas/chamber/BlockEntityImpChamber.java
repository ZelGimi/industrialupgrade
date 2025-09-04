package com.denfop.blockentity.reactors.gas.chamber;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.gas.IChamber;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpChamber extends BlockEntityMultiBlockElement implements IChamber {

    public BlockEntityImpChamber(BlockPos pos, BlockState state) {
        super(BlockGasReactorEntity.imp_gas_chamber, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.imp_gas_chamber;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 2;
    }

}
