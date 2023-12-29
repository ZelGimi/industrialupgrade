package com.denfop.integration.jei.anvil;


import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.world.WorldGenOres;
import com.denfop.world.vein.VeinType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AnvilHandler {

    private static final List<AnvilHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final ItemStack output;



    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public AnvilHandler(ItemStack input,ItemStack output) {
        this.input = input;
        this.output = output;
    }



    public static List<AnvilHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static AnvilHandler addRecipe(
            ItemStack input, ItemStack output
    ) {
        AnvilHandler recipe = new AnvilHandler(input,output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("anvil")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items.get(0)
            );


        }
    }



}
