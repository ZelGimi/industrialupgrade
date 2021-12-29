package com.denfop.recipemanager;

import com.denfop.api.IDoubleMachineRecipeManager;
import com.denfop.api.Recipes;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

public class DoubleMachineRecipeManager implements IDoubleMachineRecipeManager {

    private final Map<IDoubleMachineRecipeManager.Input, RecipeOutput> recipes = new HashMap<>();

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
        for (IDoubleMachineRecipeManager.Input input : this.recipes.keySet()) {
            for (ItemStack containerStack : container.getInputs()) {
                for (ItemStack fillStack : fill.getInputs()) {
                    if (input.matches(containerStack, fillStack)) {
                        this.recipes.remove(input);
                        this.recipes.put(
                                new IDoubleMachineRecipeManager.Input(container, fill),
                                new RecipeOutput(metadata, output)
                        );
                        return;
                    }

                }
            }
        }
        this.recipes.put(
                new IDoubleMachineRecipeManager.Input(container, fill),
                new RecipeOutput(metadata, output)
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
        for (Map.Entry<IDoubleMachineRecipeManager.Input, RecipeOutput> entry : this.recipes.entrySet()) {
            IDoubleMachineRecipeManager.Input recipeInput = entry.getKey();
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
                if (acceptTest || container.stackSize >= recipeInput.container.getAmount() && fill.stackSize >= recipeInput.fill.getAmount()) {
                    if (adjustInput) {

                        container.stackSize -= recipeInput.container.getAmount();
                        fill.stackSize -= recipeInput.fill.getAmount();
                    }
                    return entry.getValue();
                }
                break;
            } else if (recipeInput.matches1(container, fill)) {
                if (acceptTest || container.stackSize >= recipeInput.fill.getAmount() && fill.stackSize >= recipeInput.container.getAmount()) {
                    if (adjustInput) {

                        container.stackSize -= recipeInput.fill.getAmount();
                        fill.stackSize -= recipeInput.container.getAmount();
                    }
                    return entry.getValue();
                }
                break;
            }

        }
        return null;
    }

    public Map<IDoubleMachineRecipeManager.Input, RecipeOutput> getRecipes() {
        return this.recipes;
    }

    @Override
    public void removeRecipe(final Input input, final RecipeOutput output) {
        RecipeOutput recipe = this.getRecipes().get(input);
        if (recipe != null && checkListEquality(recipe.items, output.items)) {
            this.recipes.remove(input);
            for (Iterator<Map.Entry<Input, RecipeOutput>> it = Recipes.Alloysmelter
                    .getRecipes()
                    .entrySet()
                    .iterator(); it.hasNext(); ) {
                Map.Entry<Input, RecipeOutput> iRecipeInputRecipeOutputEntry = it.next();
                if (getOutputFor(iRecipeInputRecipeOutputEntry.getKey().container.getInputs().get(0),
                        iRecipeInputRecipeOutputEntry.getKey().fill.getInputs().get(0),
                        false,
                        false
                ) == null) {
                    it.remove();
                    return;
                }
            }
        }
    }

    private static boolean checkListEquality(Collection<ItemStack> a, Collection<ItemStack> b) {
        if (a.size() != b.size()) {
            return false;
        } else {
            ListIterator<ItemStack> itB = (new ArrayList(b)).listIterator();

            for (final ItemStack stack : a) {
                do {
                    if (!itB.hasNext()) {
                        return false;
                    }
                } while (!StackUtil.checkItemEqualityStrict(stack, itB.next()));

                itB.remove();

                while (itB.hasPrevious()) {
                    itB.previous();
                }
            }

            return true;
        }
    }

}
