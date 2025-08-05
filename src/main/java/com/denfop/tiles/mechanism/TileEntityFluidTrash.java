package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Fluids;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class TileEntityFluidTrash extends TileEntityInventory {

    private final Fluids.InternalFluidTank tank;

    public TileEntityFluidTrash(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.fluid_trash, pos, state);
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
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.fluid_trash;
    }


}
