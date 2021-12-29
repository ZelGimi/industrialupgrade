package com.denfop.invslot;

import com.denfop.api.IGenStoneRecipeManager;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IInvSlotProcessable;
import com.denfop.tiles.mechanism.TileEntityGenerationStone;
import ic2.api.recipe.RecipeOutput;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.item.upgrade.ItemUpgradeModule;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvSlotProcessableStone extends InvSlot implements IInvSlotProcessable {


    public InvSlotProcessableStone(
            TileEntityInventory base1,
            String name1,
            int count
    ) {
        super(base1, name1, Access.I, count);
    }



    public Map<IGenStoneRecipeManager.Input, RecipeOutput> getRecipeList() {
        return Recipes.GenStone.getRecipes();
    }

    public boolean accepts(ItemStack itemStack) {
        for (Map.Entry<IGenStoneRecipeManager.Input, RecipeOutput> entry : getRecipeList().entrySet()) {
            if ((entry.getKey()).container.matches(itemStack)
                    || (entry.getKey()).fill.matches(itemStack)) {
                return itemStack != null || !(itemStack.getItem() instanceof ItemUpgradeModule);
            }
        }
        return false;

    }

    protected RecipeOutput getOutput(ItemStack container, ItemStack fill, boolean adjustInput) {

        return Recipes.GenStone.getOutputFor(container, fill, adjustInput, false);

    }

    protected RecipeOutput getOutputFor(ItemStack input, ItemStack input1, boolean adjustInput) {
        return getOutput(input, input1, adjustInput);
    }


    @Override
    public RecipeOutput process() {
        ItemStack input = ((TileEntityGenerationStone) this.base).inputSlotA.get();
        ItemStack input1 = ((TileEntityGenerationStone) this.base).inputSlotA.get(1);
        if (input == null)
            return null;
        if (input1 == null)
            return null;
        RecipeOutput output = getOutputFor(input, input1,false);
        if (output == null)
            return null;
        List<ItemStack> itemsCopy = new ArrayList<>(output.items.size());
        itemsCopy.addAll(output.items);
        return new RecipeOutput(output.metadata, itemsCopy);
    }

    public void consume() {

        ItemStack input = ((TileEntityGenerationStone) this.base).inputSlotA.get();
        ItemStack input1 = ((TileEntityGenerationStone) this.base).inputSlotA.get(1);
        if (input != null && input.stackSize <= 1)
            ((TileEntityGenerationStone) this.base).inputSlotA.put(null);
        if (input1 != null && input1.stackSize <= 1)
            ((TileEntityGenerationStone) this.base).inputSlotA.put(1,null);



    }

    @Override
    public ItemStack get1(final int i) {
        return this.get(i);
    }


}
