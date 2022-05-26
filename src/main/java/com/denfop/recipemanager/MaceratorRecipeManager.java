package com.denfop.recipemanager;

import com.denfop.api.IMaceratorRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import ic2.core.util.StackUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MaceratorRecipeManager implements IMaceratorRecipeManager {

    private final Map<IMaceratorRecipeManager.Input, RecipeOutput> recipes = new HashMap<>();

    public MaceratorRecipeManager() {
        final Iterable<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipe = Recipes.macerator.getRecipes();
        final Iterator<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> iter = recipe.iterator();
        List<MachineRecipe<IRecipeInput, Collection<ItemStack>>> lst = new ArrayList<>();
        iter.forEachRemaining(lst::add);
        for (MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe1 : lst) {
            List<ItemStack> list = (List<ItemStack>) recipe1.getOutput();
            if(recipe1.getInput().matches(new ItemStack(Items.COAL)))
                ((List<ItemStack>)recipe1.getOutput()).get(0).setCount(1);

            this.addRecipe(recipe1.getInput(), recipe1.getMetaData(), list.get(0));
        }
    }

    public void addRecipe(IRecipeInput container, NBTTagCompound metadata, ItemStack output) {
        if (container == null) {
            throw new NullPointerException("The container recipe input is null");
        }

        if (output == null) {
            throw new NullPointerException("The recipe output is null");
        }
        if (!StackUtil.check(output)) {
            throw new IllegalArgumentException("The recipe output " + StackUtil.toStringSafe(output) + " is invalid");
        }
        for (IMaceratorRecipeManager.Input input : this.recipes.keySet()) {
            for (ItemStack containerStack : container.getInputs()) {
                if (input.matches(containerStack)) {
                    this.recipes.remove(input);
                    this.recipes.put(
                            new IMaceratorRecipeManager.Input(container),
                            new RecipeOutput(metadata, output)
                    );
                    return;
                }


            }
        }
        this.recipes.put(
                new IMaceratorRecipeManager.Input(container),
                new RecipeOutput(metadata, output)
        );
    }

    public RecipeOutput getOutputFor(ItemStack container, boolean adjustInput, boolean acceptTest) {
        if (container.isEmpty()) {
            return null;
        }
        for (Map.Entry<IMaceratorRecipeManager.Input, RecipeOutput> entry : this.recipes.entrySet()) {
            IMaceratorRecipeManager.Input recipeInput = entry.getKey();

            if (recipeInput.matches(container)) {
                if (acceptTest || container.stackSize >= recipeInput.container.getAmount()) {
                    if (adjustInput) {

                        container.stackSize -= recipeInput.container.getAmount();
                    }
                    return entry.getValue();
                }
                break;
            }

        }
        return null;
    }

    public Map<IMaceratorRecipeManager.Input, RecipeOutput> getRecipes() {
        return this.recipes;
    }


}
