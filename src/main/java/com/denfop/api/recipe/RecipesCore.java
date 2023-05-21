package com.denfop.api.recipe;

import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipesCore implements IRecipes {

    private final List<IHasRecipe> recipes = new ArrayList<>();
    private final RecipesFluidCore fluid_recipe;
    public Map<String, List<BaseMachineRecipe>> map_recipes = new HashMap<>();
    public Map<String, IBaseRecipe> map_recipe_managers = new HashMap<>();
    public Map<String, List<IRecipeInputStack>> map_recipe_managers_itemStack = new HashMap<>();

    public Map<String, List<Fluid>> map_fluid_input = new HashMap<>();

    public RecipesCore() {
        init();
        this.fluid_recipe = new RecipesFluidCore();
        fluid_recipe.init();
    }

    public List<Fluid> getInputFluidsFromRecipe(String name) {
        return map_fluid_input.get(name);
    }

    public void init() {
        this.addRecipeManager("alloysmelter", 2, true);
        this.addRecipeManager("enrichment", 2, true);
        this.addRecipeManager("painter", 2, true);
        this.addRecipeManager("sunnuriumpanel", 2, true);
        this.addRecipeManager("synthesis", 2, true);
        this.addRecipeManager("upgradeblock", 2, true);
        this.addRecipeManager("antiupgradeblock", 1, true);
        this.addRecipeManager("advalloysmelter", 3, true);
        this.addRecipeManager("genstone", 2, false);
        this.addRecipeManager("microchip", 5, true);
        this.addRecipeManager("sunnurium", 4, true);
        this.addRecipeManager("wither", 7, true, true);
        this.addRecipeManager("doublemolecular", 2, true);
        this.addRecipeManager("molecular", 1, true);
        this.addRecipeManager("plastic", 2, true);
        this.addRecipeManager("plasticplate", 1, true);
        this.addRecipeManager("converter", 1, false, true);
        this.addRecipeManager("macerator", 1, true, true);
        this.addRecipeManager("compressor", 1, true, true);
        this.addRecipeManager("extractor", 1, true, true);
        this.addRecipeManager("recycler", 1, true, true);
        this.addRecipeManager("furnace", 1, true, true);
        this.addRecipeManager("extruding", 1, true, true);
        this.addRecipeManager("cutting", 1, true, true);
        this.addRecipeManager("rolling", 1, true, true);
        this.addRecipeManager("farmer", 1, true, true);
        this.addRecipeManager("scrap", 1, true, true);
        this.addRecipeManager("comb_macerator", 1, true, true);
        this.addRecipeManager("handlerho", 1, true, true);
        this.addRecipeManager("matter", 1, false, false);
        this.addRecipeManager("rotor_assembler", 5, true, true);
        this.addRecipeManager("rod_assembler", 6, true, true);
        this.addRecipeManager("rotor_upgrade", 2, false, true);

        this.addRecipeManager("endcollector", 1, true, true);
        this.addRecipeManager("aercollector", 1, true, true);
        this.addRecipeManager("earthcollector", 1, true, true);
        this.addRecipeManager("nethercollector", 1, true, true);
        this.addRecipeManager("aquacollector", 1, true, true);
        this.addRecipeManager("defaultcollector", 1, true, true);
        this.addRecipeManager("orewashing", 1, true, false);
        this.addRecipeManager("centrifuge", 1, true, false);
        this.addRecipeManager("gearing", 1, true, false);
        this.addRecipeManager("water_rotor_assembler", 5, true, true);
        this.addRecipeManager("water_rotor_upgrade", 2, false, true);
        this.addRecipeManager("welding", 2, true, true);
        this.addRecipeManager("replicator", 1, false, false);

        this.addRecipeManager("cannerbottle", 2, true, true);
        this.addRecipeManager("cannerenrich", 2, true, true);
        this.addRecipeManager("empty", 1, false, true);

    }

    public IBaseRecipe getRecipe(String name) {
        return this.map_recipe_managers.get(name);
    }

    public void addRecipeManager(String name, int size, boolean consume) {
        this.map_recipe_managers.put(name, new RecipeManager(name, size, consume));
    }


    @Override
    public RecipesFluidCore getRecipeFluid() {
        return fluid_recipe;
    }

    public void addRecipeManager(String name, int size, boolean consume, boolean require) {
        this.map_recipe_managers.put(name, new RecipeManager(name, size, consume, require));
        if (!this.map_recipes.containsKey(name)) {
            List<BaseMachineRecipe> lst = new ArrayList<>();
            List<IRecipeInputStack> lst1 = new ArrayList<>();
            this.map_recipes.put(name, lst);
            this.map_recipe_managers_itemStack.put(name, lst1);
        }
    }

    public void removeRecipe(String name, RecipeOutput output) {
        List<BaseMachineRecipe> recipes = this.map_recipes.get(name);
        BaseMachineRecipe deleteRecipe = null;
        for(BaseMachineRecipe recipe : recipes){
            for(ItemStack stack : output.items){
                for(ItemStack output_stack : recipe.output.items){
                    if(stack.isItemEqual(output_stack)) {
                        deleteRecipe = recipe;
                        break;
                    }
                }
            }
        }
        if(deleteRecipe != null){
            recipes.remove(deleteRecipe);
            final List<IRecipeInputStack> list = this.map_recipe_managers_itemStack.get(name);
            IInput input = deleteRecipe.input;
            final List<IRecipeInput> list2 = input.getInputs();
            for(IRecipeInput input1 : list2){
                IRecipeInputStack iRecipeInputStack = new RecipeInputStack(input1);
                list.remove(iRecipeInputStack);
            }

        }

    }

    public void removeRecipe(String name, ItemStack input) {
        List<BaseMachineRecipe> recipes = this.map_recipes.get(name);
        recipes.removeIf(recipe -> recipe.input.getInputs().get(0).matches(input));

    }

    @Override
    public BaseMachineRecipe getRecipeOutputFluid(
            final IBaseRecipe recipe,
            final MachineRecipe recipeOutput,
            final boolean consume,
            final List<ItemStack> list,
            final FluidTank tank
    ) {
        if (recipeOutput == null) {
            return null;
        }
        List<Integer> list1 = recipeOutput.getList();

        if (consume) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).shrink(list1.get(i));
            }
            tank.drain(recipeOutput.getRecipe().input.getFluid(), true);
        }
        return recipeOutput.getRecipe();
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
                    tank.drain(baseMachineRecipe.input.getFluid(), true);
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
            final IBaseRecipe recipe,
            final List<BaseMachineRecipe> recipe_list,
            final boolean adjustInput,
            final List<ItemStack> list,
            final FluidTank tank
    ) {
        int size = recipe.getSize();

        for (BaseMachineRecipe baseMachineRecipe : recipe_list) {
            if (!recipe.require()) {

                int[] col = new int[size];
                int[] col1 = new int[size];
                List<Integer> lst = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    lst.add(i);
                }
                List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();
                List<Integer> lst1 = new ArrayList<>();
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
                        tank.drain(baseMachineRecipe.input.getFluid(), true);
                        break;
                    } else {
                        return new MachineRecipe(baseMachineRecipe, Arrays.stream(col)
                                .boxed()
                                .collect(Collectors.toList()));
                    }
                }
            } else {
                List<IRecipeInput> recipeInputList = baseMachineRecipe.input.getInputs();

                boolean need = true;
                for (int i = 0; i < size; i++) {
                    if (!recipeInputList.get(i).matches(list.get(i))) {
                        need = true;
                        break;

                    }
                    if (recipeInputList.get(i).getInputs().get(0).getCount() > list.get(i).getCount()) {
                        need = true;
                        break;
                    }
                    need = false;
                }

                if (need) {
                    continue;
                }
                if (baseMachineRecipe.input.hasFluids() && tank.getFluid() == null) {
                    continue;
                }
                if (baseMachineRecipe.input.hasFluids() && tank.getFluidAmount() < 1000) {
                    continue;
                }
                if (baseMachineRecipe.input.hasFluids() && !tank.getFluid().isFluidEqual(baseMachineRecipe.input.getFluid())) {
                    continue;
                }
                List<Integer> integer = new ArrayList<>();
                for (int j = 0; j < list.size(); j++) {
                    integer.add(recipeInputList.get(j).getInputs().get(0).getCount());
                }
                if (adjustInput) {
                    for (int j = 0; j < list.size(); j++) {
                        list.get(j).setCount(list.get(j).getCount() - recipeInputList.get(j).getAmount());
                    }
                    tank.drain(baseMachineRecipe.input.getFluid(), true);
                    break;
                } else {

                    return new MachineRecipe(baseMachineRecipe, integer);
                }
            }
        }
        return null;
    }

    @Override
    public void addInitRecipes(final IHasRecipe hasRecipe) {
        this.recipes.add(hasRecipe);
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
                    tank.drain(baseMachineRecipe.input.getFluid(), true);
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

    public List<IRecipeInputStack> getMap_recipe_managers_itemStack(String name) {

        return map_recipe_managers_itemStack.get(name);
    }

    @Override
    public List<IHasRecipe> getRecipesForInit() {
        return this.recipes;
    }

    public void addRecipe(String name, BaseMachineRecipe recipe) {
        if (!this.map_recipes.containsKey(name)) {
            List<IRecipeInput> iRecipeInputList = recipe.input.getInputs();
            List<IRecipeInputStack> inputStackList = new ArrayList<>();
            for (IRecipeInput recipeInput : iRecipeInputList) {
                inputStackList.add(new RecipeInputStack(recipeInput));
            }

            this.map_recipe_managers_itemStack.put(name, inputStackList);
            if (recipe.input.hasFluids()) {
                List<Fluid> fluidStackList = new ArrayList<>();
                fluidStackList.add(recipe.input.getFluid().getFluid());
                map_fluid_input.put(name, fluidStackList);
            }
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
            List<IRecipeInputStack> iRecipeInputList = this.map_recipe_managers_itemStack.get(name);
            if (recipe.input.hasFluids()) {
                List<Fluid> fluidStackList = map_fluid_input.get(name);
                if (fluidStackList != null) {
                    fluidStackList.add(recipe.input.getFluid().getFluid());
                } else {
                    fluidStackList = new ArrayList<>();
                    fluidStackList.add(recipe.input.getFluid().getFluid());
                    map_fluid_input.put(name, fluidStackList);
                }
            }
            if (iRecipeInputList.isEmpty()) {
                for (IRecipeInput input1 : recipe.input.getInputs()) {
                    iRecipeInputList.add(new RecipeInputStack(input1));
                }
            } else {
                for (IRecipeInput input1 : recipe.input.getInputs()) {
                    for (ItemStack stack : input1.getInputs()) {
                        boolean continues1 = false;
                        for (IRecipeInputStack input : iRecipeInputList) {
                            if (!input.matched(stack)) {
                                iRecipeInputList.add(new RecipeInputStack(input1));
                                continues1 = true;
                                break;
                            }

                        }
                        if (continues1) {
                            break;
                        }
                    }
                }
            }

            this.map_recipe_managers_itemStack.replace(name, iRecipeInputList);
            this.map_recipes.get(name).add(recipe);
        }
    }

    public List<BaseMachineRecipe> getRecipeList(String name) {
        return this.map_recipes.get(name);
    }

    @Override
    public Set<Map.Entry<ItemStack, BaseMachineRecipe>> getRecipeStack(final String name) {
        final List<BaseMachineRecipe> list = this.map_recipes.get(name);
        Map<ItemStack, BaseMachineRecipe> baseMachineRecipeMap = new HashMap<>();
        for (BaseMachineRecipe baseMachineRecipe : list) {
            baseMachineRecipeMap.put(baseMachineRecipe.input.getInputs().get(0).getInputs().get(0), baseMachineRecipe);
        }
        return baseMachineRecipeMap.entrySet();
    }

    @Override
    public void removeAll(final String recipe) {
        map_recipes.remove(recipe);
        map_recipe_managers_itemStack.remove(recipe);
    }

    @Override
    public void reloadRecipes(final String className) {

        this.recipes.forEach(iHasRecipe -> {
                    if (className.equals(iHasRecipe.getClass().getName())) {
                        iHasRecipe.init();
                    }

                }

        );
    }

    @Override
    public void initializationRecipes() {
        this.recipes.forEach(IHasRecipe::init);
        this.fluid_recipe.initializationRecipes();
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

                    boolean need = true;
                    for (int i = 0; i < size; i++) {
                        if (!recipeInputList.get(i).matches(stack1.get(i))) {
                            need = true;
                            break;

                        }
                        if (recipeInputList.get(i).getInputs().get(0).getCount() > stack1.get(i).getCount()) {
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

                    boolean need = true;
                    for (int i = 0; i < size; i++) {
                        if (!recipeInputList.get(i).matches(stack1.get(i))) {
                            need = true;
                            break;

                        }
                        if (recipeInputList.get(i).getInputs().get(0).getCount() > stack1.get(i).getCount()) {
                            need = true;
                            break;
                        }
                        need = false;
                    }
                    if (need) {
                        continue;
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

                    boolean need = true;
                    for (int i = 0; i < size; i++) {
                        if (!recipeInputList.get(i).matches(stack1.get(i))) {
                            need = true;
                            break;

                        }
                        if (recipeInputList.get(i).getInputs().get(0).getCount() > stack1.get(i).getCount()) {
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

                    boolean need = true;
                    for (int i = 0; i < size; i++) {
                        if (!recipeInputList.get(i).matches(stack1.get(i))) {
                            need = true;
                            break;

                        }
                        if (recipeInputList.get(i).getInputs().get(0).getCount() > stack1.get(i).getCount()) {
                            need = true;
                            break;
                        }
                        need = false;
                    }
                    if (need) {
                        continue;
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
        if (recipe == null) {
            return false;
        }
        BaseMachineRecipe recipe1 = recipe.getRecipe();
        for (int i = 0; i < recipe1.input.getInputs().size(); i++) {
            if (slot.get(i).isEmpty() || slot.get(i).getCount() < recipe1.input
                    .getInputs()
                    .get(i)
                    .getInputs()
                    .get(0)
                    .getCount()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean needContinue(final MachineRecipe recipe, final InvSlotRecipes slot, final FluidTank tank) {
        if (recipe == null) {
            return false;
        }
        BaseMachineRecipe recipe1 = recipe.getRecipe();
        for (int i = 0; i < recipe1.input.getInputs().size(); i++) {
            if (slot.get(i).isEmpty() || slot.get(i).getCount() < recipe1.input
                    .getInputs()
                    .get(i)
                    .getInputs()
                    .get(0)
                    .getCount()) {
                return false;
            }
        }
        return !recipe.getRecipe().input.hasFluids() || (tank.getFluid() != null && tank
                .getFluid()
                .getFluid()
                .equals(recipe1.input
                        .getFluid()
                        .getFluid()) && tank.getFluidAmount() >= recipe1.input.getFluid().amount);
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

                boolean need = true;
                for (int i = 0; i < size; i++) {
                    if (!recipeInputList.get(i).matches(stack1.get(i))) {
                        need = true;
                        break;

                    }
                    if (recipeInputList.get(i).getInputs().get(0).getCount() > stack1.get(i).getCount()) {
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
                        stack1.get(j).setCount(stack1.get(j).getCount() - recipeInputList.get(j).getAmount());
                    }
                    break;
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

                boolean need = true;
                for (int i = 0; i < size; i++) {
                    if (!recipeInputList.get(i).matches(stacks.get(i))) {
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
                        stacks.get(j).setCount(stacks.get(j).getCount() - recipeInputList.get(j).getAmount());
                    }
                    break;
                } else {
                    return baseMachineRecipe;
                }
            }
        }

        return null;
    }

    @Override
    public BaseMachineRecipe getRecipeConsume(
            final IBaseRecipe recipe, MachineRecipe recipes,
            final boolean adjustInput, final List<ItemStack> stacks
    ) {
        List<Integer> integerList = recipes.getList();

        if (adjustInput) {
            for (int i = 0; i < stacks.size(); i++) {
                stacks.get(i).shrink(integerList.get(i));
            }
        }


        return recipes.getRecipe();
    }

    @Override
    public MachineRecipe getMachineRecipeConsume(
            final IBaseRecipe recipe, MachineRecipe recipes,
            final boolean adjustInput, final List<ItemStack> stacks
    ) {
        if (recipes == null) {
            return null;
        }
        List<Integer> integerList = recipes.getList();

        if (adjustInput) {
            for (int i = 0; i < stacks.size(); i++) {
                stacks.get(i).shrink(integerList.get(i));
            }
        }


        return recipes;
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
                boolean need = true;
                for (int i = 0; i < size; i++) {
                    if (!recipeInputList.get(i).matches(stacks.get(i))) {
                        need = true;
                        break;

                    }

                    need = false;
                }
                if (need) {
                    continue;
                }
                List<Integer> integer = new ArrayList<>();
                for (int j = 0; j < size; j++) {
                    integer.add(recipeInputList.get(j).getInputs().get(0).getCount());
                }
                if (adjustInput) {
                    for (int j = 0; j < stacks.size(); j++) {
                        stacks.get(j).setCount(stacks.get(j).getCount() - recipeInputList.get(j).getAmount());
                    }
                    break;
                }
                return new MachineRecipe(baseMachineRecipe, integer);
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

                if (!recipeInputList.get(0).matches(stacks.get(0))) {
                    continue;

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


                if (!recipeInputList.get(0).matches(stacks.get(0))) {
                    continue;

                }


                List<Integer> integer = new ArrayList<>();
                for (int j = 0; j < stacks.size(); j++) {
                    integer.add(recipeInputList.get(j).getInputs().get(0).getCount());
                }
                if (adjustInput) {
                    for (int j = 0; j < stacks.size(); j++) {
                        stacks.get(j).setCount(stacks.get(j).getCount() - recipeInputList.get(j).getAmount());
                    }
                    break;
                } else {
                    return new MachineRecipe(baseMachineRecipe, integer);
                }
            }
        }

        return null;
    }

}
