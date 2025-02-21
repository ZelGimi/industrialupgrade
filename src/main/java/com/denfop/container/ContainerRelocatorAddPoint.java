package com.denfop.container;

import com.denfop.items.energy.ItemStackUpgradeItem;
import com.denfop.items.relocator.ItemStackRelocator;
import net.minecraft.item.ItemStack;

public class ContainerRelocatorAddPoint extends ContainerHandHeldInventory<ItemStackRelocator> {


    private final ItemStack item;

    public ContainerRelocatorAddPoint(ItemStackRelocator Toolbox1) {
        super(Toolbox1);
        this.item = Toolbox1.itemStack1;
    }


}
