package com.denfop.items;

import com.denfop.api.inv.IAdvInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IItemStackInventory {

    IAdvInventory getInventory(EntityPlayer var1, ItemStack var2);

}
