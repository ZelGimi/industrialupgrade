package com.denfop.api;

import com.denfop.api.recipe.IRecipes;
import com.denfop.api.recipe.RecipesCore;
import com.denfop.recipe.CraftingManager;
import com.denfop.recipe.IInputHandler;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Recipes {


    public static IInputHandler inputFactory;
    public static com.denfop.recipe.CraftingManager recipe;
    public static IRecipes recipes;
    static Map<String, Recipe> recipeMap = new ConcurrentHashMap<>();
    private static int recipeID = 0;

    public static void registerWithSorter() {

    }

    public static void registerRecipe(RecipeOutput consumer, RecipeBuilder recipe) {
        recipe.save(consumer, "industrialupgrade:" + "industrialupgrade_" + recipeID++);
    }

    public static void registerRecipe(RecipeOutput consumer, RecipeBuilder recipe, String id) {
        try {
            recipe.save(consumer, id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(recipe);
        }
        ;
    }

    static void loadRecipes() {
        Recipes.recipe = new com.denfop.recipe.CraftingManager();
    }

    public static void registerRecipes() {
        inputFactory = new InputHandler();
        recipes = new RecipesCore();
        loadRecipes();
    }

    public static CraftingManager getRecipe() {
        return recipe;
    }

    public static Map<String, Recipe> getRecipeMap() {
        return recipeMap;
    }

    public static String registerRecipe(Recipe Recipe) {
        String id = "industrialupgrade:" + "industrialupgrade_" + recipeID++;
        recipeMap.put(id, Recipe);
        return id;
    }
}
