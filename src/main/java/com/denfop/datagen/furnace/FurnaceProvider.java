package com.denfop.datagen.furnace;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FurnaceProvider extends VanillaRecipeProvider {
    public static List<FurnaceRecipe> furnaceRecipeList = new LinkedList<>();

    public FurnaceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> p_323814_) {
        super(output, p_323814_);
    }

    public static void addSmelting(@Nonnull ItemStack input, @Nonnull ItemStack output, float xp) {
        furnaceRecipeList.add(new FurnaceRecipe(input, output, xp));
    }

    @Override
    protected void buildRecipes(RecipeOutput p_301191_) {
        super.buildRecipes(p_301191_);
    }
}
