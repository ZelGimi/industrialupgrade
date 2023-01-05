package com.denfop.invslot;


import com.denfop.items.ItemSolidMatter;
import com.denfop.tiles.base.TileEntityConverterSolidMatter;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotConverterSolidMatter extends InvSlot {

    public InvSlotConverterSolidMatter(TileEntityInventory base1, String string) {
        super(base1, string, InvSlot.Access.I, 7, InvSlot.InvSide.TOP);

    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.getmatter();
        TileEntityConverterSolidMatter tile = (TileEntityConverterSolidMatter) base;
        if (tile.getRecipeOutput() != null) {
            tile.getrequiredmatter(tile.getRecipeOutput().getRecipe().getOutput());
        }
    }

    public void getmatter() {

        for (int i = 0; i < this.size(); i++) {
            if (!get(i).isEmpty()) {
                TileEntityConverterSolidMatter tile = (TileEntityConverterSolidMatter) base;
                int meta = get(i).getItemDamage();
                while (!this.get(i).isEmpty() && tile.quantitysolid[meta % tile.quantitysolid.length] <= 98000) {
                    tile.quantitysolid[meta % tile.quantitysolid.length] += 200;
                    this.consume(i, 1);

                }

            }
        }


    }

    public boolean accepts(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemSolidMatter;
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

}
