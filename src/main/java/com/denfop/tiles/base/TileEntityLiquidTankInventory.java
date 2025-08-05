package com.denfop.tiles.base;

import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.invslot.InvSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public abstract class TileEntityLiquidTankInventory extends TileEntityInventory {

    public final FluidTank fluidTank;

    public TileEntityLiquidTankInventory(int tanksize, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank = fluids.addTank("fluidTank", tanksize * 1000, InvSlot.TypeItemSlot.INPUT);

    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (this.hasComponent(Energy.class)) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }

        super.addInformation(stack, tooltip);
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public FluidTank getFluidTank() {
        return this.fluidTank;
    }

    public int getTankAmount() {
        return this.getFluidTank().getFluidAmount();
    }

    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }

    public boolean needsFluid() {
        return this.getFluidTank().getFluidAmount() <= this.getFluidTank().getCapacity();
    }


}
