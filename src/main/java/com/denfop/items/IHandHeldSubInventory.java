package com.denfop.items;

import com.denfop.api.inv.IHasGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IHandHeldSubInventory extends IHandHeldInventory {

    IHasGui getSubInventory(EntityPlayer var1, ItemStack var2, int var3);

}
