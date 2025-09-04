package com.denfop.items;

import com.denfop.api.container.CustomWorldContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IItemStackInventory {

    CustomWorldContainer getInventory(Player var1, ItemStack var2);

}
