package com.denfop.items;


import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class CapabilityFluidHandlerItem extends FluidHandlerItemStack {

    public CapabilityFluidHandlerItem(ItemStack container, int capacity) {
        super(container, capacity);
    }

    protected void setContainerToEmpty() {
        super.setContainerToEmpty();
        if (this.container.getOrCreateTag().isEmpty()) {
            this.container.setTag(null);
        }

    }

}
