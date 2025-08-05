package com.denfop.datagen;

import com.denfop.api.Recipes;
import com.denfop.api.crafting.BaseRecipe;
import com.denfop.api.crafting.BaseShapelessRecipe;
import com.denfop.datagen.furnace.FurnaceRecipe;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputOreDict;
import com.denfop.recipes.BaseRecipes;
import com.denfop.recipes.FurnaceRecipes;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.denfop.datagen.furnace.FurnaceProvider.furnaceRecipeList;

public class RecipeProvider extends VanillaRecipeProvider {


    public static int ID = 0;

    public RecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput p_301191_) {
        BaseRecipes.init();
        Map<String, Recipe> map = Recipes.getRecipeMap();

        for (Map.Entry<String, Recipe> entry : map.entrySet()) {
            String id = entry.getKey();
            Recipe recipe = entry.getValue();
            try {

                if (recipe instanceof BaseRecipe baseRecipe) {
                    ShapedRecipeBuilder shaped = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, baseRecipe.getOutput().getItem(), baseRecipe.getOutput().getCount());
                    baseRecipe.getRecipeGrid().getGrids().get(0).forEach(shaped::pattern);
                    shaped.add(baseRecipe);

                    shaped.unlockedBy("any", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AIR));
                    Recipes.registerRecipe(p_301191_, shaped, id.toLowerCase());
                } else if (recipe instanceof BaseShapelessRecipe baseShapelessRecipe) {
                    ShapelessRecipeBuilder shaped = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, baseShapelessRecipe.getOutput().getItem(), baseShapelessRecipe.getOutput().getCount());
                    shaped.add(baseShapelessRecipe);
                    for (IInputItemStack recipeInput : baseShapelessRecipe.getRecipeInputList())
                        if (recipeInput.getInputs().size() == 1 && !recipeInput.getInputs().get(0).getComponents().isEmpty()) {
                            HolderSet<Item> holders = HolderSet.direct(recipeInput.getInputs().get(0).getItemHolder());
                            shaped.requires(new DataComponentIngredient(holders, DataComponentPredicate.allOf(recipeInput.getInputs().get(0).getComponents()), false).toVanilla());
                        } else {
                            if (recipeInput instanceof InputOreDict)
                                shaped.requires(Ingredient.of(recipeInput.getTag()));
                            else
                                shaped.requires(Ingredient.of(recipeInput.getInputs().toArray(new ItemStack[0])));
                        }
                    shaped.unlockedBy("any", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AIR));
                    Recipes.registerRecipe(p_301191_, shaped, id.toLowerCase());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        FurnaceRecipes.recipe();
        furnaceRecipeList = new ArrayList<>(furnaceRecipeList);
        for (FurnaceRecipe furnaceRecipe : furnaceRecipeList)

            SimpleCookingRecipeBuilder.smelting(Ingredient.of(furnaceRecipe.getInput()), RecipeCategory.MISC, furnaceRecipe.getOutput().getItem(), furnaceRecipe.getXp(), 200).unlockedBy("any", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AIR)).save(p_301191_, "industrialupgrade:" + "furnace_" + ID++);

    }

}
