package com.denfop.api.recipe;

import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RecipesCore implements IRecipes {

    private final List<IHasRecipe> recipes = new LinkedList<>();
    private final List<String> registeredRecipes = new LinkedList<>();
    private final RecipesFluidCore fluid_recipe;
    public Map<String, List<BaseMachineRecipe>> map_recipes = new HashMap<>();
    public Map<String, IBaseRecipe> map_recipe_managers = new HashMap<>();
    public Map<String, RecipeArrayList<IRecipeInputStack>> map_recipe_managers_itemStack = new HashMap<>();

    public Map<String, List<Fluid>> map_fluid_input = new HashMap<>();
    public List<RecipeRemove> recipeRemoves = new ArrayList<>();
    public List<RecipeFluidRemove> recipeFluidRemoves = new ArrayList<>();

    public List<RecipeFluidAdder> recipeFluidAdders = new ArrayList<>();
    public List<RecipeAdder> recipeAdders = new ArrayList<>();

    public boolean canAdd = true;

    public RecipesCore() {
        init();
        this.fluid_recipe = new RecipesFluidCore();
        fluid_recipe.init();
    }

    public List<String> getMap_recipe_managers() {
        return map_recipe_managers.keySet().stream().toList();
    }

    public List<Fluid> getInputFluidsFromRecipe(String name) {
        return map_fluid_input.getOrDefault(name, Collections.emptyList());
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
        this.addRecipeManager("matterAmplifier", 1, true, true);
        this.addRecipeManager("scrapbox", 1, true, true);
        this.addRecipeManager("battery_factory", 9, true, true);
        this.addRecipeManager("socket_factory", 6, true, true);
        this.addRecipeManager("active_matter_factory", 1, true, true);
        this.addRecipeManager("laser", 1, true, true);
        this.addRecipeManager("graphite_recipe", 2, true, true);
        this.addRecipeManager("silicon_recipe", 2, true, true);
        this.addRecipeManager("solar_glass_recipe", 1, true, true);

        this.addRecipeManager("stamp_vent", 4, true, false);
        this.addRecipeManager("stamp_plate", 4, true, false);
        this.addRecipeManager("stamp_exchanger", 4, true, false);
        this.addRecipeManager("stamp_coolant", 4, true, false);
        this.addRecipeManager("stamp_capacitor", 4, true, false);


        this.addRecipeManager("reactor_simple_rod", 5, true, true);
        this.addRecipeManager("reactor_dual_rod", 3, true, true);
        this.addRecipeManager("reactor_quad_rod", 7, true, true);

        this.addRecipeManager("waste_recycler", 1, true, true);
        this.addRecipeManager("enchanter_books", 2, true, true);
        this.addRecipeManager("anvil", 1, true, true);
        this.addRecipeManager("upgrade_machine", 9, true, true, true);
        this.addRecipeManager("item_divider", 1, true, true);
        this.addRecipeManager("item_divider_fluid", 1, true, true);
        this.addRecipeManager("fluid_adapter", 2, true, false);
        this.addRecipeManager("fluid_integrator", 1, true, true);
        this.addRecipeManager("solid_electrolyzer", 1, true, true);
        this.addRecipeManager("squeezer", 1, true, true);

        this.addRecipeManager("impalloysmelter", 4, true);
        this.addRecipeManager("peralloysmelter", 5, true);
        this.addRecipeManager("solid_fluid_mixer", 1, true, true);
        this.addRecipeManager("primal_fluid_integrator", 1, true, true);
        this.addRecipeManager("neutron_separator", 1, true, true);
        this.addRecipeManager("positrons", 2, true, false);
        this.addRecipeManager("primal_laser_polisher", 1, true, false);
        this.addRecipeManager("cyclotron", 1, true, false);
        this.addRecipeManager("smeltery", 1, true, false);
        this.addRecipeManager("solid_mixer", 2, true, false);
        this.addRecipeManager("triple_solid_mixer", 3, true, false);
        this.addRecipeManager("single_fluid_adapter", 1, true, false);
        this.addRecipeManager("programming", 1, true, false);
        this.addRecipeManager("electronics", 5, true, false);
        this.addRecipeManager("sharpener", 1, true, false);
        this.addRecipeManager("radioactive_handler", 1, true, false);
        this.addRecipeManager("ore_purifier", 1, true, false);
        this.addRecipeManager("quantummolecular", 2, true, true);
        this.addRecipeManager("wire_insulator", 2, true, true);
        this.addRecipeManager("charger", 1, true, true);
        this.addRecipeManager("biomass", 1, true, true);
        this.addRecipeManager("strong_anvil", 1, true, true);
        this.addRecipeManager("refractory_furnace", 1, true, true);
        this.addRecipeManager("elec_refractory_furnace", 1, true, true);
        this.addRecipeManager("brewing", 2, true, false);
        this.addRecipeManager("sawmill", 1, true, false);
        this.addRecipeManager("genadditionstone", 2, true, false);
        this.addRecipeManager("incubator", 1, true, false);
        this.addRecipeManager("insulator", 1, true, false);
        this.addRecipeManager("rna_collector", 1, true, false);
        this.addRecipeManager("genetic_stabilizer", 1, true, false);
        this.addRecipeManager("genetic_centrifuge", 1, true, false);
        this.addRecipeManager("inoculator", 2, true, false);
        this.addRecipeManager("genetic_transposer", 4, true, false);
        this.addRecipeManager("genetic_polymerizer", 5, true, true);
        this.addRecipeManager("roverupgradeblock", 2, true, true);
        this.addRecipeManager("roverassembler", 40, true, true);
        this.addRecipeManager("probeassembler", 36, true, true);
        this.addRecipeManager("satelliteassembler", 36, true, true);
        this.addRecipeManager("rocketassembler", 37, true, true);


    }

    public void setCanAdd(final boolean canAdd) {
        this.canAdd = canAdd;
    }

    public void addRemoveRecipe(String name, ItemStack stack, boolean allRemove) {
        this.recipeRemoves.add(new RecipeRemove(name, stack, allRemove));
    }

    public void addRemoveRecipe(String name, ItemStack stack) {
        this.recipeRemoves.add(new RecipeRemove(name, stack, false));
    }

    public void addFluidRemoveRecipe(String name, FluidStack stack) {
        this.recipeFluidRemoves.add(new RecipeFluidRemove(name, stack, false));
    }

    public void addFluidRemoveRecipe(String name, FluidStack stack, boolean removeAll) {
        this.recipeFluidRemoves.add(new RecipeFluidRemove(name, stack, removeAll));
    }

    public void removeAllRecipesFromList() {
        this.recipeRemoves.forEach(recipeRemove -> {
            if (recipeRemove.isRemoveAll())
                this.removeAllRecipe(recipeRemove.getNameRecipe(), new RecipeOutput(null, recipeRemove.getStack()));
            else
                this.removeRecipe(recipeRemove.getNameRecipe(), new RecipeOutput(null, recipeRemove.getStack()));

        });

        this.recipeFluidRemoves.forEach(recipeRemove -> {
            this.getRecipeFluid().removeAllRecipe(recipeRemove.getNameRecipe(), recipeRemove.isRemoveAll(), recipeRemove.getStack());

        });
    }

    @Override
    public void addAdderRecipe(final String name, final BaseMachineRecipe baseMachineRecipe) {
        this.recipeAdders.add(new RecipeAdder(name, baseMachineRecipe));
    }

    @Override
    public void addFluidAdderRecipe(final String name, final BaseFluidMachineRecipe baseMachineRecipe) {
        this.recipeFluidAdders.add(new RecipeFluidAdder(name, baseMachineRecipe));
    }

    @Override
    public void addAllRecipesFromList() {
        this.recipeAdders.forEach(recipeAdder -> {
            this.addRecipe(recipeAdder.getName(), recipeAdder.getBaseMachineRecipe());
        });
        this.recipeFluidAdders.forEach(recipeAdder -> {
            this.getRecipeFluid().addRecipe(recipeAdder.getName(), recipeAdder.getBaseMachineRecipe());
        });
    }

    public IBaseRecipe getRecipe(String name) {
        return this.map_recipe_managers.get(name);
    }

    public void addRecipeManager(String name, int size, boolean consume) {
        this.map_recipe_managers.put(name, new RecipeManager(name, size, consume));
    }

    public List<String> getRecipes() {
        return map_recipes.keySet().stream().toList();
    }

    @Override
    public RecipesFluidCore getRecipeFluid() {
        return fluid_recipe;
    }

    public void addRecipeManager(String name, int size, boolean consume, boolean require) {
        this.map_recipe_managers.put(name, new RecipeManager(name, size, consume, require));
        if (!this.map_recipes.containsKey(name)) {
            List<BaseMachineRecipe> lst = new LinkedList<>();
            RecipeArrayList<IRecipeInputStack> lst1 = new RecipeArrayList<>();
            this.map_recipes.put(name, lst);
            this.map_recipe_managers_itemStack.put(name, lst1);
        }
    }

    public void addRecipeManager(String name, int size, boolean consume, boolean require, boolean workbench) {
        this.map_recipe_managers.put(name, new RecipeManager(name, size, consume, require, workbench));
        if (!this.map_recipes.containsKey(name)) {
            List<BaseMachineRecipe> lst = new LinkedList<>();
            RecipeArrayList<IRecipeInputStack> lst1 = new RecipeArrayList<>();
            this.map_recipes.put(name, lst);
            this.map_recipe_managers_itemStack.put(name, lst1);
        }
    }

    public void removeRecipe(String name, RecipeOutput output) {
        List<BaseMachineRecipe> recipes = this.map_recipes.getOrDefault(name, new ArrayList<>());
        BaseMachineRecipe deleteRecipe = null;
        for (BaseMachineRecipe recipe : recipes) {
            for (ItemStack stack : output.items) {
                for (ItemStack output_stack : recipe.output.items) {
                    if (ModUtils.checkItemEquality(output_stack, stack)) {
                        deleteRecipe = recipe;
                        break;
                    }
                }
                if (deleteRecipe != null) {
                    break;
                }
            }
            if (deleteRecipe != null) {
                break;
            }
        }
        if (deleteRecipe != null) {
            recipes.remove(deleteRecipe);
            final List<IRecipeInputStack> list = this.map_recipe_managers_itemStack.get(name);
            IInput input = deleteRecipe.input;
            final List<IInputItemStack> list2 = input.getInputs();
            for (IInputItemStack input1 : list2) {
                IRecipeInputStack iRecipeInputStack = new RecipeInputStack(input1);
                list.remove(iRecipeInputStack);
            }

        }

    }

    public void removeAllRecipe(String name, RecipeOutput output) {
        List<BaseMachineRecipe> recipes = this.map_recipes.get(name);
        List<BaseMachineRecipe> deleteRecipes = new ArrayList<>();
        for (BaseMachineRecipe recipe : recipes) {
            boolean find = false;
            for (ItemStack stack : output.items) {
                for (ItemStack output_stack : recipe.output.items) {
                    if (ModUtils.checkItemEquality(output_stack, stack)) {
                        deleteRecipes.add(recipe);
                        find = true;
                        break;
                    }
                }
                if (find) {
                    break;
                }
            }

        }
        for (BaseMachineRecipe deleteRecipe : deleteRecipes) {
            recipes.remove(deleteRecipe);
            final List<IRecipeInputStack> list = this.map_recipe_managers_itemStack.get(name);
            IInput input = deleteRecipe.input;
            final List<IInputItemStack> list2 = input.getInputs();
            for (IInputItemStack input1 : list2) {
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
            tank.drain(recipeOutput.getRecipe().input.getFluid(), IFluidHandler.FluidAction.EXECUTE);
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
            List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();
            List<Integer> lst1 = new ArrayList<>();
            if (tank.getFluid().isEmpty()) {
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
                    tank.drain(baseMachineRecipe.input.getFluid(), IFluidHandler.FluidAction.EXECUTE);
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
                if (!tank.isEmpty() && baseMachineRecipe.input.getFluid() != null && !baseMachineRecipe.input.getFluid().isEmpty()) {
                    if (!tank.getFluid().isFluidEqual(baseMachineRecipe.input.getFluid()))
                        continue;
                }
                int[] col = new int[size];
                int[] col1 = new int[size];
                List<Integer> lst = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    lst.add(i);
                }
                List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();
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
                        col[j] = stack2.getCount();
                    }
                    if (adjustInput) {
                        for (int j = 0; j < list.size(); j++) {
                            list.get(j).setCount(list.get(j).getCount() - col[j]);
                        }
                        tank.drain(baseMachineRecipe.input.getFluid(), IFluidHandler.FluidAction.EXECUTE);
                        break;
                    } else {
                        return new MachineRecipe(baseMachineRecipe, Arrays.stream(col)
                                .boxed()
                                .collect(Collectors.toList()));
                    }
                }
            } else {
                List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();
                if (!tank.isEmpty() && baseMachineRecipe.input.getFluid() != null && !baseMachineRecipe.input.getFluid().isEmpty()) {
                    if (!tank.getFluid().isFluidEqual(baseMachineRecipe.input.getFluid()))
                        continue;
                }
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

                List<Integer> integer = new ArrayList<>();
                for (int j = 0; j < list.size(); j++) {
                    integer.add(recipeInputList.get(j).getInputs().get(0).getCount());
                }
                if (adjustInput) {
                    for (int j = 0; j < list.size(); j++) {
                        list.get(j).setCount(list.get(j).getCount() - recipeInputList.get(j).getAmount());
                    }
                    tank.drain(baseMachineRecipe.input.getFluid(), IFluidHandler.FluidAction.EXECUTE);
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
        if (canAdd) {
            this.recipes.add(hasRecipe);
        }
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
            List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();
            List<Integer> lst1 = new ArrayList<>();
            if (tank.getFluid().isEmpty()) {
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
                    tank.drain(baseMachineRecipe.input.getFluid(), IFluidHandler.FluidAction.EXECUTE);
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

    public RecipeArrayList<IRecipeInputStack> getMap_recipe_managers_itemStack(String name) {

        return map_recipe_managers_itemStack.get(name);
    }

    @Override
    public List<IHasRecipe> getRecipesForInit() {
        return this.recipes;
    }

    public void addRecipe(String name, BaseMachineRecipe recipe) {
        if (!this.map_recipes.containsKey(name)) {
            List<IInputItemStack> iInputItemStackList = recipe.input.getInputs();
            RecipeArrayList<IRecipeInputStack> inputStackList = new RecipeArrayList<>();
            for (IInputItemStack recipeInput : iInputItemStackList) {
                inputStackList.add(new RecipeInputStack(recipeInput));
            }

            this.map_recipe_managers_itemStack.put(name, inputStackList);
            if (recipe.input.hasFluids()) {
                List<Fluid> fluidStackList = new ArrayList<>();
                fluidStackList.add(recipe.input.getFluid().getFluid());
                map_fluid_input.put(name, fluidStackList);
            }
            List<BaseMachineRecipe> lst = new LinkedList<>();
            if (name.equals("comb_macerator")) {
                recipe.output.items.get(0).setCount(3);
            }
            lst.add(recipe);
            this.map_recipes.put(name, lst);
        } else {
            if (name.equals("comb_macerator")) {
                recipe.output.items.get(0).setCount(3);
            }
            RecipeArrayList<IRecipeInputStack> iRecipeInputList = this.map_recipe_managers_itemStack.get(name);
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
                for (IInputItemStack input1 : recipe.input.getInputs()) {
                    iRecipeInputList.add(new RecipeInputStack(input1));
                }
            } else {
                for (IInputItemStack input1 : recipe.input.getInputs()) {
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
    public void optimize() {
        for (Map.Entry<String, List<BaseMachineRecipe>> entry : map_recipes.entrySet()) {
            entry.setValue(new ArrayList<>(entry.getValue()));
        }
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

        this.getRecipesForInit().forEach(iHasRecipe -> {
                    if (className.equals(iHasRecipe.getClass().getName())) {
                        iHasRecipe.init();
                    }

                }

        );
    }

    @Override
    public void initializationRecipes() {
        this.getRecipesForInit().forEach(iHasRecipe -> {
            if (!registeredRecipes.contains(iHasRecipe.getName())) {
                registeredRecipes.add(iHasRecipe.getName());
                iHasRecipe.init();
            }
        });
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
                int[] col = new int[size];
                int[] col1 = new int[size];
                for (BaseMachineRecipe baseMachineRecipe : recipes) {

                    List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();

                    int count = 0;
                    for (int j = 0; j < stack1.size(); j++) {
                        for (int i = 0; i < recipeInputList.size(); i++) {
                            if (recipeInputList.get(i).matches(stack1.get(j))) {
                                count++;
                                col1[j] = i;
                                break;
                            }
                        }
                    }

                    if (count == size) {
                        boolean canMatch = true;
                        for (int j = 0; j < stack1.size(); j++) {
                            IInputItemStack inputStack = recipeInputList.get(col1[j]);
                            if (stack1.get(j).getCount() < inputStack.getInputs().get(0).getCount()) {
                                canMatch = false;
                                break;
                            }
                            col[j] = inputStack.getInputs().get(0).getCount();
                        }

                        if (canMatch) {
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
                }
            } else {
                for (BaseMachineRecipe baseMachineRecipe : recipes) {
                    List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();
                    boolean matchesAll = true;

                    for (int i = 0; i < size; i++) {
                        if (!recipeInputList.get(i).matches(stack1.get(i)) ||
                                recipeInputList.get(i).getInputs().get(0).getCount() > stack1.get(i).getCount()) {
                            matchesAll = false;
                            break;
                        }
                    }

                    if (matchesAll) {
                        if (adjustInput) {
                            for (int j = 0; j < stack1.size(); j++) {
                                stack1.get(j).setCount(stack1.get(j).getCount() - recipeInputList.get(j).getAmount());
                            }
                        } else {
                            return baseMachineRecipe;
                        }
                    }
                }
            }
        } else {
            return getRecipeMultiOutput(recipe, recipes, adjustInput, stack1);
        }
        return null;

    }

    @Override
    public BaseMachineRecipe getRecipeOutput(final String name, final boolean adjustInput, final ItemStack... stacks) {
        List<ItemStack> stackList = Arrays.asList(stacks);
        final IBaseRecipe recipe = this.getRecipe(name);
        final List<BaseMachineRecipe> recipes = this.getRecipeList(name);
        int size = recipe.getSize();

        if (size > 1) {
            if (!recipe.require()) {
                int[] requiredCounts = new int[size];
                int[] matchingIndices = new int[size];
                for (BaseMachineRecipe baseMachineRecipe : recipes) {

                    boolean[] matched = new boolean[size];

                    List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();


                    for (int j = 0; j < stackList.size(); j++) {
                        for (int i = 0; i < recipeInputList.size(); i++) {
                            if (!matched[i] && recipeInputList.get(i).matches(stackList.get(j))) {
                                matched[i] = true;
                                matchingIndices[j] = i;
                                break;
                            }
                        }
                    }


                    if (IntStream.range(0, size).allMatch(idx -> matched[idx])) {
                        for (int j = 0; j < stackList.size(); j++) {
                            ItemStack requiredStack = recipeInputList.get(matchingIndices[j]).getInputs().get(0);
                            ItemStack providedStack = stackList.get(j);

                            if (providedStack.getCount() < requiredStack.getCount()) {
                                return null;
                            }

                            requiredCounts[j] = requiredStack.getCount();
                        }

                        if (adjustInput) {
                            for (int j = 0; j < stackList.size(); j++) {
                                stackList.get(j).setCount(stackList.get(j).getCount() - requiredCounts[j]);
                            }
                            break;
                        } else {
                            return baseMachineRecipe;
                        }
                    }
                }
            } else {
                for (BaseMachineRecipe baseMachineRecipe : recipes) {
                    List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();
                    boolean isValid = true;

                    for (int i = 0; i < size; i++) {
                        if (!recipeInputList.get(i).matches(stackList.get(i)) ||
                                recipeInputList.get(i).getInputs().get(0).getCount() > stackList.get(i).getCount()) {
                            isValid = false;
                            break;
                        }
                    }

                    if (!isValid) {
                        continue;
                    }

                    if (adjustInput) {
                        for (int j = 0; j < stackList.size(); j++) {
                            stackList.get(j).setCount(stackList.get(j).getCount() - recipeInputList.get(j).getAmount());
                        }
                        break;
                    } else {
                        return baseMachineRecipe;
                    }
                }
            }
        } else {
            return getRecipeMultiOutput(recipe, recipes, adjustInput, stackList);
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
                    List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();
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
                    List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();

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
    public boolean needContinue(final MachineRecipe recipe, final InventoryRecipes slot) {
        if (recipe == null) {
            return false;
        }
        BaseMachineRecipe recipe1 = recipe.getRecipe();
        if (slot.getRecipe().workbench()) {
            for (int i = 0; i < recipe1.input.getInputs().size(); i++) {
                if (slot.get(i).getCount() < recipe.getList().get(i)) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < recipe1.input.getInputs().size(); i++) {
                if (slot.get(i).isEmpty() || slot.get(i).getCount() < recipe.getList().get(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean needContinue(final MachineRecipe recipe, final InventoryRecipes slot, final FluidTank tank) {
        if (recipe == null) {
            return false;
        }
        BaseMachineRecipe recipe1 = recipe.getRecipe();
        for (int i = 0; i < recipe1.input.getInputs().size(); i++) {
            if (slot.get(i).isEmpty() || slot.get(i).getCount() < recipe.getList().get(i)) {
                return false;
            }
        }
        return !recipe.getRecipe().input.hasFluids() || (!tank.getFluid().isEmpty() && tank
                .getFluid()
                .getFluid()
                .equals(recipe1.input
                        .getFluid()
                        .getFluid()) && tank.getFluidAmount() >= recipe1.input.getFluid().getAmount());
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
                List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();
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
                List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();

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
                List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();
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
                List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();

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
                List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();
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
                List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();
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
            int[] col = new int[size];
            int[] col1 = new int[size];
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                boolean[] matched = new boolean[size];


                List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();


                for (int j = 0; j < stacks.size(); j++) {
                    for (int i = 0; i < recipeInputList.size(); i++) {
                        if (!matched[i] && recipeInputList.get(i).matches(stacks.get(j))) {
                            matched[i] = true;
                            col1[j] = i;
                            break;
                        }
                    }
                }

                if (IntStream.range(0, size).allMatch(idx -> matched[idx])) {
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
                List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();
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
            int[] col = new int[size];
            int[] col1 = new int[size];
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                boolean[] matched = new boolean[size];

                List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();


                for (int j = 0; j < stacks.size(); j++) {
                    for (int i = 0; i < recipeInputList.size(); i++) {
                        if (!matched[i] && recipeInputList.get(i).matches(stacks.get(j))) {
                            matched[i] = true;
                            col1[j] = i;
                            break;
                        }
                    }
                }


                if (IntStream.range(0, size).allMatch(idx -> matched[idx])) {
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
                        return new MachineRecipe(
                                baseMachineRecipe,
                                Arrays.stream(col).boxed().collect(Collectors.toList())
                        );
                    }
                }
            }
        } else {
            for (BaseMachineRecipe baseMachineRecipe : recipes) {
                List<IInputItemStack> recipeInputList = baseMachineRecipe.input.getInputs();


                if (!recipeInputList.get(0).matches(stacks.get(0))) {
                    continue;
                }

                List<Integer> amounts = new ArrayList<>();
                for (int j = 0; j < stacks.size(); j++) {
                    amounts.add(recipeInputList.get(j).getInputs().get(0).getCount());
                }

                if (adjustInput) {
                    for (int j = 0; j < stacks.size(); j++) {
                        stacks.get(j).setCount(stacks.get(j).getCount() - recipeInputList.get(j).getAmount());
                    }
                    break;
                } else {
                    return new MachineRecipe(baseMachineRecipe, amounts);
                }
            }
        }

        return null;

    }

}
