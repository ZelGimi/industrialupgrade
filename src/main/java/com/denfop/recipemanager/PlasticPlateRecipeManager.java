package com.denfop.recipemanager;

import com.denfop.api.IPlasticPlateRecipemanager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

public class PlasticPlateRecipeManager implements IPlasticPlateRecipemanager {

    private final Map<IPlasticPlateRecipemanager.Input, RecipeOutput> recipes = new HashMap<>();

    @Override
    public void addRecipe(IRecipeInput container, FluidStack fluidStack, ItemStack output) {
        if (container == null) {
            throw new NullPointerException("The container recipe input is null");
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
        for (IPlasticPlateRecipemanager.Input input : this.recipes.keySet()) {
            for (ItemStack containerStack : container.getInputs()) {

                if (input.matches(containerStack, fluidStack)) {
                    throw new RuntimeException(
                            "ambiguous recipe: [" + container.getInputs() + "+" + " -> " + output
                                    + "], conflicts with [" + input.container.getInputs() + "+"
                                    + " -> " + this.recipes.get(input) + "]");
                }
            }

        }
        this.recipes.put(
                new IPlasticPlateRecipemanager.Input(container, fluidStack),
                new RecipeOutput(null, output)
        );
    }

    @Override
    public RecipeOutput getOutputFor(ItemStack container, FluidStack fluidStack, boolean adjustInput, boolean acceptTest) {
        if (acceptTest) {
            if (container == null && fluidStack == null) {
                return null;
            }
        } else if (container == null || fluidStack == null) {
            return null;
        }


        for (Map.Entry<IPlasticPlateRecipemanager.Input, RecipeOutput> entry : this.recipes.entrySet()) {
            IPlasticPlateRecipemanager.Input recipeInput = entry.getKey();
            if (acceptTest && container == null) {

                continue;
            }

            if (recipeInput.matches(container, fluidStack)) {
                if (acceptTest || container.getCount() >= recipeInput.container.getAmount() && fluidStack.amount >= recipeInput.fluidStack.amount) {
                    if (adjustInput) {

                        container.setCount(container.getCount() - recipeInput.container.getAmount());
                        fluidStack.amount -= recipeInput.fluidStack.amount;

                    }
                    return entry.getValue();
                }
                break;
            }
        }
        return null;
    }

    @Override
    public Map<IPlasticPlateRecipemanager.Input, RecipeOutput> getRecipes() {
        return this.recipes;
    }

}
