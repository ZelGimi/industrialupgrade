package com.denfop.api.recipe;


import com.denfop.recipe.IInputItemStack;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IRecipeInputStack {

    List<ItemStack> getItemStack();

    boolean matched(ItemStack stack);

    IInputItemStack getInput();
}
