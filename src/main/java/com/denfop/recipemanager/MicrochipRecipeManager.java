package com.denfop.recipemanager;

import com.denfop.api.IMicrochipFarbricatorRecipeManager;
import com.denfop.api.recipe.BaseMachineRecipe;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MicrochipRecipeManager implements IMicrochipFarbricatorRecipeManager {

    private  Map<IMicrochipFarbricatorRecipeManager.Input, RecipeOutput> recipes = new HashMap<>();
    private  List<BaseMachineRecipe>  recipe = new ArrayList<>();
    public void addRecipe(
            IRecipeInput container, IRecipeInput fill, IRecipeInput container1, IRecipeInput fill1,
            IRecipeInput fill2, ItemStack output, NBTTagCompound tag
    ) {
        if (container == null) {
            throw new NullPointerException("The slot 1 recipe input is null");
        }
        if (fill == null) {
            throw new NullPointerException("The slot 2 recipe input is null");
        }
        if (fill1 == null) {
            throw new NullPointerException("The slot 3 recipe input is null");
        }
        if (fill2 == null) {
            throw new NullPointerException("The slot 4 recipe input is null");
        }
        if (container1 == null) {
            throw new NullPointerException("The slot 5 recipe input is null");
        }
        if (output == null) {
            throw new NullPointerException("The recipe output is null");
        }
        if (!StackUtil.check(output)) {
            throw new IllegalArgumentException("The recipe output " + StackUtil.toStringSafe(output) + " is invalid");
        }
        for (IMicrochipFarbricatorRecipeManager.Input input : this.recipes.keySet()) {
            for (ItemStack containerStack : container.getInputs()) {
                for (ItemStack fillStack : fill.getInputs()) {
                    for (ItemStack containerStack1 : container1.getInputs()) {
                        for (ItemStack fillStack1 : fill1.getInputs()) {
                            for (ItemStack fillStack2 : fill2.getInputs()) {

                                if (input.matches(containerStack, fillStack, fillStack1, fillStack2, containerStack1)) {
                                    this.recipes.replace(
                                            new IMicrochipFarbricatorRecipeManager.Input(container, fill, fill1, fill2, container1),
                                            new RecipeOutput(tag, output)
                                    );

                                }
                            }
                        }
                    }
                }
            }
        }
        this.recipes.put(
                new IMicrochipFarbricatorRecipeManager.Input(container, fill, fill1, fill2, container1),
                new RecipeOutput(tag, output)
        );
        this.recipe.add(new BaseMachineRecipe( new IMicrochipFarbricatorRecipeManager.Input(container, fill, fill1, fill2,
                container1), new RecipeOutput(tag, output)));
    }

    public RecipeOutput getOutputFor(
            ItemStack container, ItemStack fill, ItemStack container1, ItemStack fill1,
            ItemStack fill2, boolean adjustInput, boolean acceptTest
    ) {
        if (container.isEmpty() || fill.isEmpty() || container1.isEmpty() || fill1.isEmpty() || fill2.isEmpty()) {
            return null;
        }
        List<ItemStack> stack1 = new ArrayList<>();
        stack1.add(container);
        stack1.add(fill);
        stack1.add(container1);
        stack1.add(fill1);
        stack1.add(fill2);
        for (Map.Entry<IMicrochipFarbricatorRecipeManager.Input, RecipeOutput> entry : this.recipes.entrySet()) {
            IMicrochipFarbricatorRecipeManager.Input recipeInput = entry.getKey();
            List<IRecipeInput> recipeInputList = recipeInput.getList();
            int[] col = new int[5];
            int[] col1 = new int[5];
            List<Integer> lst = new ArrayList<>();
            lst.add(0);
            lst.add(1);
            lst.add(2);
            lst.add(3);
            lst.add(4);
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

    @Override
    public Map<Input, RecipeOutput> getRecipe(
            final ItemStack output
    ) {
        Map<Input, RecipeOutput> map = new HashMap<>();
        if (output.isEmpty()) {
            return map;
        }

        for (Map.Entry<IMicrochipFarbricatorRecipeManager.Input, RecipeOutput> entry : this.recipes.entrySet()) {
            if(entry.getValue().items.get(0).isItemEqual(output)){
                map.put(entry.getKey(),entry.getValue());
                return map;
            }

        }


        return map;
    }

    public Map<IMicrochipFarbricatorRecipeManager.Input, RecipeOutput> getRecipes() {
        return this.recipes;
    }

    @Override
    public List<BaseMachineRecipe> getListRecipe() {
        return this.recipe;
    }


}
