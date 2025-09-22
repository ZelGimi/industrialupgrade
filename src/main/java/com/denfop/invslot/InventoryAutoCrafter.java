package com.denfop.invslot;

import com.denfop.api.inv.VirtualSlot;
import com.denfop.tiles.mechanism.TileEntityAutoCrafter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class InventoryAutoCrafter extends Inventory implements VirtualSlot {

    public InventoryAutoCrafter(TileEntityAutoCrafter base, final TypeItemSlot typeItemSlot, final int count) {
        super(base, typeItemSlot, count);
    }

    @Override
    public boolean accepts(final int index, final ItemStack stack) {
        return isItemValidForSlot(index,stack);
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        ((TileEntityAutoCrafter) this.base).updateCraft();
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

}
