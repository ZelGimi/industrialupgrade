package com.denfop.datagen.furnace;

import com.denfop.recipes.FurnaceRecipes;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class FurnaceProvider extends net.minecraft.data.recipes.RecipeProvider {
    public static int ID = 0;
    public static List<FurnaceRecipe> furnaceRecipeList = new LinkedList<>();

    public FurnaceProvider(DataGenerator generator) {
        super(generator);
    }

    public static void addSmelting(@Nonnull ItemStack input, @Nonnull ItemStack output, float xp) {
        furnaceRecipeList.add(new FurnaceRecipe(input, output, xp));
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        FurnaceRecipes.recipe();
        furnaceRecipeList = new ArrayList<>(furnaceRecipeList);
        for (FurnaceRecipe furnaceRecipe : furnaceRecipeList)

            SimpleCookingRecipeBuilder.smelting(Ingredient.of(furnaceRecipe.getInput()), furnaceRecipe.getOutput().getItem(), furnaceRecipe.getXp(), 200).unlockedBy("any", new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, new ItemPredicate[]{ItemPredicate.Builder.item().of(Blocks.COBBLESTONE).build()})).save(consumer, "industrialupgrade:" + "furnace_" + ID++);

    }
}
