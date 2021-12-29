package com.denfop.recipemanager;

import com.denfop.api.IGenStoneRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GenStoneRecipeManager implements IGenStoneRecipeManager {

    public void addRecipe(IRecipeInput container, IRecipeInput fill, ItemStack output) {
        if (container == null) {
            throw new NullPointerException("The container recipe input is null");
        }
        if (fill == null) {
            throw new NullPointerException("The fill recipe input is null");
        }
        if (output == null) {
            throw new NullPointerException("The recipe output is null");
        }
        if (!StackUtil.check(output)) {
            throw new IllegalArgumentException("The recipe output " + StackUtil.toStringSafe(output) + " is invalid");
        }
        for (IGenStoneRecipeManager.Input input : this.recipes.keySet()) {
            for (ItemStack containerStack : container.getInputs()) {
                for (ItemStack fillStack : fill.getInputs()) {
                    if (input.matches(containerStack, fillStack)) {
                        throw new RuntimeException(
                                "ambiguous recipe: [" + container.getInputs() + "+" + fill.getInputs() + " -> " + output
                                        + "], conflicts with [" + input.container.getInputs() + "+"
                                        + input.fill.getInputs() + " -> " + this.recipes.get(input) + "]");
                    }
                }
            }
        }
        this.recipes.put(
                new IGenStoneRecipeManager.Input(container, fill),
                new RecipeOutput(null, output)
        );
    }

    public RecipeOutput getOutputFor(ItemStack container, ItemStack fill, boolean adjustInput, boolean acceptTest) {
        if (acceptTest) {
            if (container == null && fill == null) {
                return null;
            }
        } else if (container == null || fill == null) {
            return null;
        }
        for (Map.Entry<IGenStoneRecipeManager.Input, RecipeOutput> entry : this.recipes.entrySet()) {
            IGenStoneRecipeManager.Input recipeInput = entry.getKey();
            if (acceptTest && container == null) {
                if (recipeInput.fill.matches(fill)) {
                    return entry.getValue();
                }
                continue;
            }
            if (acceptTest && fill == null) {
                if (recipeInput.container.matches(container)) {
                    return entry.getValue();
                }
                continue;
            }
            if (recipeInput.matches(container, fill)) {
                if (acceptTest || container.getCount() >= recipeInput.container.getAmount() && fill.getCount() >= recipeInput.fill.getAmount()) {
                    if (adjustInput) {


                        container.setCount(container.getCount() - recipeInput.container.getAmount());
                        fill.setCount(fill.getCount() - recipeInput.fill.getAmount());
                    }
                    return entry.getValue();
                }
                break;
            }
        }
        return null;
    }

    public Map<IGenStoneRecipeManager.Input, RecipeOutput> getRecipes() {
        return this.recipes;
    }

    private final Map<IGenStoneRecipeManager.Input, RecipeOutput> recipes = new HashMap<>();

}
