package com.denfop.tiles.reactors.water.outputfluid;

import com.denfop.componets.Fluids;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.IOutput;
import com.denfop.tiles.reactors.water.inputfluid.FluidHandlerReactor;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TileEntityOutputFluid extends TileEntityMultiBlockElement implements IOutput {

    public List<Fluids> internalFluidTankList = new ArrayList<>();

    public TileEntityOutputFluid() {
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
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        } else {
            return super.hasCapability(capability, facing);
        }
    }

    @Override
    public <T> T getCapability(@NotNull final Capability<T> capability, final EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) new FluidHandlerReactor(this.internalFluidTankList);
        } else {
            return super.getCapability(capability, facing);
        }

    }

}
