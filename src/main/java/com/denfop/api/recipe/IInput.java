package com.denfop.api.recipe;

import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IInput {

   List<IRecipeInput> getInputes();

   boolean matches(ItemStack... stack);

   boolean matches(List<IRecipeInput> stack);
}
