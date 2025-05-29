package com.denfop.datagen.furnace;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class FurnaceProvider extends VanillaRecipeProvider {
    public static List<FurnaceRecipe> furnaceRecipeList = new LinkedList<>();

    public FurnaceProvider(PackOutput output) {
        super(output);
    }

    public static void addSmelting(@Nonnull ItemStack input, @Nonnull ItemStack output, float xp) {
        furnaceRecipeList.add(new FurnaceRecipe(input, output, xp));
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

    }
}
