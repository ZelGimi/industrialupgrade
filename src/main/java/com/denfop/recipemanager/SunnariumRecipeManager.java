package com.denfop.recipemanager;

import com.denfop.api.ISunnariumRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SunnariumRecipeManager implements ISunnariumRecipeManager {

    private final Map<ISunnariumRecipeManager.Input, RecipeOutput> recipes = new HashMap<>();

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
            if (container.isEmpty() && fill.isEmpty() && fill1.isEmpty() && fill2.isEmpty()) {
                return null;
            }
        } else if (container.isEmpty() || fill.isEmpty() || fill1.isEmpty() || fill2.isEmpty()) {
            return null;
        }

        List<ItemStack> stack1 = new ArrayList<>();
        stack1.add(container);
        stack1.add(fill);
        stack1.add(fill1);
        stack1.add(fill2);
        for (Map.Entry<ISunnariumRecipeManager.Input, RecipeOutput> entry : this.recipes.entrySet()) {
            ISunnariumRecipeManager.Input recipeInput = entry.getKey();
            List<IRecipeInput> recipeInputList = recipeInput.getList();
            int[] col = new int[4];
            int[] col1 = new int[4];
            List<Integer> lst = new ArrayList<>();
            lst.add(0);
            lst.add(1);
            lst.add(2);
            lst.add(3);
            List<Integer> lst1 = new ArrayList<>();
            for (int j = 0; j < stack1.size(); j++) {
                for (int i = 0; i < recipeInputList.size(); i++) {
                    if (recipeInputList.get(i).matches(stack1.get(j)) && !lst1.contains(i)) {
                        lst1.add(i);
                        col1[j] = i;
                        break;
                    }
                }
            }
            if (lst.size() == lst1.size()) {
                for (int j = 0; j < stack1.size(); j++) {
                    ItemStack stack2 = recipeInputList.get(col1[j]).getInputs().get(0);
                    ItemStack stack = stack1.get(j);
                    if (stack.getCount() < stack2.getCount()) {
                        return null;
                    }
                    col[j] = stack2.getCount();
                }
                if (adjustInput) {
                    for (int j = 0; j < stack1.size(); j++) {
                        stack1.get(j).setCount(stack1.get(j).getCount() - col[j]);
                    }
                    break;
                } else {
                    return entry.getValue();
                }
            }
        }

        return null;
    }

    public Map<ISunnariumRecipeManager.Input, RecipeOutput> getRecipes() {
        return this.recipes;
    }

}
