package com.denfop.invslot;


import com.denfop.api.recipe.ISlotInv;
import com.denfop.items.ItemSolidMatter;
import com.denfop.tiles.base.TileConverterSolidMatter;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.item.ItemStack;

public class InventoryConverterSolidMatter extends Inventory implements ISlotInv {

    public InventoryConverterSolidMatter(TileEntityInventory base1) {
        super(base1, TypeItemSlot.INPUT, 8);

    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.getmatter();
        TileConverterSolidMatter tile = (TileConverterSolidMatter) base;
        if (tile.getRecipeOutput() != null) {
            tile.getrequiredmatter(tile.getRecipeOutput().getRecipe().getOutput());
        }
    }

    public void getmatter() {

        for (int i = 0; i < this.size(); i++) {
            if (!get(i).isEmpty()) {
                TileConverterSolidMatter tile = (TileConverterSolidMatter) base;
                int meta = get(i).getItemDamage();
                while (!this.get(i).isEmpty() && tile.quantitysolid[meta % tile.quantitysolid.length] <= 99800) {
                    tile.quantitysolid[meta % tile.quantitysolid.length] += 200;
                    this.consume(i, 1);

                }

            }
        }


    }

    public boolean acceptAllOrIndex() {
        return false;
    }

    public boolean isItemValidForSlot(final int index, ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemSolidMatter && index == itemStack.getItemDamage();
    }

    public void consume(int content, int amount) {
        consume(content, amount, false);
    }

    public void consume(int content, int amount, boolean simulate) {

        ItemStack stack = get(content);
        if (!stack.isEmpty() && amount > 0) {
            int currentAmount = Math.min(amount, stack.getCount());
            if (!simulate) {
                stack.shrink(currentAmount);
            }
        }

    }

    @Override
    public boolean accepts(final int index, final ItemStack stack) {
        return index == stack.getItemDamage();
    }

}
