package com.denfop.api.recipe;


import com.denfop.Ic2Items;
import com.denfop.api.Recipes;
import com.denfop.componets.ProcessMultiComponent;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.api.upgrade.IUpgradeItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvSlotMultiRecipes extends InvSlot {

    private final IMultiUpdateTick tile;
    private final ProcessMultiComponent processMultiComponent;
    public MachineRecipe recycler_output;
    private List<IRecipeInputStack> accepts;
    private List<BaseMachineRecipe> recipe_list;
    private IBaseRecipe recipe;
    private FluidTank tank;

    public InvSlotMultiRecipes(
            final TileEntityInventory base, IBaseRecipe baseRecipe, IMultiUpdateTick tile, int size,
            ProcessMultiComponent processMultiComponent
    ) {
        super(base, "input", Access.I, size);
        this.recipe = baseRecipe;
        this.recipe_list = Recipes.recipes.getRecipeList(this.recipe.getName());
        this.tile = tile;
        this.tank = null;
        this.accepts = Recipes.recipes.getMap_recipe_managers_itemStack(this.recipe.getName());

        recycler_output = new MachineRecipe(new BaseMachineRecipe(null, new RecipeOutput(
                null,
                Ic2Items.scrap
        )), Collections.singletonList(1));
        this.processMultiComponent = processMultiComponent;
    }

    public InvSlotMultiRecipes(
            final TileEntityInventory base, String baseRecipe, IMultiUpdateTick tile, int size,
            ProcessMultiComponent processMultiComponent
    ) {
        this(base, Recipes.recipes.getRecipe(baseRecipe), tile, size, processMultiComponent);

    }

    public InvSlotMultiRecipes(
            final TileEntityInventory base,
            String baseRecipe,
            IMultiUpdateTick tile,
            FluidTank tank,
            int size, ProcessMultiComponent processMultiComponent
    ) {
        this(base, Recipes.recipes.getRecipe(baseRecipe), tile, size, processMultiComponent);
        this.tank = tank;
    }

    public IBaseRecipe getRecipe() {
        return recipe;
    }

    public void load() {
        this.recipe_list = Recipes.recipes.getRecipeList(this.recipe.getName());
        if (this.recipe.getName().equals("recycler")) {
            this.recycler_output = new MachineRecipe(new BaseMachineRecipe(null, new RecipeOutput(
                    null,
                    Ic2Items.scrap
            )), Collections.singletonList(1));
        }
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        if (!recipe.getName().equals("recycler")) {
            this.tile.setRecipeOutput(this.process(index), index);
        } else {
            processMultiComponent.getOutput(index);

        }
        this.tile.onUpdate();
    }

    public BaseMachineRecipe consume(MachineRecipe recipe) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).isEmpty()) {
                return null;
            }
        }
        List<ItemStack> list = new ArrayList<>();
        this.forEach(list::add);
        if (this.tank == null) {
            return Recipes.recipes.getRecipeConsume(this.recipe, recipe, this.recipe.consume(), list);
        } else {
            return Recipes.recipes.getRecipeOutputFluid(this.recipe.getName(), this.recipe.consume(), list, this.tank);
        }

    }

    @Override
    public boolean accepts(final ItemStack itemStack, final int index) {
        return !itemStack.isEmpty() && !(itemStack.getItem() instanceof IUpgradeItem) && (recipe
                .getName()
                .equals("painter") || recipe
                .getName()
                .equals("upgradeblock") || recipe
                .getName()
                .equals("recycler") || accepts.contains(
                new RecipeInputStack(itemStack)));
    }

    public void consume(int number, int amount) {
        this.consume(number, amount, false, false);
    }

    public void consume(int number, int amount, boolean simulate, boolean consumeContainers) {


        ItemStack stack = this.get(number);
        if (!stack.isEmpty() && stack.getCount() >= 1 && this.accepts(
                stack,
                number
        ) && (stack.getCount() >= 1 || consumeContainers || !stack
                .getItem()
                .hasContainerItem(stack))) {
            int currentAmount = Math.min(amount, stack.getCount());
            if (!simulate) {
                if (stack.getCount() == currentAmount) {
                    if (!consumeContainers && stack.getItem().hasContainerItem(stack)) {
                        this.put(number, stack.getItem().getContainerItem(stack));
                    } else {
                        this.put(number, null);
                    }
                } else {
                    stack.setCount(stack.getCount() - currentAmount);
                }
            }


        }


    }

    @Override
    public ItemStack get(final int index) {
        return super.get(index);
    }

    public boolean continue_proccess(InvSlotOutput slot, int slotid) {
        if (tile.getRecipeOutput(slotid) == null) {
            return false;
        }
        if (!this.recipe.getName().equals("recycler")) {
            return slot.canAdd(tile.getRecipeOutput(slotid).getRecipe().output.items) && this.get(slotid).getCount() >= tile
                    .getRecipeOutput(
                            slotid).getList().get(0);
        } else {
            return !this.get(slotid).isEmpty() && slot.canAdd(tile.getRecipeOutput(slotid).getRecipe().output.items);
        }
    }

    public MachineRecipe process(int slotid) {

        if (this.get(slotid).isEmpty()) {
            return null;
        }

        return getOutputFor(slotid);
    }

    public MachineRecipe fastprocess(int slotid) {

        if (this.get(slotid).isEmpty()) {
            return null;
        }

        MachineRecipe output;
        output = getOutputFor(slotid);


        return output;
    }

    public MachineRecipe consume(int slotid) {

        if (this.get(slotid).isEmpty()) {
            if (fastprocess(slotid) == null) {
                return null;
            }
            throw new NullPointerException();
        }
        if (!this.recipe.getName().equals("recycler")) {
            List<ItemStack> list = new ArrayList<>();
            list.add(this.get(slotid));
            if (this.tank == null) {
                return Recipes.recipes.getMachineRecipeConsume(
                        this.recipe,
                        this.tile.getRecipeOutput(slotid),
                        this.recipe.consume(),
                        list
                );
            } else {
                return Recipes.recipes.getRecipeOutputMachineFluid(this.recipe.getName(), this.recipe.consume(), list, this.tank);
            }
        } else {
            this.get(slotid).shrink(1);
            return recycler_output;
        }
    }

    private MachineRecipe getOutputFor(int slotid) {
        List<ItemStack> list = new ArrayList<>();
        list.add(this.get(slotid));
        if (this.tank == null) {
            return Recipes.recipes.getRecipeMachineMultiOutput(this.recipe, this.recipe_list, false, list);
        } else {
            return Recipes.recipes.getRecipeOutputMachineFluid(this.recipe.getName(), false, list, this.tank);
        }

    }

    public void setNameRecipe(String nameRecipe) {
        this.recipe = Recipes.recipes.getRecipe(nameRecipe);
        this.recipe_list = Recipes.recipes.getRecipeList(this.recipe.getName());
        this.accepts = Recipes.recipes.getMap_recipe_managers_itemStack(this.recipe.getName());

    }


}
