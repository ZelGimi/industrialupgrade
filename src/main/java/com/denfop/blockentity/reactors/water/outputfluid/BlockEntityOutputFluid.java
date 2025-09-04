package com.denfop.blockentity.reactors.water.outputfluid;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.water.IOutput;
import com.denfop.blockentity.reactors.water.inputfluid.FluidHandlerReactor;
import com.denfop.componets.Fluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityOutputFluid extends BlockEntityMultiBlockElement implements IOutput {

    public List<Fluids> internalFluidTankList = new ArrayList<>();

    public BlockEntityOutputFluid(MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
    }

    @Override
    public void addFluids(final Fluids fluids) {
        internalFluidTankList.add(fluids);
    }

    @Override
    public void clearList() {
        internalFluidTankList.clear();
    }

    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.FluidHandler.BLOCK) {
            return (T) new FluidHandlerReactor(this.internalFluidTankList);
        }
        return super.getCapability(cap, side);
    }


}
