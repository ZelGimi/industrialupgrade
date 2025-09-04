package com.denfop.inventory;


import com.denfop.IUItem;
import com.denfop.blockentity.base.BlockEntityConverterSolidMatter;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.items.ItemSolidMatter;
import net.minecraft.world.item.ItemStack;

public class InventoryConverterSolidMatter extends Inventory {

    public InventoryConverterSolidMatter(BlockEntityInventory base1) {
        super(base1, TypeItemSlot.INPUT, 8);

    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        this.getmatter();
        BlockEntityConverterSolidMatter tile = (BlockEntityConverterSolidMatter) base;
        if (tile.getRecipeOutput() != null) {
            tile.getrequiredmatter(tile.getRecipeOutput().getRecipe().getOutput());
        }
        return content;
    }

    public void getmatter() {

        for (int i = 0; i < this.size(); i++) {
            if (!get(i).isEmpty()) {
                BlockEntityConverterSolidMatter tile = (BlockEntityConverterSolidMatter) base;
                int meta = IUItem.matter.getMeta(get(i));
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

    public boolean canPlaceItem(final int index, ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemSolidMatter && index == IUItem.matter.getMeta(itemStack);
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
