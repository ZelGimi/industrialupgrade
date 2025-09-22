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
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CraftingManager {

    public CraftingManager() {
    }

    public BaseRecipe addRecipe(String output, Object... input) {
        return addRecipe(OreDictionary.getOres(output).get(0), input);
    }

    public BaseRecipe addRecipe(Item output, Object... input) {
        return addRecipe(new ItemStack(output), input);
    }

    public BaseRecipe addRecipe(ItemStack output, Object... input) {
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
        return new BaseRecipe(output, grid, objectMap);
    }

    public BaseShapelessRecipe addShapelessRecipe(ItemStack output, Object... input) {
        List<IInputItemStack> recipeInputList = new ArrayList<>();
        for (Object object : input) {
            IInputItemStack recipeInput = getRecipeInput(object);
            recipeInputList.add(recipeInput);
        }
        return new BaseShapelessRecipe(output, recipeInputList);
    }

    public BaseShapelessRecipe addShapelessRecipe(Item output, Object... input) {
        return addShapelessRecipe(new ItemStack(output), input);
    }


    public BaseShapelessRecipe addShapelessRecipe(Block output, Object... input) {
        return addShapelessRecipe(new ItemStack(output), input);
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
