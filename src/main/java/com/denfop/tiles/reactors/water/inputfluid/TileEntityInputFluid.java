package com.denfop.tiles.reactors.water.inputfluid;

import com.denfop.componets.Fluids;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.IInput;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TileEntityInputFluid extends TileEntityMultiBlockElement implements IInput {

    public List<Fluids> internalFluidTankList = new ArrayList<>();

    public TileEntityInputFluid() {
    }

    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            for (Fluids fluids : internalFluidTankList) {
                if (ModUtils.interactWithFluidHandler(player, hand,
                        fluids.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
                )) {
                    return true;
                }
            }
            return false;
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
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

    @Override
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        } else {
            return super.hasCapability(capability, facing);
        }
    }

    @Override
    public void addFluids(final Fluids fluids) {
        internalFluidTankList.add(fluids);
    }

    @Override
    public void clearList() {
        internalFluidTankList.clear();
    }


}
