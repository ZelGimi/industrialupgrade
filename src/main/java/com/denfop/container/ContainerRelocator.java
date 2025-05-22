package com.denfop.container;

import com.denfop.items.relocator.ItemStackRelocator;
import net.minecraft.item.ItemStack;

public class ContainerRelocator extends ContainerHandHeldInventory<ItemStackRelocator> {


    public final ItemStack item;

    public ContainerRelocator(ItemStackRelocator Toolbox1) {
        super(Toolbox1);
        this.item = Toolbox1.itemStack1;
    }


}
