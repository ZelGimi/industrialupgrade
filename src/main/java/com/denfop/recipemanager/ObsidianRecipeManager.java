package com.denfop.recipemanager;

import com.denfop.api.IObsidianGenerator;
import ic2.api.recipe.RecipeOutput;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

public class ObsidianRecipeManager implements IObsidianGenerator {

    private final Map<IObsidianGenerator.Input, RecipeOutput> recipes = new HashMap<>();

    @Override
    public void addRecipe(FluidStack fluidStack, FluidStack fluidStack1, ItemStack output) {
        if (fluidStack == null) {
            throw new NullPointerException("The container recipe input is null");
        }
        if (fluidStack1 == null) {
            throw new NullPointerException("The fill recipe input is null");
        }
        if (output == null) {
            throw new NullPointerException("The recipe output is null");
        }
        if (!StackUtil.check(output)) {
            throw new IllegalArgumentException("The recipe output " + StackUtil.toStringSafe(output) + " is invalid");
        } else {

            for (Input value : this.recipes.keySet()) {


                if (value.matches(fluidStack, fluidStack1)) {
                    throw new RuntimeException("ambiguous recipe");
                }

            }
        }
        this.recipes.put(
                new ObsidianRecipeManager.Input(fluidStack, fluidStack1),
                new RecipeOutput(null, output)
        );

    }

    @Override
    public RecipeOutput getOutputFor(FluidStack fluidStack, FluidStack fluidStack1, boolean adjustInput, boolean acceptTest) {
        if (acceptTest) {

            for (Map.Entry<Input, RecipeOutput> inputRecipeOutputEntry : this.recipes.entrySet()) {
                IObsidianGenerator.Input input = inputRecipeOutputEntry.getKey();
                if (acceptTest && (fluidStack == null || fluidStack1 == null)) {

                    return null;


                } else if (input.matches(fluidStack, fluidStack1)) {
                    if (input.fluidStack != null && fluidStack.amount >= input.fluidStack.amount && input.fluidStack1 != null && fluidStack1.amount >= input.fluidStack1.amount) {

                        if (adjustInput) {


                            fluidStack1.amount -= input.fluidStack1.amount;
                            fluidStack.amount -= input.fluidStack.amount;


                        }

                        return inputRecipeOutputEntry.getValue();
                    }

                    return null;
                }
            }

        }
        return null;
    }

    @Override
    public Map<Input, RecipeOutput> getRecipes() {
        return this.recipes;

    }

}
