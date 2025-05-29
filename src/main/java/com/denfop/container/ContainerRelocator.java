package com.denfop.container;

import com.denfop.items.relocator.ItemStackRelocator;
import net.minecraft.world.item.ItemStack;

public class ContainerRelocator extends ContainerHandHeldInventory<ItemStackRelocator> {


    public final ItemStack item;

    public ContainerRelocator(ItemStackRelocator Toolbox1) {
        super(Toolbox1, Toolbox1.player);
        this.item = Toolbox1.itemStack1;
    }


}
