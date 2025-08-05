package com.denfop.integration.jei.smelterycasting;


import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class SmelteryCastingHandler {

    private static final List<SmelteryCastingHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final ItemStack output;
    private final FluidStack inputFluid;


    public SmelteryCastingHandler(ItemStack input, ItemStack output, FluidStack inputFluid) {
        this.input = input;
        this.output = output;
        this.inputFluid = inputFluid;
    }

    public static List<SmelteryCastingHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }


    public static SmelteryCastingHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {
        final List<BaseFluidMachineRecipe> list = Recipes.recipes.getRecipeFluid().getRecipeList(
                "ingot_casting");
        for (BaseFluidMachineRecipe baseFluidMachineRecipe : list) {
            ItemStack input = new ItemStack(IUItem.crafting_elements.getStack(496), 1);
            FluidStack inputFluid = baseFluidMachineRecipe.input.getInputs().get(0);
            ItemStack output = baseFluidMachineRecipe.getOutput().items.get(0);


            addRecipe(input, output,
                    inputFluid
            );
        }
        for (BaseFluidMachineRecipe baseFluidMachineRecipe : Recipes.recipes.getRecipeFluid().getRecipeList(
                "gear_casting")) {
            ItemStack input = new ItemStack(IUItem.crafting_elements.getStack(497), 1);
            FluidStack inputFluid = baseFluidMachineRecipe.input.getInputs().get(0);
            ItemStack output = baseFluidMachineRecipe.getOutput().items.get(0);


            addRecipe(input, output,
                    inputFluid
            );
        }

    }

    private static SmelteryCastingHandler addRecipe(ItemStack input, ItemStack output, FluidStack inputFluid) {
        SmelteryCastingHandler recipe = new SmelteryCastingHandler(input, output, inputFluid);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }


}
