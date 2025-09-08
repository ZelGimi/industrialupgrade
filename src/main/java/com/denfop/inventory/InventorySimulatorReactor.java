package com.denfop.inventory;

import com.denfop.api.menu.VirtualSlot;
import com.denfop.blockentity.base.BlockEntitySimulatorReactor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class InventorySimulatorReactor extends Inventory implements VirtualSlot {

    public InventorySimulatorReactor(BlockEntitySimulatorReactor base, final TypeItemSlot typeItemSlot, final int count) {
        super(base, typeItemSlot, count);
    }


    @Override
    public boolean isFluid() {
        return false;
    }

    @Override
    public List<FluidStack> getFluidStackList() {
        return Collections.emptyList();
    }

    @Override
    public void setFluidList(final List<FluidStack> fluidStackList) {

    }

    @Override
    public boolean canPlaceVirtualItem(int index, ItemStack stack) {
        return this.canPlaceItem(index,stack);
    }

}
