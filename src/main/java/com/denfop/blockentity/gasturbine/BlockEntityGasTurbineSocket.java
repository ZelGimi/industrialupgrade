package com.denfop.blockentity.gasturbine;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasTurbineEntity;
import com.denfop.componets.Energy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGasTurbineSocket extends BlockEntityMultiBlockElement implements ISocket {

    private final Energy energy;

    public BlockEntityGasTurbineSocket(BlockPos pos, BlockState state) {
        super(BlockGasTurbineEntity.gas_turbine_socket, pos, state);
        this.energy = this.addComponent(Energy.asBasicSource(this, 2000000, 14));
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasTurbineEntity.gas_turbine_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gasTurbine.getBlock(getTeBlock());
    }

    @Override
    public Energy getEnergy() {
        return energy;
    }

}
