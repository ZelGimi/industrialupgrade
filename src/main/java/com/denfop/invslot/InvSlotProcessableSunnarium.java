package com.denfop.invslot;


import com.denfop.api.ISunnariumRecipeManager;
import com.denfop.api.Recipes;
import com.denfop.tiles.base.TileSunnariumMaker;
import ic2.api.recipe.RecipeOutput;
import ic2.core.block.TileEntityInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvSlotProcessableSunnarium extends InvSlotProcessable {

    public InvSlotProcessableSunnarium(TileEntityInventory base1, String name1, int oldStartIndex1, int count) {
        super(base1, name1, null, count);

    }

    public Map<ISunnariumRecipeManager.Input, RecipeOutput> getRecipeList() {
        return Recipes.sunnurium.getRecipes();
    }

    public boolean accepts(ItemStack itemStack) {
        for (Map.Entry<ISunnariumRecipeManager.Input, RecipeOutput> entry : getRecipeList().entrySet()) {
            if ((entry.getKey()).container.matches(itemStack)
                    || (entry.getKey()).fill.matches(itemStack) || (entry.getKey()).fill2.matches(itemStack) || (entry.getKey()).fill3.matches(
                    itemStack)) {
                return itemStack != null;
            }
        }
        return false;

    }

    protected RecipeOutput getOutput(ItemStack container, ItemStack fill, ItemStack fill1, ItemStack fill2, boolean adjustInput) {

        return Recipes.sunnurium.getOutputFor(container, fill, fill1, fill2, adjustInput, false);

    }

    protected RecipeOutput getOutputFor(
            ItemStack input,
            ItemStack input1,
            ItemStack input2,
            ItemStack input3,
            boolean adjustInput
    ) {
        return getOutput(input, input1, input2, input3, adjustInput);
    }

    public RecipeOutput process() {
        ItemStack input = ((TileSunnariumMaker) this.base).inputSlotA.get(0);
        ItemStack input1 = ((TileSunnariumMaker) this.base).inputSlotA.get(1);
        ItemStack input2 = ((TileSunnariumMaker) this.base).inputSlotA.get(2);
        ItemStack input3 = ((TileSunnariumMaker) this.base).inputSlotA.get(3);

        if (input == null) {
            return null;
        }
        if (input1 == null) {
            return null;
        }
        if (input2 == null) {
            return null;
        }
        if (input3 == null) {
            return null;
        }
        RecipeOutput output = getOutputFor(input, input1, input2, input3, false);
        if (output == null) {
            return null;
        }
        List<ItemStack> itemsCopy = new ArrayList<>(output.items.size());
        itemsCopy.addAll(output.items);
        return new RecipeOutput(output.metadata, itemsCopy);
    }

    public void consume() {

        ItemStack input = ((TileSunnariumMaker) this.base).inputSlotA.get(0);
        ItemStack input1 = ((TileSunnariumMaker) this.base).inputSlotA.get(1);
        ItemStack input2 = ((TileSunnariumMaker) this.base).inputSlotA.get(2);
        ItemStack input3 = ((TileSunnariumMaker) this.base).inputSlotA.get(3);
        getOutputFor(input, input1, input2, input3, true);

        if (input != null && input.getCount() <= 0) {
            ((TileSunnariumMaker) this.base).inputSlotA.put(null);
        }
        if (input1 != null && input1.getCount() <= 0) {
            ((TileSunnariumMaker) this.base).inputSlotA.put(1, null);
        }
        if (input2 != null && input2.getCount() <= 0) {
            ((TileSunnariumMaker) this.base).inputSlotA.put(2, null);
        }
        if (input3 != null && input3.getCount() <= 0) {
            ((TileSunnariumMaker) this.base).inputSlotA.put(3, null);
        }

    }


}
