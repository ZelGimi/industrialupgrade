package com.denfop.items.storage;


import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record ItemGridTooltipComponent(List<Entry> entries) implements TooltipComponent {

    public sealed interface Entry permits EntryItem, EntryFluid {
        long count();
    }

    public record EntryItem(ItemStack stack, long count) implements Entry {
    }

    public record EntryFluid(FluidStack stack, long count) implements Entry {
    }
}
