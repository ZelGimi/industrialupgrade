package com.denfop.datagen;

import com.denfop.api.Recipes;
import com.denfop.api.crafting.BaseRecipe;
import com.denfop.api.crafting.BaseShapelessRecipe;
import com.denfop.api.crafting.PartRecipe;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.IngredientInput;
import com.denfop.recipes.BaseRecipes;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;
import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        BaseRecipes.init();
        Map<String, Recipe> map = Recipes.getRecipeMap();
        for (Map.Entry<String, Recipe> entry : map.entrySet()) {
            try {
                String id = entry.getKey();
                Recipe recipe = entry.getValue();
                if (recipe instanceof BaseRecipe baseRecipe) {
                    ShapedRecipeBuilder shaped = ShapedRecipeBuilder.shaped(baseRecipe.getOutput().getItem(), baseRecipe.getOutput().getCount());
                    baseRecipe.getRecipeGrid().getGrids().get(0).forEach(shaped::pattern);
                    boolean has = false;
                    for (PartRecipe partRecipe : baseRecipe.getPartRecipe()) {
                        Character character = partRecipe.getIndex().charAt(0);
                        IInputItemStack recipeInput = partRecipe.getInput();
                        shaped.define(character, new IngredientInput(recipeInput).getInput());
                    }
                    shaped.unlockedBy("any", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AIR));
                    Recipes.registerRecipe(consumer, shaped, id.toLowerCase());
                } else if (recipe instanceof BaseShapelessRecipe baseShapelessRecipe) {
                    ShapelessRecipeBuilder shaped = ShapelessRecipeBuilder.shapeless(baseShapelessRecipe.getOutput().getItem(), baseShapelessRecipe.getOutput().getCount());
                    for (IInputItemStack recipeInput : baseShapelessRecipe.getRecipeInputList())
                        shaped.requires(new IngredientInput(recipeInput).getInput());

                    shaped.unlockedBy("any", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AIR));
                    Recipes.registerRecipe(consumer, shaped, id.toLowerCase());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
