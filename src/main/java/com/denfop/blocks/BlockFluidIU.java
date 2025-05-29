package com.denfop.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class BlockFluidIU extends LiquidBlock implements IFluidBlock {

    public BlockFluidIU(java.util.function.Supplier<? extends FlowingFluid> p_54694, Properties p_54695_) {
        super(p_54694, p_54695_);
    }



    @Override
    public int place(Level level, BlockPos pos, @NotNull FluidStack fluidStack, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public @NotNull FluidStack drain(Level level, BlockPos pos, IFluidHandler.FluidAction action) {
        return FluidStack.EMPTY;
    }

    @Override
    public boolean canDrain(Level level, BlockPos pos) {
        BlockState p_153774_ = level.getBlockState(pos);
        return p_153774_.getValue(LEVEL) == 0;
    }

    @Override
    public float getFilledPercentage(Level level, BlockPos pos) {
        return 0;
    }
}
