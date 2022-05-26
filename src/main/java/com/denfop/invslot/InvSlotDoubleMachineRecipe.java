package com.denfop.invslot;

import com.denfop.api.IDoubleMachineRecipeManager;
import com.denfop.api.inv.IInvSlotProcessableMulti;
import com.denfop.tiles.base.TileEntityDoubleElectricMachine;
import ic2.api.recipe.RecipeOutput;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvSlotDoubleMachineRecipe extends InvSlot implements IInvSlotProcessableMulti {

    public final IDoubleMachineRecipeManager recipes;

    public InvSlotDoubleMachineRecipe(
            TileEntityInventory base1,
            String name1,
            int count,
            IDoubleMachineRecipeManager recipes
    ) {
        super(base1, name1, Access.I, count);
        this.recipes = recipes;

    }

    public Map<IDoubleMachineRecipeManager.Input, RecipeOutput> getRecipeList() {
        return recipes.getRecipes();
    }

    public boolean accepts(ItemStack itemStack) {

        for (Map.Entry<IDoubleMachineRecipeManager.Input, RecipeOutput> entry : getRecipeList().entrySet()) {
            if ((entry.getKey()).container.matches(itemStack)
                    || (entry.getKey()).fill.matches(itemStack)) {
                return !itemStack.isEmpty();
            }
        }
        return false;

    }

    protected RecipeOutput getOutput(ItemStack container, ItemStack fill, boolean adjustInput) {

        return recipes.getOutputFor(container, fill, adjustInput, false);

    }

    protected RecipeOutput getOutputFor(ItemStack input, ItemStack input1, boolean adjustInput) {
        return getOutput(input, input1, adjustInput);
    }


    @Override
    public RecipeOutput process(final int slotId) {
        ItemStack input = ((TileEntityDoubleElectricMachine) this.base).inputSlotA.get(0);
        ItemStack input1 = ((TileEntityDoubleElectricMachine) this.base).inputSlotA.get(1);
        if (input == null) {
            return null;
        }
        if (input1 == null) {
            return null;
        }
        RecipeOutput output = getOutputFor(input, input1, false);
        if (output == null) {
            return null;
        }
        List<ItemStack> itemsCopy = new ArrayList<>(output.items.size());
        itemsCopy.addAll(output.items);
        return new RecipeOutput(output.metadata, itemsCopy);
    }

    public void consume(final int slotId) {

        ItemStack input = ((TileEntityDoubleElectricMachine) this.base).inputSlotA.get(0);
        ItemStack input1 = ((TileEntityDoubleElectricMachine) this.base).inputSlotA.get(1);
        getOutputFor(input, input1, true);

        if (input != null && input.getCount() <= 0) {
            ((TileEntityDoubleElectricMachine) this.base).inputSlotA.put(0, null);
        }
        if (input1 != null && input1.getCount() <= 0) {
            ((TileEntityDoubleElectricMachine) this.base).inputSlotA.put(1, null);
        }


    }

    @Override
    public ItemStack get1(final int i) {
        return this.get(i);
    }


}
