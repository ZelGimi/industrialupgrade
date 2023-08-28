package com.denfop.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class CapabilityFluidHandlerItem extends FluidHandlerItemStack {

    public CapabilityFluidHandlerItem(ItemStack container, int capacity) {
        super(container, capacity);
    }

    protected void setContainerToEmpty() {
        super.setContainerToEmpty();
        if (this.container.getTagCompound().hasNoTags()) {
            this.container.setTagCompound(null);
        }

    }

}
