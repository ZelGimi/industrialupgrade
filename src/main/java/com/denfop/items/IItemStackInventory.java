package com.denfop.items;

import com.denfop.api.inv.IAdvInventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IItemStackInventory {

    IAdvInventory getInventory(Player var1, ItemStack var2);

}
