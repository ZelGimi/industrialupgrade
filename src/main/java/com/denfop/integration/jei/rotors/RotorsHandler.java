package com.denfop.integration.jei.rotors;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RotorsHandler implements IJeiVariantRecipe {

    private static final List<RotorsHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
private final ItemStack output;
    private final ItemStack[] input;

    public RotorsHandler(ItemStack output, ItemStack... inputs) {
        this.input = inputs;
        this.output = output;
    }

    public static List<RotorsHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RotorsHandler addRecipe(ItemStack output, ItemStack... inputs) {
        RotorsHandler recipe = new RotorsHandler(output, inputs);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("rotor_assembler")) {
            JeiIngredientHelper.attachInputVariants(addRecipe(
                    container.getOutput().items.get(0),
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
                    container.input.getInputs().get(4).getInputs().get(0)
            ), container);


        }
    }


    public ItemStack[] getInputs() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }




    @Override
    public void setInputVariants(final List<List<ItemStack>> inputVariants) {
        this.inputVariants = inputVariants == null ? new ArrayList<>() : inputVariants;
    }

    @Override
    public List<ItemStack> getInputVariants(final int slot, final ItemStack fallback) {
        return JeiIngredientHelper.getInputVariants(this.inputVariants, slot, fallback);
    }
}
