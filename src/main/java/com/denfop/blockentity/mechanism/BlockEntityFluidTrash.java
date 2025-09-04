package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.Fluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockEntityFluidTrash extends BlockEntityInventory {

    private final Fluids.InternalFluidTank tank;

    public BlockEntityFluidTrash(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.fluid_trash, pos, state);
        final Fluids fluids = this.addComponent(new Fluids(this));
        tank = fluids.addTankInsert("tank", Integer.MAX_VALUE);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.tank.getFluidAmount() > 0) {
            this.tank.drain(this.tank.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.fluid_trash;
    }


}
