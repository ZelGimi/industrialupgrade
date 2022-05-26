package com.denfop.recipemanager;

import com.denfop.api.IDoubleMolecularRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

public class DoubleMolecularRecipeManager implements IDoubleMolecularRecipeManager {

    private final Map<IDoubleMolecularRecipeManager.Input, RecipeOutput> recipes = new HashMap<>();

    public void addRecipe(IRecipeInput container, IRecipeInput fill, NBTTagCompound metadata, ItemStack output) {
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
        for (IDoubleMolecularRecipeManager.Input input : this.recipes.keySet()) {
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
                new IDoubleMolecularRecipeManager.Input(container, fill),
                new RecipeOutput(metadata, output)
        );
    }

    public RecipeOutput getOutputFor(ItemStack container, ItemStack fill, boolean adjustInput, boolean acceptTest) {
        if (acceptTest) {
            if (container.isEmpty() && fill.isEmpty()) {
                return null;
            }
        } else if (container.isEmpty() || fill.isEmpty()) {
            return null;
        }
        for (Map.Entry<IDoubleMolecularRecipeManager.Input, RecipeOutput> entry : this.recipes.entrySet()) {
            IDoubleMolecularRecipeManager.Input recipeInput = entry.getKey();
            if (acceptTest && container.isEmpty()) {
                if (recipeInput.fill.matches(fill)) {
                    return entry.getValue();
                }
                continue;
            }
            if (acceptTest && fill.isEmpty()) {
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

    public Map<IDoubleMolecularRecipeManager.Input, RecipeOutput> getRecipes() {
        return this.recipes;
    }

}
