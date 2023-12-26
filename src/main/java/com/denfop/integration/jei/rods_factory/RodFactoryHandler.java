package com.denfop.integration.jei.rods_factory;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RodFactoryHandler {

    private static final List<RodFactoryHandler> recipes = new ArrayList<>();
    private final ItemStack  output;
    private final List<ItemStack> inputs;

    public RodFactoryHandler(
            List<ItemStack> inputs,
            ItemStack output
    ) {
        this.inputs = inputs;
        this.output = output;
    }

    public static List<RodFactoryHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RodFactoryHandler addRecipe(
            List<ItemStack> inputs,
            ItemStack output
    ) {
        RodFactoryHandler recipe = new RodFactoryHandler(inputs, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static RodFactoryHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (RodFactoryHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("reactor_simple_rod")) {
            List<ItemStack> stacks = new ArrayList<>();
            container.input.getInputs().forEach(iInputItemStack -> stacks.add(iInputItemStack.getInputs().get(0)));
            addRecipe(stacks,
                    container.getOutput().items.get(0)
            );
        }
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("reactor_dual_rod")) {
            List<ItemStack> stacks = new ArrayList<>();
            container.input.getInputs().forEach(iInputItemStack -> stacks.add(iInputItemStack.getInputs().get(0)));
            addRecipe(stacks,
                    container.getOutput().items.get(0)
            );
        }
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("reactor_quad_rod")) {
            List<ItemStack> stacks = new ArrayList<>();
            container.input.getInputs().forEach(iInputItemStack -> stacks.add(iInputItemStack.getInputs().get(0)));
            addRecipe(stacks,
                    container.getOutput().items.get(0)
            );
        }
    }


    public List<ItemStack> getInputs() {
        return inputs;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        for(ItemStack input : this.getInputs()){
            if(input.isItemEqual(is))
                return true;
        }
        return false;
    }

}
