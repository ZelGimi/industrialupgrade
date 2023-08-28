package com.denfop.api;

import com.denfop.api.crafting.BaseRecipe;
import com.denfop.api.crafting.BaseShapelessRecipe;
import com.denfop.api.recipe.IRecipes;
import com.denfop.recipe.CraftingManager;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.InputHandler;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.RecipeSorter;

public class Recipes {

    public static IRecipes recipes;

    public static IInputHandler inputFactory;
    public static CraftingManager recipe;
    private static int recipeID = 0;

    public static void registerWithSorter() {
        RecipeSorter.Category shaped = RecipeSorter.Category.SHAPED;
        RecipeSorter.Category shapeless = RecipeSorter.Category.SHAPELESS;
        RecipeSorter.register("iu:shaped", BaseRecipe.class, shaped, "after:minecraft:shapeless");
        RecipeSorter.register("iu:shapeless", BaseShapelessRecipe.class, shapeless, "after:iu:shaped");

    }

    public static void registerRecipe(IRecipe recipe) {
        registerRecipe(new ResourceLocation("industrialupgrade", "" + recipeID++), recipe);
    }

    public static void registerRecipe(ResourceLocation rl, IRecipe recipe) {
        recipe.setRegistryName(rl);
        ForgeRegistries.RECIPES.register(recipe);
    }

    static void loadRecipes() {
        Recipes.recipe = new CraftingManager();
    }

    public static void registerRecipes() {
        inputFactory = new InputHandler();
        loadRecipes();
    }

}
