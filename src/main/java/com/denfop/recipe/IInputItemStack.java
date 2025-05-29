package com.denfop.recipe;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IInputItemStack {

    boolean matches(ItemStack var1);

    int getAmount();

    List<ItemStack> getInputs();

    boolean hasTag();

    TagKey<Item> getTag();

    void toNetwork(FriendlyByteBuf buffer);

    void growAmount(int count);
}
