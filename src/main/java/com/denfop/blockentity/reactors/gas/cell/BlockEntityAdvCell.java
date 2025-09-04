package com.denfop.blockentity.reactors.gas.cell;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.reactors.gas.ICell;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvCell extends BlockEntityMainTank implements ICell {

    public BlockEntityAdvCell(BlockPos pos, BlockState state) {
        super(60000, BlockGasReactorEntity.adv_gas_cell, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.adv_gas_cell;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 1;
    }

}
