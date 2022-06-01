package com.denfop.api.recipe;


import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.List;

public interface IRecipes {

    void addRecipe(String name, BaseMachineRecipe recipe);

    void addRecipeList(String name, List<BaseMachineRecipe> list);

    List<BaseMachineRecipe> getRecipeList(String name);

    void addRecipeManager(String name, int size, boolean consume);

    BaseMachineRecipe getRecipeOutput(
            final IBaseRecipe recipe,
            List<BaseMachineRecipe> recipes,
            boolean adjustInput,
            ItemStack... stacks
    );

    MachineRecipe getRecipeMachineRecipeOutput(
            final IBaseRecipe recipe,
            List<BaseMachineRecipe> recipes,
            boolean adjustInput,
            ItemStack... stacks
    );

    BaseMachineRecipe getRecipeOutput(String name, boolean adjustInput, ItemStack... stacks);

    MachineRecipe getRecipeMachineOutput(String name, boolean adjustInput, ItemStack... stacks);

    boolean needContinue(MachineRecipe recipe, InvSlotRecipes slot);

    BaseMachineRecipe getRecipeOutputFromInstruments(String name, boolean adjustInput, ItemStack... stacks);


    BaseMachineRecipe getRecipeOutput(
            final IBaseRecipe recipe,
            List<BaseMachineRecipe> recipes,
            boolean adjustInput,
            List<ItemStack> stacks
    );

    MachineRecipe getRecipeMachineRecipeOutput(
            final IBaseRecipe recipe,
            List<BaseMachineRecipe> recipes,
            boolean adjustInput,
            List<ItemStack> stacks
    );

    BaseMachineRecipe getRecipeMultiOutput(
            final IBaseRecipe recipe,
            List<BaseMachineRecipe> recipes,
            boolean adjustInput,
            List<ItemStack> stacks
    );

    MachineRecipe getRecipeMachineMultiOutput(
            final IBaseRecipe recipe,
            List<BaseMachineRecipe> recipes,
            boolean adjustInput,
            List<ItemStack> stacks
    );

    IBaseRecipe getRecipe(String name);

    void removeRecipe(String name, RecipeOutput output);

    BaseMachineRecipe getRecipeOutputFluid(String name, boolean consume, List<ItemStack> list, FluidTank tank);

    MachineRecipe getRecipeOutputMachineFluid(String name, boolean consume, List<ItemStack> list, FluidTank tank);

}
