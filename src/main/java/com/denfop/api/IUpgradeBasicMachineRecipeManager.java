package com.denfop.api;

import ic2.api.recipe.IBasicMachineRecipeManager;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public interface IUpgradeBasicMachineRecipeManager extends IBasicMachineRecipeManager {
  void  removeRecipe(ItemStack input, Collection<ItemStack> output);

}
