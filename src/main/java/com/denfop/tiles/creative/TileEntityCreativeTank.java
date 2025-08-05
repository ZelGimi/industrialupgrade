package com.denfop.tiles.creative;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCreativeBlocks;
import com.denfop.tiles.base.TileEntityLiquedTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class TileEntityCreativeTank extends TileEntityLiquedTank {
    public TileEntityCreativeTank(BlockPos pos, BlockState state) {
        super(2560 * 800, BlockCreativeBlocks.creative_tank_storage, pos, state);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.creativeBlock.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCreativeBlocks.creative_tank_storage;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.fluidTank.getFluid().isEmpty()) {
            this.fluidTank.fill(new FluidStack(this.fluidTank.getFluid().getFluid(), 2560 * 800 * 1000), IFluidHandler.FluidAction.EXECUTE);
        }
    }
}
