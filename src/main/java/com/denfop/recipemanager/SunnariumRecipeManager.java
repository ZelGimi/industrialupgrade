package com.denfop.recipemanager;

import com.denfop.api.ISunnariumRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SunnariumRecipeManager implements ISunnariumRecipeManager {

    public void addRecipe(IRecipeInput container, IRecipeInput fill, IRecipeInput fill1, IRecipeInput fill2, ItemStack output) {
        if (container == null) {
            throw new NullPointerException("The container recipe input is null");
        }
        if (fill == null) {
            throw new NullPointerException("The fill recipe input is null");
        }
        if (fill1 == null) {
            throw new NullPointerException("The fill recipe input is null");
        }
        if (fill2 == null) {
            throw new NullPointerException("The fill recipe input is null");
        }

        if (output == null) {
            throw new NullPointerException("The recipe output is null");
        }
        if (!StackUtil.check(output)) {
            throw new IllegalArgumentException("The recipe output " + StackUtil.toStringSafe(output) + " is invalid");
        }
        for (ISunnariumRecipeManager.Input input : this.recipes.keySet()) {
            for (ItemStack containerStack : container.getInputs()) {
                for (ItemStack fillStack : fill.getInputs()) {
                    for (ItemStack fillStack1 : fill1.getInputs()) {
                        for (ItemStack fillStack2 : fill2.getInputs()) {
                            if (input.matches(containerStack, fillStack, fillStack1, fillStack2)) {
                                throw new RuntimeException(
                                        "ambiguous recipe: [" + container.getInputs() + "+" + fill.getInputs() + " -> " + output
                                                + "], conflicts with [" + input.container.getInputs() + "+"
                                                + input.fill.getInputs() + "+"
                                                + input.fill2.getInputs() + "+"
                                                + input.fill3.getInputs() + " -> " + this.recipes.get(input) + "]");
                            }
                        }
                    }
                }
            }
        }
        this.recipes.put(
                new ISunnariumRecipeManager.Input(container, fill, fill1, fill2),
                new RecipeOutput(null, output)
        );
    }

    public RecipeOutput getOutputFor(
            ItemStack container,
            ItemStack fill,
            ItemStack fill1,
            ItemStack fill2,
            boolean adjustInput,
            boolean acceptTest
    ) {
        if (acceptTest) {
            if (container == null && fill == null && fill1 == null && fill2 == null) {
                return null;
            }
        } else if (container == null || fill == null || fill1 == null || fill2 == null) {
            return null;
        }
        for (Map.Entry<ISunnariumRecipeManager.Input, RecipeOutput> entry : this.recipes.entrySet()) {
            ISunnariumRecipeManager.Input recipeInput = entry.getKey();
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
            if (acceptTest && fill1 == null) {
                if (recipeInput.container.matches(container)) {
                    return entry.getValue();
                }
                continue;
            }
            if (acceptTest && fill2 == null) {
                if (recipeInput.container.matches(container)) {
                    return entry.getValue();
                }
                continue;
            }
            if (recipeInput.matches(container, fill, fill1, fill2)) {
                if (acceptTest || container.getCount() >= recipeInput.container.getAmount() && fill.getCount() >= recipeInput.fill.getAmount() && fill1 != null && fill1.getCount() >= recipeInput.fill2.getAmount() && fill2 != null && fill2.getCount() >= recipeInput.fill3.getAmount()) {
                    if (adjustInput) {

                        container.setCount(container.getCount() - recipeInput.container.getAmount());

                        fill.setCount(fill.getCount() - recipeInput.fill.getAmount());
                        fill1.setCount(fill1.getCount() - recipeInput.fill2.getAmount());
                        fill2.setCount(fill2.getCount() - recipeInput.fill3.getAmount());
                    }
                    return entry.getValue();
                }
                break;
            }
        }
        return null;
    }

    public Map<ISunnariumRecipeManager.Input, RecipeOutput> getRecipes() {
        return this.recipes;
    }

    private final Map<ISunnariumRecipeManager.Input, RecipeOutput> recipes = new HashMap<>();

}
