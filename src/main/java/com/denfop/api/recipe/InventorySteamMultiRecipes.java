package com.denfop.api.recipe;


import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.componets.SteamProcessMultiComponent;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemRecipeSchedule;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventorySteamMultiRecipes extends Inventory {

    private final IMultiUpdateTick tile;
    private final SteamProcessMultiComponent processMultiComponent;
    private final RecipeArrayList<IRecipeInputStack> default_accepts;
    public MachineRecipe recycler_output;
    MachineRecipe[] prev = new MachineRecipe[4];
    private RecipeArrayList<IRecipeInputStack> accepts;
    private List<BaseMachineRecipe> recipe_list;
    private IBaseRecipe recipe;
    private FluidTank tank;

    public InventorySteamMultiRecipes(
            final BlockEntityInventory base, IBaseRecipe baseRecipe, IMultiUpdateTick tile, int size,
            SteamProcessMultiComponent processMultiComponent
    ) {
        super(base, TypeItemSlot.INPUT, size);
        this.recipe = baseRecipe;
        this.recipe_list = Recipes.recipes.getRecipeList(this.recipe.getName());
        this.tile = tile;
        this.tank = null;
        this.default_accepts = this.accepts = Recipes.recipes.getMap_recipe_managers_itemStack(this.recipe.getName());

        recycler_output = new MachineRecipe(new BaseMachineRecipe(null, new RecipeOutput(
                null,
                IUItem.scrap
        )), Collections.singletonList(1));
        this.processMultiComponent = processMultiComponent;
    }

    public InventorySteamMultiRecipes(
            final BlockEntityInventory base, String baseRecipe, IMultiUpdateTick tile, int size,
            SteamProcessMultiComponent processMultiComponent
    ) {
        this(base, Recipes.recipes.getRecipe(baseRecipe), tile, size, processMultiComponent);

    }

    public InventorySteamMultiRecipes(
            final BlockEntityInventory base,
            String baseRecipe,
            IMultiUpdateTick tile,
            FluidTank tank,
            int size, SteamProcessMultiComponent processMultiComponent
    ) {
        this(base, Recipes.recipes.getRecipe(baseRecipe), tile, size, processMultiComponent);
        this.tank = tank;
    }

    public void changeAccepts(ItemStack stack) {
        if (stack.isEmpty()) {
            this.accepts = this.default_accepts;
        } else {
            ItemRecipeSchedule itemRecipeSchedule = (ItemRecipeSchedule) stack.getItem();
            this.accepts = itemRecipeSchedule.getInputs(this.recipe, stack);
        }
    }

    public IBaseRecipe getRecipe() {
        return recipe;
    }

    public void load() {
        this.recipe_list = Recipes.recipes.getRecipeList(this.recipe.getName());
        if (this.recipe.getName().equals("recycler")) {
            this.recycler_output = new MachineRecipe(new BaseMachineRecipe(null, new RecipeOutput(
                    null,
                    IUItem.scrap
            )), Collections.singletonList(1));
        }
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        if (!recipe.getName().equals("recycler")) {
            final ItemStack input = this.get(index);
            if (input.isEmpty()) {
                this.tile.setRecipeOutput(null, index);
            } else {
                MachineRecipe recipe = prev[index];
                if (recipe == null) {
                    prev[index] = this.process(index);
                    this.tile.setRecipeOutput(prev[index], index);
                } else {
                    if (recipe.getRecipe().input.getInputs().get(0).matches(input)) {
                        this.tile.setRecipeOutput(recipe, index);
                    } else {
                        prev[index] = this.process(index);
                        this.tile.setRecipeOutput(prev[index], index);
                    }
                }
            }
        } else {
            processMultiComponent.getOutput(index);

        }
        this.tile.onUpdate();
        return content;
    }

    public BaseMachineRecipe consume(MachineRecipe recipe) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).isEmpty()) {
                return null;
            }
        }
        List<ItemStack> list = new ArrayList<>(this.contents);
        if (this.tank == null) {
            return Recipes.recipes.getRecipeConsume(this.recipe, recipe, this.recipe.consume(), list);
        } else {
            return Recipes.recipes.getRecipeOutputFluid(this.recipe.getName(), this.recipe.consume(), list, this.tank);
        }

    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack itemStack) {
        if (recipe.getName().equals("recycler") && !itemStack.isEmpty()) {
            return true;
        }
        return !itemStack.isEmpty() && !(itemStack.getItem() instanceof IUpgradeItem) && (recipe
                .getName()
                .equals("painter") || recipe
                .getName()
                .equals("upgradeblock") || recipe
                .getName()
                .equals("recycler") || (!recipe
                .getName()
                .equals("furnace") ? accepts.contains(
                itemStack) : accepts.contains(
                new RecipeInputStack(itemStack))));
    }

    public void consume(int number, int amount) {
        this.consume(number, amount, false, false);
    }

    public void consume(int number, int amount, boolean simulate, boolean consumeContainers) {


        ItemStack stack = this.get(number);
        if (!stack.isEmpty() && stack.getCount() >= 1 && this.canPlaceItem(
                number, stack
        ) && (stack.getCount() >= 1 || consumeContainers || !stack
                .getItem()
                .hasCraftingRemainingItem(stack))) {
            int currentAmount = Math.min(amount, stack.getCount());
            if (!simulate) {
                if (stack.getCount() == currentAmount) {
                    if (!consumeContainers && stack.getItem().hasCraftingRemainingItem(stack)) {
                        this.set(number, stack.getItem().getCraftingRemainingItem(stack));
                    } else {
                        this.set(number, null);
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

    public boolean continue_proccess(InventoryOutput slot, int slotid) {
        if (tile.getRecipeOutput(slotid) == null) {
            return false;
        }
        if (!this.recipe.getName().equals("recycler")) {
            return slot.addWithoutIgnoring(tile.getRecipeOutput(slotid).getRecipe().output.items, true) && this
                    .get(slotid)
                    .getCount() >= tile
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


    public void consume(int slotid, int size, Integer integer) {
        this.get(slotid).shrink(size * integer);
    }

}
