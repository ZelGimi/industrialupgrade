package com.denfop.datagen;

import com.denfop.api.Recipes;
import com.denfop.api.crafting.BaseRecipe;
import com.denfop.api.crafting.BaseShapelessRecipe;
import com.denfop.api.crafting.PartRecipe;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.IngredientInput;
import com.denfop.recipes.BaseRecipes;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

import java.util.ArrayList;
import java.util.List;
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
                        if (!recipeInput.getInputs().isEmpty() &&recipeInput.getInputs().get(0).hasTag()){
                            has = true;
                            if (recipeInput.getInputs().size() == 1){
                                shaped.define(character, StrictNBTIngredient.of(recipeInput.getInputs().get(0)));
                            }else {
                                List<Item> items = new ArrayList<>();
                                recipeInput.getInputs().forEach(stack -> items.add(stack.getItem()));
                                shaped.define(character, PartialNBTIngredient.of(recipeInput.getInputs().get(0).getTag(),items.toArray(new Item[0])));
                            }
                        }else {
                            shaped.define(character, new IngredientInput(recipeInput));
                        }
                    }
                    shaped.unlockedBy("any", new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, new ItemPredicate[]{ItemPredicate.Builder.item().of(Blocks.COBBLESTONE).build()}));
                    Recipes.registerRecipe(consumer, shaped, id.toLowerCase());
                } else if (recipe instanceof BaseShapelessRecipe baseShapelessRecipe) {
                    ShapelessRecipeBuilder shaped = ShapelessRecipeBuilder.shapeless(baseShapelessRecipe.getOutput().getItem(), baseShapelessRecipe.getOutput().getCount());
                    for (IInputItemStack recipeInput : baseShapelessRecipe.getRecipeInputList())
                        if (!recipeInput.getInputs().isEmpty() &&recipeInput.getInputs().get(0).hasTag()){
                            if (recipeInput.getInputs().size() == 1){
                                shaped.requires(StrictNBTIngredient.of(recipeInput.getInputs().get(0)));
                            }else {
                                List<Item> items = new ArrayList<>();
                                recipeInput.getInputs().forEach(stack -> items.add(stack.getItem()));
                                shaped.requires(PartialNBTIngredient.of(recipeInput.getInputs().get(0).getTag(),items.toArray(new Item[0])));
                            }
                        }else {
                            shaped.requires(new IngredientInput(recipeInput));
                        }

                    shaped.unlockedBy("any", new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, new ItemPredicate[]{ItemPredicate.Builder.item().of(Blocks.COBBLESTONE).build()}));
                    Recipes.registerRecipe(consumer, shaped, id.toLowerCase());
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
