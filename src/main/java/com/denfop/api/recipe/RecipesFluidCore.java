package com.denfop.api.recipe;

import com.denfop.api.Recipes;
import com.denfop.recipe.IInputItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

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
        this.addRecipeManager("gas_combiner", 2, true);
        this.addRecipeManager("electrolyzer", 1, true);
        this.addRecipeManager("refrigerator", 1, true);
        this.addRecipeManager("item_divider", 1, true);
        this.addRecipeManager("item_divider_fluid", 1, true);
        this.addRecipeManager("fluid_separator", 1, true);
        this.addRecipeManager("polymerizer", 1, true);
        this.addRecipeManager("fluid_adapter", 1, true);
        this.addRecipeManager("fluid_integrator", 1, true);
        this.addRecipeManager("solid_electrolyzer", 1, true);
        this.addRecipeManager("squeezer", 1, true);
        this.addRecipeManager("oil_purifier", 1, true);
        this.addRecipeManager("dryer", 1, true);
        this.addRecipeManager("oil_refiner", 1, true);
        this.addRecipeManager("adv_oil_refiner", 1, true);
        this.addRecipeManager("imp_oil_refiner", 1, true);
        this.addRecipeManager("fluid_mixer", 2, true);
        this.addRecipeManager("solid_fluid_mixer", 1, true);
        this.addRecipeManager("heat", 1, true);
        this.addRecipeManager("gas_chamber", 2, true);
        this.addRecipeManager("primal_fluid_integrator", 1, true);
        this.addRecipeManager("smeltery", 1, true);

        this.addRecipeManager("empty", 1, true);
        this.addRecipeManager("ingot_casting", 1, true);
        this.addRecipeManager("gear_casting", 1, true);
        this.addRecipeManager("solid_fluid_integrator", 2, true);
        this.addRecipeManager("single_fluid_adapter", 1, true);
        this.addRecipeManager("biomass", 1, true);
        this.addRecipeManager("refractory_furnace", 1, true);
        this.addRecipeManager("mini_smeltery", 1, true);
        this.addRecipeManager("incubator", 1, true);
        this.addRecipeManager("insulator", 2, true);
        this.addRecipeManager("rna_collector", 1, true);
        this.addRecipeManager("mutatron", 2, true);
        this.addRecipeManager("reverse_transcriptor", 1, true);
        this.addRecipeManager("genetic_stabilizer", 1, true);
        this.addRecipeManager("genetic_replicator", 2, true);

    }

    public void addRecipeManager(String name, int size, boolean consume) {
        this.map_recipe_managers.put(name, new RecipeManager(name, size, consume, true));
    }

    public IBaseRecipe getRecipeFromName(String name) {
        return map_recipe_managers.get(name);
    }
    public List<String> getRecipes() {
        return map_recipe_managers.keySet().stream().toList();
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

    public void removeAllRecipe(String name, boolean removeAll, FluidStack output) {
        List<BaseFluidMachineRecipe> recipes = this.map_recipes_fluid.get(name);
        if (recipes == null)
            return;
        List<BaseFluidMachineRecipe> deleteRecipes = new ArrayList<>();
        boolean find = false;
        List<Integer> integers = new LinkedList<>();
        int i = 0;
        for (BaseFluidMachineRecipe recipe : recipes) {
            for (FluidStack output_stack : recipe.getOutput_fluid()) {
                if (output.isFluidEqual(output_stack) && (removeAll || !find)) {
                    deleteRecipes.add(recipe);
                    find = true;
                    integers.add(i);
                    break;
                }
            }
            i++;

        }
      boolean hasRecipe = Recipes.recipes.getRecipeList(name) != null;
        List<RecipeRemove> recipeRemoves = new ArrayList<>();
        for (BaseFluidMachineRecipe deleteRecipe : deleteRecipes) {
            recipes.remove(deleteRecipe);
            final List<IRecipeInputFluidStack> list = this.map_recipe_managers_itemStack.get(name);
            if (hasRecipe) {
                Integer recipe = integers.remove(0);
                BaseMachineRecipe recipeBase = Recipes.recipes.getRecipeList(name).get(recipe);
                recipeRemoves.add(new RecipeRemove(name, recipeBase.input.getInputs().get(recipeBase.input.getInputs().size() - 1).getInputs().get(0), removeAll));
            }
            IInputFluid input = deleteRecipe.input;
            final List<FluidStack> list2 = input.getInputs();
            for (FluidStack input1 : list2) {
                IRecipeInputFluidStack iRecipeInputStack = new RecipeInputFluidStack(input1);
                list.remove(iRecipeInputStack);
            }

        }
        recipeRemoves.forEach(recipeRemove -> {
            if (recipeRemove.isRemoveAll())
                Recipes.recipes.removeAllRecipe(recipeRemove.getNameRecipe(),  new RecipeOutput(null, recipeRemove.getStack()));
            else
                Recipes.recipes.removeRecipe(recipeRemove.getNameRecipe(), new RecipeOutput(null, recipeRemove.getStack()));

        });
    }

    public List<BaseFluidMachineRecipe> getRecipeList(String name) {
        return this.map_recipes_fluid.getOrDefault(name, Collections.emptyList());
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
                if (recipeInputList.get(i).getAmount() > stack1.get(i).getAmount()) {
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
                    stack1.get(j).setAmount(stack1.get(j).getAmount() - recipeInputList.get(j).getAmount());
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
            if (recipeInputList == null || recipeInputList.isEmpty()) {
                continue;
            }
            boolean need = true;
            for (int i = 0; i < size; i++) {
                if (!recipeInputList.get(i).isFluidEqual(stacks.get(i))) {
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
                    stacks.get(j).setAmount(stacks.get(j).getAmount() - recipeInputList.get(j).getAmount());
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
            ItemStack stack
    ) {
        int size = recipe.getSize();
        for (BaseFluidMachineRecipe baseMachineRecipe : recipes) {
            final IInputItemStack recipeInputList = baseMachineRecipe.input.getStack();

            boolean need = true;
            for (int i = 0; i < size; i++) {
                if (!recipeInputList.matches(stack)) {
                    need = true;
                    break;

                }

                need = false;
            }
            if (need) {
                continue;
            }
            return baseMachineRecipe;

        }

        return null;
    }

    public void removeRecipe(String name, FluidStack output) {
        List<BaseFluidMachineRecipe> recipes = this.map_recipes_fluid.get(name);
        List<BaseFluidMachineRecipe> deleteRecipes = new ArrayList<>();
        cycle:
        for (BaseFluidMachineRecipe recipe : recipes) {
            for (FluidStack output_stack : recipe.getOutput_fluid()) {
                if (output.isFluidEqual(output_stack)) {
                    deleteRecipes.add(recipe);
                    break cycle;
                }
            }

        }
        for (BaseFluidMachineRecipe deleteRecipe : deleteRecipes) {
            recipes.remove(deleteRecipe);
            final List<IRecipeInputFluidStack> list = this.map_recipe_managers_itemStack.get(name);
            IInputFluid input = deleteRecipe.input;
            final List<FluidStack> list2 = input.getInputs();
            for (FluidStack input1 : list2) {
                IRecipeInputFluidStack iRecipeInputStack = new RecipeInputFluidStack(input1);
                list.remove(iRecipeInputStack);
            }

        }
    }
}
