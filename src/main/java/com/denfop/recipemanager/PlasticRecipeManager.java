package com.denfop.recipemanager;

import com.denfop.api.IPlasticRecipemanager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

public class PlasticRecipeManager implements IPlasticRecipemanager {

    private final Map<IPlasticRecipemanager.Input, RecipeOutput> recipes = new HashMap<>();

    @Override
    public void addRecipe(IRecipeInput container, IRecipeInput fill, FluidStack fluidStack, ItemStack output) {
        if (container == null) {
            throw new NullPointerException("The container recipe input is null");
        }
        if (fill == null) {
            throw new NullPointerException("The fill recipe input is null");
        }
        if (output == null) {
            throw new NullPointerException("The recipe output is null");
        }
        if (fluidStack == null) {
            throw new NullPointerException("The fluidStack is null");
        }

        if (!StackUtil.check(output)) {
            throw new IllegalArgumentException("The recipe output " + StackUtil.toStringSafe(output) + " is invalid");
        }
        for (IPlasticRecipemanager.Input input : this.recipes.keySet()) {
            for (ItemStack containerStack : container.getInputs()) {
                for (ItemStack fillStack : fill.getInputs()) {
                    if (input.matches(containerStack, fillStack, fluidStack)) {
                        throw new RuntimeException(
                                "ambiguous recipe: [" + container.getInputs() + "+" + fill.getInputs() + " -> " + output
                                        + "], conflicts with [" + input.container.getInputs() + "+"
                                        + input.fill.getInputs() + " -> " + this.recipes.get(input) + "]");
                    }
                }
            }
        }
        this.recipes.put(
                new IPlasticRecipemanager.Input(container, fill, fluidStack),
                new RecipeOutput(null, output)
        );
    }

    @Override
    public RecipeOutput getOutputFor(
            ItemStack container,
            ItemStack fill,
            FluidStack fluidStack,
            boolean adjustInput,
            boolean acceptTest
    ) {
        if (acceptTest) {
            if (container == null && fill == null || fluidStack == null) {
                return null;
            }
        } else if (container == null || fill == null || fluidStack == null) {
            return null;
        }


        for (Map.Entry<IPlasticRecipemanager.Input, RecipeOutput> entry : this.recipes.entrySet()) {
            IPlasticRecipemanager.Input recipeInput = entry.getKey();
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
            if (recipeInput.matches(container, fill, fluidStack)) {
                if (acceptTest || container.getCount() >= recipeInput.container.getAmount() && fill.getCount() >= recipeInput.fill.getAmount() && fluidStack.amount >= recipeInput.fluidStack.amount) {
                    if (adjustInput) {

                        container.setCount(container.getCount() - recipeInput.container.getAmount());
                        fluidStack.amount -= recipeInput.fluidStack.amount;
                        fill.setCount(fill.getCount() - recipeInput.fill.getAmount());
                    }
                    return entry.getValue();
                }
                else if (container.getCount() >= recipeInput.fill.getAmount() && fill.getCount() >= recipeInput.container.getAmount() && fluidStack.amount >= recipeInput.fluidStack.amount) {
                    if (adjustInput) {

                        container.setCount(container.getCount() - recipeInput.fill.getAmount());
                        fluidStack.amount -= recipeInput.fluidStack.amount;
                        fill.setCount(fill.getCount() - recipeInput.container.getAmount());
                    }
                    return entry.getValue();
                }
                break;
            }
        }
        return null;
    }

    @Override
    public Map<IPlasticRecipemanager.Input, RecipeOutput> getRecipes() {
        return this.recipes;
    }

}
