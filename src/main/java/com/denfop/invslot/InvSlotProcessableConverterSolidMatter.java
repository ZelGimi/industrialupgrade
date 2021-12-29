package com.denfop.invslot;


import com.denfop.api.Recipes;
import ic2.api.recipe.IBasicMachineRecipeManager;
import ic2.api.recipe.MachineRecipeResult;
import ic2.api.recipe.RecipeOutput;
import ic2.core.block.TileEntityInventory;
import ic2.core.item.upgrade.ItemUpgradeModule;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InvSlotProcessableConverterSolidMatter extends InvSlotProcessable {
    public final IBasicMachineRecipeManager recipeManager;

    public InvSlotProcessableConverterSolidMatter(
            TileEntityInventory base1,
            String name1,
            int count,
            IBasicMachineRecipeManager recipeManager1
    ) {
        super(base1, name1,recipeManager1,  count);
        this.recipeManager = recipeManager1;
    }

    public boolean accepts(ItemStack itemStack) {
        if (itemStack != null &&
                itemStack.getItem() instanceof ItemUpgradeModule)
            return false;
        return (getOutputFor1(itemStack, false) != null);
    }

    public RecipeOutput process() {
        ItemStack input = get();
        if (input == null)
            return null;
        RecipeOutput output = getOutputFor1(input, false);
        if (output == null)
            return null;
        List<ItemStack> itemsCopy = new ArrayList<>(output.items.size());
        for (ItemStack itemStack : output.items)
            itemsCopy.add(itemStack.copy());
        return new RecipeOutput(output.metadata, itemsCopy);
    }

    public void consume() {
        ItemStack input = get();
        if (input == null)
            throw new IllegalStateException("consume from empty slot");
        RecipeOutput output = getOutputFor1(input, true);
        if (output == null)
            throw new IllegalStateException("consume without a processing result");
        if (!input.isEmpty() && input.stackSize <= 0)
            put(null);
    }
    protected RecipeOutput getOutputFor1(ItemStack input, boolean adjustInput) {
        return Recipes.matterrecipe.getOutputFor(input, adjustInput);
    }
    protected MachineRecipeResult getOutputFor(ItemStack input, boolean adjustInput) {
        return null;
    }


}
