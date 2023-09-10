package com.denfop.recipe;

import com.denfop.api.Recipes;
import com.denfop.api.crafting.BaseRecipe;
import com.denfop.api.crafting.BaseShapelessRecipe;
import com.denfop.api.crafting.PartRecipe;
import com.denfop.api.crafting.RecipeGrid;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CraftingManager {

    public CraftingManager() {
    }


    public void addRecipe(Item output, Object... input) {
        addRecipe(new ItemStack(output), input);
    }

    public void addRecipe(ItemStack output, Object... input) {
        List<Object> objects = Arrays.asList(input);
        List<String> list = new ArrayList<>();
        List<PartRecipe> objectMap = new ArrayList<>();
        int i = 0;
        for (; i < 3; i++) {
            if (objects.get(i) instanceof String) {
                list.add((String) objects.get(i));
            } else {
                break;
            }
        }
        RecipeGrid grid = new RecipeGrid(list);
        for (; i < objects.size(); i += 2) {
            Character character = (Character) objects.get(i);
            Object object = objects.get(i + 1);
            IInputItemStack recipeInput = getRecipeInput(object);
            objectMap.add(new PartRecipe(character.toString(), recipeInput));
        }
        new BaseRecipe(output, grid, objectMap);
    }

    public void addShapelessRecipe(ItemStack output, Object... input) {
        List<IInputItemStack> recipeInputList = new ArrayList<>();
        for (Object object : input) {
            IInputItemStack recipeInput = getRecipeInput(object);
            recipeInputList.add(recipeInput);
        }
        new BaseShapelessRecipe(output, recipeInputList);
    }

    public void addShapelessRecipe(Item output, Object... input) {
        addShapelessRecipe(new ItemStack(output), input);
    }

    public void addShapelessRecipe(Block output, Object... input) {
        addShapelessRecipe(new ItemStack(output), input);
    }

    private IInputItemStack getRecipeInput(Object o) {
        if (o == null) {
            throw new NullPointerException("Null recipe input object.");
        } else if (o instanceof IInputItemStack) {
            return (IInputItemStack) o;
        } else if (o instanceof ItemStack) {
            return Recipes.inputFactory.getInput((ItemStack) o);
        } else if (o instanceof Block) {
            return Recipes.inputFactory.getInput(new ItemStack((Block) o));
        } else if (o instanceof Item) {
            return Recipes.inputFactory.getInput(new ItemStack((Item) o));
        } else if (o instanceof String) {
            return Recipes.inputFactory.getInput((String) o);
        } else if (o instanceof Fluid) {
            return Recipes.inputFactory.getInput((Fluid) o);
        } else if (o instanceof FluidStack) {
            return Recipes.inputFactory.getInput(((FluidStack) o).getFluid(), ((FluidStack) o).amount);
        }
        throw new RuntimeException();
    }

}
