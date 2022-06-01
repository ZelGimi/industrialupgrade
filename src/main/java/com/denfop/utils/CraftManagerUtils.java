package com.denfop.utils;

import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.ArrayList;

//TODO: Author: will0376
public class CraftManagerUtils {

    public static IRecipe getRecipe(ItemStack removed) {
        try {
            ArrayList<IRecipe> recipes = Lists.newArrayList(ForgeRegistries.RECIPES.getValuesCollection());
            for (IRecipe recipe : recipes) {
                if (recipe.getRecipeOutput().getItem() != Items.AIR
                        && !removed.isEmpty()
                        && removed.getItem() == recipe.getRecipeOutput().getItem()
                        && recipe.getRecipeOutput().getMetadata() == removed.getMetadata()) {
                    return recipe;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeCrafting(IRecipe recipe) {
        try {
            ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
            recipeRegistry.unfreeze();
            recipeRegistry.remove(recipe.getRegistryName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
