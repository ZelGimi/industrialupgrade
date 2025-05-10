package com.denfop.container;

import com.denfop.items.relocator.ItemStackRelocator;
import net.minecraft.world.item.ItemStack;

public class ContainerRelocatorAddPoint extends ContainerHandHeldInventory<ItemStackRelocator> {


    private final ItemStack item;

    public ContainerRelocatorAddPoint(ItemStackRelocator Toolbox1) {
        super(Toolbox1, Toolbox1.player);
        this.item = Toolbox1.itemStack1;
    }


}
