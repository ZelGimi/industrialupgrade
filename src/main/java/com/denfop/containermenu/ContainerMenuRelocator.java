package com.denfop.containermenu;

import com.denfop.items.relocator.ItemStackRelocator;
import net.minecraft.world.item.ItemStack;

public class ContainerMenuRelocator extends ContainerMenuHandHeldInventory<ItemStackRelocator> {


    public final ItemStack item;

    public ContainerMenuRelocator(ItemStackRelocator Toolbox1) {
        super(Toolbox1, Toolbox1.player);
        this.item = Toolbox1.itemStack1;
    }


}
