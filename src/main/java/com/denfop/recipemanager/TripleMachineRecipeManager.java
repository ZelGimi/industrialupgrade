package com.denfop.recipemanager;

import com.denfop.api.ITripleMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

public class TripleMachineRecipeManager implements ITripleMachineRecipeManager {

    private final Map<ITripleMachineRecipeManager.Input, RecipeOutput> recipes = new HashMap<>();

    public void addRecipe(IRecipeInput container, IRecipeInput fill, IRecipeInput fill1, ItemStack output, NBTTagCompound tag) {
        if (container == null) {
            throw new NullPointerException("The container recipe input is null");
        }
        if (fill == null) {
            throw new NullPointerException("The fill recipe input is null");
        }
        if (fill1 == null) {
            throw new NullPointerException("The fill1 recipe input is null");
        }
        if (output == null) {
            throw new NullPointerException("The recipe output is null");
        }
        if (!StackUtil.check(output)) {
            throw new IllegalArgumentException("The recipe output " + StackUtil.toStringSafe(output) + " is invalid");
        }
        for (ITripleMachineRecipeManager.Input input : this.recipes.keySet()) {
            for (ItemStack containerStack : container.getInputs()) {
                for (ItemStack fillStack : fill.getInputs()) {
                    for (ItemStack fillStack1 : fill1.getInputs()) {
                        if (input.matches(containerStack, fillStack, fillStack1)) {
                            this.recipes.remove(input);
                            this.recipes.put(
                                    new ITripleMachineRecipeManager.Input(container, fill, fill1),
                                    new RecipeOutput(null, output)
                            );
                            return;
                        }

                    }
                }
            }
        }
        this.recipes.put(
                new ITripleMachineRecipeManager.Input(container, fill, fill1),
                new RecipeOutput(tag, output)
        );
    }

    public RecipeOutput getOutputFor(
            ItemStack container,
            ItemStack fill,
            ItemStack fill1,
            boolean adjustInput,
            boolean acceptTest
    ) {
        if (acceptTest) {
            if (container.isEmpty() && fill.isEmpty() && fill1.isEmpty()) {
                return null;
            }
        } else if (container.isEmpty() || fill.isEmpty() || fill1.isEmpty()) {
            return null;
        }
        for (Map.Entry<ITripleMachineRecipeManager.Input, RecipeOutput> entry : this.recipes.entrySet()) {
            ITripleMachineRecipeManager.Input recipeInput = entry.getKey();
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
            if (acceptTest && fill1.isEmpty()) {
                if (recipeInput.container.matches(container)) {
                    return entry.getValue();
                }
                continue;
            }
            if (recipeInput.matches(container, fill, fill1)) {
                if (acceptTest || container.stackSize >= recipeInput.container.getAmount() && fill.stackSize >= recipeInput.fill.getAmount() && fill1.stackSize >= recipeInput.fill1.getAmount()) {
                    if (adjustInput) {

                        container.stackSize -= recipeInput.container.getAmount();

                        fill.stackSize -= recipeInput.fill.getAmount();
                        fill1.stackSize -= recipeInput.fill1.getAmount();
                    }
                    return entry.getValue();
                }
                break;
            } else if (recipeInput.matches1(container, fill, fill1)) {
                if (acceptTest || container.stackSize >= recipeInput.container.getAmount() && fill.stackSize >= recipeInput.fill1.getAmount() && fill1.stackSize >= recipeInput.fill.getAmount()) {
                    if (adjustInput) {

                        container.stackSize -= recipeInput.container.getAmount();

                        fill.stackSize -= recipeInput.fill1.getAmount();
                        fill1.stackSize -= recipeInput.fill.getAmount();
                    }
                    return entry.getValue();
                }
                break;
            } else if (recipeInput.matches2(container, fill, fill1)) {
                if (acceptTest || container.stackSize >= recipeInput.fill.getAmount() && fill.stackSize >= recipeInput.container.getAmount() && fill1.stackSize >= recipeInput.fill1.getAmount()) {
                    if (adjustInput) {

                        container.stackSize -= recipeInput.fill.getAmount();

                        fill.stackSize -= recipeInput.container.getAmount();
                        fill1.stackSize -= recipeInput.fill1.getAmount();
                    }
                    return entry.getValue();
                }
                break;
            } else if (recipeInput.matches3(container, fill, fill1)) {
                if (acceptTest || container.stackSize >= recipeInput.fill1.getAmount() && fill.stackSize >= recipeInput.container.getAmount() && fill1.stackSize >= recipeInput.fill.getAmount()) {
                    if (adjustInput) {

                        fill.stackSize -= recipeInput.container.getAmount();
                        container.stackSize -= recipeInput.fill1.getAmount();
                        fill1.stackSize -= recipeInput.fill.getAmount();
                    }
                    return entry.getValue();
                }
                break;
            } else if (recipeInput.matches4(container, fill, fill1)) {
                if (acceptTest || container.stackSize >= recipeInput.fill1.getAmount() && fill.stackSize >= recipeInput.fill.getAmount() && fill1.stackSize >= recipeInput.container.getAmount()) {
                    if (adjustInput) {

                        fill1.stackSize -= recipeInput.container.getAmount();
                        container.stackSize -= recipeInput.fill1.getAmount();

                        fill.stackSize -= recipeInput.fill.getAmount();

                    }
                    return entry.getValue();
                }
                break;
            } else if (recipeInput.matches5(container, fill, fill1)) {
                if (acceptTest || container.stackSize >= recipeInput.fill.getAmount() && fill.stackSize >= recipeInput.fill1.getAmount() && fill1.stackSize >= recipeInput.container.getAmount()) {
                    if (adjustInput) {

                        fill1.stackSize -= recipeInput.container.getAmount();
                        container.stackSize -= recipeInput.fill.getAmount();
                        fill.stackSize -= recipeInput.fill1.getAmount();

                    }
                    return entry.getValue();
                }
                break;
            }
        }
        return null;
    }

    public Map<ITripleMachineRecipeManager.Input, RecipeOutput> getRecipes() {
        return this.recipes;
    }

}
