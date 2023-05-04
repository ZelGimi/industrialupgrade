package com.denfop.items;

import com.denfop.api.inv.IHasGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IHandHeldInventory {

    IHasGui getInventory(EntityPlayer var1, ItemStack var2);

}
