package com.denfop.inventory;


import com.denfop.blockentity.base.BlockEntityBaseWorldCollector;
import com.denfop.items.ItemSolidMatter;
import net.minecraft.world.item.ItemStack;

public class InventoryWorldCollector extends Inventory {

    private final BlockEntityBaseWorldCollector tile;

    public InventoryWorldCollector(BlockEntityBaseWorldCollector base1) {
        super(base1, TypeItemSlot.INPUT, 1);
        this.tile = base1;
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        this.getmatter();
        BlockEntityBaseWorldCollector tile = (BlockEntityBaseWorldCollector) base;
        if (tile.getRecipeOutput() != null) {
            tile.getrequiredmatter(tile.getRecipeOutput().getRecipe().getOutput());
        }
        return content;
    }

    public void getmatter() {

        for (int i = 0; i < this.size(); i++) {
            if (!get(i).isEmpty()) {
                int col = 50;
                col -= Math.ceil(this.tile.matter_energy / 200);
                col = Math.min(col, get(i).getCount());
                this.tile.matter_energy += 200 * col;
                this.consume(i, col);


            }
        }


    }

    public boolean canPlaceItem(final int index, ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemSolidMatter && ((ItemSolidMatter<?>) itemStack.getItem()).getElement().getId() == this.tile.enumTypeCollector.getMeta();
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
