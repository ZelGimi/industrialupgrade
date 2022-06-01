package com.denfop.api.recipe;

import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListRecipes implements IRecipes {

    public Map<String, List<BaseMachineRecipe>> map_recipes = new HashMap<>();
    public Map<String, IBaseRecipe> map_recipe_managers = new HashMap<>();

    public ListRecipes() {
        init();
    }

    public void init() {
        this.addRecipeManager("alloysmelter", 2, true);
        this.addRecipeManager("enrichment", 2, true);
        this.addRecipeManager("painter", 2, true);
        this.addRecipeManager("sunnuriumpanel", 2, true);
        this.addRecipeManager("synthesis", 2, true);
        this.addRecipeManager("upgradeblock", 2, true);
        this.addRecipeManager("advalloysmelter", 3, true);
        this.addRecipeManager("genstone", 2, false);
        this.addRecipeManager("microchip", 5, true);
        this.addRecipeManager("sunnurium", 4, true);
        this.addRecipeManager("wither", 7, true);
        this.addRecipeManager("doublemolecular", 2, true);
        this.addRecipeManager("molecular", 1, true);
        this.addRecipeManager("plastic", 2, true);
        this.addRecipeManager("plasticplate", 1, true);
        this.addRecipeManager("converter", 1, false);
        this.addRecipeManager("macerator", 1, true);
        this.addRecipeManager("compressor", 1, true);
        this.addRecipeManager("extractor", 1, true);
        this.addRecipeManager("recycler", 1, true);
        this.addRecipeManager("furnace", 1, true);
        this.addRecipeManager("extruding", 1, true);
        this.addRecipeManager("cutting", 1, true);
        this.addRecipeManager("rolling", 1, true);
        this.addRecipeManager("farmer", 1, true);
        this.addRecipeManager("scrap", 1, true);
        this.addRecipeManager("comb_macerator", 1, true);
        this.addRecipeManager("handlerho", 1, true);
    }

    public IBaseRecipe getRecipe(String name) {
        return this.map_recipe_managers.get(name);
    }

    public void addRecipeManager(String name, int size, boolean consume) {
        this.map_recipe_managers.put(name, new RecipeManager(name, size, consume));
        if (!this.map_recipes.containsKey(name)) {
            List<BaseMachineRecipe> lst = new ArrayList<>();
            this.map_recipes.put(name, lst);
        }
    }

    public void addRecipeManager(String name, int size, boolean consume, boolean require) {
        this.map_recipe_managers.put(name, new RecipeManager(name, size, consume, require));
        if (!this.map_recipes.containsKey(name)) {
            List<BaseMachineRecipe> lst = new ArrayList<>();
            this.map_recipes.put(name, lst);
        }
    }

    public void removeRecipe(String name, RecipeOutput output) {
        List<BaseMachineRecipe> recipes = this.map_recipes.get(name);
        recipes.removeIf(recipe -> recipe.getOutput().items.get(0).isItemEqual(output.items.get(0)));

    }

    @Override
    public BaseMachineRecipe getRecipeOutputFluid(
            final String name,
            final boolean adjustInput,
            final List<ItemStack> list,
            final FluidTank tank
    ) {
        List<BaseMachineRecipe> recipes = this.map_recipes.get(name);
        int size = this.map_recipe_managers.get(name).getSize();
        for (BaseMachineRecipe baseMachineRecipe : recipes) {
            int[] col = new int[size];
            int[] col1 = new int[size];
            List<Integer> lst = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                lst.add(i);
            }
            List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();
            List<Integer> lst1 = new ArrayList<>();
            if (tank.getFluid() == null) {
                return null;
            }
            if (!tank.getFluid().isFluidEqual(baseMachineRecipe.input.getFluid())) {
                return null;
            }
            if (tank.getFluidAmount() < 1000) {
                return null;
            }

            for (int j = 0; j < list.size(); j++) {
                for (int i = 0; i < recipeInputList.size(); i++) {
                    if (recipeInputList.get(i).matches(list.get(j)) && !lst1.contains(i)) {
                        lst1.add(i);

                        col1[j] = i;
                        break;
                    }
                }
            }
            if (lst.size() == lst1.size()) {
                for (int j = 0; j < list.size(); j++) {
                    ItemStack stack2 = recipeInputList.get(col1[j]).getInputs().get(0);
                    ItemStack stack = list.get(j);
                    if (stack.getCount() < stack2.getCount()) {
                        return null;
                    }
                    col[j] = stack2.getCount();
                }
                if (adjustInput) {
                    for (int j = 0; j < list.size(); j++) {
                        list.get(j).setCount(list.get(j).getCount() - col[j]);
                    }
                    tank.drain(1000, true);
                    break;
                } else {
                    return baseMachineRecipe;
                }
            }
        }
        return null;
    }

    @Override
    public MachineRecipe getRecipeOutputMachineFluid(
            final String name,
            final boolean consume,
            final List<ItemStack> list,
            final FluidTank tank
    ) {
        List<BaseMachineRecipe> recipes = this.map_recipes.get(name);
        int size = this.map_recipe_managers.get(name).getSize();
        for (BaseMachineRecipe baseMachineRecipe : recipes) {
            int[] col = new int[size];
            int[] col1 = new int[size];
            List<Integer> lst = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                lst.add(i);
            }
            List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();
            List<Integer> lst1 = new ArrayList<>();
            if (tank.getFluid() == null) {
                return null;
            }
            if (!tank.getFluid().isFluidEqual(baseMachineRecipe.input.getFluid())) {
                return null;
            }
            if (tank.getFluidAmount() < 1000) {
                return null;
            }

            for (int j = 0; j < list.size(); j++) {
                for (int i = 0; i < recipeInputList.size(); i++) {
                    if (recipeInputList.get(i).matches(list.get(j)) && !lst1.contains(i)) {
                        lst1.add(i);

                        col1[j] = i;
                        break;
                    }
                }
            }
            if (lst.size() == lst1.size()) {
                for (int j = 0; j < list.size(); j++) {
                    ItemStack stack2 = recipeInputList.get(col1[j]).getInputs().get(0);
                    ItemStack stack = list.get(j);
                    if (stack.getCount() < stack2.getCount()) {
                        return null;
                    }
                    col[j] = stack2.getCount();
                }
                if (consume) {
                    for (int j = 0; j < list.size(); j++) {
                        list.get(j).setCount(list.get(j).getCount() - col[j]);
                    }
                    tank.drain(1000, true);
                    break;
                } else {
                    return new MachineRecipe(baseMachineRecipe, Arrays.stream(col)
                            .boxed()
                            .collect(Collectors.toList()));
                }
            }
        }
        return null;
    }


    public void addRecipeList(String name, List<BaseMachineRecipe> list) {
        if (!this.map_recipes.containsKey(name)) {
            this.map_recipes.put(name, list);
        } else {
            this.map_recipes.replace(name, list);
        }
    }

    public void addRecipe(String name, BaseMachineRecipe recipe) {
        if (!this.map_recipes.containsKey(name)) {
            List<BaseMachineRecipe> lst = new ArrayList<>();
            if (name.equals("comb_macerator")) {
                recipe.output.items.get(0).setCount(3);
            }
            lst.add(recipe);
            this.map_recipes.put(name, lst);
        } else {
            if (name.equals("comb_macerator")) {
                recipe.output.items.get(0).setCount(3);
            }
            this.map_recipes.get(name).add(recipe);
        }
    }

    public List<BaseMachineRecipe> getRecipeList(String name) {
        return this.map_recipes.get(name);
    }

    public BaseMachineRecipe getRecipeOutput(
            final IBaseRecipe recipe,
            List<BaseMachineRecipe> recipes,
            boolean adjustInput,
            ItemStack... stacks
    ) {
        List<ItemStack> stack1 = Arrays.asList(stacks);
        int size = recipe.getSize();
        if (size > 1) {
            if (!recipe.require()) {
                for (BaseMachineRecipe baseMachineRecipe : recipes) {
                    int[] col = new int[size];
                    int[] col1 = new int[size];
                    List<Integer> lst = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        lst.add(i);
                    }
                    List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();
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
                            return baseMachineRecipe;
                        }
                    }
                }
            } else {
                for (BaseMachineRecipe baseMachineRecipe : recipes) {
                    List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();

                    for (int i = 0; i < size; i++) {
                        if (!recipeInputList.get(i).matches(stack1.get(i))) {
                            continue;

                        } else {
                            if (!(stack1.get(i).getCount() >= recipeInputList.get(i).getAmount())) {
                                continue;
                            }
                        }
                    }
                    if (adjustInput) {
                        for (int j = 0; j < stack1.size(); j++) {
                            stack1.get(j).setCount(stack1.get(j).getCount() - recipeInputList.get(j).getAmount());
                        }

                    } else {
                        return baseMachineRecipe;
                    }
                }
            }
        } else {
            return getRecipeMultiOutput(recipe, recipes, adjustInput, Arrays.asList(stacks));
        }
        return null;
    }

    @Override
    public MachineRecipe getRecipeMachineRecipeOutput(
            final IBaseRecipe recipe,
            final List<BaseMachineRecipe> recipes,
            final boolean adjustInput,
            final ItemStack... stacks
    ) {
        List<ItemStack> stack1 = Arrays.asList(stacks);
        int size = recipe.getSize();
        if (size > 1) {
            if (!recipe.require()) {
                for (BaseMachineRecipe baseMachineRecipe : recipes) {
                    int[] col = new int[size];
                    int[] col1 = new int[size];
                    List<Integer> lst = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        lst.add(i);
                    }
                    List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();
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
                            return new MachineRecipe(baseMachineRecipe, Arrays.stream(col)
                                    .boxed()
                                    .collect(Collectors.toList()));
                        }
                    }
                }
            } else {
                for (BaseMachineRecipe baseMachineRecipe : recipes) {
                    List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();

                    for (int i = 0; i < size; i++) {
                        if (!recipeInputList.get(i).matches(stack1.get(i))) {
                            continue;

                        } else {
                            if (!(stack1.get(i).getCount() >= recipeInputList.get(i).getAmount())) {
                                continue;
                            }
                        }
                    }
                    List<Integer> integer = new ArrayList<>();
                    for (int j = 0; j < stack1.size(); j++) {
                        integer.add(recipeInputList.get(j).getInputs().get(0).getCount());
                    }
                    if (adjustInput) {
                        for (int j = 0; j < stack1.size(); j++) {
                            stack1.get(j).setCount(stack1.get(j).getCount() - recipeInputList.get(j).getAmount());
                        }

                    } else {
                        return new MachineRecipe(baseMachineRecipe, integer);
                    }
                }
            }
        } else {
            return getRecipeMachineMultiOutput(recipe, recipes, adjustInput, Arrays.asList(stacks));
        }
        return null;
    }

    @Override
    public BaseMachineRecipe getRecipeOutput(final String name, final boolean adjustInput, final ItemStack... stacks) {
        List<ItemStack> stack1 = Arrays.asList(stacks);
        final IBaseRecipe recipe = this.getRecipe(name);
        final List<BaseMachineRecipe> recipes = this.getRecipeList(name);
        int size = recipe.getSize();
        if (size > 1) {
            if (!recipe.require()) {
                for (BaseMachineRecipe baseMachineRecipe : recipes) {
                    int[] col = new int[size];
                    int[] col1 = new int[size];
                    List<Integer> lst = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        lst.add(i);
                    }
                    List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();
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
                            return baseMachineRecipe;
                        }
                    }
                }
            } else {
                for (BaseMachineRecipe baseMachineRecipe : recipes) {
                    List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();

                    for (int i = 0; i < size; i++) {
                        if (!recipeInputList.get(i).matches(stack1.get(i))) {
                            continue;

                        } else {
                            if (!(stack1.get(i).getCount() >= recipeInputList.get(i).getAmount())) {
                                continue;
                            }
                        }
                    }
                    if (adjustInput) {
                        for (int j = 0; j < stack1.size(); j++) {
                            stack1.get(j).setCount(stack1.get(j).getCount() - recipeInputList.get(j).getAmount());
                        }

                    } else {
                        return baseMachineRecipe;
                    }
                }
            }
        } else {
            return getRecipeMultiOutput(recipe, recipes, adjustInput, Arrays.asList(stacks));
        }
        return null;
    }

    @Override
    public MachineRecipe getRecipeMachineOutput(final String name, final boolean adjustInput, final ItemStack... stacks) {
        List<ItemStack> stack1 = Arrays.asList(stacks);
        final IBaseRecipe recipe = this.getRecipe(name);
        final List<BaseMachineRecipe> recipes = this.getRecipeList(name);
        int size = recipe.getSize();
        if (size > 1) {
            if (!recipe.require()) {
                for (BaseMachineRecipe baseMachineRecipe : recipes) {
                    int[] col = new int[size];
                    int[] col1 = new int[size];
                    List<Integer> lst = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        lst.add(i);
                    }
                    List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();
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
                            return new MachineRecipe(baseMachineRecipe, Arrays.stream(col)
                                    .boxed()
                                    .collect(Collectors.toList()));
                        }
                    }
                }
            } else {
                for (BaseMachineRecipe baseMachineRecipe : recipes) {
                    List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();

                    for (int i = 0; i < size; i++) {
                        if (!recipeInputList.get(i).matches(stack1.get(i))) {
                            continue;

                        } else {
                            if (!(stack1.get(i).getCount() >= recipeInputList.get(i).getAmount())) {
                                continue;
                            }
                        }
                    }
                    List<Integer> integer = new ArrayList<>();
                    for (int j = 0; j < stack1.size(); j++) {
                        integer.add(recipeInputList.get(j).getInputs().get(0).getCount());
                    }
                    if (adjustInput) {
                        for (int j = 0; j < stack1.size(); j++) {
                            stack1.get(j).setCount(stack1.get(j).getCount() - recipeInputList.get(j).getAmount());
                        }

                    } else {
                        return new MachineRecipe(baseMachineRecipe, integer);

                    }
                }
            }
        } else {
            return getRecipeMachineMultiOutput(recipe, recipes, adjustInput, Arrays.asList(stacks));
        }
        return null;
    }

    @Override
    public boolean needContinue(final MachineRecipe recipe, final InvSlotRecipes slot) {
        BaseMachineRecipe recipe1 = recipe.getRecipe();
        for (int i = 0; i < recipe1.input.getInputs().size(); i++) {
            if (slot.get(i).isEmpty() || slot.get(i).getCount() < recipe1.input
                    .getInputs()
                    .get(0)
                    .getInputs()
                    .get(0)
                    .getCount()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public BaseMachineRecipe getRecipeOutputFromInstruments(
            final String name,
            final boolean adjustInput,
            final ItemStack... stacks
    ) {
        List<BaseMachineRecipe> recipes = this.map_recipes.get(name);
        List<ItemStack> stack1 = Arrays.asList(stacks);
        final IBaseRecipe recipe = this.map_recipe_managers.get(name);
        int size = recipe.getSize();
        if (!recipe.require()) {
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                int[] col = new int[size];
                int[] col1 = new int[size];
                List<Integer> lst = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    lst.add(i);
                }
                List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();
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
                        return baseMachineRecipe;
                    }
                }
            }
        } else {
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();

                for (int i = 0; i < size; i++) {
                    if (!recipeInputList.get(i).matches(stack1.get(i))) {
                        continue;

                    } else {
                        if (!(stack1.get(i).getCount() >= recipeInputList.get(i).getAmount())) {
                            continue;
                        }
                    }
                }
                if (adjustInput) {
                    for (int j = 0; j < stack1.size(); j++) {
                        stack1.get(j).setCount(stack1.get(j).getCount() - recipeInputList.get(j).getAmount());
                    }

                } else {
                    return baseMachineRecipe;
                }
            }
        }

        return null;
    }

    @Override
    public BaseMachineRecipe getRecipeOutput(
            final IBaseRecipe recipe, List<BaseMachineRecipe> recipes,
            final boolean adjustInput, final List<ItemStack> stacks
    ) {

        int size = recipe.getSize();
        if (!recipe.require()) {
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                int[] col = new int[size];
                int[] col1 = new int[size];
                List<Integer> lst = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    lst.add(i);
                }
                List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();
                List<Integer> lst1 = new ArrayList<>();
                for (int j = 0; j < stacks.size(); j++) {
                    for (int i = 0; i < recipeInputList.size(); i++) {
                        if (recipeInputList.get(i).matches(stacks.get(j)) && !lst1.contains(i)) {
                            lst1.add(i);

                            col1[j] = i;
                            break;
                        }
                    }
                }
                if (lst.size() == lst1.size()) {
                    for (int j = 0; j < stacks.size(); j++) {
                        ItemStack stack2 = recipeInputList.get(col1[j]).getInputs().get(0);
                        ItemStack stack = stacks.get(j);
                        if (stack.getCount() < stack2.getCount()) {
                            return null;
                        }
                        col[j] = stack2.getCount();
                    }
                    if (adjustInput) {
                        for (int j = 0; j < stacks.size(); j++) {
                            stacks.get(j).setCount(stacks.get(j).getCount() - col[j]);
                        }
                        break;
                    } else {
                        return baseMachineRecipe;
                    }
                }
            }
        } else {
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();

                for (int i = 0; i < size; i++) {
                    if (!recipeInputList.get(i).matches(stacks.get(i))) {
                        continue;

                    } else {
                        if (!(stacks.get(i).getCount() >= recipeInputList.get(i).getAmount())) {
                            continue;
                        }
                    }
                }
                if (adjustInput) {
                    for (int j = 0; j < stacks.size(); j++) {
                        stacks.get(j).setCount(stacks.get(j).getCount() - recipeInputList.get(j).getAmount());
                    }

                } else {
                    return baseMachineRecipe;
                }
            }
        }

        return null;
    }

    @Override
    public MachineRecipe getRecipeMachineRecipeOutput(
            final IBaseRecipe recipe,
            final List<BaseMachineRecipe> recipes,
            final boolean adjustInput,
            final List<ItemStack> stacks
    ) {
        int size = recipe.getSize();
        if (!recipe.require()) {
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                int[] col = new int[size];
                int[] col1 = new int[size];
                List<Integer> lst = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    lst.add(i);
                }
                List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();
                List<Integer> lst1 = new ArrayList<>();
                for (int j = 0; j < stacks.size(); j++) {
                    for (int i = 0; i < recipeInputList.size(); i++) {
                        if (recipeInputList.get(i).matches(stacks.get(j)) && !lst1.contains(i)) {
                            lst1.add(i);

                            col1[j] = i;
                            break;
                        }
                    }
                }
                if (lst.size() == lst1.size()) {
                    for (int j = 0; j < stacks.size(); j++) {
                        ItemStack stack2 = recipeInputList.get(col1[j]).getInputs().get(0);
                        ItemStack stack = stacks.get(j);
                        if (stack.getCount() < stack2.getCount()) {
                            return null;
                        }
                        col[j] = stack2.getCount();
                    }
                    if (adjustInput) {
                        for (int j = 0; j < stacks.size(); j++) {
                            stacks.get(j).setCount(stacks.get(j).getCount() - col[j]);
                        }
                        break;
                    } else {
                        return new MachineRecipe(baseMachineRecipe, Arrays.stream(col)
                                .boxed()
                                .collect(Collectors.toList()));
                    }
                }
            }
        } else {
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();

                for (int i = 0; i < size; i++) {
                    if (!recipeInputList.get(i).matches(stacks.get(i))) {
                        continue;

                    } else {
                        if (!(stacks.get(i).getCount() >= recipeInputList.get(i).getAmount())) {
                            continue;
                        }
                    }
                }
                List<Integer> integer = new ArrayList<>();
                for (int j = 0; j < stacks.size(); j++) {
                    integer.add(recipeInputList.get(j).getInputs().get(0).getCount());
                }
                if (adjustInput) {
                    for (int j = 0; j < stacks.size(); j++) {
                        stacks.get(j).setCount(stacks.get(j).getCount() - recipeInputList.get(j).getAmount());
                    }

                } else {
                    return new MachineRecipe(baseMachineRecipe, integer);
                }
            }
        }

        return null;
    }

    @Override
    public BaseMachineRecipe getRecipeMultiOutput(
            final IBaseRecipe recipe,
            List<BaseMachineRecipe> recipes,
            final boolean adjustInput,
            final List<ItemStack> stacks
    ) {
        int size = recipe.getSize();
        if (!recipe.require()) {
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                int[] col = new int[size];
                int[] col1 = new int[size];
                List<Integer> lst = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    lst.add(i);
                }
                List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();
                List<Integer> lst1 = new ArrayList<>();
                for (int j = 0; j < stacks.size(); j++) {
                    for (int i = 0; i < recipeInputList.size(); i++) {
                        if (recipeInputList.get(i).matches(stacks.get(j)) && !lst1.contains(i)) {
                            lst1.add(i);

                            col1[j] = i;
                            break;
                        }
                    }
                }
                if (lst.size() == lst1.size()) {
                    for (int j = 0; j < stacks.size(); j++) {
                        ItemStack stack2 = recipeInputList.get(col1[j]).getInputs().get(0);
                        ItemStack stack = stacks.get(j);

                        col[j] = stack2.getCount();
                    }
                    if (adjustInput) {
                        for (int j = 0; j < stacks.size(); j++) {
                            stacks.get(j).setCount(stacks.get(j).getCount() - col[j]);
                        }
                        break;
                    } else {
                        return baseMachineRecipe;
                    }
                }
            }
        } else {
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();

                for (int i = 0; i < size; i++) {
                    if (!recipeInputList.get(i).matches(stacks.get(i))) {
                        continue;

                    } else {

                    }
                }
                if (adjustInput) {
                    for (int j = 0; j < stacks.size(); j++) {
                        stacks.get(j).setCount(stacks.get(j).getCount() - recipeInputList.get(j).getAmount());
                    }

                } else {
                    return baseMachineRecipe;
                }
            }
        }

        return null;
    }

    @Override
    public MachineRecipe getRecipeMachineMultiOutput(
            final IBaseRecipe recipe,
            final List<BaseMachineRecipe> recipes,
            final boolean adjustInput,
            final List<ItemStack> stacks
    ) {
        int size = recipe.getSize();
        if (!recipe.require()) {
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                int[] col = new int[size];
                int[] col1 = new int[size];
                List<Integer> lst = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    lst.add(i);
                }
                List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();
                List<Integer> lst1 = new ArrayList<>();
                for (int j = 0; j < stacks.size(); j++) {
                    for (int i = 0; i < recipeInputList.size(); i++) {
                        if (recipeInputList.get(i).matches(stacks.get(j)) && !lst1.contains(i)) {
                            lst1.add(i);

                            col1[j] = i;
                            break;
                        }
                    }
                }
                if (lst.size() == lst1.size()) {
                    for (int j = 0; j < stacks.size(); j++) {
                        ItemStack stack2 = recipeInputList.get(col1[j]).getInputs().get(0);

                        col[j] = stack2.getCount();
                    }
                    if (adjustInput) {
                        for (int j = 0; j < stacks.size(); j++) {
                            stacks.get(j).setCount(stacks.get(j).getCount() - col[j]);
                        }
                        break;
                    } else {
                        return new MachineRecipe(baseMachineRecipe, Arrays.stream(col)
                                .boxed()
                                .collect(Collectors.toList()));
                    }
                }
            }
        } else {
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();

                for (int i = 0; i < size; i++) {
                    if (!recipeInputList.get(i).matches(stacks.get(i))) {
                        continue;

                    } else {

                    }
                }
                List<Integer> integer = new ArrayList<>();
                for (int j = 0; j < stacks.size(); j++) {
                    integer.add(recipeInputList.get(j).getInputs().get(0).getCount());
                }
                if (adjustInput) {
                    for (int j = 0; j < stacks.size(); j++) {
                        stacks.get(j).setCount(stacks.get(j).getCount() - recipeInputList.get(j).getAmount());
                    }

                } else {
                    return new MachineRecipe(baseMachineRecipe, integer);
                }
            }
        }

        return null;
    }

}
