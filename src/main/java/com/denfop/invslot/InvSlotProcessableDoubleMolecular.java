package com.denfop.invslot;

import com.denfop.api.IDoubleMolecularRecipeManager;
import com.denfop.api.Recipes;
import com.denfop.tiles.base.TileEntityDoubleMolecular;
import ic2.api.recipe.RecipeOutput;
import ic2.core.block.TileEntityInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvSlotProcessableDoubleMolecular extends InvSlotProcessable {

    public InvSlotProcessableDoubleMolecular(TileEntityInventory base1, String name1, int count) {
        super(base1, name1, null, count);

    }

    public Map<IDoubleMolecularRecipeManager.Input, RecipeOutput> getRecipeList() {
        return Recipes.doublemolecular.getRecipes();
    }

    public boolean accepts(ItemStack itemStack) {
        for (Map.Entry<IDoubleMolecularRecipeManager.Input, RecipeOutput> entry : getRecipeList().entrySet()) {
            if ((entry.getKey()).container.matches(itemStack)
                    || (entry.getKey()).fill.matches(itemStack)) {
                return itemStack != null;
            }
        }
        return false;

    }

    protected RecipeOutput getOutput(ItemStack container, ItemStack fill, boolean adjustInput) {

        return Recipes.doublemolecular.getOutputFor(container, fill, adjustInput, false);

    }

    protected RecipeOutput getOutputFor(ItemStack input, ItemStack input1, boolean adjustInput) {
        return getOutput(input, input1, adjustInput);
    }

    public RecipeOutput process() {
        ItemStack input = ((TileEntityDoubleMolecular) this.base).inputSlot.get(0);
        ItemStack input1 = ((TileEntityDoubleMolecular) this.base).inputSlot.get(1);
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

    public void consume() {

        ItemStack input = ((TileEntityDoubleMolecular) this.base).inputSlot.get(0);
        ItemStack input1 = ((TileEntityDoubleMolecular) this.base).inputSlot.get(1);
        getOutputFor(input, input1, true);

        if (input != null && input.stackSize <= 0) {
            ((TileEntityDoubleMolecular) this.base).inputSlot.put(0, null);
        }
        if (input1 != null && input1.stackSize <= 0) {
            ((TileEntityDoubleMolecular) this.base).inputSlot.put(1, null);
        }


    }


}
