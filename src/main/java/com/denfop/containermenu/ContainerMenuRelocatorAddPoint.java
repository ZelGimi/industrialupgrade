package com.denfop.containermenu;

import com.denfop.items.relocator.ItemStackRelocator;
import net.minecraft.world.item.ItemStack;

public class ContainerMenuRelocatorAddPoint extends ContainerMenuHandHeldInventory<ItemStackRelocator> {


    private final ItemStack item;

    public ContainerMenuRelocatorAddPoint(ItemStackRelocator Toolbox1) {
        super(Toolbox1, Toolbox1.player);
        this.item = Toolbox1.itemStack1;
    }


}
