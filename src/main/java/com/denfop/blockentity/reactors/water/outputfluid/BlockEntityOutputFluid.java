package com.denfop.blockentity.reactors.water.outputfluid;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.water.IOutput;
import com.denfop.blockentity.reactors.water.inputfluid.FluidHandlerReactor;
import com.denfop.componets.Fluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
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
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return LazyOptional.of(() -> (T) new FluidHandlerReactor(this.internalFluidTankList)).cast();
        return super.getCapability(cap, facing);
    }


}
