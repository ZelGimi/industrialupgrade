package com.denfop.blockentity.gaswell;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasWellEntity;
import com.denfop.componets.Energy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGasWellEnergy extends BlockEntityMultiBlockElement implements ISocket {

    Energy energy;

    public BlockEntityGasWellEnergy(BlockPos pos, BlockState state) {
        super(BlockGasWellEntity.gas_well_socket, pos, state);
        energy = this.addComponent(Energy.asBasicSink(this, 4000, 14));
    }


    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasWellEntity.gas_well_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_well.getBlock(getTeBlock());
    }

    @Override
    public Energy getEnergy() {
        return energy;
    }

}
