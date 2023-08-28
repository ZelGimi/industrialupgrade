package com.denfop.api.recipe;

import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipesFluidCore implements IFluidRecipes {

    private final List<IHasRecipe> recipes = new ArrayList<>();
    private final List<String> registeredRecipes = new ArrayList<>();
    public Map<String, IBaseRecipe> map_recipe_managers = new HashMap<>();
    public Map<String, List<IRecipeInputFluidStack>> map_recipe_managers_itemStack = new HashMap<>();
    public Map<String, List<BaseFluidMachineRecipe>> map_recipes_fluid = new HashMap<>();

    public void init() {
        this.addRecipeManager("obsidian", 2, true);
        this.addRecipeManager("mixer", 1, true);
        this.addRecipeManager("replicator", 1, true);
        this.addRecipeManager("mixer_double", 1, true);
    }

    public void addRecipeManager(String name, int size, boolean consume) {
        this.map_recipe_managers.put(name, new RecipeManager(name, size, consume, true));
    }

    public IBaseRecipe getRecipeFromName(String name) {
        return map_recipe_managers.get(name);
    }

    public void addInitRecipes(final IHasRecipe hasRecipe) {
        this.recipes.add(hasRecipe);
    }

    public void initializationRecipes() {
        this.recipes.forEach(iHasRecipe -> {
            if (!registeredRecipes.contains(iHasRecipe.getName())) {
                registeredRecipes.add(iHasRecipe.getName());
                iHasRecipe.init();
            }
        });
    }

    public void addRecipe(String name, BaseFluidMachineRecipe recipe) {
        if (!this.map_recipes_fluid.containsKey(name)) {
            List<FluidStack> iRecipeInputList = recipe.input.getInputs();
            List<IRecipeInputFluidStack> inputStackList = new ArrayList<>();

            for (FluidStack recipeInput : iRecipeInputList) {
                inputStackList.add(new RecipeInputFluidStack(recipeInput));
            }

            this.map_recipe_managers_itemStack.put(name, inputStackList);
            List<BaseFluidMachineRecipe> lst = new ArrayList<>();
            lst.add(recipe);
            this.map_recipes_fluid.put(name, lst);
        } else {
            List<IRecipeInputFluidStack> iRecipeInputList = this.map_recipe_managers_itemStack.get(name);

            if (iRecipeInputList.isEmpty()) {
                for (FluidStack input1 : recipe.input.getInputs()) {
                    iRecipeInputList.add(new RecipeInputFluidStack(input1));
                }
            } else {
                for (FluidStack stack : recipe.input.getInputs()) {
                    boolean continues1 = false;
                    for (IRecipeInputFluidStack input : iRecipeInputList) {
                        if (!input.matched(stack)) {
                            iRecipeInputList.add(new RecipeInputFluidStack(stack));
                            continues1 = true;
                            break;
                        }

                    }
                    if (continues1) {
                        break;
                    }
                }
            }

            this.map_recipe_managers_itemStack.replace(name, iRecipeInputList);
            this.map_recipes_fluid.get(name).add(recipe);
        }
    }

    public List<BaseFluidMachineRecipe> getRecipeList(String name) {
        return this.map_recipes_fluid.get(name);
    }

    public BaseFluidMachineRecipe getRecipeOutput(
            final IBaseRecipe recipe,
            List<BaseFluidMachineRecipe> recipes,
            boolean adjustInput,
            FluidStack... stacks
    ) {
        List<FluidStack> stack1 = Arrays.asList(stacks);
        int size = recipe.getSize();
        for (BaseFluidMachineRecipe baseMachineRecipe : recipes) {
            List<FluidStack> recipeInputList = baseMachineRecipe.input.getInputs();

            boolean need = true;
            for (int i = 0; i < size; i++) {
                if (!recipeInputList.get(i).isFluidEqual(stack1.get(i))) {
                    need = true;
                    break;

                }
                if (recipeInputList.get(i).amount > stack1.get(i).amount) {
                    need = true;
                    break;
                }
                need = false;
            }
            if (need) {
                continue;
            }
            if (adjustInput) {
                for (int j = 0; j < stack1.size(); j++) {
                    stack1.get(j).amount = stack1.get(j).amount - recipeInputList.get(j).amount;
                }

            } else {
                return baseMachineRecipe;
            }

        }

        return null;
    }

    public BaseFluidMachineRecipe getRecipeOutput(
            final IBaseRecipe recipe,
            List<BaseFluidMachineRecipe> recipes,
            boolean adjustInput,
            List<FluidStack> stacks
    ) {
        int size = recipe.getSize();
        for (BaseFluidMachineRecipe baseMachineRecipe : recipes) {
            List<FluidStack> recipeInputList = baseMachineRecipe.input.getInputs();

            boolean need = true;
            for (int i = 0; i < size; i++) {
                if (!recipeInputList.get(i).isFluidEqual(stacks.get(i))) {
                    need = true;
                    break;

                }
                if (recipeInputList.get(i).amount > stacks.get(i).amount) {
                    need = true;
                    break;
                }
                need = false;
            }
            if (need) {
                continue;
            }
            if (adjustInput) {
                for (int j = 0; j < stacks.size(); j++) {
                    stacks.get(j).amount = stacks.get(j).amount - recipeInputList.get(j).amount;
                }

            } else {
                return baseMachineRecipe;
            }

        }

        return null;
    }

}
