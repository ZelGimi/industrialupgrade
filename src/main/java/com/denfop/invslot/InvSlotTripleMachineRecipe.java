package com.denfop.invslot;

import com.denfop.api.ITripleMachineRecipeManager;
import com.denfop.api.inv.IInvSlotProcessable;
import com.denfop.tiles.base.TileEntityTripleElectricMachine;
import ic2.api.recipe.RecipeOutput;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.item.upgrade.ItemUpgradeModule;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvSlotTripleMachineRecipe extends InvSlot implements IInvSlotProcessable {

    public final ITripleMachineRecipeManager recipes;

    public InvSlotTripleMachineRecipe(
            TileEntityInventory base1,
            String name1,
            int count,
            ITripleMachineRecipeManager recipes
    ) {
        super(base1, name1, Access.I, count);
        this.recipes = recipes;
    }

    public Map<ITripleMachineRecipeManager.Input, RecipeOutput> getRecipeList() {
        return recipes.getRecipes();
    }

    public boolean accepts(ItemStack itemStack) {
        for (Map.Entry<ITripleMachineRecipeManager.Input, RecipeOutput> entry : getRecipeList().entrySet()) {
            if ((entry.getKey()).container.matches(itemStack)
                    || (entry.getKey()).fill.matches(itemStack) || (entry.getKey()).fill1.matches(itemStack)) {
                return itemStack != null || !(itemStack.getItem() instanceof ItemUpgradeModule);
            }
        }
        return false;

    }

    protected RecipeOutput getOutput(ItemStack container, ItemStack fill, ItemStack fill1, boolean adjustInput) {

        return recipes.getOutputFor(container, fill, fill1, adjustInput, false);

    }

    protected RecipeOutput getOutputFor(ItemStack input, ItemStack input1, ItemStack input2, boolean adjustInput) {
        return getOutput(input, input1, input2, adjustInput);
    }


    @Override
    public RecipeOutput process() {
        ItemStack input = ((TileEntityTripleElectricMachine) this.base).inputSlotA.get(0);
        ItemStack input1 = ((TileEntityTripleElectricMachine) this.base).inputSlotA.get(1);
        ItemStack input2 = ((TileEntityTripleElectricMachine) this.base).inputSlotA.get(2);
        if (input == null) {
            return null;
        }
        if (input1 == null) {
            return null;
        }
        if (input2 == null) {
            return null;
        }
        RecipeOutput output = getOutputFor(input2, input1, input, false);
        if (output == null) {
            return null;
        }
        List<ItemStack> itemsCopy = new ArrayList<>(output.items.size());
        itemsCopy.addAll(output.items);
        return new RecipeOutput(output.metadata, itemsCopy);
    }

    public void consume() {

        ItemStack input = ((TileEntityTripleElectricMachine) this.base).inputSlotA.get(0);
        ItemStack input1 = ((TileEntityTripleElectricMachine) this.base).inputSlotA.get(1);
        ItemStack input2 = ((TileEntityTripleElectricMachine) this.base).inputSlotA.get(2);
        getOutputFor(input2, input1, input, true);

        if (input != null && input.getCount() <= 0) {
            ((TileEntityTripleElectricMachine) this.base).inputSlotA.put(0, null);
        }
        if (input1 != null && input1.getCount() <= 0) {
            ((TileEntityTripleElectricMachine) this.base).inputSlotA.put(1, null);
        }
        if (input2 != null && input2.getCount() <= 0) {
            ((TileEntityTripleElectricMachine) this.base).inputSlotA.put(2, null);
        }


    }

    @Override
    public ItemStack get1(final int i) {
        return this.get(i);
    }


}
