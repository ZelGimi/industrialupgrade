package com.denfop.items;


import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;

import java.util.function.Supplier;

public class CapabilityFluidHandlerItem extends FluidHandlerItemStack {

    public CapabilityFluidHandlerItem(Supplier<DataComponentType<SimpleFluidContent>> componentType, ItemStack container, int capacity) {
        super(componentType, container, capacity);
    }

    protected void setContainerToEmpty() {
        super.setContainerToEmpty();


    }

}
