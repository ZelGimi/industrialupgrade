package com.denfop.api.recipe;

import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.componets.Fluids;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotConsumableLiquidByList;
import com.denfop.tiles.base.TileEntityConverterSolidMatter;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.api.upgrade.IUpgradeItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class InvSlotRecipes extends InvSlot implements ITypeSlot {

    public final IUpdateTick tile;
    private final HashMap<Integer, List<IRecipeInputStack>> map = new HashMap<>();
    private IBaseRecipe recipe;
    private List<IRecipeInputStack> accepts;
    private List<BaseMachineRecipe> recipe_list;
    private Fluids.InternalFluidTank tank;

    private InvSlotConsumableLiquidByList invSlotConsumableLiquidByList = null;


    public InvSlotRecipes(final TileEntityInventory base, IBaseRecipe baseRecipe, IUpdateTick tile) {
        super(base, "input", Access.I, baseRecipe.getSize());
        this.recipe = baseRecipe;
        this.recipe_list = Recipes.recipes.getRecipeList(this.recipe.getName());
        this.accepts = Recipes.recipes.getMap_recipe_managers_itemStack(this.recipe.getName());
        if (recipe_list != null) {
            for (BaseMachineRecipe baseMachineRecipe : this.recipe_list) {
                final IInput input = baseMachineRecipe.input;
                for (int i = 0; i < input.getInputs().size(); i++) {
                    final List<IRecipeInputStack> list = this.map.get(i);
                    if (list == null) {
                        List<IRecipeInputStack> inputStackList = new ArrayList<>();
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

    public InvSlotRecipes(final TileEntityInventory base, String baseRecipe, IUpdateTick tile) {
        this(base, Recipes.recipes.getRecipe(baseRecipe), tile);

    }

    public InvSlotRecipes(final TileEntityInventory base, String baseRecipe, IUpdateTick tile, Fluids.InternalFluidTank tank) {
        this(base, Recipes.recipes.getRecipe(baseRecipe), tile);
        this.tank = tank;
    }

    public IUpdateTick getTile() {
        return tile;
    }

    public void setInvSlotConsumableLiquidByList(final InvSlotConsumableLiquidByList invSlotConsumableLiquidByList) {
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
                    final List<IRecipeInputStack> list = this.map.get(i);
                    if (list == null) {
                        List<IRecipeInputStack> inputStackList = new ArrayList<>();
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
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        final MachineRecipe recipe1 = this.process();
        this.tile.setRecipeOutput(recipe1);
        this.tile.onUpdate();
    }

    @Override
    public boolean accepts(final ItemStack itemStack, final int index) {
        if (index > this.recipe.getSize()) {
            return false;
        }
        if (!this.recipe.require()) {
            return !itemStack.isEmpty() && !(itemStack.getItem() instanceof IUpgradeItem) && (recipe
                    .getName()
                    .equals("painter") || recipe
                    .getName()
                    .equals("upgradeblock") || recipe
                    .getName()
                    .equals("recycler") || accepts.contains(
                    new RecipeInputStack(itemStack)));
        } else {
            List<IRecipeInputStack> list = map.get(index);
            if ((itemStack.getItem() instanceof IUpgradeItem)) {
                return false;
            }
            return list.contains(new RecipeInputStack(itemStack));
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
        for (int i = 0; i < Math.min(this.size(), this.recipe.getSize()); i++) {
            if (this.get(i).isEmpty()) {
                return null;
            }
        }
        MachineRecipe output;
        output = this.getOutputFor();
        if (this.tile instanceof TileEntityConverterSolidMatter) {
            TileEntityConverterSolidMatter mechanism = (TileEntityConverterSolidMatter) this.tile;
            if (output != null) {
                mechanism.getrequiredmatter(output.getRecipe().getOutput());
            }
        }

        return output;
    }

    public BaseMachineRecipe consume() {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).isEmpty()) {
                return null;
            }
        }
        List<ItemStack> list = new ArrayList<>();
        this.forEach(list::add);
        if (this.tank == null) {
            return Recipes.recipes.getRecipeConsume(this.recipe, this.tile.getRecipeOutput(), this.recipe.consume(), list);
        } else {
            return Recipes.recipes.getRecipeOutputFluid(this.recipe, this.tile.getRecipeOutput(), this.recipe.consume(), list,
                    this.tank
            );

        }

    }

    public boolean continue_proccess(InvSlotOutput slot) {
        if (tile.getRecipeOutput() == null) {
            return false;
        }
        if (this.tank == null) {
            return

                    slot.canAdd(tile.getRecipeOutput().getRecipe().output.items) && this.get().getCount() >= tile
                            .getRecipeOutput().getList().get(0);
        } else {
            return slot.canAdd(tile.getRecipeOutput().getRecipe().output.items) && this.get().getCount() >= tile
                    .getRecipeOutput().getList().get(0) && this.tank.getFluidAmount() >= this.tile
                    .getRecipeOutput()
                    .getRecipe().input.getFluid().amount;
        }

    }

    private MachineRecipe getOutputFor() {
        List<ItemStack> list = new ArrayList<>();
        this.forEach(list::add);
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

        return null;
    }

}
