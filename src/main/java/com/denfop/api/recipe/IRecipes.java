package com.denfop.api.recipe;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRecipes {

    void addRecipe(String name, BaseMachineRecipe recipe);

    void addRecipeList(String name, List<BaseMachineRecipe> list);

    List<BaseMachineRecipe> getRecipeList(String name);

    void optimize();

    Set<Map.Entry<ItemStack, BaseMachineRecipe>> getRecipeStack(String name);

    void removeAll(String recipe);

    void reloadRecipes(String recipe);

    void initializationRecipes();

    void addRecipeManager(String name, int size, boolean consume);


    RecipesFluidCore getRecipeFluid();

    RecipeArrayList<IRecipeInputStack> getMap_recipe_managers_itemStack(String name);

    List<IHasRecipe> getRecipesForInit();

    BaseMachineRecipe getRecipeOutput(
            final IBaseRecipe recipe,
            List<BaseMachineRecipe> recipes,
            boolean adjustInput,
            ItemStack... stacks
    );

    BaseMachineRecipe getRecipeOutput(String name, boolean adjustInput, ItemStack... stacks);

    MachineRecipe getRecipeMachineOutput(String name, boolean adjustInput, ItemStack... stacks);

    boolean needContinue(MachineRecipe recipe, InvSlotRecipes slot);

    BaseMachineRecipe getRecipeOutputFromInstruments(String name, boolean adjustInput, ItemStack... stacks);

    boolean needContinue(final MachineRecipe recipe, final InvSlotRecipes slot, final FluidTank tank);

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

    BaseMachineRecipe getRecipeConsume(
            final IBaseRecipe recipe, MachineRecipe recipes,
            final boolean adjustInput, final List<ItemStack> stacks
    );

    MachineRecipe getMachineRecipeConsume(
            final IBaseRecipe recipe, MachineRecipe recipes,
            final boolean adjustInput, final List<ItemStack> stacks
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

    List<Fluid> getInputFluidsFromRecipe(String name);

    IBaseRecipe getRecipe(String name);

    void addRemoveRecipe(String name, ItemStack stack, boolean allRemove);

    void addRemoveRecipe(String name, ItemStack stack);

    void addFluidRemoveRecipe(String name, FluidStack stack);

    void removeAllRecipesFromList();

    void addAdderRecipe(String name, BaseMachineRecipe stack);

    void addFluidAdderRecipe(final String name, final BaseFluidMachineRecipe baseMachineRecipe);

    void addAllRecipesFromList();

    void removeRecipe(String name, RecipeOutput output);

    void removeAllRecipe(String name, RecipeOutput output);

    void removeRecipe(String name, ItemStack output);

    BaseMachineRecipe getRecipeOutputFluid(String name, boolean consume, List<ItemStack> list, FluidTank tank);

    MachineRecipe getRecipeOutputMachineFluid(String name, boolean consume, List<ItemStack> list, FluidTank tank);

    BaseMachineRecipe getRecipeOutputFluid(
            IBaseRecipe recipe,
            MachineRecipe recipeOutput,
            boolean consume,
            List<ItemStack> list,
            FluidTank tank
    );

    MachineRecipe getRecipeOutputMachineFluid(
            IBaseRecipe recipe,
            List<BaseMachineRecipe> recipe_list,
            boolean b,
            List<ItemStack> list,
            FluidTank tank
    );

    void addInitRecipes(IHasRecipe hasRecipe);


}
