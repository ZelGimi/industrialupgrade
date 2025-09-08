package com.denfop.api.recipe;

import com.denfop.api.Recipes;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.base.BlockEntityConverterSolidMatter;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.componets.Fluids;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.items.ItemRecipeSchedule;
import com.denfop.recipe.IInputItemStack;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InventoryRecipes extends Inventory implements ITypeSlot {

    public final IUpdateTick tile;
    private final ConcurrentHashMap<Integer, RecipeArrayList<IRecipeInputStack>> map = new ConcurrentHashMap<>();
    private final RecipeArrayList<IRecipeInputStack> default_accepts;
    private IBaseRecipe recipe;
    private RecipeArrayList<IRecipeInputStack> accepts;
    private List<BaseMachineRecipe> recipe_list;
    private Fluids.InternalFluidTank tank;

    private InventoryFluidByList invSlotConsumableLiquidByList = null;
    private int index;


    public InventoryRecipes(final BlockEntityInventory base, IBaseRecipe baseRecipe, IUpdateTick tile) {
        super(base, TypeItemSlot.INPUT, baseRecipe.getSize());
        this.recipe = baseRecipe;
        this.recipe_list = Recipes.recipes.getRecipeList(this.recipe.getName());
        this.default_accepts = this.accepts = Recipes.recipes.getMap_recipe_managers_itemStack(this.recipe.getName());
        if (recipe_list != null) {
            for (BaseMachineRecipe baseMachineRecipe : this.recipe_list) {
                final IInput input = baseMachineRecipe.input;
                for (int i = 0; i < input.getInputs().size(); i++) {
                    final RecipeArrayList<IRecipeInputStack> list = this.map.get(i);
                    if (list == null) {
                        RecipeArrayList<IRecipeInputStack> inputStackList = new RecipeArrayList<>();
                        inputStackList.add(new RecipeInputStack(input.getInputs().get(i)));
                        this.map.put(i, inputStackList);
                    } else {
                        list.add(new RecipeInputStack(input.getInputs().get(i)));
                    }
                }
            }
        }
        this.tile = tile;
        this.tank = null;
    }

    public InventoryRecipes(final BlockEntityInventory base, String baseRecipe, IUpdateTick tile) {
        this(base, Recipes.recipes.getRecipe(baseRecipe), tile);

    }

    public InventoryRecipes(final BlockEntityInventory base, String baseRecipe, IUpdateTick tile, Fluids.InternalFluidTank tank) {
        this(base, Recipes.recipes.getRecipe(baseRecipe), tile);
        this.tank = tank;
    }

    @Override
    public boolean hasItemList() {
        return true;
    }

    @Override
    public List<IInputItemStack> getStacks(int index) {
        if (!this.recipe.require()) {
            List<IInputItemStack> uniqueStacks = new ArrayList<>();
            Set<IInputItemStack> seenStacks = new HashSet<>();

            for (IInputItemStack input : accepts.stream().map(IRecipeInputStack::getInput).toList()) {
                boolean isNew = seenStacks.stream().noneMatch(existing ->
                        existing.equals(input)
                );
                if (isNew) {
                    seenStacks.add(input);
                    uniqueStacks.add(input);
                }
            }
            return uniqueStacks;

        } else {
            RecipeArrayList<IRecipeInputStack> list = map.get(index);
            List<IInputItemStack> stacks = new LinkedList<>();
            list.forEach(accept -> stacks.add(accept.getInput()));
            return stacks;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public void changeAccepts(ItemStack stack) {
        if (stack.isEmpty()) {
            this.accepts = this.default_accepts;
        } else {
            ItemRecipeSchedule itemRecipeSchedule = (ItemRecipeSchedule) stack.getItem();
            this.accepts = itemRecipeSchedule.getInputs(base.getWorld().registryAccess(), this.recipe, stack);
        }
    }

    public IUpdateTick getTile() {
        return tile;
    }

    public void setInvSlotConsumableLiquidByList(final InventoryFluidByList invSlotConsumableLiquidByList) {
        this.invSlotConsumableLiquidByList = invSlotConsumableLiquidByList;
    }

    public Fluids.InternalFluidTank getTank() {
        return tank;
    }

    public void load() {
        this.recipe_list = Recipes.recipes.getRecipeList(this.recipe.getName());
        this.map.clear();
        if (recipe_list != null) {
            for (BaseMachineRecipe baseMachineRecipe : this.recipe_list) {
                final IInput input = baseMachineRecipe.input;
                for (int i = 0; i < input.getInputs().size(); i++) {
                    final RecipeArrayList<IRecipeInputStack> list = this.map.get(i);
                    if (list == null) {
                        RecipeArrayList<IRecipeInputStack> inputStackList = new RecipeArrayList<>();
                        inputStackList.add(new RecipeInputStack(input.getInputs().get(i)));
                        this.map.put(i, inputStackList);
                    } else {
                        list.add(new RecipeInputStack(input.getInputs().get(i)));
                    }
                }
            }
        }
        if (this.invSlotConsumableLiquidByList != null) {
            this.invSlotConsumableLiquidByList.setAcceptedFluids(new HashSet<>(Recipes.recipes.getInputFluidsFromRecipe(this.recipe.getName())));
        }
        if (tank != null) {
            tank.setAcceptedFluids(Fluids.fluidPredicate(Recipes.recipes.getInputFluidsFromRecipe(this.recipe.getName())));
        }

    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        final MachineRecipe recipe1 = this.process();
        this.tile.setRecipeOutput(this.index, recipe1);
        this.tile.onUpdate();
        return content;
    }

    public boolean acceptAllOrIndex() {
        return !this.recipe.require();
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack itemStack) {
        if (index > this.recipe.getSize()) {
            return false;
        }
        if (!this.recipe.require()) {
            return !itemStack.isEmpty() && (recipe
                    .getName()
                    .equals("painter") || recipe
                    .getName()
                    .equals("upgradeblock") || recipe
                    .getName()
                    .equals("roverupgradeblock") || recipe
                    .getName()
                    .equals("recycler") || accepts.contains(
                    itemStack));
        } else {
            RecipeArrayList<IRecipeInputStack> list = map.get(index);
            if (list == null) {
                return false;
            }
            return list.contains(itemStack);
        }
    }

    public void consume(int number, int amount) {
        this.consume(number, amount, false);
    }

    public boolean continue_process(MachineRecipe recipe) {
        if (this.tank == null) {
            return Recipes.recipes.needContinue(recipe, this);
        } else {
            return Recipes.recipes.needContinue(recipe, this, tank);
        }
    }

    public void consume(int number, int amount, boolean simulate) {


        ItemStack stack = this.get(number);
        if (!stack.isEmpty() && stack.getCount() >= 1) {
            int currentAmount = Math.min(amount, stack.getCount());
            if (!simulate) {
                stack.shrink(currentAmount);
            }


        }


    }

    @Override
    public ItemStack get(final int index) {
        return super.get(index);
    }

    public MachineRecipe process() {
        if (!this.recipe.workbench()) {
            for (int i = 0; i < Math.min(this.size(), this.recipe.getSize()); i++) {
                if (this.get(i).isEmpty()) {
                    return null;
                }
            }
            MachineRecipe output;
            output = this.getOutputFor();
            if (this.tile instanceof BlockEntityConverterSolidMatter) {
                BlockEntityConverterSolidMatter mechanism = (BlockEntityConverterSolidMatter) this.tile;
                if (output != null) {
                    mechanism.getrequiredmatter(output.getRecipe().getOutput());
                }
            }

            return output;
        } else {
            MachineRecipe output;
            output = this.getOutputFor();
            return output;
        }
    }

    public BaseMachineRecipe consume() {
        for (int i = 0; i < this.recipe.getSize(); i++) {
            if (this.get(i).isEmpty()) {
                return null;
            }
        }
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < this.recipe.getSize(); i++) {
            list.add(this.contents.get(i));
        }

        if (this.tank == null) {
            return Recipes.recipes.getRecipeConsume(this.recipe, this.tile.getRecipeOutput(this.index), this.recipe.consume(), list);
        } else {
            return Recipes.recipes.getRecipeOutputFluid(this.recipe, this.tile.getRecipeOutput(this.index), this.recipe.consume(), list,
                    this.tank
            );

        }

    }

    public boolean continue_proccess(InventoryOutput slot) {
        if (tile.getRecipeOutput(index) == null) {
            return false;
        }
        if (this.tank == null) {
            return tile
                    .getRecipeOutput(index) != null &&

                    slot.canAdd(tile.getRecipeOutput(index).getRecipe().output.items)&&  this.get(0).getCount() >= tile
                            .getRecipeOutput(index).getList().get(0);
        } else {
            return tile
                    .getRecipeOutput(index) != null && slot.canAdd(tile.getRecipeOutput(index).getRecipe().output.items) && this.get(0).getCount() >= tile
                    .getRecipeOutput(index).getList().get(0) && this.tank.getFluidAmount() >= this.tile
                    .getRecipeOutput(index)
                    .getRecipe().input.getFluid().getAmount();
        }

    }

    private MachineRecipe getOutputFor() {
        List<ItemStack> list = new ArrayList<>(this.contents);
        list = list.stream().limit(this.recipe.getSize()).collect(Collectors.toList());
        if (this.tank == null) {
            return Recipes.recipes.getRecipeMachineRecipeOutput(this.recipe, this.recipe_list, false, list);
        } else {
            return Recipes.recipes.getRecipeOutputMachineFluid(this.recipe, this.recipe_list, false, list, this.tank);
        }
    }

    public IBaseRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(final IBaseRecipe recipe) {
        this.recipe = recipe;
        this.accepts = Recipes.recipes.getMap_recipe_managers_itemStack(this.recipe.getName());
        this.load();
    }

    public void setRecipe(final String recipe) {
        this.setRecipe(Recipes.recipes.getRecipe(recipe));
    }

    public List<BaseMachineRecipe> getRecipe_list() {
        return recipe_list;
    }

    @Override
    public EnumTypeSlot getTypeSlot(int slotid) {
        if (recipe.getName().equals("rotor_assembler")) {
            if (slotid == 4) {
                return EnumTypeSlot.ROD_PART1;
            }
            return EnumTypeSlot.ROD_PART;
        }
        if (recipe.getName().equals("water_rotor_assembler")) {
            if (slotid == 4) {
                return EnumTypeSlot.ROD_PART1;
            }
            return EnumTypeSlot.WATER_ROD_PART;
        }
        return null;
    }

    public void consume(int size, MachineRecipe output) {
        if (this.recipe.consume()) {
            if (this.recipe.workbench()) {
                for (int i = 0; i < output.getList().size(); i++) {
                    ItemStack stack = this.get(i);
                    if (!stack.isEmpty()) {
                        stack.shrink(size * output.getList().get(i));
                    }
                }
                if (output.getRecipe().input.getFluid() != null) {
                    this.getTank().drain(output.getRecipe().input.getFluid().getAmount() * size, IFluidHandler.FluidAction.EXECUTE);
                }
            } else {
                for (int i = 0; i < output.getList().size(); i++) {
                    ItemStack stack = this.get(i);
                    stack.shrink(size * output.getList().get(i));
                }
                if (output.getRecipe().input.getFluid() != null) {
                    this.getTank().drain(output.getRecipe().input.getFluid().getAmount() * size, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

}
