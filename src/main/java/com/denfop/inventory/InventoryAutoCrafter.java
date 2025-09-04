package com.denfop.inventory;

import com.denfop.api.menu.VirtualSlot;
import com.denfop.blockentity.mechanism.BlockEntityAutoCrafter;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class InventoryAutoCrafter extends Inventory implements VirtualSlot {

    public InventoryAutoCrafter(BlockEntityAutoCrafter base, final TypeItemSlot typeItemSlot, final int count) {
        super(base, typeItemSlot, count);
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        ((BlockEntityAutoCrafter) this.base).updateCraft();
        return content;
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
